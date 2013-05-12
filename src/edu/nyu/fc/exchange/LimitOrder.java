package edu.nyu.fc.exchange;

import orderGenerator.NewOrder;

/**
 * Implementation of new order that allows client to add additional information
 * to an existing NewOrder implementation, such as a flag to determine if the
 * order has been marked "dead"
 * 
 * @author Yourii Martiak
 */
public class LimitOrder implements ILimitOrder {
    
    private final NewOrder order;
    private boolean isDead;
    private int size;
    private double limitPrice;
    
    public LimitOrder(NewOrder order) {
        this.order = order;
        setSize(order.getSize());
        setLimitPrice(order.getLimitPrice());
    }

    @Override
    public double getLimitPrice() {
        return limitPrice;
    }

    @Override
    public String getOrderId() {
        return order.getOrderId();
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String getSymbol() {
        return order.getSymbol();
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    @Override
    public void setDead() {
        isDead = true;        
    }

    @Override
    public void setSize(int size) {
        this.size = size;     
    }

    @Override
    public void setLimitPrice(double limitPrice) {
        this.limitPrice = limitPrice;        
    }
    
    @Override
    public String toString() {
        return order.getOrderId() + " : " + size + " " + order.getSymbol() + " @ " + limitPrice;
    }

    @Override
    public String toStatusString() {
        return String.format("%.2f,%s,%d\n", limitPrice, size < 0 ? "ask" : "bid", size);
    }

}
