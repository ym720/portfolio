package edu.nyu.fc.messaging;

/**
 * Implementation of payout response message. This type of message is sent after
 * a request for payout has been completed and a payout value calculated.
 * 
 * @author Yourii Martiak
 *
 */
public class PayoutResponse implements IMessage {
    
    /**
     * Serial version ID used for serializable objects
     */
    private static final long serialVersionUID = -4679196448178194964L;
    
    /**
     * Calculated payout value
     */
    private final double payout;
    
    /**
     * Message specific selector ID that allows selective filtering in the
     * message queue
     */
    private final String selectorID;
    
    /**
     * Creates new instance of payout response message.
     * 
     * @param payout calculated payout value
     * @param selectorID message-specific selector ID used for filtering
     */
    public PayoutResponse(double payout, String selectorID) {
        this.payout = payout;
        this.selectorID = selectorID;
    }
    
    /**
     * Get calculated payout value
     * 
     * @return calculated payout value
     */
    public double getPayout() {
        return payout;
    }

    @Override
    public String getSelectorID() {
        return selectorID;
    }

}
