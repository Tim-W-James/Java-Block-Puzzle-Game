package comp1110.ass2;

public enum Shape {
    A,
    B,
    C,
    D,
    E,
    F,
    G,
    H,
    I,
    J;

    public char toChar() {
        switch (this) {
            case A:
                return 'a';

            case B:
                return 'b';

            case C:
                return 'c';

            case D:
                return 'd';

            case E:
                return 'e';

            case F:
                return 'f';

            case G:
                return 'g';

            case H:
                return 'h';

            case I:
                return 'i';

            default:
                return 'j';
        }
    }
}
