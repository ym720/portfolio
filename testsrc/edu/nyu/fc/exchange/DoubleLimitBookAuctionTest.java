package edu.nyu.fc.exchange;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import orderGenerator.Message;
import orderGenerator.NewOrder;
import orderGenerator.OrderCxR;
import orderGenerator.OrdersIterator;

import org.junit.Before;
import org.junit.Test;

public class DoubleLimitBookAuctionTest {
    
    private OrdersIterator ordersIterator;
    
    @Before
    public void setUp() {
        ordersIterator = new OrdersIterator();
    }

    @Test
    public void test() {
        IDoubleLimitBookAuction auction = new DoubleLimitBookAuction();
        Iterator<Message> iterator = ordersIterator.getIterator();
        int i = 0;
        while (iterator.hasNext()) {
            Message message = iterator.next();
            auction.handleMessage(message);
                System.out.println(auction.getTopOfTheBooks());
                System.out.println(auction);
            i++;
        } 
        System.out.println(auction.getTopOfTheBooks());
    }
    
    public class OrdersIterator {
        
        List<Message> list;
        
        public OrdersIterator() {
            list = new ArrayList<Message>();
            init();
        }
        
        private final void init() {
            list.add(new NOMessage("IBM1",100,"IBM",100.0));
            list.add(new NOMessage("IBM2",100,"IBM",101.0));
            list.add(new NOMessage("IBMX",-150,"IBM",Double.NaN));
            list.add(new NOMessage("IBM3",100,"IBM",Double.NaN));
            list.add(new NOMessage("MSFT1",100,"MSFT",100.0));
            list.add(new NOMessage("MSFT2",100,"MSFT",101.0));
            list.add(new NOMessage("MSFT3",-20,"MSFT",100.0));
            list.add(new NOMessage("MSFT4",-10,"MSFT",101.0));
            list.add(new NOMessage("MSFT5",-30,"MSFT",Double.NaN));
            list.add(new NOMessage("MSFT6",-50,"MSFT",100.0));
            list.add(new NOMessage("MSFT7",-300,"MSFT",101.0));
        }
        
        public Iterator<Message> getIterator() {
            return list.iterator();
            
        }
    }
    
    private class NOMessage implements NewOrder {
        
        private final String orderID;
        private final int size;
        private final String symbol;
        private final double limitPrice;
        
        public NOMessage(String orderID, int size, String symbol, double limitPrice) {
            this.orderID = orderID;
            this.size = size;
            this.symbol = symbol;
            this.limitPrice = limitPrice;
        }

        @Override
        public double getLimitPrice() {
            return limitPrice;
        }

        @Override
        public String getOrderId() {
            return orderID;
        }

        @Override
        public int getSize() {
            return size;
        }

        @Override
        public String getSymbol() {
            return symbol;
        }
        
    }
    
    private class CXRMessage implements OrderCxR {
        
        private final String orderID;
        private final int size;
        private final String symbol;
        private final double limitPrice;
        
        public CXRMessage(String orderID, int size, String symbol, double limitPrice) {
            this.orderID = orderID;
            this.size = size;
            this.symbol = symbol;
            this.limitPrice = limitPrice;
        }

        @Override
        public double getLimitPrice() {
            return limitPrice;
        }

        @Override
        public String getOrderId() {
            return orderID;
        }

        @Override
        public int getSize() {
            return size;
        }
        
    }

}
