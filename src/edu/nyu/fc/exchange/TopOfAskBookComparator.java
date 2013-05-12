package edu.nyu.fc.exchange;

import java.util.Comparator;

/**
 * This comparator is used for comparing two arguments for natural sorted order.
 * To be used for determining top of ASK limit order book, where elements need
 * to be sorted in ascending order so that the lowest sell price is at the head
 * of the queue.
 *  
 * @author Yourii Martiak
 *
 * @param <T> - type of argument used for sorting
 */
public class TopOfAskBookComparator<T> implements Comparator<T> {

    @Override
    public int compare(T arg0, T arg1) {
        int c = ((Comparable<? super T>) arg0).compareTo(arg1);
        
        return c;
    }

}
