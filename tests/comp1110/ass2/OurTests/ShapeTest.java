package comp1110.ass2.OurTests;

import comp1110.ass2.Direction;
import comp1110.ass2.Shape;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

/*
Authorship: Timothy James, Nicholas Dale
 */

public class ShapeTest {

    @Rule
    public Timeout globalTimeout = Timeout.millis(500);

    @Test
    public void getMaxReach() {
        // check the max reach for given shapes
        assertEquals("Max reach for G should be 3",
                3,
                Shape.G.getMaxReach());
        assertEquals("Max reach for J should be 4",
                4,
                Shape.J.getMaxReach());
        assertEquals("Max reach for I should be 2",
                2,
                Shape.I.getMaxReach());

        // check different rotations
        assertEquals("Max vert reach for G facing south should be 2",
                2,
                Shape.G.getMaxReach(Direction.SOUTH, true));
        assertEquals("Max hori reach for J facing north should be 4",
                4,
                Shape.J.getMaxReach(Direction.NORTH, false));
        assertEquals("Max vert reach for I facing west should be 2",
                2,
                Shape.I.getMaxReach(Direction.WEST, true));
    }

    @Test
    public void returnCorrectLetters() {
        assertEquals("Shape A returns 'a'", 'a',
                Shape.A.toChar());
        assertEquals("Shape B returns 'b'", 'b',
                Shape.B.toChar());
        assertEquals("Shape C returns 'c'", 'c',
                Shape.C.toChar());
        assertEquals("Shape D returns 'd'", 'd',
                Shape.D.toChar());
        assertEquals("Shape E returns 'e'", 'e',
                Shape.E.toChar());
        assertEquals("Shape F returns 'f'", 'f',
                Shape.F.toChar());
        assertEquals("Shape G returns 'g'", 'g',
                Shape.G.toChar());
        assertEquals("Shape H returns 'h'", 'h',
                Shape.H.toChar());
        assertEquals("Shape I returns 'i'", 'i',
                Shape.I.toChar());
        assertEquals("Shape J returns 'j'", 'j',
                Shape.J.toChar());

    }

    @Test
    public void createShapeFromChar() {
        assertEquals("Shape A created from 'a'", Shape.A,
                Shape.charToShape('a'));
        assertEquals("Shape E created from 'e'", Shape.E,
                Shape.charToShape('e'));
        assertEquals("Shape J created from 'j'", Shape.J,
                Shape.charToShape('j'));

    }
}