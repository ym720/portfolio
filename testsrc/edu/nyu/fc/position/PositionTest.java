package edu.nyu.fc.position;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class to verify that functionality of
 * {@link edu.nyu.fc.position.Position} class works as expected.
 * 
 * @author Yourii Martiak
 * 
 */
public class PositionTest {

    /**
     * Instance of position under test
     */
    IPosition position;

    /**
     * Setup and initialize resources needed before invocation of each test
     * method listed in this test class.
     */
    @Before
    public void setUp() {
        position = new Position(100, "IBM");
    }

    /**
     * Test and verify that a new instance of position successfully created as a
     * result of a previous call to setUp() method
     */
    @Test
    public void testPosition() {
        assertNotNull("Position created", position);
    }

    /**
     * Test new position creation and verify that the symbol returned by
     * getSymbol() method corresponds to the expected symbol value for which
     * this position was created.
     */
    @Test
    public void testGetSymbol() {
        assertEquals("IBM", position.getSymbol());
    }

    /**
     * Test new position creation and verify that the quantity returned by
     * getQuantity() method corresponds to the expected quantity value for which
     * this position was created.
     */
    @Test
    public void testGetQuantity() {
        assertEquals(100, position.getQuantity());
    }

    /**
     * Test and verify that PositionQuantityException is thrown during an
     * attempt to create new position with zero quantity.
     */
    @Test(expected = PositionQuantityException.class)
    public void testPosition_ZeroQuantity() {
        position = new Position(0, "IBM");
    }

    /**
     * Test and verify that IllegalArgumentException is thrown during an attempt
     * to create new position with null symbol.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPosition_NullSymbol() {
        position = new Position(100, null);
    }

}
