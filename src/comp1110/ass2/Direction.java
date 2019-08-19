package comp1110.ass2;

/*
    Valid directions for a piece
 */
public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    public char toChar() {
        switch (this) {
            case NORTH:
                return '0';

            case EAST:
                return '1';

            case SOUTH:
                return '2';

            default:
                return '3';
        }
    }
}
