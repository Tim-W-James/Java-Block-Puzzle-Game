package comp1110.ass2;

public class Challenge {
    private String challenge;

    public Challenge(String challenge) {
        this.challenge = challenge;
    }

    public String getChallengeStr () {
        return challenge;
    }

    // replace the central 3*3 square on a GameBoardArray with the challenge condition
    private GameBoardArray overlayOnGBA (GameBoardArray initBoard) {
        //GameBoardArray finlBoard = super.clone(initBoard); // TODO change to copy instead

        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 3; l++) {
                initBoard.getBoardState()[3+l][1+i] = State.charToState(challenge.charAt(l+3*i));
            }
        }
        return initBoard;
    }

    // check if a given GameBoardArray fits the challenge condition
    public boolean isChallengeVld (GameBoardArray initBoard) {
        GameBoardArray challengeBoard = overlayOnGBA(new GameBoardArray());

        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 3; l++) {
                if (initBoard.getBoardState()[3+l][1+i] != challengeBoard.getBoardState()[3+l][1+i]) {
                    return false;
                }
            }
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
}
