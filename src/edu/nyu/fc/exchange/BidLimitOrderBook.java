package edu.nyu.fc.exchange;

/**
 * Concrete implementation of limit order book functionality specific to BID
 * limit order books.
 * 
 * @author Yourii Martiak
 *
 */
public class BidLimitOrderBook extends LimitOrderBook {
    
    /**
     * Creates new instance of limit order book where sorting is done according
     * to BID book requirements (reverse order to determine the highest price
     * available)
     */
    public BidLimitOrderBook() {
        super(new TopOfBidBookComparator<Double>());
    }

    @Override
    public double getBestPriceFor(double price) {
        double topOfBookPrice = getBestPrice();
        
        return topOfBookPrice == 0 || topOfBookPrice < price ? price : topOfBookPrice;
    }

}
