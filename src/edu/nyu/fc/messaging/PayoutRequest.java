package edu.nyu.fc.messaging;

import edu.nyu.fc.pricing.MonteCarloSimulation.Distribution;
import edu.nyu.fc.pricing.MonteCarloSimulation.OptionType;

/**
 * Implementation of a payout request message. This type of message is sent
 * when requesting option payout specific to its required parameters
 * 
 * @author Yourii Martiak
 *
 */
public class PayoutRequest implements IMessage {

    /**
     * Serial version ID used for serializable objects
     */
    private static final long serialVersionUID = -2912144911325967150L;

    private final int daysToExpire;
    private final double price;
    private final double rate;
    private final double sigma;
    private final double strikePrice;
    private final OptionType optionType;
    private final Distribution distribution;
    private final String selectorID;

    /**
     * Creates new instance of payout request message
     * 
     * @param daysToExpire
     * @param price
     * @param rate
     * @param sigma
     * @param strikePrice
     * @param optionType
     * @param distribution
     * @param selectorID
     */
    public PayoutRequest(final int daysToExpire, final double price,
            final double rate, final double sigma, final double strikePrice,
            final OptionType optionType, final Distribution distribution, final String selectorID) {
        this.daysToExpire = daysToExpire;
        this.price = price;
        this.rate = rate;
        this.sigma = sigma;
        this.strikePrice = price;
        this.optionType = optionType;
        this.distribution = distribution;
        this.selectorID = selectorID;
    }

    public int getDaysToExpire() {
        return daysToExpire;
    }

    public double getPrice() {
        return price;
    }

    public double getRate() {
        return rate;
    }

    public double getSigma() {
        return sigma;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public Distribution getDistribution() {
        return distribution;
    }
    
    @Override
    public String getSelectorID() {
        return selectorID;
    }

    @Override
    public String toString() {
        return "PayoutRequest [daysToExpire=" + daysToExpire + " price="
                + price + " rate=" + rate + " sigma=" + sigma + " strikePrice="
                + strikePrice + " optionType=" + optionType + " distribution="
                + distribution + " selectorID=" + selectorID + "]";
    }

}
