package edu.nyu.fc.position;

/**
 * Default implementation of {@link edu.nyu.fc.position.IPosition} interface. An
 * instance of this class is immutable, updates on an existing instance are not
 * possible once the object has been created. A new instance must be created to
 * replace an existing object in memory in order for an update operation to take
 * place.
 * 
 * @author Yourii Martiak
 * 
 */
public class Position implements IPosition {

    /**
     * Number of shares current position holds
     */
    private final int quantity;

    /**
     * Security symbol that identified this position
     */
    private final String symbol;

    /**
     * One and only public constructor that allows external client to create an
     * instance of {@link Position} such that instance cannot be changed once
     * created. This class does not allow clients to create position with zero
     * quantity and/or null security symbol values.
     * 
     * @param quantity
     *            number of shares that this position currently holds
     * @param symbol
     *            security symbol for an asset that this position currently
     *            holds
     * 
     * @throws PositionQuantityException
     *             if an attempt to create a position with zero quantity is made
     * @throws IllegalArgumentException
     *             if an attempt to create a position with null security symbol
     *             is made
     */
    public Position(final int quantity, final String symbol) {
        // we do not allow positions with zero quantity because that indicates
        // that no position exists
        if (quantity == 0) {
            throw new PositionQuantityException("Invalid position quantity 0");
        }
        // position with null security symbol is invalid
        if (symbol == null) {
            throw new IllegalArgumentException("Invalid symbol null");
        }
        this.quantity = quantity;
        this.symbol = symbol;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

}
