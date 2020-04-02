package org.seckill.exception;

/**
 * @author yoyo
 * @mail yoyo185644@163.com
 * @date 2020/3/28 9:18 下午
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
