package edu.nyu.fc.portfolio;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.nyu.fc.position.IPosition;
import edu.nyu.fc.position.Position;
import edu.nyu.fc.position.PositionQuantityException;

/**
 * Default implementation of portfolio holding a collection of positions. The
 * following is an example of interaction with this portfolio. For instance, a
 * client added a position of 100 shares of IBM to the portfolio. Then another
 * trade of -200 shares of MSFT occurs. A new position for -200 shares of MSFT
 * will get added to the portfolio. Now, if another trade for -200 shares of IBM
 * occurs, the current IBM position of 100 shares is updated to -100 and placed
 * back into portfolio. Next, another trade of 200 shares of MSFT will result in
 * a zero quantity position for MSFT, which is invalid and such position is
 * removed from portfolio all together.
 * 
 * @author Yourii Martiak
 * 
 */
public class Portfolio implements IPortfolio<IPosition> {

    /**
     * Internal store for collection of portfolio positions
     */
    private final Map<String, IPosition> portfolio;

    /**
     * Default constructor, creates empty portfolio with initial capacity for
     * holding 16 positions. The capacity is dynamically resized as portfolio
     * size grows.
     */
    public Portfolio() {
        this(16);
    }

    /**
     * Construct an empty portfolio, which preallocates internal store initial
     * capacity to size parameter. The capacity is dynamically resized as
     * portfolio size grows.
     * 
     * @param size
     *            internal resource allocation for initial portfolio capacity
     */
    public Portfolio(final int size) {
        portfolio = new HashMap<String, IPosition>(size);
    }

    @Override
    public void newTrade(final String symbol, final int quantity) {
        try {
            IPosition position = portfolio.get(symbol);
            if (position == null) {
                // create completely new position if portfolio did not contain
                // prior position for the symbol
                position = new Position(quantity, symbol);
            } else {
                // create new position with an updated quantity if portfolio
                // contained prior position for the symbol
                position = new Position(position.getQuantity() + quantity,
                        symbol);
            }
            portfolio.put(symbol, position);
        } catch (final PositionQuantityException pqe) {
            // remove position resulting in zero quantity after the new trade
            if (portfolio.containsKey(symbol)) {
                portfolio.remove(symbol);
            }
        } catch (final IllegalArgumentException iae) {
            System.err.println("New trade not processed - " + iae.getMessage());
        }
    }

    @Override
    public IPositionIter<IPosition> getPositionIter() {
        return new Positioniter();
    }

    /**
     * Position iterator that allows clients of this portfolio to iterate over
     * the list of all portfolio positions. Used as an adapter over existing
     * internal data store iterator.
     */
    private class Positioniter implements IPositionIter<IPosition> {

        /**
         * Get internal store's iterator to be used by adapter
         */
        Iterator<IPosition> iterator = portfolio.values().iterator();

        /**
         * Get next iterator position or null if already at the last position or
         * iterator is empty.
         */
        @Override
        public IPosition getNextPosition() {
            return iterator.hasNext() ? iterator.next() : null;
        }

    }

}
