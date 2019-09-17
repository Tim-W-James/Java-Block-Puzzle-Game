package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TileTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(120000);

    private Shape sA = Shape.B;
    private Position pA = new Position(1,3);
    private Direction dA = Direction.SOUTH;
    private Tile tA = new Tile(sA, pA, dA);
    private Tile tB = new Tile("g721");
    private Tile tC = new Tile("g723");
    private Tile tD = new Tile("h611");

    @Test
    public void checkConstructors () {
        assertEquals("Must match Tile using (String placement) constructor input", tA, new Tile("b132"));
        assertEquals("Must match Tile using (int x, int y) constructor input", tA, new Tile(sA,1,3, dA));
        assertEquals("Must be strictly symmetric", tB, tC);
        assertNotEquals("Two different Tiles must not be equal", tA, tB);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkInvalidConstructors () { // check an exception is thrown for invalid inputs
        new Tile("12345"); // placement input must be 4 characters long
        new Tile("123"); // placement input must be 4 characters long
        new Tile("z000"); // the first character must be in the range a .. j
        new Tile("a900"); // the second character must be in the range 0 .. 8
        new Tile("a050"); // the third character must be in the range 0 .. 4
        new Tile("a004"); // the fourth character must be in the range 0 .. 3
    }

    @Test
    public void checkGetFieldMethods () {
        assertEquals("Method .getShape must return the correct value", sA, tA.getShape());
        assertEquals("Method .getPosition must return the correct value", pA, tA.getPosition());
        assertEquals("Method .getDirection must return the correct value", dA, tA.getDirection());
        assertEquals("Method .getPlacement must return the correct value", "b132", tA.getPlacement());
    }

    @Test
    public void checkGetShapeArrangement () {
        Position[] posArrA = {
                new Position(1,4, State.GRN),
                new Position(2,4, State.GRN),
                new Position(3,4, State.BLE),
                new Position(3,3, State.WTE),
                new Position(4,3, State.WTE)};
        assertArrayEquals("getShapeArrangement must match for Tile ["+tA+"]", posArrA, tA.getShapeArrangement());

        Position[] posArrB = {
                new Position(8,2, State.WTE),
                new Position(7,3, State.BLE),
                new Position(8,3, State.BLE),
                new Position(7,4, State.WTE)};
        assertArrayEquals("getShapeArrangement must match for Tile ["+tB+"]", posArrB, tB.getShapeArrangement());
        assertArrayEquals("getShapeArrangement must match for Tile ["+tC+"] (symmetric)", posArrB, tC.getShapeArrangement());

        System.out.println(Arrays.toString(tC.getShapeArrangement()));
        Position[] posArrD = {
                new Position(6,1, State.WTE),
                new Position(7,1, State.WTE),
                new Position(8,1, State.RED),
                new Position(8,2, State.GRN),
                new Position(8,3, State.GRN)};
        assertArrayEquals("getShapeArrangement must match for Tile ["+tC+"]", posArrD, tD.getShapeArrangement());
    }
}
