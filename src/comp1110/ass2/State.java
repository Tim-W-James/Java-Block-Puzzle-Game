package comp1110.ass2;

/*
Authorship: Timothy James
*/

// stores the state of a position for the GameBoardArray class
public enum State {
    NLL, // null state for unused positions
    EMP, // empty
    RED, // red
    GRN, // green
    BLE, // blue
    WTE;  // white

    // class method to convert a character to a state
    public static State charToState (char s) {
        switch (s) {
            case 'N':
                return NLL;

            case 'E':
                return EMP;

            case 'R':
                return RED;

            case 'G':
                return GRN;

            case 'B':
                return BLE;

            default:
                return WTE;
        }
    }

    public char toChar() {
        switch (this) {
            case NLL:
                return 'N';

            case EMP:
                return 'E';

            case RED:
                return 'R';

            case GRN:
                return 'G';

            case BLE:
                return 'B';

            default:
                return 'W';
        }
    }
}
