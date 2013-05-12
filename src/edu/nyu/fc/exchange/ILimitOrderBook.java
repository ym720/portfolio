package edu.nyu.fc.exchange;


/**
 * This interface defines a common contract for all implementing classes
 * (namely ask and bid limit order books) to interact with their clients.
 */
public interface ILimitOrderBook {
    
    /**
     * Get next order for a given price level and remove from queue
     * 
     * @param price to indicate price level
     * @return next order as per time priority or null if either price level
     * does not exist or all orders at this price level already done
     */
    public ILimitOrder pollNextOrderFor(double price);
    
    /**
     * Put order at the end of a determined price level queue
     * 
     * @param limitOrder
     */
    public void addLast(ILimitOrder limitOrder);
    
    /**
     * Put order in front of a determined price level queue
     * 
     * @param limitOrder
     */
    public void addFirst(ILimitOrder limitOrder);
    
    /**
     * Determine top of the book price (highest bid or lowest ask
     * 
     * @return best price if available, or zero otherwise
     */
    public double getBestPrice();
    
    /**
     * Get best price based on the top of the book in relation to a given
     * limit price
     * 
     * @param price
     * @return best top of the book price if better than a given limit price
     */
    public double getBestPriceFor(double price);
    
    /**
     * Get top of book representation
     * 
     * @return top of book order or null if empty
     */
    public String getTopOfBook();
}
