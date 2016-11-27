package top.oahnus.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.oahnus.dao.SeckillDao;
import top.oahnus.entity.Seckill;

import static org.junit.Assert.*;

/**
 * Created by oahnus on 2016/11/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-dao.xml"})
public class RedisDaoTest {

    private long id = 1002;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testSeckill() throws Exception {
        Seckill seckill = redisDao.getSeckill(id);

        if(seckill == null){
            seckill = seckillDao.queryById(id);
            String result = redisDao.putSeckill(seckill);
System.out.println(result);
            Seckill seckillNew = redisDao.getSeckill(id);
System.out.println(seckillNew);
        }
    }

}