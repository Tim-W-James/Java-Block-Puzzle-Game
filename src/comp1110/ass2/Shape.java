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

    public static Shape charToShape(char s) {
        switch (s) {
            case 'a':
                return A;
            case 'b':
                return B;
            case 'c':
                return C;
            case 'd':
                return D;
            case 'e':
                return E;
            case 'f':
                return F;
            case 'g':
                return G;
            case 'i':
                return I;
            default:
                return J;
        }
    }

    public char toChar() {
        return type;
    }
}
