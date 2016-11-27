package top.oahnus.dao.cache;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import top.oahnus.entity.Seckill;

/**
 * Created by oahnus on 2016/11/27.
 */
//
public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(RedisDao.class);
    // JedisPool相当于数据库连接池
    private final JedisPool jedisPool;

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip,int port){
        this.jedisPool = new JedisPool(ip,port);
    }

    /**
     * 从redis取出对象信息
     * @param seckillId SeckillId
     * @return 存在，返回Seckill对象，不存在返回null
     */
    public Seckill getSeckill(long seckillId){
        // redis 操作逻辑
        try{
            // Jedis 相当于数据库连接
            Jedis jedis = jedisPool.getResource();

            try{
                String key = "seckill:"+seckillId;
                // redis 并没有实现内部序列化
                // 从redis取出的数据要反序列化
                // byte[] -> 反序列化 —> Object(Seckill)
                // 采用自定义序列化 protostuff
                // protostuff需要pojo（有get,set方法）
                byte[] bytes = jedis.get(key.getBytes());
                // 如果不为空，则获取到对象
                if(bytes != null){
                    // 反序列化
                    // 使用protostuff创建一个空对象
                    Seckill seckill = schema.newMessage();
                    // 将空对象传递给工具类中的mergeFrom方法
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    // 执行方法后，seckill中将保存有从redis取出的数据
                    return seckill;
                }
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 保存对象到redis
     * @param seckill Seckill对象
     * @return 结果，成功返回ok，失败返回错误信息
     */
    public String putSeckill(Seckill seckill){
        // 将对象序列化为byte数组
        try{
            Jedis jedis = jedisPool.getResource();

            try{
                // 构建key
                String key = "seckill:"+seckill.getSeckillId();
                // 要序列化的对象，schema，缓存器，allocate方法设置缓存器大小
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // 超时缓存
                int timeout = 60*60;//redis 超时时间单位为秒，60*60=1小时
                String result = jedis.setex(key.getBytes(),timeout,bytes);
                // result返回信息，成功返回ok，错误返回错误信息
                return result;
            }finally {
                jedis.close();
            }
        } catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}
