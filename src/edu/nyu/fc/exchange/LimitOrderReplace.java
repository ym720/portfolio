package edu.nyu.fc.exchange;

import orderGenerator.OrderCxR;

/**
 * Implementation of order cancel/replace that allows client to pull information
 * from both - original (order currently being replaced) and the cancel/replace
 * request.
 * 
 * @author Yourii Martiak
 */
public class LimitOrderReplace implements ILimitOrder {
    
    private double limitPrice;
    private final String orderID;
    private int size;
    private final String symbol;
    private boolean isDead;
    
    public LimitOrderReplace(ILimitOrder order, OrderCxR replace) {
        setLimitPrice(replace.getLimitPrice());
        this.orderID = replace.getOrderId();
        setSize(replace.getSize());
        this.symbol = order.getSymbol();
        this.isDead = false;
    }

    @Override
    public double getLimitPrice() {
        return limitPrice;
    }

    @Override
    public String getOrderId() {
        return orderID;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String getSymbol() {
        return symbol;
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
        return orderID + " : " + size + " " + symbol + " @ " + limitPrice;
    }
    
    @Override
    public String toStatusString() {
        return String.format("%.2f,%s,%d\n", limitPrice, size < 0 ? "ask" : "bid", size);
    }

}
