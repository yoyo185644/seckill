package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring 配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        long id = 1000L;
        long userPhone = 18813100968L;
        int insertCount = successKilledDao.insertSuccessKilled(id,userPhone);
        System.out.println("insertCount = " + insertCount);
    }

    /**
     * java.sql.SQLSyntaxErrorException: Unknown column 'sk.userphone' in 'field list'
     */
    /**
     * Caused by: com.mysql.cj.exceptions.DataReadException: Zero date value prohibited
     */
    @Test
    public void queryByIdWithSeckill() {
        long id = 1000L;
        long userPhone = 18813100968L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id,userPhone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}
