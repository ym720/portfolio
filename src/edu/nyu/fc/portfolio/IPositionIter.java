package edu.nyu.fc.portfolio;

/**
 * Iterator over a collection of positions in portfolio.
 * 
 * @param <E> type of position on a portfolio
 * 
 * @author Yourii Martiak
 * 
 */
public interface IPositionIter<E> {

    /**
     * Returns next position in a collection of positions in portfolio.
     * 
     * @return next position or {@code null} when no more positions exist
     */
    public E getNextPosition();

}
