package comp1110.ass2;

import javafx.geometry.Pos;

// stores a position for a tile
public class Position {
    private int x; // x is the column
    private int y; // y is the row
    private State s; // associate with State if needed

    public Position (int x, int y) {
        this.x = x;
        this.y = y;
        this.s = null;
    }

    // can also store a state with a position
    public Position (int x, int y, State s) {
        this.x = x;
        this.y = y;
        this.s = s;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public String getPosString() { return x+""+y; }

    public State getS() { return s; }

    @Override
    public String toString() {
        if (s == null)
            return "[X:"+x+",Y:"+y+"]";
        else
            return "[X:"+x+",Y:"+y+"] State:"+s;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) { // must match instance variables
            if (s == null && ((Position) obj).s == null)
                return (x == ((Position) obj).x
                        && y == ((Position) obj).y);
            else if (s != null && ((Position) obj).s != null)
                return (x == ((Position) obj).x
                        && y == ((Position) obj).y
                        && s.equals(((Position) obj).s));
            else
                return false;
        }
        else
            return false;
    }
}
