package edu.nyu.fc.exchange;

/**
 * Concrete implementation of limit order book functionality specific to ASK
 * limit order books.
 * 
 * @author Yourii Martiak
 *
 */
public class AskLimitOrderBook extends LimitOrderBook {
    
    /**
     * Creates new instance of limit order book where sorting is done according
     * to ASK book requirements (reverse order to determine the highest price
     * available)
     */
    public AskLimitOrderBook() {
        super(new TopOfAskBookComparator<Double>());
    }

    @Override
    public double getBestPriceFor(double price) {
        double topOfBookPrice = getBestPrice();
        
        return topOfBookPrice == 0 || topOfBookPrice > price ? price : topOfBookPrice;
    }

}
