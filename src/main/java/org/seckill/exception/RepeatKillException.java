package org.seckill.exception;

/**
 * @author yoyo
 * @mail yoyo185644@163.com
 * @date 2020/3/28 9:14 下午
 * 重复秒杀异常（运行时异常）
 */
public class RepeatKillException extends SeckillException{
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
