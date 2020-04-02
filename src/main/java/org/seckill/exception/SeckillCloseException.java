package org.seckill.exception;

/**
 * @author yoyo
 * @mail yoyo185644@163.com
 * @date 2020/3/28 9:17 下午
 * 秒杀关闭日常
 */
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
