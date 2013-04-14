package edu.nyu.fc.messaging;

import java.io.Serializable;

/**
 * Common interface for messages processed by the {@link IMessageProcessor} that
 * allows messages to be serialized over the wire.
 * 
 * @author Yourii Martiak
 *
 */
public interface IMessage extends Serializable {
    
    /**
     * Get message specific selector ID. This allows messages to be sent over
     * different channels using a single queue.
     * 
     * @return message-specific selector ID or null if not set
     */
    public String getSelectorID();
    
}
