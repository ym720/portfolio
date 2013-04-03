package edu.nyu.fc.pricing;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Implementation of variation in prices that simulates real-life price
 * variations that occur during lifetime of a given option. The variations in
 * prices follow geometric Brownian motion
 * 
 * @author Yourii Martiak
 * 
 */
public class StockPath implements IStockPath {
    /**
     * Number of days in one year, used to calculate time slice value
     */
    private static final double DAYS_IN_YEAR = 365.0d;

    /**
     * Number of days during which we simulate price variations
     */
    private final int timeSlots;
    /**
     * Asset price
     */
    private final double price;
    /**
     * Interest rate
     */
    private final double rate;
    /**
     * Volatility daily
     */
    private final double sigma;
    /**
     * Random values from normal distribution
     */
    private final double[] volatilityVector;
    /**
     * List of price points
     */
    private final List<IPricePoint> stockPrices;

    /**
     * Created new instance of StockPath representing price variations during
     * lifetime of an option
     * 
     * @param price
     *            asset price
     * @param rate
     *            interest rate
     * @param sigma
     *            volatility
     * @param randomValues
     *            normal distribution
     */
    public StockPath(final double price, final double rate, final double sigma,
            final double[] randomValues) {
        this.price = price;
        this.rate = rate;
        this.sigma = sigma;
        volatilityVector = randomValues;
        timeSlots = volatilityVector.length;
        stockPrices = new ArrayList<IPricePoint>(timeSlots);
        initialize();
    }

    /**
     * Calculate price points using formula for geometric Brownian motion
     */
    private void initialize() {
        // starting price
        double priceAtTime = price;
        // value of a single time slot assigned according to total number of
        // days for which the option is valid
        final double timeDelta = timeSlots / DAYS_IN_YEAR / timeSlots;
        // calculate square of volatility used in formula
        final double sigmaSquare = sigma * sigma;
        // starting timestamp
        DateTime dateTime = new DateTime();
        for (final double priceDelta : volatilityVector) {
            dateTime = dateTime.plusDays(1);
            // calculate price for a single time slot using geometric Brownian
            // motion formula
            priceAtTime = priceAtTime
                    * Math.exp((rate - 0.5 * sigmaSquare) * timeDelta
                            + Math.sqrt(sigma * timeDelta) * priceDelta);
            // add price point to the list while adding one day to the timestamp
            stockPrices.add(new PricePoint(dateTime, priceAtTime));
        }
    }

    @Override
    public List<IPricePoint> getPrices() {
        return stockPrices;
    }

}
