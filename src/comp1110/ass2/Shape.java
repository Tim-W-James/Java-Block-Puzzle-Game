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
        this.type = c;
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
            case 'h':
                return H;
            case 'i':
                return I;
            default:
                return J;
        }
    }

    // returns the max distance a shape extends from its origin position
    public int getMaxReach() {
        switch (this) {
            case A:
                return 3;
            case B:
                return 4;
            case C:
                return 4;
            case D:
                return 3;
            case E:
                return 3;
            case F:
                return 3;
            case G:
                return 3;
            case H:
                return 3;
            case I:
                return 2;
            default:
                return 4;
        }
    }

    public char toChar() {
        return type;
    }
}
