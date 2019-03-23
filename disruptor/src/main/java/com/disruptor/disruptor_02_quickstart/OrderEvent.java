package com.disruptor.disruptor_02_quickstart;

/**
 * Author: zhihu
 * Description: Event类
 * Date: Create in 2019/3/10 14:13
 */
public class OrderEvent {
    
    private long value; // 订单的价格
    
    public long getValue() {
        return value;
    }
    
    public void setValue(long value) {
        this.value = value;
    }
}
