package edu.nyu.fc.pricing;

import java.util.List;

/**
 * The interface used for implementation of price fluctuations during the lifetime
 * of an option
 * 
 * @author Yourii Martiak
 *
 */
public interface IStockPath {
    
    /**
     * Obtain a list of price points taken during the lifetime of an option
     * 
     * @return list of {@link IPricePoint} implementations
     */
    public List<IPricePoint> getPrices();
}
