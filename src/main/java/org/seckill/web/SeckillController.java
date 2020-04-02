package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author yoyo
 * @mail yoyo185644@163.com
 * @date 2020/3/29 8:39 下午
 */
@Controller //@Service @Component
@RequestMapping("/seckill") //url:/模块/资源/{id}/细分
public class SeckillController {

    @Autowired
    private SeckillService seckillService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        //list.jsp + model = ModelAndView
        model.addAttribute("list", list);
        return "list"; //WEB-INF/jsp/"list".jsp

    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (null == seckillId) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (null == seckill) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);//承载最终要返回给用户的数据，返回Seckill类数据
        return "detail";//view

    }

    //ajax json
    @RequestMapping(value = "/{seckillId}/exposer",
            method = {RequestMethod.POST,RequestMethod.GET},
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer (Long seckillId){
        SeckillResult<Exposer> result;

        try {

            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true,exposer);

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result = new SeckillResult<Exposer>(false,e.getMessage());

        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = {RequestMethod.POST,RequestMethod.GET},
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<SeckillExcution> excute(@PathVariable("seckillId") Long seckillId,
                                                  @PathVariable("md5") String md5,
                                                  @CookieValue(value = "killPhone" ,required = false) Long phone ){
        //验证phone或者spring MVC验证（此处没有）
        if(phone == null){
            return new SeckillResult<SeckillExcution>(false,"未注册");
        }
        SeckillResult<SeckillExcution> result;
        try {
            //SeckillExcution seckillExcution = seckillService.executeSeckill(seckillId,phone,md5);
            //通过存储过程去获取
            SeckillExcution seckillExcution = seckillService.executeSeckillProcedure(seckillId,phone,md5);
            result = new SeckillResult<SeckillExcution>(true,seckillExcution);
        } catch (RepeatKillException e) {
            SeckillExcution excution = new SeckillExcution(seckillId, SeckillStatEnum.REPEAT_KILL);
            result = new SeckillResult<SeckillExcution>(true,excution);
        }catch (SeckillCloseException e) {
            SeckillExcution excution = new SeckillExcution(seckillId, SeckillStatEnum.END);
            result = new SeckillResult<SeckillExcution>(true,excution);
        }catch (Exception e) {
            SeckillExcution excution = new SeckillExcution(seckillId, SeckillStatEnum.INNER_ERROR);
            result = new SeckillResult<SeckillExcution>(true,excution);
        }
        return result;

    }

    //获取系统时间
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult<> (true,now.getTime());

    }
}
