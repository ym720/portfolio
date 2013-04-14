package edu.nyu.fc.pricing;

import org.junit.Test;

public class MonteCarloSimulationTest {

    /**
     * Test and verify that simulation with valid parameters for American call
     * option starts and completes without errors
     */
    @Test
    public void testMonteCarloSimulation_US_CALL() {
        String[] args = {
                "d=252",
                "p=152.35",
                "r=0.0001",
                "s=0.01",
                "sp=165",
                "pr=2.05",
                "e=0.01"
        };
        
        MonteCarloSimulation.main(args);
    }
    
    /**
     * Test and verify that simulation with valid parameters for Asian call
     * option starts and completes without errors
     */
    @Test
    public void testMonteCarloSimulation_AS_CALL() {
        String[] args = {
                "d=252",
                "p=152.35",
                "r=0.0001",
                "s=0.01",
                "sp=164",
                "pr=2.05",
                "e=0.01",
                "t=AS_CALL",
                "ds=ATD"
        };
        
        MonteCarloSimulation.main(args);
    }
    
    /**
     * Test and verify that simulation fails when one or more required parameter
     * is missing
     */
    @Test(expected = RuntimeException.class)
    public void testMonteCarloSimulation_AS_CALL_Required_Missing() {
        String[] args = {
                "p=152.35",
                "r=0.0001",
                "s=0.01",
                "sp=164",
                "pr=2.05",
                "e=0.01",
                "t=AS_CALL",
                "ds=ATD"
        };
        
        MonteCarloSimulation.main(args);
    }
    
    /**
     * Test and verify that simulation fails when one or more required parameter
     * has bad format
     */
    @Test(expected = RuntimeException.class)
    public void testMonteCarloSimulation_AS_CALL_Required_Bad_Format() {
        String[] args = {
                "d=252",
                "p152.35",
                "r=0.0001",
                "s=0.01",
                "sp=164",
                "pr=2.05",
                "e=0.01",
                "t=AS_CALL",
                "ds=ATD"
        };
        
        MonteCarloSimulation.main(args);
    }
    
    /**
     * Test and verify that simulation fails when one or more optional parameter
     * has wrong values
     */
    @Test(expected = RuntimeException.class)
    public void testMonteCarloSimulation_AS_CALL_Optional_Bad_Value() {
        String[] args = {
                "d=252",
                "p=152.35",
                "r=0.0001",
                "s=0.01",
                "sp=164",
                "pr=2.05",
                "e=0.01",
                "t=AS_CALL",
                "ds=ABC"
        };
        
        MonteCarloSimulation.main(args);
    }
    
    /**
     * Test and verify that simulation with valid parameters for Asian call
     * option starts and completes without errors
     */
    @Test (expected=RuntimeException.class)
    public void testMonteCarloSimulation_AS_CALL_Distributed() {
        
        // this is a hack to start simulation in another thread and shut down 
        // payout calculator when simulation is finished
        final Thread thread = new Thread() {
            @Override
            public void run() {
                
                final String[] args = {
                        "d=252",
                        "p=152.35",
                        "r=0.0001",
                        "s=0.01",
                        "sp=164",
                        "pr=2.05",
                        "e=0.01",
                        "t=AS_CALL",
                        "ds=ATD",
                        "req=RESPONSE.OPT",
                        "rsp=REQUEST.OPT"
                };
                
                MonteCarloSimulation.main(args);
                System.out.println("Closing calculator ...");
                PayoutCalculator.close();
            }
        };
        thread.start();
        
        final String[] args = { "REQUEST.OPT", "RESPONSE.OPT" };
        PayoutCalculator.main(args);

    }
    
    /**
     * Test and verify that simulation with valid parameters for Asian call
     * option starts and completes without errors
     */
    @Test
    public void testMonteCarloSimulation_AS_CALL_Distributed_Multi() {
        
        // this is a hack to start simulation in another thread and shut down 
        // payout calculator when simulations are finished
        final Thread thread1 = new Thread() {
            @Override
            public void run() {
                
                final String[] args = {
                        "d=252",
                        "p=152.35",
                        "r=0.0001",
                        "s=0.01",
                        "sp=165",
                        "pr=2.05",
                        "e=0.01",
                        "t=US_CALL",
                        "ds=ATD",
                        "req=RESPONSE.OPT",
                        "rsp=REQUEST.OPT"
                };
                
                MonteCarloSimulation.main(args);
            }
        };
        thread1.start();
        
        // start another simulation in parallel
        final Thread thread2 = new Thread() {
            @Override
            public void run() {
                
                final String[] args = {
                        "d=252",
                        "p=152.35",
                        "r=0.0001",
                        "s=0.01",
                        "sp=164",
                        "pr=2.05",
                        "e=0.01",
                        "t=AS_CALL",
                        "ds=ATD",
                        "req=RESPONSE.OPT",
                        "rsp=REQUEST.OPT"
                };
                
                MonteCarloSimulation.main(args);
            }
        };
        thread2.start();
        
        // start payout calculator in parallel
        final Thread thread3 = new Thread() {
            @Override
            public void run() {                
                // start payout calculator
                final String[] args = { "REQUEST.OPT", "RESPONSE.OPT" };
                PayoutCalculator.main(args);
            }
        };
        thread3.start();
        
        // after both simulations finished, stop the payout calculator
        try {
            thread1.join();
            thread2.join();
            System.out.println("Closing calculator ...");
            PayoutCalculator.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
