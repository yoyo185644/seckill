package org.seckill.service.Impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yoyo
 * @mail yoyo185644@163.com
 * @date 2020/3/28 9:22 下午
 */
//@Component @Service @ Controller
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //注入Service依赖
    @Autowired  //自动注入 不需要自己去实现new对象
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;
    //md5盐值字符串，用于混淆MD5
    private final String slat = "ndldnnsjdni[dj2i3jfe2901=-i30ex4u017#*(&#^!(#*)";
    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,8);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：缓存优化,超时的基础上维护一致性
        /**
         * get from cache
         * if null
         *     get db
         *     put cache
         * locgoin
         */
        //1、访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null){
            //2访问数据库
             seckill = seckillDao.queryById(seckillId);
            if (null==seckill){
                return new Exposer(false,seckillId);
            }else{
                //放入redis
                redisDao.putSeckill(seckill);
            }

        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统当前时间
        Date nowTime = new Date();
        if (nowTime.getTime()<startTime.getTime()
            ||nowTime.getTime()>endTime.getTime()) {
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),
                    endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5=getMd5(seckillId);//TODO
        return new Exposer(true,md5,seckillId);
    }
    private String getMd5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点：
     * 1、开发团队达成一直约定，约定明确标注事务方法的编程风格。
     * 2、保证事务方法的执行时间尽可能短，不要穿插其他的网络操作（RPC/HTTP请求），或者剥离到方法外面。
     * 3、不是所有的方法都需要事务，如只有一条修改操作、只读操作。
     */
    public SeckillExcution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
      if (null == md5 || !getMd5(seckillId).equals(md5)) {
          throw new SeckillException("seckill data rewrite");
      }
      //执行秒杀行为
        Date nowTime = new Date();
        try {
            //记录购买行为
            //先记录购买行为 ，降低行级锁占用时间
            int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
            //seckillId-userPhone唯一
            if (insertCount<=0){
                //重复秒杀
                throw new RepeatKillException("seckill repeat");
            }
            else {
                //减库存,热点商品竞争
                int updateNumber = seckillDao.reduceNumber(seckillId,nowTime);
                if (updateNumber <= 0){
                    //没有更新的记录，秒杀结束，rollback
                    throw new SeckillCloseException("seckill is closed");
                }else {
                    //秒杀成功，commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExcution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }

            }

        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //所有编译时异常转化为运行时异常
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }

    @Override
    public SeckillExcution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if (null == md5 || !getMd5(seckillId).equals(md5)){
            return new SeckillExcution(seckillId,SeckillStatEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        //执行完存储过程之后result被赋值
        try {
            seckillDao.killByProcedure(map);
            //获取result
            int result = MapUtils.getInteger(map,"result",-2);
            if (result ==1){
                SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExcution(seckillId,SeckillStatEnum.SUCCESS,sk);
            } else {
                return new SeckillExcution(seckillId,SeckillStatEnum.stateOf(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillExcution(seckillId,SeckillStatEnum.INNER_ERROR);
        }


    }
}
