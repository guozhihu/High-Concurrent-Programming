package com.disruptor.netty.entity;

import io.netty.channel.ChannelHandlerContext;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/23 10:09
 */
public class TranslatorDataWapper {
    private TranslatorData data;
    private ChannelHandlerContext ctx;
    
    public TranslatorData getData() {
        return data;
    }
    
    public void setData(TranslatorData data) {
        this.data = data;
    }
    
    public ChannelHandlerContext getCtx() {
        return ctx;
    }
    
    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
