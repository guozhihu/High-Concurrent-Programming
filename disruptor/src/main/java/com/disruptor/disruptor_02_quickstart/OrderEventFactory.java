package com.disruptor.disruptor_02_quickstart;

import com.lmax.disruptor.EventFactory;

/**
 * Author: zhihu
 * Description: 工厂Event类，用于创建Event类实例对象
 * Date: Create in 2019/3/10 14:14
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
    
    @Override
    public OrderEvent newInstance() {
        return new OrderEvent(); // 这个方法就是为了返回空的数据对象(Event)
    }
}
