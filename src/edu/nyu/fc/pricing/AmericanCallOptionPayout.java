package edu.nyu.fc.pricing;

import java.util.List;

/**
 * Payout implementation for American call option. This implementation requires
 * a sample {@link IStockPath} to calculate its payout value
 * 
 * @author Yourii Martiak
 * 
 */
public class AmericanCallOptionPayout implements IPayout {

    /**
     * Strike price used to calculate this option payout
     */
    private final double strikePrice;

    /**
     * Creates new instance of American call option payout for a given strike
     * price
     * 
     * @param strikePrice
     *            strike price at any point during the time to expire
     */
    public AmericanCallOptionPayout(final double strikePrice) {
        this.strikePrice = strikePrice;
    }

    @Override
    public double getPayout(final IStockPath stockPath) {
        // get list of price points for a given stock path
        final List<IPricePoint> pricePoints = stockPath.getPrices();
        double payoutSum = 0.0d;
        for (final IPricePoint pricePoint : pricePoints) {
            // sum up payouts according to American call option payout formula
            // where S = (S(T) - K)
            payoutSum += Math.max(pricePoint.getPrice() - strikePrice, 0.0);
        }
        // get the average payout for a give stock path
        return payoutSum / pricePoints.size();
    }

}
