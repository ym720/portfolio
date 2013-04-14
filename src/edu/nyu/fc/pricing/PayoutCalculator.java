package edu.nyu.fc.pricing;

import javax.jms.JMSException;
import javax.naming.NamingException;

import edu.nyu.fc.messaging.AMQMessageProcessor;
import edu.nyu.fc.messaging.IMessageProcessor;
import edu.nyu.fc.messaging.MessagingException;
import edu.nyu.fc.messaging.PayoutRequest;
import edu.nyu.fc.messaging.PayoutResponse;

/**
 * This payout calculator listens for payout requests on the request queue and
 * processes them according to the contents of the request messages. After a
 * payout has been calculated, it send a payout response message on the response
 * queue.
 * 
 * @author Yourii Martiak
 * 
 */
public class PayoutCalculator {

    /**
     * Message processor for sending/receiving messages
     */
    private static IMessageProcessor messageProcessor;

    /**
     * Creates new instance of payout calculator to listen for message on a
     * given request queue and send responses back on the response queue
     * 
     * @param requestQueueName
     *            request queue
     * @param responseQueueName
     *            response queue
     * @throws JMSException
     *             when there are JMS related issues
     * @throws MessagingException
     *             when there are messaging related issues
     */
    public PayoutCalculator(final String requestQueueName,
            final String responseQueueName) throws JMSException,
            MessagingException {
        messageProcessor = new AMQMessageProcessor(requestQueueName,
                responseQueueName);
    }

    /**
     * A builder method for validating input and creating new instance of payout
     * calculator class
     * 
     * @param args
     *            input parameters
     * @return new instance of a payout calculator class
     * @throws JMSException
     * @throws MessagingException
     */
    private static PayoutCalculator createNewCalculator(final String[] args)
            throws NamingException, JMSException, MessagingException {
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments!");
        }

        final String requestQueueName = args[0];
        final String responseQueueName = args[1];

        if (requestQueueName == null || requestQueueName.isEmpty()) {
            throw new IllegalArgumentException("Request queue name invalid");
        }
        if (responseQueueName == null || responseQueueName.isEmpty()) {
            throw new IllegalArgumentException("Response queue name invalid");
        }

        return new PayoutCalculator(requestQueueName, responseQueueName);
    }

    /**
     * Starts this payout calculator for message processing
     * 
     * @throws MessagingException
     *             when something goes wrong while processing
     */
    public void run() throws MessagingException {

        // start message processor
        messageProcessor.start();

        while (messageProcessor.isRunning()) {
            try {
                final PayoutRequest payoutRequest = (PayoutRequest) messageProcessor
                        .receiveMessage();
                handlePayoutRequest(payoutRequest);
            } catch (final MessagingException e) {
                e.printStackTrace();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        // stop message processor
        messageProcessor.stop();

        // close message processor and release resources
        messageProcessor.close();

    }

    public static void close() {
        try {
            messageProcessor.close();
        } catch (final MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Handle received payout request according the message contents. Create
     * specific instance of {@link IPayout} and perform calculations. When done,
     * send {@link PayoutResponse} back to sender
     * 
     * @param payoutRequest
     *            payout request message received on the message queue
     */
    private void handlePayoutRequest(final PayoutRequest payoutRequest) {
        // create payout and random number generator implementations
        final IPayout payout = MonteCarloSimulation.createPayout(
                payoutRequest.getStrikePrice(), payoutRequest.getOptionType());
        final IRandomVectorGenerator vectorGenerator = MonteCarloSimulation
                .createVectorGenerator(payoutRequest.getDaysToExpire(),
                        payoutRequest.getDistribution());
        // now send back the response
        try {
            // when sending, set selector ID to be the same as the selectorID
            // value received in the payout request message
            messageProcessor.sendMessage(new PayoutResponse(payout
                    .getPayout(new StockPath(payoutRequest.getPrice(),
                            payoutRequest.getRate(), payoutRequest.getSigma(),
                            vectorGenerator.getVector())), payoutRequest
                    .getSelectorID()));
        } catch (final MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints usage information
     */
    private static void usage() {
        System.out
                .println("Usage: ./PayoutCalculator [requestQueueName] [responseQueueName]");
    }

    @Override
    public String toString() {
        return "PayoutCalculator [messageProcessor=" + messageProcessor + "]";
    }

    public static void main(final String[] args) {
        try {
            final PayoutCalculator pc = createNewCalculator(args);
            System.out.println("Starting " + pc);
            pc.run();
        } catch (final Exception e) {
            System.err
                    .println("Exception occured while creating new PayoutCalculator "
                            + e.getMessage());
            usage();
            throw new RuntimeException(e);
        }

    }

}
