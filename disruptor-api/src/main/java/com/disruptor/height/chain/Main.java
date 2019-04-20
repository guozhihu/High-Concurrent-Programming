package com.disruptor.height.chain;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/18 9:18
 */
public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        // 构建一个线程池用于提交任务
        ExecutorService es1 = Executors.newFixedThreadPool(1);
        ExecutorService es2 = Executors.newFixedThreadPool(5); // 单消费者模式下，该线程池的数量应大于等于消费者监听的数量，即下文的handler数量
        // 1.构建Disruptor
        Disruptor<Trade> disruptor = new Disruptor<Trade>(
            new EventFactory<Trade>() {
                @Override
                public Trade newInstance() {
                    return new Trade();
                }
            },
            1024 * 1024,
            es2,
            ProducerType.SINGLE,
            new BusySpinWaitStrategy()
        );
        
        // 2.把消费者设置到Disruptor中 handleEventsWith
        
        // 2.1串行操作（使用链式调用的方式）---先执行handler1，再执行handler2，最后执行handler3，按顺序执行
        /**
        disruptor
            .handleEventsWith(new Handler1())
            .handleEventsWith(new Handler2())
            .handleEventsWith(new Handler3());
         */
        
        // 2.2并行操作（使用单独调用的方式）---handler1，handler2，handler3是并发的，异步
        // 方式一
        /**
        disruptor.handleEventsWith(new Handler1());
        disruptor.handleEventsWith(new Handler2());
        disruptor.handleEventsWith(new Handler3());
         */
        // 方式二
        /**
        disruptor.handleEventsWith(new Handler1(), new Handler2(), new Handler3());
         */
        
        // 2.3菱形操作---handler1和handler2是并行执行的，它们都执行完后再执行handler3
        // 方式一
        /**
        disruptor.handleEventsWith(new Handler1(), new Handler2())
        .handleEventsWith(new Handler3());
         */
        // 方式二
        /**
        EventHandlerGroup<Trade> ehGroup = disruptor.handleEventsWith(new Handler1(), new Handler2());
        ehGroup.then(new Handler3());
         */
        // 实现如下操作
        /**
         *   handler1 ----> handler2
         *  /                       \
         * S                         handler3
         *  \                       /
         *   handler4 ----> handler5
         * 即handler1和handler4并行执行，
         * handler1和handler2串行执行，
         * handler4和handler5串行执行，
         * handler2和handler5也是并行执行，
         * 当handler2和handler5都执行完后，再执行handler3
         */
        Handler1 handler1 = new Handler1();
        Handler2 handler2 = new Handler2();
        Handler3 handler3 = new Handler3();
        Handler4 handler4 = new Handler4();
        Handler5 handler5 = new Handler5();
        disruptor.handleEventsWith(handler1, handler4);
        disruptor.after(handler1).handleEventsWith(handler2); // 执行完handler1后再执行handler2
        disruptor.after(handler4).handleEventsWith(handler5);
        disruptor.after(handler2, handler5).handleEventsWith(handler3); // 当执行完handler2和handler5后再执行handler3
        
        // 3.启动disruptor
        RingBuffer<Trade> ringBuffer = disruptor.start();
        
        CountDownLatch latch = new CountDownLatch(1);
        
        long beginTime = System.currentTimeMillis();
        
        es1.submit(new TradePushlisher(latch, disruptor)); // 异步提交
        
        latch.await(); // 进行向下
        
        disruptor.shutdown();
        es1.shutdown();
        es2.shutdown();
        System.err.println("总耗时：" + (System.currentTimeMillis() - beginTime));
    }
}
