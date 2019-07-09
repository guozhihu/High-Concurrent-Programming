package com.disruptor.netty.disruptor;

import com.disruptor.netty.entity.TranslatorDataWapper;
import com.lmax.disruptor.EventFactory;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/23 15:24
 */
public class TranslatorDataWapperEventFactory implements EventFactory<TranslatorDataWapper> {
    @Override
    public TranslatorDataWapper newInstance() {
        return new TranslatorDataWapper();
    }
}
