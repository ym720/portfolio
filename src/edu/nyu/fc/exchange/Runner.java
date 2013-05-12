package edu.nyu.fc.exchange;

import java.util.Iterator;

import orderGenerator.Message;
import orderGenerator.OrdersIterator;

/**
 * Utility class for running a double limit order book auction simulation. This
 * simulation is verbose, where a top of the book is printed after each message
 * that is being processed. Verbose information printing incurs a significant
 * amount of additional overhead.
 *
 * @author Yourii Martiak
 */
public class Runner {
    
    public static void main(String[] args) {
        IDoubleLimitBookAuction auction = new DoubleLimitBookAuction();
        Iterator<Message> iterator = OrdersIterator.getIterator();
        int i = 0;
        long start = System.nanoTime();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            auction.handleMessage(message);
            System.out.println(auction.getTopOfTheBooks());
        }
        long stop = System.nanoTime();
        System.out.println(auction);
        System.out.println(String.format("\n===\nRun time: %dns. averaging %dns. per message\n", stop-start, (stop-start)/i));
    }
}
