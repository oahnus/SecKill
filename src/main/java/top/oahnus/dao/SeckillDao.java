package top.oahnus.dao;

import org.apache.ibatis.annotations.Param;
import top.oahnus.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Created by oahnus on 2016/11/24.
 */
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId 秒杀商品id
     * @param killTime 秒杀时间
     * @return 执行语句的行数
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    /**
     * 根据id检索
     * @param seckillId 秒杀商品id
     * @return 秒杀商品
     */
    Seckill queryById(@Param("seckillId") long seckillId);

    /**
     *
     * @param offset 起始位置
     * @param limit 数量
     * @return 秒杀数据
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 调用处处过程执行秒杀
     * @param map
     */
    void seckillByProcedure(Map<String,Object> map);
}
