package top.oahnus.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.oahnus.entity.SuccessKilled;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by oahnus on 2016/11/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        int insertNumber = successKilledDao.insertSuccessKilled(1000L,13945334122L);
        System.out.println(insertNumber);
    }

    @Test
    public void queryByIdWithSecKill() throws Exception {
        SuccessKilled successKilled = successKilledDao.queryByIdWithSecKill(1000L,13945334122L);
        System.out.println(successKilled);
    }

}