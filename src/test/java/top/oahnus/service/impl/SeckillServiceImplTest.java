package top.oahnus.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.oahnus.dto.Exposer;
import top.oahnus.dto.SeckillExection;
import top.oahnus.entity.Seckill;
import top.oahnus.exception.RepeatException;
import top.oahnus.exception.SeckillCloseException;
import top.oahnus.service.SeckillService;

import java.util.List;

/**
 * Created by oahnus on 2016/11/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-dao.xml","classpath:spring-service.xml"})
public class SeckillServiceImplTest {
    private Logger logger = LoggerFactory.getLogger(SeckillServiceImplTest.class);

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSecKillList() throws Exception {
        List<Seckill> seckillList = seckillService.getSecKillList();
        logger.info("list={}", seckillList);
    }

    @Test
    public void getById() throws Exception {
        logger.info("secKill={}", seckillService.getById(1000L));
    }

    @Test
    public void testSeckillByProcedure(){
        Exposer exposer = seckillService.exposeSecKillUrl(1002L);
        String md5 = "";
        long id = 1002;
        long phone = 13413135487L;
        if(exposer.isExposed()){
            md5 = exposer.getMd5();
        }
        SeckillExection exection = seckillService.executeSecKillProcedure(id,phone,md5);
        logger.info(exection.getStateInfo());
    }

    /**
     * 测试代码逻辑
     * @throws Exception
     */
    @Test
    public void testSecKillLogic() throws Exception {
        long seckillId = 1003L;
        Exposer exposer = seckillService.exposeSecKillUrl(seckillId);
        logger.info("exposer={}", exposer);
        if(exposer.isExposed()){
            try {
                long userPhone = 15751774919L;
                String md5 = exposer.getMd5();
                SeckillExection result = seckillService.executeSecKill(seckillId,userPhone,md5);
                logger.info("result={}",result);
            }catch (RepeatException e){
                logger.error(e.getMessage());
            }catch (SeckillCloseException e){
                logger.error(e.getMessage());
            }
        }else{
            logger.warn("秒杀未开启exporter={}", exposer);
        }
    }
    /**
     * 包含事务处理
     21:53:29.014 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Creating a new SqlSession
     21:53:29.025 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Registering transaction synchronization for SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@49c66ade]
     21:53:29.036 [main] DEBUG o.m.s.t.SpringManagedTransaction - JDBC Connection [com.mchange.v2.c3p0.impl.NewProxyConnection@2796aeae] will be managed by Spring
     21:53:29.043 [main] DEBUG t.oahnus.dao.SeckillDao.reduceNumber - ==>  Preparing: UPDATE seckill SET number = number - 1 WHERE seckill_id = ? AND start_time <= ? AND end_time >= ? AND seckill.number > 0
     21:53:29.082 [main] DEBUG t.oahnus.dao.SeckillDao.reduceNumber - ==> Parameters: 1000(Long), 2016-11-24 21:53:28.993(Timestamp), 2016-11-24 21:53:28.993(Timestamp)
     21:53:29.125 [main] DEBUG t.oahnus.dao.SeckillDao.reduceNumber - <==    Updates: 1
     21:53:29.126 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@49c66ade]
     21:53:29.126 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@49c66ade] from current transaction
     21:53:29.126 [main] DEBUG t.o.d.S.insertSuccessKilled - ==>  Preparing: INSERT IGNORE INTO success_killed (seckill_id, user_phone,state) VALUE (?,?,0)
     21:53:29.128 [main] DEBUG t.o.d.S.insertSuccessKilled - ==> Parameters: 1000(Long), 12312312241(Long)
     21:53:29.129 [main] DEBUG t.o.d.S.insertSuccessKilled - <==    Updates: 1
     21:53:29.138 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@49c66ade]
     21:53:29.139 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@49c66ade] from current transaction
     21:53:29.141 [main] DEBUG t.o.d.S.queryByIdWithSecKill - ==>  Preparing: SELECT sk.seckill_id,sk.user_phone,sk.state,sk.create_time, s.seckill_id "seckill.seckill_id", s.name "seckill.name", s.number "seckill.number", s.start_time "seckill.start_time", s.end_time "seckill.end_time", s.create_time "seckill.create_time" FROM success_killed sk INNER JOIN seckill s ON sk.seckill_id = s.seckill_id WHERE sk.seckill_id = ? AND user_phone = ?
     21:53:29.142 [main] DEBUG t.o.d.S.queryByIdWithSecKill - ==> Parameters: 1000(Long), 12312312241(Long)
     21:53:29.169 [main] DEBUG t.o.d.S.queryByIdWithSecKill - <==      Total: 1
     21:53:29.178 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@49c66ade]
     21:53:29.179 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization committing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@49c66ade]
     21:53:29.179 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization deregistering SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@49c66ade]
     21:53:29.179 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization closing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@49c66ade]
     21:53:29.358 [main] INFO  t.o.s.impl.SeckillServiceImplTest - result=SeckillExection{seckillId=1000, state=1, stateInfo='成功', successKilled=SuccessKilled{seckillId=1000, userPhone=12312312241, state=0, createTime=Thu Nov 24 21:53:29 CST 2016, seckill=Seckill{seckillId=1000, name='500元秒杀IPHONE', number=97, startTime=Thu Nov 24 21:53:29 CST 2016, endTime=Wed Nov 30 00:00:00 CST 2016, createTime=Thu Nov 24 14:33:25 CST 2016}}}
     */
}