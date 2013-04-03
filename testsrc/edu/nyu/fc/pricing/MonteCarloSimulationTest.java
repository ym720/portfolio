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

}
