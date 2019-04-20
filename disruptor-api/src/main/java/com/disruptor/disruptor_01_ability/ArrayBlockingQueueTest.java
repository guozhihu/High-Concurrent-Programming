package com.disruptor.disruptor_01_ability;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Author: zhihu
 * Description: BlockingQueue的压力性能测试
 * Date: Create in 2019/3/8 11:12
 */
public class ArrayBlockingQueueTest {
    public static void main(String[] args) {
        final ArrayBlockingQueue<Data> queue = new ArrayBlockingQueue<Data>(100000000);
        final long startTime = System.currentTimeMillis();
        
        //向容器中添加元素，生产者
        new Thread(new Runnable() {
            
            public void run() {
                long i = 0;
//                while (i < Constants.EVENT_NUM_OM) {
//                while (i < Constants.EVENT_NUM_FM) {
                while (i < Constants.EVENT_NUM_OHM) {
                    Data data = new Data(i, "c" + i);
                    try {
                        queue.put(data);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        }).start();
        
        // 向容器中取出数据，消费者
        new Thread(new Runnable() {
            public void run() {
                int k = 0;
                while (k < Constants.EVENT_NUM_OHM) {
                    try {
                        queue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    k++;
                }
                long endTime = System.currentTimeMillis();
                System.out.println("ArrayBlockingQueue costTime = " + (endTime - startTime) + "ms");
            }
        }).start();
    }
}
