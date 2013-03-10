package edu.nyu.fc.position;

/**
 * This exception is thrown to indicate that the code has attempted to create
 * and instance of {@link edu.nyu.fc.position.IPosition} with invalid position
 * quantity.
 * 
 * @author Yourii Martiak
 * 
 */
public class PositionQuantityException extends RuntimeException {

    /**
     * Serial version ID used for serializable objects
     */
    private static final long serialVersionUID = 5411726820363949946L;

    /**
     * Default constructor without any specific message describing the reason
     * for this exception being thrown.
     */
    public PositionQuantityException() {
        super();
    }

    /**
     * Constructor allowing user to provide reason for this exception being
     * thrown.
     * 
     * @param s
     *            reason for this exception being thrown
     */
    public PositionQuantityException(final String s) {
        super(s);
    }

}
