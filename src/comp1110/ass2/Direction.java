package comp1110.ass2;

// encodes the direction a tile is facing
public enum Direction {
    NORTH, //0
    EAST,  //1
    SOUTH, //2
    WEST;  //3

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
