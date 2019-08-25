package comp1110.ass2;

// stores a position for a tile
public class Position {
    private int x; // x is the column
    private int y; // y is the row
    private Tile.State s; // associate with State if needed

    public Position (int x, int y) {
        this.x = x;
        this.y = y;
        this.s = null;
    }

    // can also store a state with a position
    public Position (int x, int y, Tile.State s) {
        this.x = x;
        this.y = y;
        this.s = s;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public String getPosString() { return x+""+y; }

    public Tile.State getS() { return s; }

    @Override
    public String toString() {
        if (s == null)
            return "[X:"+x+"][Y:"+y+"]";
        else
            return "[X:"+x+"][Y:"+y+"] State:"+s;
    }
}
