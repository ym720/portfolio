package edu.nyu.fc.messaging;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

/**
 * Concrete implementation of message processor over ActiveMQ middleware.
 * 
 * @author Yourii Martiak
 * 
 */
public class AMQMessageProcessor implements IMessageProcessor {

    public static final String DEFAULT_SELECTOR_ID = "NONE";

    private final String requestQueueName;
    private final String responseQueueName;
    private final String selectorID;

    private final Queue requestQueue;
    private final Queue responseQueue;
    private final QueueConnection connection;
    private final QueueSession session;
    private final MessageProducer sender;
    private final MessageConsumer receiver;

    private boolean isRunning;

    /**
     * Creates new instance of ActiveMQ message processor with specific
     * request/response message queues and default selector ID.
     * 
     * @param requestQueueName
     * @param responseQueueName
     * @throws NamingException
     * @throws JMSException
     * @throws MessagingException
     */
    public AMQMessageProcessor(final String requestQueueName,
            final String responseQueueName) throws 
            JMSException, MessagingException {
        this(requestQueueName, responseQueueName, DEFAULT_SELECTOR_ID);
    }

    /**
     * Creates new instance of ActiveMQ message processor with specific
     * request/response message queues and a specific selector ID, which allows
     * to process only those messages addressed to this selector and filter out
     * all others from the request queue.
     * 
     * @param requestQueueName
     * @param responseQueueName
     * @param selectorID
     * @throws NamingException
     * @throws JMSException
     * @throws MessagingException
     */
    public AMQMessageProcessor(final String requestQueueName,
            final String responseQueueName, final String selectorID)
            throws JMSException, MessagingException {

        if (requestQueueName == null || requestQueueName.isEmpty()) {
            throw new IllegalArgumentException("Missing request queue name");
        }
        if (responseQueueName == null || responseQueueName.isEmpty()) {
            throw new IllegalArgumentException("Missing response queue name");
        }

        this.requestQueueName = requestQueueName;
        this.responseQueueName = responseQueueName;
        this.selectorID = selectorID;

        // setup ActiveMQ connection, session, request/response queues
        final QueueConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        requestQueue = new ActiveMQQueue(requestQueueName);
        responseQueue = new ActiveMQQueue(responseQueueName);
        connection = connectionFactory.createQueueConnection();
        session = connection
                .createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        sender = session.createProducer(responseQueue);
        
        // set delivery mode to non-persistent for improved performance
        sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        // created either default receiver or receiver using
        // JMSCorrelationID='selectorID' as a message filtering criteria
        receiver = DEFAULT_SELECTOR_ID.equals(selectorID) ? session
                .createConsumer(requestQueue) : session.createConsumer(
                requestQueue,
                String.format("JMSCorrelationID='%s'", selectorID));
    }

    @Override
    public IMessage receiveMessage() throws MessagingException {
        try {
            return (IMessage) ((ObjectMessage) receiver.receive()).getObject();
        } catch (final JMSException e) {
            throw new MessagingException(e);
        }
    }

    @Override
    public void sendMessage(final IMessage message) throws MessagingException {
        try {
            final Message jmsMessage = session.createObjectMessage(message);
            final String messageSelectorID = message.getSelectorID();
            if (messageSelectorID != null) {
                jmsMessage.setJMSCorrelationID(messageSelectorID);
            }
            sender.send(jmsMessage);
        } catch (final JMSException e) {
            throw new MessagingException(e);
        }
    }

    @Override
    public void start() throws MessagingException {
        try {
            connection.start();
            isRunning = true;
        } catch (final JMSException e) {
            throw new MessagingException(e);
        }
    }

    @Override
    public void stop() throws MessagingException {
        try {
            connection.stop();
            isRunning = false;
        } catch (final JMSException e) {
            throw new MessagingException(e);
        }
    }

    @Override
    public void close() throws MessagingException {
        try {
            stop();
            connection.close();
            session.close();
            receiver.close();
            sender.close();
        } catch (final JMSException e) {
            throw new MessagingException(e);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public String toString() {
        return "MessageProcessor [requestQueue=" + requestQueueName
                + " responseQueue=" + responseQueueName + " selectorID="
                + selectorID + "]";
    }

}
