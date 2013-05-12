package edu.nyu.fc.exchange;

import java.util.Comparator;

/**
 * This comparator is used for comparing two arguments for reversed sorted order.
 * To be used for determining top of BID limit order book, where elements need
 * to be sorted in descending order so that the highest buy price is at the head
 * of the queue.
 * 
 * @author Yourii Martiak
 *
 * @param <T>
 */
public class TopOfBidBookComparator<T> implements Comparator<T> {

    @Override
    public int compare(T arg0, T arg1) {
        int c = ((Comparable<? super T>) arg0).compareTo(arg1);
        
        return c == 0 ? 0 : -c;
    }

}
