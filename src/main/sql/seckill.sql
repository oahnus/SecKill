-- 秒杀执行存储过程
-- 修改换行标示
DELIMITER $$ -- 代替; 换行
-- 定义存储过程
-- in表示输入参数，out表示输出参数，输出参数只能被赋值
-- row_count() 返回上一条被修改的行数
-- row_count() 结果0:未修改数据,>0,被修改数据行数,<0,SQL错误或未执行

CREATE PROCEDURE seckill.execute_seckill
  (IN v_seckill_id BIGINT,IN v_phone BIGINT,
    IN v_kill_time TIMESTAMP,OUT r_result INT)
  BEGIN
    DECLARE insert_count INT DEFAULT 0;
    START TRANSACTION ;
    INSERT IGNORE INTO success_killed (seckill_id, user_phone, create_time)
      VALUES (v_seckill_id,v_phone,v_kill_time);
    SELECT row_count() INTO insert_count;
    IF (insert_count = 0) THEN
      ROLLBACK ;
      SET r_result = -1;
    ELSEIF(insert_count < 0) THEN
      ROLLBACK ;
      SET r_result = -2;
    ELSE
      UPDATE seckill SET number = number - 1
      WHERE seckill_id = v_seckill_id AND end_time > v_kill_time
            AND seckill.start_time < v_kill_time AND number > 0;
      SELECT row_count() INTO insert_count;
      IF (insert_count = 0) THEN
        ROLLBACK ;
        SET r_result = 0;
      ELSEIF (insert_count < 0) THEN
        ROLLBACK ;
        SET r_result = -2;
      ELSE
        COMMIT ;
        SET r_result = 1;
      END IF;
    END IF;
  END;
$$

-- 存储过程定义结束

-- DELIMITER ;
-- SET @r_result = -3;
#-- 执行存储过程
-- CALL seckill.execute_seckill(1003,13934343231,now(),@r_result);

#-- 获取结果
-- select @r_result;

#-- 存储过程，优化的是事务行级锁持有的时间，不应过渡依赖存储过程，(银行依靠存储过程)
#-- 简单逻辑可以使用存储过程