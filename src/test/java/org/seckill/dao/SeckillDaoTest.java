package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 首先需要配置spring 和 junit 整合，为了junit启动时加载springIOC容器
 * spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring 配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    //注入Dao依赖
    @Resource
    private  SeckillDao seckillDao;

    @Test
    public void reduceNumber() {
        Date killTime =new Date();
        int updateCount = seckillDao.reduceNumber(1000L,killTime);
        System.out.println("updateCount=" + updateCount);

    }

    /**
     * Caused by: org.springframework.core.NestedIOException:
     * Failed to parse mapping resource:
     * 'file [/Users/yoyo/IdeaProjects/seckill/target/classes/mapper/SeckillDao.xml]';
     * nested exception is org.apache.ibatis.builder.BuilderException: Error creating document instance.
     * Cause: org.xml.sax.SAXParseException; lineNumber: 30; columnNumber: 10; XML 文档结构必须从头至尾包含在同一个实体内。
     */
    @Test
    public void queryById() {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    /**
     * Caused by: org.apache.ibatis.binding.BindingException:
     * Parameter 'offset' not found. Available parameters are [0, 1, param1, param2]
     *    List<Seckill> queryAll(int offset , int limit);a
     *    java没有保存行参的表述
     */
    @Test
    public void queryAll() {
        List<Seckill> seckills = seckillDao.queryAll(0,100);
        for (Seckill seckill:seckills) {
            System.out.println(seckill);

        }
    }
}
