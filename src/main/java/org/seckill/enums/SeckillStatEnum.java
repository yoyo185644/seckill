package org.seckill.enums;

/**
 * @author yoyo
 * @mail yoyo185644@163.com
 * @date 2020/3/28 10:11 下午
 * 使用枚举表示常量字典
 */
public enum  SeckillStatEnum {
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀失败"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据篡改");
    private int state;
    private String stateInfo;

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStatEnum stateOf(int index){
        for (SeckillStatEnum state: values()) {
            if (index == state.getState()){
                return state;
            }
        }
        return null;
    }
}
