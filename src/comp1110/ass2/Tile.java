package comp1110.ass2;

import static comp1110.ass2.Direction.*;


public class Tile {

    private Shape shape;
    private Position pos;
    private Direction dir;
    private String placement;

    public Tile (String piecePlacement) {
        if (FocusGame.isPiecePlacementWellFormed(piecePlacement)) {
            this.shape = placementToShape(piecePlacement);
            this.pos = placementToPosition(piecePlacement);
            this.dir = placementToDirection(piecePlacement);
            this.placement = piecePlacement;
        }
        else
            throw new IllegalArgumentException("Invalid Placement Input");
    }

    public Shape getTileType() {
        return shape;
    }

    public Position getPosition() {
        return pos;
    }

    public Direction getDirection() {
        return dir;
    }

    public String getPlacement() { return placement; }

    public static Shape placementToShape (String placement) {
        // shape indexed at [0]
        return Shape.valueOf(Character.toString((placement.charAt(0) - 32)).toUpperCase());
    }

    public static Position placementToPosition (String placement) {
        // position indexed at x: [1] and y: [2]
        return new Position(
                Character.getNumericValue(placement.charAt(1)),
                Character.getNumericValue(placement.charAt(2)));
    }

    public static Direction placementToDirection (String placement) {
        switch (placement.charAt(3)) { // direction indexed at [3]
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
}
