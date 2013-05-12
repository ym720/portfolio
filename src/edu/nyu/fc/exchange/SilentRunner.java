package edu.nyu.fc.exchange;

import java.util.Iterator;

import orderGenerator.Message;
import orderGenerator.OrdersIterator;

/**
 * Utility class for running a double limit order book auction simulation. This
 * simulation is "silent", meaning only the end summary results are printed out
 * to avoid additional overhead.
 *
 * @author Yourii Martiak
 */
public class SilentRunner {
    
    public static void main(String[] args) {
        IDoubleLimitBookAuction auction = new DoubleLimitBookAuction();
        Iterator<Message> iterator = OrdersIterator.getIterator();
        int i = 0;
        long start = System.nanoTime();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            auction.handleMessage(message);
            i++;
        }
        long stop = System.nanoTime();
        System.out.println(auction);
        System.out.println(String.format("\n===\nRun time: %dns. averaging %dns. per message\n", stop-start, (stop-start)/i));
    }
}
