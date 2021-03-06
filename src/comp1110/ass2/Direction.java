package comp1110.ass2;

/*
Authorship: Timothy James, Nicholas Dale
 */

// encodes the direction a tile is facing
public enum Direction {
    NORTH('0'), //0
    EAST('1'),  //1
    SOUTH('2'), //2
    WEST('3');  //3

    char direction;

    Direction(char direction) {
        this.direction = direction;
    }

    public char toChar() {
        return direction;
    }

    public double toDegree() {
        return (int) direction * 90;
    }
}
