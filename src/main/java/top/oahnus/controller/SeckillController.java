package top.oahnus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.oahnus.dto.Exposer;
import top.oahnus.dto.SeckillExection;
import top.oahnus.dto.SeckillResult;
import top.oahnus.entity.Seckill;
import top.oahnus.enums.SeckillStateEnum;
import top.oahnus.exception.RepeatException;
import top.oahnus.exception.SeckillCloseException;
import top.oahnus.exception.SeckillException;
import top.oahnus.service.SeckillService;

import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import java.util.Date;
import java.util.List;

/**
 * Created by oahnus on 2016/11/25.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger(SeckillController.class);

    @Autowired
    private SeckillService seckillService;

    /**
     *
     * @param model 用来存放渲染jsp的数据,model+jsp=ModelAndView
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){
        // 获取列表
        List<Seckill> seckillList = seckillService.getSecKillList();
        model.addAttribute("list",seckillList);
        return "list";
    }

    /**
     * 秒杀商品详情页
     * @param model
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(Model model,@PathVariable("seckillId") Long seckillId){
        if(seckillId == null){
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if(seckill == null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * ajax 返回秒杀地址信息
     *
     * @param seckillId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exposeSecKillUrl(seckillId);
            result = new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result = new SeckillResult<Exposer>(false,e.getMessage());
        }
        return result;
    }

    /**
     *
     * @param seckillId
     * @param md5
     * @param userPhone 从cookie总读取手机号
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<SeckillExection> execute(
            @PathVariable("seckillId")Long seckillId,
            @PathVariable("md5") String md5,
            @CookieValue(value = "killPhone",required = false) Long userPhone){
        SeckillResult<SeckillExection> result;
        if(userPhone == null){
            return new SeckillResult<SeckillExection>(false,"未注册");
        }
        try {
            SeckillExection exection = seckillService.executeSecKill(seckillId, userPhone, md5);
            return new SeckillResult<SeckillExection>(true, exection);
        } catch (RepeatException e){
            SeckillExection exection = new SeckillExection(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExection>(true,exection);
        } catch (SeckillCloseException e){
            SeckillExection exection = new SeckillExection(seckillId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExection>(true,exection);
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            SeckillExection exection = new SeckillExection(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExection>(true,exection);
        }
    }

    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        return new SeckillResult<Long>(true,new Date().getTime());
    }
}