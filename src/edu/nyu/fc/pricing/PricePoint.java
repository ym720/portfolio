package edu.nyu.fc.pricing;

import org.joda.time.DateTime;

/**
 * Concrete implementation of a pair mapping price to a point in time
 * 
 * @author Yourii Martiak
 * 
 */
public class PricePoint implements IPricePoint {

    /**
     * Time stamp for the point in time for which the given price is valid
     */
    private final DateTime dateTime;

    /**
     * Price generated at a given point in time during lifetime of an option
     */
    private final double price;

    /**
     * Creates new instance of price point mapping
     * 
     * @param dateTime
     *            timestamp for a price
     * @param price
     *            for a given point in time during option lifetime
     */
    public PricePoint(final DateTime dateTime, final double price) {
        this.dateTime = dateTime;
        this.price = price;
    }

    @Override
    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public double getPrice() {
        return price;
    }
    
    @Override
    public String toString() {
        return "{dateTime=" + dateTime + ", price=" + price + "}";
    }

}
