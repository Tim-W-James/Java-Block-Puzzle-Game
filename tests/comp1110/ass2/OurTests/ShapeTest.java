package comp1110.ass2.OurTests;

import comp1110.ass2.Direction;
import comp1110.ass2.Shape;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShapeTest {

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
}