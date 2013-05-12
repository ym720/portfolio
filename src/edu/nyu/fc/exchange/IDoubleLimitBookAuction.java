package edu.nyu.fc.exchange;

import orderGenerator.Message;

/**
 * Common functionality used for double limit book auction implementation.
 *
 */
public interface IDoubleLimitBookAuction {
    
    /**
     * Handle message according to its type.
     * 
     * @param message
     */
    public void handleMessage(Message message);
    
    /**
     * Execute trade between two sides - bid and ask orders
     * 
     * @param bid - order to buy
     * @param ask - order to sell
     */
    public void newTrade(ILimitOrder bid, ILimitOrder ask);
    
    /**
     * Get string representation of top-of-the-book across all limit books
     * that are actively used in this auction
     * 
     * @return string representation of top of the book
     */
    public String getTopOfTheBooks();

}
