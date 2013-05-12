package edu.nyu.fc.exchange;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import orderGenerator.Message;
import orderGenerator.NewOrder;
import orderGenerator.OrderCxR;

/**
 * Implementation of double limit book auction. This implementation consists
 * mainly of two maps holding references to two sided books implementations -
 * bid and ask, keyed by the symbol.
 * 
 * @author Yourii Martiak
 *
 */
public class DoubleLimitBookAuction implements IDoubleLimitBookAuction {
    
    /**
     * Map that holds references to ASK limit order books, that can be accessed
     * through the symbol
     */
    private final Map<String,ILimitOrderBook> askLimitOrderBookMap;
    
    /**
     * Map that holds references to BID limit order books, that can be accessed
     * through the symbol
     */
    private final Map<String,ILimitOrderBook> bidLimitOrderBookMap;

    /**
     * Creates new instance of double limit book auction
     */
    public DoubleLimitBookAuction() {
        askLimitOrderBookMap = new HashMap<String,ILimitOrderBook>();
        bidLimitOrderBookMap = new HashMap<String,ILimitOrderBook>();
    }
    
    /**
     * Get ASK limit order book corresponding to a given symbol
     * 
     * @param symbol
     * @return reference to the limit order book
     */
    private ILimitOrderBook getAskBookForSymbol(String symbol) {
        ILimitOrderBook book = askLimitOrderBookMap.get(symbol);
        if (book == null) {
            book = new AskLimitOrderBook();
            askLimitOrderBookMap.put(symbol, book);
        }
        return book;
    }
    
    /**
     * Get BID limit order book corresponding to a given symbol
     * 
     * @param symbol
     * @return reference to the limit order book
     */
    private ILimitOrderBook getBidBookForSymbol(String symbol) {
        ILimitOrderBook book = bidLimitOrderBookMap.get(symbol);
        if (book == null) {
            book = new BidLimitOrderBook();
            bidLimitOrderBookMap.put(symbol, book);
        }
        return book;
    }

    @Override
    public final void handleMessage(final Message message) {
        if (message instanceof NewOrder) {
            handleNewOrderMessage(new LimitOrder((NewOrder) message));
        } else if (message instanceof OrderCxR) {
            handleOrderCxRMessage((OrderCxR) message);
        } else {
            System.err.println("Invalid message " + message);
        }
    }

    /**
     * Handle new order, which may or may not result in execution against the
     * opposite side matching price limits (or best price for market orders).
     * 
     * @param order
     */
    private void handleNewOrderMessage(final ILimitOrder order) {
        // first check if the order is buy or sell
        final int size = order.getSize();
        final double limitPrice = order.getLimitPrice();
        final String symbol = order.getSymbol();
        final ILimitOrderBook bidLimitOrderBook = getBidBookForSymbol(symbol);
        final ILimitOrderBook askLimitOrderBook = getAskBookForSymbol(symbol);
        if (size < 0) {
            // handle sell, check if we have the opposite side order to cross
            ILimitOrder bid = null;
            do {
                bid = bidLimitOrderBook
                    .pollNextOrderFor(limitPrice); 
                if (bid != null) {
                    newTrade(bid, order);
                    if (bid.getSize() > 0) {
                        // put leaves quantity back in front of the queue for a
                        // given price level
                        bidLimitOrderBook.addFirst(bid);
                    }
                }
            } while (order.getSize() < 0 && bid != null);
            
            if (order.getSize() < 0) {
                // put leaves quantity at the end of the queue for a given
                // price level
                askLimitOrderBook.addLast(order);
            }
        } else {
            // handle buy, check if we have the opposite side to cross
            ILimitOrder ask = null;
            do {
                ask = askLimitOrderBook
                    .pollNextOrderFor(limitPrice);
                if (ask != null) {
                    newTrade(order, ask);
                    if (ask.getSize() < 0) {
                        // put leaves quantity back in front of the queue for a
                        // given price level
                        askLimitOrderBook.addFirst(ask);
                    }
                }                
            } while (order.getSize() > 0 && ask != null);

            if (order.getSize() > 0) {
                // put leaves quantity at the end of the queue for a given
                // price level
                bidLimitOrderBook.addLast(order);
            }
        }
    }

    /**
     * Handle order cancels and replaces through this method. In case of straight
     * cancel, orders get marked as "dead". In case of replace, a new order is
     * also created.
     * 
     * @param orderCxR
     */
    private void handleOrderCxRMessage(final OrderCxR orderCxR) {
        // first, cancel original order
        final ILimitOrder originalOrder = LimitOrderBook
                .cancelOriginalOrderFor(orderCxR);

        // check if this is a straight cancel
        final int size = orderCxR.getSize();
        if (size != 0 && originalOrder != null) {
            // handle replace
            handleNewOrderMessage(new LimitOrderReplace(originalOrder, orderCxR));
        }
    }

    @Override
    public void newTrade(final ILimitOrder bid, final ILimitOrder ask) {
        final int sizeDelta = bid.getSize() + ask.getSize();
        if (sizeDelta > 0) {
            // bid order has leaves quantity
            bid.setSize(sizeDelta);
            ask.setSize(0);
        } else if (sizeDelta < 0) {
            // ask order has leaves quantity
            ask.setSize(sizeDelta);
            bid.setSize(0);
        } else {
            // both orders fully filled
            bid.setSize(0);
            ask.setSize(0);
        }

        System.out.println(String.format("Order %s traded with order %s",
                bid.getOrderId(), ask.getOrderId()));
    }

    @Override
    public String getTopOfTheBooks() {
        StringBuilder sb = new StringBuilder("TOP OF BOOK\n============");
        sb.append("\nBID\n===\n");
        Collection<ILimitOrderBook> bidBooks = bidLimitOrderBookMap.values();
        for (ILimitOrderBook book : bidBooks) {
            sb.append(book.getTopOfBook()).append("\n");
        }
        sb.append("\nASK\n===\n");
        Collection<ILimitOrderBook> askBooks = askLimitOrderBookMap.values();
        for (ILimitOrderBook book : askBooks) {
            sb.append(book.getTopOfBook()).append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BOOK STATUS\n============");
        sb.append("\nBID\n===\n");
        Collection<ILimitOrderBook> bidBooks = bidLimitOrderBookMap.values();
        for (ILimitOrderBook book : bidBooks) {
            sb.append(book).append("\n");
        }
        sb.append("\nASK\n===\n");
        Collection<ILimitOrderBook> askBooks = askLimitOrderBookMap.values();
        for (ILimitOrderBook book : askBooks) {
            sb.append(book).append("\n");
        }
        return sb.toString();
    }
}
