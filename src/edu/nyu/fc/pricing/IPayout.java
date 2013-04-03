package edu.nyu.fc.pricing;

/**
 * Common interface to be used by concrete payout implementation for different
 * option types
 * 
 * @author Yourii Martiak
 *
 */
public interface IPayout {
    
    /**
     * Calculates payout specific to the implementing option type, among which
     * could be put or call, American or Asian, etc.
     * 
     * @param stockPath list of price points for a given option type
     * @return calculated payout value
     */
    public double getPayout(IStockPath stockPath);

}
