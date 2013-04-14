package edu.nyu.fc.messaging;

/**
 * This exception is thrown to indicate problems related to code executing in
 * messaging framework either during initialization/shutdown or during message
 * processing.
 * 
 * @author Yourii Martiak
 * 
 */
public class MessagingException extends Exception {

    /**
     * Serial version ID used for serializable objects
     */
    private static final long serialVersionUID = -8590224317213663144L;

    /**
     * Creates new exception indicating the reason for this exception being
     * thrown
     * 
     * @param e
     *            reason for this exception being thrown
     */
    public MessagingException(final Throwable e) {
        super(e);
    }

}
