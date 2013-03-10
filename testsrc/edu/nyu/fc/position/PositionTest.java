package edu.nyu.fc.position;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class PositionTest {
    
    IPosition position;
    
    @Before
    public void setUp() {
        position = new Position(100, "IBM");        
    }

    @Test
    public void testPosition() {
        assertNotNull("Position created", position);
    }

    @Test
    public void testGetSymbol() {
        assertEquals("IBM", position.getSymbol());
    }

    @Test
    public void testGetQuantity() {
        assertEquals(100, position.getQuantity());
    }
    
    @Test (expected=PositionQuantityException.class)
    public void testPosition_ZeroQuantity() {
        position = new Position(0, "IBM");
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void testPosition_NullSymbol() {
        position = new Position(100, null);
    }

}
