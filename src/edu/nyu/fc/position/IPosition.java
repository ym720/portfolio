package edu.nyu.fc.position;

/**
 * A position representing the current quantity for a particular security
 * symbol. It is up to concrete implementations of this interface to decide
 * whether a position is mutable (i.e. the quantity can be updated in an
 * existing object) or immutable (new object created for each update).
 * 
 * @author Yourii Martiak
 * 
 */
public interface IPosition {

    /**
     * Get security symbol that this position currently holds.
     * 
     * @return security symbol held by this position
     */
    public String getSymbol();

    /**
     * Get number of shares for a particular security symbol this position
     * currently holds.
     * 
     * @return number of shares held by this position
     */
    public int getQuantity();

}
