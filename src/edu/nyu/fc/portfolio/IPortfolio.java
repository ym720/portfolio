package edu.nyu.fc.portfolio;

/**
 * Portfolio representing a collection of positions and some update rule. To
 * update position in a portfolio (add new, update/remove existing position)
 * client needs to call newTrade method providing a security symbol and number
 * of shares for an update to take place.
 * 
 * @param <E> type of position in portfolio
 * 
 * @author Yourii Martiak
 * 
 */
public interface IPortfolio<E> {

    /**
     * Interact with portfolio and add new, update/remove existing position by
     * calling this method.
     * 
     * @param symbol
     *            security symbol for a position being traded
     * @param quantity
     *            number of share for a position being traded
     */
    public void newTrade(String symbol, int quantity);

    /**
     * Obtain an iterator for a collection of positions that this portfolio
     * holds.
     * 
     * @return position iterator implementation that allows client to iterate
     *         over positions held by this portfolio
     */
    public IPositionIter<E> getPositionIter();

}
