package comp1110.ass2.OurTests;

import comp1110.ass2.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Arrays;

import static org.junit.Assert.*;

/*
Authorship: Timothy James
 */

public class TileTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(500);

    private Shape sA = Shape.B;
    private Position pA = new Position(1,3);
    private Direction dA = Direction.SOUTH;
    private Tile tA = new Tile(sA, pA, dA);
    private Tile tB = new Tile("g721");
    private Tile tC = new Tile("g723");
    private Tile tD = new Tile("h611");

    private Tile tE = new Tile("a110");
    private Tile tF = new Tile("a110");
    private Tile tG = new Tile(Shape.A,1,1,Direction.NORTH);
    private Tile tH = new Tile(Shape.A, new Position(1,1), Direction.NORTH);


    /*
     instance methods
     */

    @Test
    public void checkEquals() {
        assertEquals("Tiles created with identical strings are equal", tE, tF);
        assertEquals("Tiles created with position object constructor are equal to those created with integer position", tG, tH);
        assertEquals("Tile created with string equal to tile created with Shape, int, int, Direction", tE, tG);
        assertEquals("Tile created with string equal to tile created with Shape Position, Direction", tF, tH);
    }

    @Test
    public void checkHash() {
        assertEquals("Equal tiles have equal hashes", tE.hashCode(), tF.hashCode());
        assertEquals("Equal tiles created with different constructors (first, second) have different hashes", tE.hashCode(),tG.hashCode());

        assertNotEquals("Different tiles have different hashes",tA, tB);
        assertNotEquals("Different tiles have different hashes",tC, tD);
        assertNotEquals("Different tiles have different hashes",tA, tE);


    }

    @Test
    public void checkConstructors () {
        // check that tiles are created and compared correctly
        assertEquals("Must match Tile using (String placement) constructor input", tA, new Tile("b132"));
        assertEquals("Must match Tile using (int x, int y) constructor input", tA, new Tile(sA,1,3, dA));
        assertEquals("Must be strictly symmetric", tB, tC);
        assertNotEquals("Two different Tiles must not be equal", tA, tB);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkInvalidConstructors () {
        // check an exception is thrown for invalid inputs
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
        assertEquals("Method .getRawPlacement must return the correct value", "g723", tC.getRawPlacement());
    }

    @Test
    public void checkGetShapeArrangement () {
        // check that tiles return the correct arrangement

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

        Position[] posArrD = {
                new Position(6,1, State.WTE),
                new Position(7,1, State.WTE),
                new Position(8,1, State.RED),
                new Position(8,2, State.GRN),
                new Position(8,3, State.GRN)};
        assertArrayEquals("getShapeArrangement must match for Tile ["+tC+"]", posArrD, tD.getShapeArrangement());
        assertTrue("doesTileContainPosition contains [3,3] should return true for Tile["+tA+"]", tA.doesTileContainPosition(new Position(3, 3)));
        assertTrue("doesTileContainPosition contains [8,3,GRN] should return true for Tile["+tD+"]", tD.doesTileContainPosition(new Position(8, 3, State.GRN)));
    }


    /*
     class methods
     */

    @Test
    public void checkPlacementToField () {
        assertEquals("Method .placementToShape must return the correct value",
                sA,
                Tile.placementToShape("b132"));
        assertEquals("Method .placementToPosition must return the correct value",
                pA,
                Tile.placementToPosition("b132"));
        assertEquals("Method .placementToDirection must return the correct value",
                dA,
                Tile.placementToDirection("b132"));
        assertEquals("Method .placementToDirection must return the correct value (symmetric)",
                Direction.EAST,
                Tile.placementToDirection("f003"));
    }

    @Test
    public void checkPlacementConversion () {
        String placementString = "a000b013c113d302e323f400g420h522i613j701";
        String[] placementStringArr = {
                "a000",
                "b013",
                "c113",
                "d302",
                "e323",
                "f400",
                "g420",
                "h522",
                "i613",
                "j701"
        };
        assertArrayEquals("Must be divided into length 4 substrings where " +
                        "each substring represents a piece placement",
                placementStringArr,
                Tile.placementToPieceArray(placementString));

        Tile[] placementTileArr = {
                new Tile("a000"),
                new Tile("b013"),
                new Tile("c113"),
                new Tile("d302"),
                new Tile("e323"),
                new Tile("f400"),
                new Tile("g420"),
                new Tile("h522"),
                new Tile("i613"),
                new Tile("j701")
        };
        assertArrayEquals("Must be broken into separate instances of " +
                        "Tiles each representing a single piece placement",
                placementTileArr,
                Tile.placementToTileArray(placementString));

        assertEquals("Placement Strings must contain blocks of 4 characters " +
                        "that represent individual piece placements",
                placementString,
                Tile.pieceArrayToPlacement(placementStringArr));

        assertEquals("Placement Strings must contain blocks of 4 characters " +
                        "that represent individual piece placements",
                placementString,
                Tile.tileArrayToPlacement(placementTileArr));
    }
}
