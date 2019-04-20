package com.disruptor.height.multi;

/**
 * Author: zhihu
 * Description: Disruptor中的Event
 * Date: Create in 2019/4/19 8:56
 */
public class Order {
    private String id;
    private String name;
    private double price;
    
    public Order() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
}
