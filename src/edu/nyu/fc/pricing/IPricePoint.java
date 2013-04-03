package edu.nyu.fc.pricing;

import org.joda.time.DateTime;

/**
 * This class represents a mapping of price for a given date timestamp
 * 
 * @author Yourii Martiak
 * 
 */
public interface IPricePoint {

    /**
     * Obtain timestamp from this price point
     * 
     * @return timestamp for which the price is valid
     */
    public DateTime getDateTime();

    /**
     * Obtain price associated with the timestamp
     * 
     * @return price for this timestamp
     */
    public double getPrice();
}
