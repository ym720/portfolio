package edu.nyu.fc.pricing;

import java.util.List;
/**
* Payout implementation for Asian call option. This implementation requires
* a sample {@link IStockPath} to calculate its payout value
*/
public class AsianCallOptionPayout implements IPayout {
    
    /**
     * Strike price used to calculate this option payout
     */
    private final double strikePrice;
    
    /**
     * Creates new instance of Asian call option payout for a given strike
     * price
     * 
     * @param strikePrice
     *            strike price at any point during the time to expire
     */
    public AsianCallOptionPayout(double strikePrice) {
        this.strikePrice = strikePrice;
    }

    @Override
    public double getPayout(IStockPath stockPath) {
     // get list of price points for a given stock path
        final List<IPricePoint> pricePoints = stockPath.getPrices();
        double payoutSum = 0.0d;
        for (IPricePoint pricePoint : pricePoints) {
            // sum up payouts
            payoutSum += pricePoint.getPrice();
        }
        // calculate payout as per Asian call option payout formula
        // where S = (Avg(S(T)) - K)
        return Math.max((payoutSum / (double) pricePoints.size()) - strikePrice, 0.0);
    }

}
