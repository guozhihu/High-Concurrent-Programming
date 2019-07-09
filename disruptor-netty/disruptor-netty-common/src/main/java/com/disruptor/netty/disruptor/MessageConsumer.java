package com.disruptor.netty.disruptor;

import com.disruptor.netty.entity.TranslatorDataWapper;
import com.lmax.disruptor.WorkHandler;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/23 10:15
 */
public abstract class MessageConsumer implements WorkHandler<TranslatorDataWapper> {
    
    protected String consumerId;
    
    public MessageConsumer(String consumerId) {
        this.consumerId = consumerId;
    }
    
    public String getConsumerId() {
        return this.consumerId;
    }
    
    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }
}
