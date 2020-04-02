package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author yoyo
 * @mail yoyo185644@163.com
 * @date 2020/3/29 10:26 上午
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
                        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    /**
     * Closing non transactional SqlSession
     * list=[seckill{seckillid=1004, name='100元秒杀iPhoneXS', number=500, startTime=Sat Mar 28 13:00:00 CST 2020, endTime=Sun Mar 29 13:00:00 CST 2020, createTime=Sat Mar 28 10:13:38 CST 2020},
     * seckill{seckillid=1003, name='100元秒杀iPhoneX', number=400, startTime=Sat Mar 28 13:00:00 CST 2020, endTime=Sun Mar 29 13:00:00 CST 2020, createTime=Sat Mar 28 10:13:38 CST 2020},
     * seckill{seckillid=1002, name='100元秒杀iPhone8', number=300, startTime=Sat Mar 28 13:00:00 CST 2020, endTime=Sun Mar 29 13:00:00 CST 2020, createTime=Sat Mar 28 10:13:38 CST 2020},
     * seckill{seckillid=1001, name='100元秒杀iPhone7', number=200, startTime=Sat Mar 28 13:00:00 CST 2020, endTime=Sun Mar 29 13:00:00 CST 2020, createTime=Sat Mar 28 10:13:38 CST 2020}]
     */
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    /**
     *  Closing non transactional SqlSession
     *  seckill=seckill{seckillid=1000, name='100元秒杀iPhone6', number=99, startTime=Sat Mar 28 13:00:00 CST 2020, endTime=Sun Mar 29 13:00:00 CST 2020, createTime=Sat Mar 28 10:13:38 CST 2020}
     */
    public void getById() {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }

    @Test
    /**
     * long id = 1000L;
     * exposer=Exposer{exposed=true, md5='f7a0316be1379dbb0900c1647ee81a81', seckillId=1000, now=0, start=0, end=0}
     */
//    public void exportSeckillUrl() {
//        long id = 1000L;
//        Exposer exposer = seckillService.exportSeckillUrl(id);
//        logger.info("exposer={}",exposer);
//    }
    //集成测试测试代码完整逻辑，注意可重复执行
    public void testSeckillLogic() {
        long id = 1001L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()){
            logger.info("exposer={}",exposer);
            long phone = 18813100919L;
            String md5 = exposer.getMd5();
            //重复操作会抛出异常 需要使用try catch

            try {
                SeckillExcution excution = seckillService.executeSeckill(id, phone, md5);
                logger.info("execution={}", excution);
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            }
        }
        else {
            logger.warn("exposer={}",exposer);
        }
    }

//    @Test
//    /**
//     * Releasing transactional SqlSession
//     * execution=SeckillExcution{seckillId=1000,
//     * state=1, stateInfo='秒杀成功',
//     * successKilled=SuccessKilled{seckillId=1000,
//     * userPhone=18813100919, state=-1, createTime=Sun Mar 29 23:52:11 CST 2020}}
//     */
//    public void executeSeckill() {
//        long id = 1000L;
//        long phone = 18813100919L;
//        String md5 = "f7a0316be1379dbb0900c1647ee81a81";
//        //重复操作会抛出异常 需要使用try catch
//
//        try {
//            SeckillExcution excution = seckillService.executeSeckill(id, phone, md5);
//            logger.info("execution={}", excution);
//        } catch (SeckillCloseException e) {
//            logger.error(e.getMessage());
//        } catch (RepeatKillException e) {
//            logger.error(e.getMessage());
//        }
//    }
    @Test
    public void executeSeckillProcedure(){
        long seckillId = 1006;
        long phone = 13423456789L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExcution excution = seckillService.executeSeckillProcedure(seckillId,phone,md5);
            logger.info(excution.getStateInfo());
        }

    }
}
