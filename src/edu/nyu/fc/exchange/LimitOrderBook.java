package edu.nyu.fc.exchange;

import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import orderGenerator.OrderCxR;

/**
 * Implementation of limit order book functionality, which is common between
 * bid and ask books. This class needs to be extended to implement certain
 * functionality that is expected to be different and thus defined as abstract
 * 
 * @author Yourii Martiak
 *
 */
public abstract class LimitOrderBook implements ILimitOrderBook {

    /**
     * Mapping of order IDs to actual orders. Used for quick access to an order
     * in case of cancel. The map is define static, with an assumption that
     * order IDs are unique across ask/bid books.
     */
    private static final Map<String, ILimitOrder> orderIDMap = new HashMap<String, ILimitOrder>(
            1024);

    /**
     * Mapping of price levels to the limit order queues, having one queue per
     * price level. This allows for execution of orders in price/time priority.
     */
    private final Map<Double, Deque<ILimitOrder>> priceTimePriorityMap;
    
    /**
     * Priority queue used to determine top of the book prices.
     */
    private final Queue<Double> pricePriorityQueue;

    /**
     * Creates new limit order book, in which sorting order for price levels is
     * determined by {@link java.util.Comparator}
     * 
     * @param comparator used for sorting price levels
     */
    public LimitOrderBook(final Comparator<Double> comparator) {
        priceTimePriorityMap = new HashMap<Double, Deque<ILimitOrder>>(1024);
        pricePriorityQueue = new PriorityQueue<Double>(11, comparator);
    }

    /**
     * Locate original order corresponding to this OrderCxR request
     * 
     * @param orderCxR
     *            cancel or replace request
     * @return original order matching order ID specified in OrderCxR or null if
     *         corresponding order not found
     */
    public static ILimitOrder cancelOriginalOrderFor(final OrderCxR orderCxR) {
        final String orderID = orderCxR.getOrderId();
        final ILimitOrder originalOrder = orderIDMap.get(orderID);
        if (originalOrder != null) {
            originalOrder.setDead();
        }
        return originalOrder;
    }

    @Override
    public ILimitOrder pollNextOrderFor(final double price) {
        ILimitOrder order = null;
        // go over price levels trying to find active order until all price
        // levels better or at given price have been explored
        double bestPrice = getBestPriceFor(price);
        for (Deque<ILimitOrder> list = priceTimePriorityMap.get(bestPrice); list != null
                && !list.isEmpty(); list = priceTimePriorityMap.get(bestPrice)) {
            do {
                // poll orders from the queue while removing dead orders until
                // we find first active order or reach the end of the queue
                order = list.pollFirst();
                if (list.isEmpty()) {
                    // if no more orders for this price level exist, remove best
                    // price from price priority queue and break out of the loop
                    if (pricePriorityQueue.peek() == bestPrice) {
                        pricePriorityQueue.remove();
                    }
                    break;
                }
            } while (order.isDead());

            if (order != null) {
                break;
            }
            bestPrice = getBestPriceFor(price);
        }

        return order;
    }

    @Override
    public void addFirst(final ILimitOrder limitOrder) {
        final double limitPrice = limitOrder.getLimitPrice();
        if (Double.isNaN(limitPrice)) {
            return;
        }

        Deque<ILimitOrder> list = priceTimePriorityMap.get(limitPrice);
        if (list == null) {
            list = createNewPriceLevel(limitPrice);
        } else if (list.isEmpty()) {
            pricePriorityQueue.offer(limitPrice);
        }
        orderIDMap.put(limitOrder.getOrderId(), limitOrder);
        list.offerFirst(limitOrder);
    }

    @Override
    public void addLast(final ILimitOrder limitOrder) {
        final double limitPrice = limitOrder.getLimitPrice();
        if (Double.isNaN(limitPrice)) {
            return;
        }

        Deque<ILimitOrder> list = priceTimePriorityMap.get(limitPrice);
        if (list == null) {
            list = createNewPriceLevel(limitPrice);
        } else if (list.isEmpty()) {
            pricePriorityQueue.offer(limitPrice);
        }
        orderIDMap.put(limitOrder.getOrderId(), limitOrder);
        list.offerLast(limitOrder);
    }

    @Override
    public double getBestPrice() {
        final Double bestPrice = pricePriorityQueue.peek();

        return bestPrice == null ? 0.0d : bestPrice;
    }

    private Deque<ILimitOrder> createNewPriceLevel(final double limitPrice) {
        final Deque<ILimitOrder> list = new LinkedList<ILimitOrder>();
        priceTimePriorityMap.put(limitPrice, list);
        pricePriorityQueue.offer(limitPrice);

        return list;
    }

    @Override
    public abstract double getBestPriceFor(double price);

    @Override
    public String getTopOfBook() {
        ILimitOrder topOfBookOrder = null;
        for (final double price : pricePriorityQueue) {
            final Deque<ILimitOrder> list = priceTimePriorityMap.get(price);
            if (list == null) {
                return null;
            }
            for (final ILimitOrder order : list) {
                if (!order.isDead()) {
                    topOfBookOrder = order;
                    break;
                }
            }
            if (topOfBookOrder != null) {
                return topOfBookOrder.toString();
            }
        }

        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final double price : pricePriorityQueue) {
            final Deque<ILimitOrder> list = priceTimePriorityMap.get(price);
            if (list != null) {
                for (final ILimitOrder order : list) {
                    if (!order.isDead()) {
                        sb.append(order.toStatusString());
                    }
                }
            }
        }

        return sb.toString();
    }

}
