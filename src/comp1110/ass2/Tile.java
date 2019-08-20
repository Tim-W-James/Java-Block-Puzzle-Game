package comp1110.ass2;

import static comp1110.ass2.Direction.*;

// Tile stores information from a piece placement string,
// provides more intuitive methods for dealing with tiles
public class Tile {

    private Shape shape;   // encoded at [0], in the range a .. j
    private Position pos;  // encoded x at [1], in the range 0 .. 8, and y at [2], in the range 0 .. 4
    private Direction dir; // encoded at [3], in the range 0 .. 3
    private String placement;
    static private String invalidTileMsg = "Invalid Placement Input, \n\tPlease ensure:\n" +
            "\t- the first character is in the range a .. j (shape)\n" +
            "\t- the second character is in the range 0 .. 8 (column)\n" +
            "\t- the third character is in the range 0 .. 4 (row)\n" +
            "\t- the fourth character is in the range 0 .. 3 (orientation)";

    public Tile (String piecePlacement) {
        // ensure placement is valid
        if (FocusGame.isPiecePlacementWellFormed(piecePlacement)) {
            this.shape = placementToShape(piecePlacement);
            this.pos = placementToPosition(piecePlacement);
            this.dir = placementToDirection(piecePlacement);
            this.placement = piecePlacement;
        }
        else
            throw new IllegalArgumentException(invalidTileMsg);
    }

    /*
     get methods
      */

    public Shape getShape() { return shape; }

    public Position getPosition() { return pos; }

    public Direction getDirection() { return dir; }

    public String getPlacement() { return placement; }

    /*
     class methods to convert from piece placement String
      */

    public static Shape placementToShape (String piecePlacement) {
        if (!FocusGame.isPiecePlacementWellFormed(piecePlacement)) // check input is valid
            throw new IllegalArgumentException(invalidTileMsg);

        // shape indexed at [0]
        return Shape.valueOf(Character.toString((piecePlacement.charAt(0) - 32)).toUpperCase());
    }

    public static Position placementToPosition (String piecePlacement) {
        if (!FocusGame.isPiecePlacementWellFormed(piecePlacement)) // check input is valid
            throw new IllegalArgumentException(invalidTileMsg);

        // position indexed at x: [1] and y: [2]
        return new Position(
            Character.getNumericValue(piecePlacement.charAt(1)),
            Character.getNumericValue(piecePlacement.charAt(2)));
    }

    public static Direction placementToDirection (String piecePlacement) {
        if (!FocusGame.isPiecePlacementWellFormed(piecePlacement)) // check input is valid
            throw new IllegalArgumentException(invalidTileMsg);

        switch (piecePlacement.charAt(3)) { // direction indexed at [3]
            case '0':
                return NORTH;

            case '1':
                return EAST;

            case '2':
                return SOUTH;

            default:
                return WEST;
        }
    }

    @Override
    public String toString() {
        return "Shape: "+shape+", X: "+pos.getX()+", Y: "+pos.getY()+", Dir: "+dir;
    }
}
