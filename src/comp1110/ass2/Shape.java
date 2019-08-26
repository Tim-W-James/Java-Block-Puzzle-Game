package comp1110.ass2;

// encodes the type of shape for a tile
public enum Shape {
    A('a'),
    B('b'),
    C('c'),
    D('d'),
    E('e'),
    F('f'),
    G('g'),
    H('h'),
    I('i'),
    J('j');

    char type;

    Shape(char c) {
        this.type = type;
    }

    public char toChar() {
        return type;
    }
}
