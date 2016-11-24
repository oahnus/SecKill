package top.oahnus.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.oahnus.entity.Seckill;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

/**
 * Created by oahnus on 2016/11/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-dao.xml"})
public class SeckillDaoTest {

    // 注入依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() throws Exception {
        int updateNum = seckillDao.reduceNumber(1000,new Date());
        System.out.println(updateNum);
    }

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill);
    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> list = seckillDao.queryAll(0,1000);
        System.out.println(list.size());
        for(Seckill seckill :list){
            System.out.println(seckill);
        }
    }

}