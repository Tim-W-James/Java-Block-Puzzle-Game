package comp1110.ass2.OurTests;

import comp1110.ass2.Position;
import comp1110.ass2.State;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class PositionTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(500);

    // create positions to reference
    private Position pA = new Position(1,2);
    private Position pB = new Position(1,2);
    private Position pC = new Position(4, 5);
    private Position pD = new Position(1,2, State.RED);
    private Position pE = new Position(1,2, State.GRN);
    private Position pF = new Position(4,5, State.RED);
    private Position pG = new Position(4,5, State.RED);

    @Test
    public void checkConstructorsEqual() {
        // check that positions are created and compared correctly
        assertEquals("Same coordinates must be equal", pA, pB);
        assertNotEquals("Different coordinates must not be equal", pA, pC);
        assertNotEquals("Different coordinates must not be equal", pD, pF);
        assertEquals("Same coordinates must be equal", pA, pD);
        assertNotEquals("Different States must not be equal", pD, pE);
        assertEquals("Same coordinates and States must be equal", pF, pG);
    }

    @Test
    public void checkGetMethods () {
        // check get methods for positions with and without states
        assertEquals("Method .getX must return the correct value", 1, pA.getX());
        assertEquals("Method .getY must return the correct value", 2, pA.getY());
        assertNull("Method .getS must return the correct value for Stateless Positions", pA.getS());
        assertEquals("Method .getS must return the correct value", State.RED, pD.getS());
        assertEquals("Method .getPosString must return the correct value", "45", pC.getPosString());
    }
}
