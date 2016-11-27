package top.oahnus.service;

import top.oahnus.dto.Exposer;
import top.oahnus.dto.SeckillExection;
import top.oahnus.entity.Seckill;
import top.oahnus.exception.RepeatException;
import top.oahnus.exception.SeckillException;

import java.util.List;

/**
 * Created by oahnus on 2016/11/24.
 */
public interface SeckillService {

    /**
     * 获取所有的秒杀商品信息
     * @return
     */
    List<Seckill> getSecKillList();

    /**
     *
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀接口的地址
     * @param seckillId
     */
    Exposer exposeSecKillUrl(long seckillId);

    /**
     * 执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExection executeSecKill(long seckillId, long userPhone, String md5)
    throws SeckillException,RepeatException,SeckillException;

    // 调用存储过程执行秒杀
    SeckillExection executeSecKillProcedure(long seckillId, long userPhone, String md5)
            throws SeckillException,RepeatException,SeckillException;
}
