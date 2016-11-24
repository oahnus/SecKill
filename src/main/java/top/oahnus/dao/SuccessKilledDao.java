package top.oahnus.dao;

import org.apache.ibatis.annotations.Param;
import top.oahnus.entity.SuccessKilled;

/**
 * Created by oahnus on 2016/11/24.
 */
public interface SuccessKilledDao {
    /**
     * 添加秒杀成功数据
     * @param seckillId 秒杀商品id
     * @param userPhone 用户手机号
     * @return 影响行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);

    /**
     * 根据id查询秒杀成功的数据并携带秒杀商品的数据
     * @param seckillId 秒杀商品id
     * @return 秒杀成功的数据
     */
    SuccessKilled queryByIdWithSecKill(@Param("seckillId") long seckillId,@Param("userPhone")long userPhone);
}
