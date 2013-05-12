package edu.nyu.fc.exchange;

import orderGenerator.NewOrder;

/**
 * This interface is used for marking orders invalid, as they are canceled in
 * the limit order book.
 */
public interface ILimitOrder extends NewOrder {
    
    /**
     * Check if the order is still valid
     * 
     * @return true if the order is invalid, meaning it was intentionally
     * marked "dead", return false otherwise
     */
    public boolean isDead();
    
    /**
     * Mark this order as invalid, in other words "dead"
     * @param isDead
     */
    public void setDead();
    
    /**
     * Set size of this order
     * 
     * @param size
     */
    public void setSize(int size);
    
    /**
     * Set limit price for this order
     * 
     * @param limitPrice
     */
    public void setLimitPrice(double limitPrice);
    
    /**
     * Get string representation in the form of price, bid/ask, size
     * @return
     */
    public String toStatusString();

}
