package edu.nyu.fc.pricing;

import org.junit.Test;

public class PayoutCalculatorTest {

    /**
     * Test and verify that calculator with valid parameters starts
     */
    @Test(expected = java.lang.RuntimeException.class)
    public void testPayoutCalculator_Valid() {
        final String[] args = { "REQUEST.OPT", "RESPONSE.OPT" };
        // this is a hack to shut down calculator and let another test proceed
        final Thread stopThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Closing calculator ...");
                PayoutCalculator.close();
            }
        };
        stopThread.start();
        PayoutCalculator.main(args);
    }

    /**
     * Test and verify that calculator with valid parameters starts
     */
    @Test(expected = java.lang.RuntimeException.class)
    public void testPayoutCalculator_Invalid() {
        final String[] args = { "REQUEST.OPT" };
        // this is a hack to shut down calculator and let another test proceed
        final Thread stopThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Closing calculator ...");
                PayoutCalculator.close();
            }
        };
        stopThread.start();
        PayoutCalculator.main(args);
    }

}
