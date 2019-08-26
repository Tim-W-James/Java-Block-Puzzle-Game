package comp1110.ass2;

import static comp1110.ass2.Direction.*;
import static comp1110.ass2.State.*;

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

    /**
     get methods
      */

    public Shape getShape() { return shape; }

    public Position getPosition() { return pos; }

    public Direction getDirection() { return dir; }

    public String getPlacement() { return placement; }

    /**
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

        // encode vertical symmetry
        if ((placementToShape(piecePlacement) == Shape.F ||
                placementToShape(piecePlacement) == Shape.G) &&
                piecePlacement.charAt(3) == '2')
            return NORTH;

        // encode horizontal symmetry
        else if ((placementToShape(piecePlacement) == Shape.F ||
                placementToShape(piecePlacement) == Shape.G) &&
                piecePlacement.charAt(3) == '3')
            return EAST;

        // encode as normal
        else {
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
    }

    // returns an array of positions a tile has, given a shape and direction,
    // relative to [0][0], with [x][y] origin position offset
    public Position[] getShapeArrangement () {
        Position[] result;

        // case by case for direction and shape
        switch (getDirection()) {
            case NORTH:
                switch (getShape()) {
                    case A:
                        result = new Position[4];
                        result[0] = new Position(pos.getX(), pos.getY(), GRN);
                        result[1] = new Position(1+pos.getX(),pos.getY(), WTE);
                        result[2] = new Position(2+pos.getX(),pos.getY(), RED);
                        result[3] = new Position(1+pos.getX(),1+pos.getY(), RED);
                        return result;

                    case B:
                        result = new Position[5];
                        result[0] = new Position(1+pos.getX(), pos.getY(), BLE);
                        result[1] = new Position(pos.getX(),1+pos.getY(), WTE);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), WTE);
                        result[3] = new Position(2+pos.getX(),pos.getY(), GRN);
                        result[4] = new Position(3+pos.getX(),pos.getY(), GRN);
                        return result;

                    case C:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), 1+pos.getY(), RED);
                        result[1] = new Position(1+pos.getX(),1+pos.getY(), RED);
                        result[2] = new Position(2+pos.getX(), 1+pos.getY(), WTE);
                        result[3] = new Position(3+pos.getX(),1+pos.getY(), BLE);
                        result[4] = new Position(2+pos.getX(),pos.getY(), GRN);
                        return result;

                    case D:
                        result = new Position[4];
                        result[0] = new Position(pos.getX(), pos.getY(), RED);
                        result[1] = new Position(1+pos.getX(), pos.getY(), RED);
                        result[2] = new Position(2+pos.getX(), pos.getY(), RED);
                        result[3] = new Position(2+pos.getX(),1+pos.getY(), BLE);
                        return result;

                    case E:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), pos.getY(), BLE);
                        result[1] = new Position(1+pos.getX(), pos.getY(), BLE);
                        result[2] = new Position(2+pos.getX(), pos.getY(), BLE);
                        result[3] = new Position(pos.getX(),1+pos.getY(), RED);
                        result[4] = new Position(1+pos.getX(),1+pos.getY(), RED);
                        return result;

                    case F:
                        result = new Position[3];
                        result[0] = new Position(pos.getX(), pos.getY(), WTE);
                        result[1] = new Position(1+pos.getX(), pos.getY(), WTE);
                        result[2] = new Position(2+pos.getX(), pos.getY(), WTE);
                        return result;

                    case G:
                        result = new Position[4];
                        result[0] = new Position(pos.getX(), pos.getY(), WTE);
                        result[1] = new Position(1+pos.getX(), pos.getY(), BLE);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), BLE);
                        result[3] = new Position(2+pos.getX(), 1+pos.getY(), WTE);
                        return result;

                    case H:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), pos.getY(), RED);
                        result[1] = new Position(1+pos.getX(), pos.getY(), GRN);
                        result[2] = new Position(2+pos.getX(), pos.getY(), GRN);
                        result[3] = new Position(pos.getX(), 1+pos.getY(), WTE);
                        result[4] = new Position(pos.getX(), 2+pos.getY(), WTE);
                        return result;

                    case I:
                        result = new Position[3];
                        result[0] = new Position(pos.getX(), pos.getY(), BLE);
                        result[1] = new Position(1+pos.getX(), pos.getY(), BLE);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), WTE);
                        return result;

                    default:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), pos.getY(), GRN);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), GRN);
                        result[2] = new Position(1+pos.getX(), pos.getY(), GRN);
                        result[3] = new Position(2+pos.getX(), pos.getY(), WTE);
                        result[4] = new Position(3+pos.getX(), pos.getY(), RED);
                        return result;
                }

            case EAST:
                switch (getShape()) {
                    case A:
                        result = new Position[4];
                        result[0] = new Position(1+pos.getX(), pos.getY(), GRN);
                        result[1] = new Position(pos.getX(),1+pos.getY(), RED);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), WTE);
                        result[3] = new Position(1+pos.getX(),2+pos.getY(), RED);
                        return result;

                    case B:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), pos.getY(), WTE);
                        result[1] = new Position(pos.getX(),1+pos.getY(), WTE);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), BLE);
                        result[3] = new Position(1+pos.getX(), 2+pos.getY(), GRN);
                        result[4] = new Position(1+pos.getX(), 3+pos.getY(), GRN);
                        return result;

                    case C:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), pos.getY(), RED);
                        result[1] = new Position(pos.getX(),1+pos.getY(), RED);
                        result[2] = new Position(pos.getX(), 2+pos.getY(), WTE);
                        result[3] = new Position(pos.getX(),3+pos.getY(), BLE);
                        result[4] = new Position(1+pos.getX(), 2+pos.getY(), GRN);
                        return result;

                    case D:
                        result = new Position[4];
                        result[0] = new Position(1+pos.getX(), pos.getY(), RED);
                        result[1] = new Position(1+pos.getX(), 1+pos.getY(), RED);
                        result[2] = new Position(1+pos.getX(), 2+pos.getY(), RED);
                        result[3] = new Position(pos.getX(), 2+pos.getY(), BLE);
                        return result;

                    case E:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), pos.getY(), RED);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), RED);
                        result[2] = new Position(1+pos.getX(), pos.getY(), BLE);
                        result[3] = new Position(1+pos.getX(),1+pos.getY(), BLE);
                        result[4] = new Position(1+pos.getX(),2+pos.getY(), BLE);
                        return result;

                    case F:
                        result = new Position[3];
                        result[0] = new Position(pos.getX(), pos.getY(), WTE);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), WTE);
                        result[2] = new Position(pos.getX(), 2+pos.getY(), WTE);
                        return result;

                    case G:
                        result = new Position[4];
                        result[0] = new Position(1+pos.getX(), pos.getY(), WTE);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), BLE);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), BLE);
                        result[3] = new Position(pos.getX(), 2+pos.getY(), WTE);
                        return result;

                    case H:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), pos.getY(), WTE);
                        result[1] = new Position(1+pos.getX(), pos.getY(), WTE);
                        result[2] = new Position(2+pos.getX(), pos.getY(), RED);
                        result[3] = new Position(2+pos.getX(), 1+pos.getY(), GRN);
                        result[4] = new Position(2+pos.getX(), 2+pos.getY(), GRN);
                        return result;

                    case I:
                        result = new Position[3];
                        result[0] = new Position(1+pos.getX(), pos.getY(), BLE);
                        result[1] = new Position(1+pos.getX(), 1+pos.getY(), BLE);
                        result[2] = new Position(pos.getX(), 1+pos.getY(), WTE);
                        return result;

                    default:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), pos.getY(), GRN);
                        result[1] = new Position(1+pos.getX(), pos.getY(), GRN);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), GRN);
                        result[3] = new Position(1+pos.getX(), 2+pos.getY(), WTE);
                        result[4] = new Position(1+pos.getX(), 3+pos.getY(), RED);
                        return result;
                }

            case SOUTH:
                switch (getShape()) {
                    case A:
                        result = new Position[4];
                        result[0] = new Position(1+pos.getX(), pos.getY(), RED);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), RED);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), WTE);
                        result[3] = new Position(2+pos.getX(),1+pos.getY(), GRN);
                        return result;

                    case B:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), 1+pos.getY(), GRN);
                        result[1] = new Position(1+pos.getX(),1+pos.getY(), GRN);
                        result[2] = new Position(2+pos.getX(), 1+pos.getY(), BLE);
                        result[3] = new Position(2+pos.getX(), pos.getY(), WTE);
                        result[4] = new Position(3+pos.getX(), pos.getY(), WTE);
                        return result;

                    case C:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), pos.getY(), BLE);
                        result[1] = new Position(1+pos.getX(), pos.getY(), WTE);
                        result[2] = new Position(2+pos.getX(), pos.getY(), RED);
                        result[3] = new Position(3+pos.getX(), pos.getY(), RED);
                        result[4] = new Position(1+pos.getX(), 1+pos.getY(), GRN);
                        return result;

                    case D:
                        result = new Position[4];
                        result[0] = new Position(pos.getX(), pos.getY(), BLE);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), RED);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), RED);
                        result[3] = new Position(2+pos.getX(),1+pos.getY(), RED);
                        return result;

                    case E:
                        result = new Position[5];
                        result[0] = new Position(1+pos.getX(), pos.getY(), RED);
                        result[1] = new Position(2+pos.getX(), pos.getY(), RED);
                        result[2] = new Position(pos.getX(), 1+pos.getY(), BLE);
                        result[3] = new Position(1+pos.getX(),1+pos.getY(), BLE);
                        result[4] = new Position(2+pos.getX(),1+pos.getY(), BLE);
                        return result;

                    case F:
                        result = new Position[3];
                        result[0] = new Position(pos.getX(), pos.getY(), WTE);
                        result[1] = new Position(1+pos.getX(), pos.getY(), WTE);
                        result[2] = new Position(2+pos.getX(), pos.getY(), WTE);
                        return result;

                    case G:
                        result = new Position[4];
                        result[0] = new Position(pos.getX(), pos.getY(), WTE);
                        result[1] = new Position(1+pos.getX(), pos.getY(), BLE);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), BLE);
                        result[3] = new Position(2+pos.getX(), 1+pos.getY(), WTE);
                        return result;

                    case H:
                        result = new Position[5];
                        result[0] = new Position(2+pos.getX(), pos.getY(), WTE);
                        result[1] = new Position(2+pos.getX(), 1+pos.getY(), WTE);
                        result[2] = new Position(2+pos.getX(), 2+pos.getY(), RED);
                        result[3] = new Position(pos.getX(), 2+pos.getY(), GRN);
                        result[4] = new Position(1+pos.getX(), 2+pos.getY(), GRN);
                        return result;

                    case I:
                        result = new Position[3];
                        result[0] = new Position(pos.getX(), pos.getY(), WTE);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), BLE);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), BLE);
                        return result;

                    default:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), 1+pos.getY(), RED);
                        result[1] = new Position(1+pos.getX(), 1+pos.getY(), WTE);
                        result[2] = new Position(2+pos.getX(), 1+pos.getY(), GRN);
                        result[3] = new Position(3+pos.getX(), 1+pos.getY(), GRN);
                        result[4] = new Position(3+pos.getX(), pos.getY(), GRN);
                        return result;
                }

            default:
                switch (getShape()) {
                    case A:
                        result = new Position[4];
                        result[0] = new Position(pos.getX(), pos.getY(), RED);
                        result[1] = new Position(pos.getX(),1+pos.getY(), WTE);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), RED);
                        result[3] = new Position(pos.getX(),2+pos.getY(), GRN);
                        return result;

                    case B:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), pos.getY(), GRN);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), GRN);
                        result[2] = new Position(pos.getX(), 2+pos.getY(), BLE);
                        result[3] = new Position(1+pos.getX(), 2+pos.getY(), WTE);
                        result[4] = new Position(1+pos.getX(), 3+pos.getY(), WTE);
                        return result;

                    case C:
                        result = new Position[5];
                        result[0] = new Position(1+pos.getX(), pos.getY(), BLE);
                        result[1] = new Position(pos.getX(),1+pos.getY(), GRN);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), WTE);
                        result[3] = new Position(1+pos.getX(),2+pos.getY(), RED);
                        result[4] = new Position(1+pos.getX(), 3+pos.getY(), RED);
                        return result;

                    case D:
                        result = new Position[4];
                        result[0] = new Position(pos.getX(), pos.getY(), RED);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), RED);
                        result[2] = new Position(pos.getX(), 2+pos.getY(), RED);
                        result[3] = new Position(1+pos.getX(), pos.getY(), BLE);
                        return result;

                    case E:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), pos.getY(), BLE);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), BLE);
                        result[2] = new Position(pos.getX(), 2+pos.getY(), BLE);
                        result[3] = new Position(1+pos.getX(),1+pos.getY(), RED);
                        result[4] = new Position(1+pos.getX(),2+pos.getY(), RED);
                        return result;

                    case F:
                        result = new Position[3];
                        result[0] = new Position(pos.getX(), pos.getY(), WTE);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), WTE);
                        result[2] = new Position(pos.getX(), 2+pos.getY(), WTE);
                        return result;

                    case G:
                        result = new Position[4];
                        result[0] = new Position(1+pos.getX(), pos.getY(), WTE);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), BLE);
                        result[2] = new Position(1+pos.getX(), 1+pos.getY(), BLE);
                        result[3] = new Position(pos.getX(), 2+pos.getY(), WTE);
                        return result;

                    case H:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), pos.getY(), GRN);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), GRN);
                        result[2] = new Position(pos.getX(), 2+pos.getY(), RED);
                        result[3] = new Position(1+pos.getX(), 2+pos.getY(), WTE);
                        result[4] = new Position(2+pos.getX(), 2+pos.getY(), WTE);
                        return result;

                    case I:
                        result = new Position[3];
                        result[0] = new Position(pos.getX(), pos.getY(), BLE);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), BLE);
                        result[2] = new Position(1+pos.getX(), pos.getY(), WTE);
                        return result;

                    default:
                        result = new Position[5];
                        result[0] = new Position(pos.getX(), pos.getY(), RED);
                        result[1] = new Position(pos.getX(), 1+pos.getY(), WTE);
                        result[2] = new Position(pos.getX(), 2+pos.getY(), GRN);
                        result[3] = new Position(pos.getX(), 3+pos.getY(), GRN);
                        result[4] = new Position(1+pos.getX(), 3+pos.getY(), GRN);
                        return result;
                }
        }
    }

    // class method to convert a placement to an array of piece placements
    public static String[] placementToPieceArray (String placement) {
        // check placement is well formed
        if (!FocusGame.isPlacementStringWellFormed(placement))
            throw new IllegalArgumentException("Invalid Placement Input");

        String[] result = new String[placement.length()/4];

        String substring = "";
        int acc = 0;
        for (char x : placement.toCharArray()) {
            substring += x;
            if ((acc+1) % 4 == 0) { // piece placements are composed of 4 characters
                result[acc/4] = substring;
                substring = "";
            }
            acc++;
        }

        return result;
    }

    // class method to convert a placement to an array of tiles
    public static Tile[] placementToTileArray (String placement) {
        Tile[] result = new Tile[placementToPieceArray(placement).length];
        int acc = 0;

        for (String s : placementToPieceArray(placement)) {
            result[acc] = new Tile(s);
            acc++;
        }

        return result;
    }

    @Override
    public String toString() {
        return "Shape: "+shape+", X: "+pos.getX()+", Y: "+pos.getY()+", Dir: "+dir;
    }

}
