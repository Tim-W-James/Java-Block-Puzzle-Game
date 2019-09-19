package comp1110.ass2.OurTests;

import comp1110.ass2.Position;
import comp1110.ass2.State;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class PositionTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(120000);

    private Position pA = new Position(1,2);
    private Position pB = new Position(4, 5);
    private Position pC = new Position(1,2, State.RED);
    private Position pD = new Position(1,2, State.GRN);
    private Position pE = new Position(4,5, State.RED);

    @Test
    public void checkConstructors () {
        assertNotEquals("Different coordinates must not be equal", pA, pB);
        assertNotEquals("Different coordinates must not be equal", pC, pE);
        assertNotEquals("Different States must not be equal", pC, pD);
        assertNotEquals("Different States must not be equal", pA, pC);
    }

    @Test
    public void checkGetMethods () {
        assertEquals("Method .getX must return the correct value", 1, pA.getX());
        assertEquals("Method .getY must return the correct value", 2, pA.getY());
        assertNull("Method .getS must return the correct value for Stateless Positions", pA.getS());
        assertEquals("Method .getS must return the correct value", State.RED, pC.getS());
        assertEquals("Method .getPosString must return the correct value", "45", pB.getPosString());
    }
}
