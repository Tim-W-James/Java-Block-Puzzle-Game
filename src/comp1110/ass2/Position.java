package comp1110.ass2;

// stores a position for a tile
public class Position {
    private int x; // x is the column
    private int y; // y is the row
    private State s; // associate with State if needed

    public Position (int x, int y) {
        this.x = x;
        this.y = y;
    }

    // can also store a state with a position
    public Position (int x, int y, State s) {
        this.x = x;
        this.y = y;
        this.s = s;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public State getS() { return s; }

    @Override
    public String toString() {
        return x+""+y;
    }
}
