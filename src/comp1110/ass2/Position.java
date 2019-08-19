package comp1110.ass2;

// stores a position for a tile
public class Position {
    private int x; //x is the column
    private int y; // y is the row

    public Position () {
        this(-1,-1);
    }

    public Position (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    @Override
    public String toString() {
        return x+""+y;
    }
}
