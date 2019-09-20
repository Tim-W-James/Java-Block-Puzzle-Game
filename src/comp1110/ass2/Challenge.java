package comp1110.ass2;

public class Challenge {
    // 9 characters long with only characters that correspond to a state
    private String challenge;

    public Challenge(String challenge) {
        if (!isChallengeWFormed(challenge)) // check if well-formed
            throw new IllegalArgumentException("Input Challenge Invalid");

        this.challenge = challenge;
    }

    public String getChallengeStr () {
        return challenge;
    }

    // replace the central 3*3 square on a GameBoardArray with the challenge condition
    private GameBoardArray overlayOnGBA (GameBoardArray initBoard) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 3; l++) {
                initBoard.getBoardState()[3+l][1+i] = State.charToState(challenge.charAt(l+3*i));
            }
        }
        return initBoard;
    }

    // check if a given GameBoardArray fits the challenge condition
    // isPartial ignores empty states
    public boolean isChallengeVld (GameBoardArray initBoard, boolean isPartial) {
        GameBoardArray challengeBoard = overlayOnGBA(new GameBoardArray());

        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 3; l++) {
                if (isPartial) {
                    if (initBoard.getBoardState()[3+l][1+i] != State.EMP
                            && initBoard.getBoardState()[3+l][1+i] != challengeBoard.getBoardState()[3+l][1+i])
                        return false;
                }
                else {
                    if (initBoard.getBoardState()[3+l][1+i] != challengeBoard.getBoardState()[3+l][1+i])
                        return false;
                }
            }
        }

        return true;
    }
    public boolean isChallengeVld (GameBoardArray initBoard) {
        return isChallengeVld(initBoard, false);
    }

    // class method to check if a challenge is well-formed
    public static boolean isChallengeWFormed (String challenge) {
        if (challenge.length() != 9) // check length
            return false;

        for (char c : challenge.toCharArray()) { // check if each character is valid
            if (!(c == 'R' || c == 'G' || c == 'B' || c == 'W' || c == 'N' || c == 'E'))
                return false;
        }

        return true;
    }

    @Override
    public String toString() {
        String result = "";
        GameBoardArray board = overlayOnGBA(new GameBoardArray());

        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 3; l++) {
                int x = 3+l;
                int y = 1+i;
                result += "(["+x+"]["+y+"]:"+board.getBoardState()[3+l][1+i]+")";
            }
            result += "\n";
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Challenge) // must match instance variables
            return challenge.equals(((Challenge) obj).challenge);
        else
            return false;
    }
}
