package edu.nyu.fc.messaging;

/**
 * Common interface for sending/receiving messages over middleware. It provides
 * administrative methods for starting/stopping the processor as well as closing
 * and releasing resources.
 * 
 * @author Yourii Martiak
 *
 */
public interface IMessageProcessor {
    
    /**
     * Receive message from a point-to-point message queue. This method call is
     * blocking while queue is empty.
     * 
     * @return message sent by another party
     * @throws MessagingException if something went wrong while receiving
     */
    public IMessage receiveMessage() throws MessagingException;
    
    /**
     * Send message to a point-to-point message queue.
     * 
     * @param message to be sent to another party
     * @throws MessagingException if something went wrong while sending
     */
    public void sendMessage(IMessage message) throws MessagingException;
    
    /**
     * Start connection
     * 
     * @throws MessagingException if something went wrong while starting
     */
    public void start() throws MessagingException;
    
    /**
     * Stop connection without closing the session
     * 
     * @throws MessagingException if something went wrong while stopping
     */
    public void stop() throws MessagingException;
    
    /**
     * Close session and release resources
     * 
     * @throws MessagingException if something went wrong while closing
     */
    public void close() throws MessagingException;
    
    /**
     * Check if this message processor is running, connection started.
     * 
     * @return true if connection started, false otherwise
     */
    public boolean isRunning();

}
