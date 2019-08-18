package comp1110.ass2;

public class Tile {

    private TileType type;
    private Position pos;
    private Direction dir;
    private String placement;

    public Tile (String placement) {
//        this.type = ;
//        this.pos = ;
//        this.dir = ;
        this.placement = placement;
    }

    public TileType getTileType() {
        return type;
    }

    public Position getPosition() {
        return pos;
    }

    public Direction getDirection() {
        return dir;
    }

    //getOrientation
    //getPosition (x,y coordinates - where X is the leftmost square, Y is the row where the top square of the piece is in)
    //getTileType (a,b,c,d.. etc.)

}
