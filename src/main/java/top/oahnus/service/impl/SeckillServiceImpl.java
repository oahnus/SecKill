package top.oahnus.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import top.oahnus.dao.SeckillDao;
import top.oahnus.dao.SuccessKilledDao;
import top.oahnus.dto.Exporter;
import top.oahnus.dto.SeckillResult;
import top.oahnus.entity.Seckill;
import top.oahnus.entity.SuccessKilled;
import top.oahnus.enums.SeckillStateEnum;
import top.oahnus.exception.RepeatException;
import top.oahnus.exception.SeckillCloseException;
import top.oahnus.exception.SeckillException;
import top.oahnus.service.SeckillService;

import java.util.Date;
import java.util.List;

/**
 * Created by oahnus on 2016/11/24.
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(SeckillService.class);

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    private final String salt = "jsioei*jr33n:{<?}f23fa;";

    public List<Seckill> getSecKillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    /**
     * 导出秒杀url
     * @param seckillId 秒杀商品id
     * @return 导出dto类
     */
    public Exporter exportSecKillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);

        if(seckill == null){
            return new Exporter(false,seckillId);
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        if(nowTime.getTime() <startTime.getTime()|| nowTime.getTime()>endTime.getTime()){
            return new Exporter(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }

        String md5 = getMD5(seckillId);
        return new Exporter(true,md5,seckillId);
    }

    /**
     * 执行秒杀
     * @param seckillId 秒杀商品id
     * @param userPhone 用户电话
     * @param md5 md5
     * @return 秒杀结果
     * @throws SeckillException
     * @throws RepeatException
     * @throws SeckillException
     */
    @Transactional
    /**
     * 使用声明式注解的优点
     * 1. 可以达成一致约定
     * 2. 保证事务方法执行时间尽可能短，不要穿插其他HTTP/RPC网络操作，或者剥离到方法外部
     * 3. 不是所有的方法都需要事务操作，只有一条修改操作不需要事务控制
     * 4.
     */
    public SeckillResult executeSecKill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatException, SeckillException {
        try {
            if (md5 == null || !md5.equals(getMD5(seckillId))) {
                throw new SeckillException("seckill data rewrite");
            }
            // 减少商品数量
            int updateNum = seckillDao.reduceNumber(seckillId, new Date());
            if (updateNum != 1) {
                throw new SeckillCloseException("seckill closed");
            } else {
                // 记录购买记录
                int insertNum = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertNum == 0) {
                    throw new RepeatException("repeat kill");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSecKill(seckillId, userPhone);
                    return new SeckillResult(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }
            }
        } catch (SeckillCloseException e){
            throw e;
        } catch (RepeatException e){
            throw e;
        } catch (Exception e){
            logger.info(e.getMessage(),e);
            // 拦截所有编译期异常，转化为运行期异常
            throw new SeckillException(e.getMessage());
        }
    }

    private String getMD5(long seckillId){
        String base = seckillId+"/"+salt;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
