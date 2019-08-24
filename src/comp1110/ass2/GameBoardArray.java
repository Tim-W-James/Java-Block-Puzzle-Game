package comp1110.ass2;

import java.util.EmptyStackException;
import java.util.Random;

import static comp1110.ass2.State.*;

public class GameBoardArray {

    // initially all states are empty, except (0,4) and (8,4) which are unused
    // 9*5, 43 used squares
    private State[][] gameBoard;
    private String errorMsg;

    public GameBoardArray () { // zero arg constructor
        gameBoard = new State[][]{
                {EMP, EMP, EMP, EMP, NLL},
                {EMP, EMP, EMP, EMP, EMP},
                {EMP, EMP, EMP, EMP, EMP},
                {EMP, EMP, EMP, EMP, EMP},
                {EMP, EMP, EMP, EMP, EMP},
                {EMP, EMP, EMP, EMP, EMP},
                {EMP, EMP, EMP, EMP, EMP},
                {EMP, EMP, EMP, EMP, EMP},
                {EMP, EMP, EMP, EMP, NLL}
        };
    }

    // build board from placements
    public GameBoardArray (String placements) {
        gameBoard = new GameBoardArray().gameBoard;
        for (Tile t : Tile.placementToTileArray(placements)) {
            updateBoardPosition(t);
        }
    }

    /**
     get methods for the class
      */

    public State[][] getBoardState() { return gameBoard; }

    // find the state at a given position
    public State getStateAt(Position pos) { return gameBoard[pos.getX()][pos.getY()]; }
    public State getStateAt(int x, int y) { return gameBoard[x][y]; }


    /**
     useful methods to handle changes in the game board
     */

    /* checkValidPosition should check if a tile can go in a given board position
     * according to the rules:
     *  - piece must be entirely on the board
     *  - piece must not overlap each other
     */
    public boolean checkValidPosition(Tile t) {

        // ensure tile does not fall off board
        for (Position p : t.getShapeArrangement()) {
            if ((p.getX() < 0 || p.getX() > 8) ||
                    (p.getY() < 0 || p.getY() > 4) ||
                    // factoring in the two columns with nulls at each end
                    (p.getY() == 4 && (p.getX() == 0 || p.getX() == 8))) {
                errorMsg = "\n  piece ("+t+") must be entirely on the board";
                return false;
            }
        }

        // ensure tile is not overlapping
        for (Position p : t.getShapeArrangement()) {
            if (gameBoard[p.getX()][p.getY()] != EMP) {
                errorMsg = "\n  piece ("+t+") overlaps another piece";
                return false;
            }
        }

        return true;
    }
    public boolean checkValidPosition(String piecePlacement) { // also accepts String input
        Tile t = new Tile(piecePlacement);
        return checkValidPosition(t);
    }

    // updates a board position given a tile
    public void updateBoardPosition(Tile t) {
        // check position is valid
        if (!checkValidPosition(t))
            throw new IllegalArgumentException("Invalid Tile Input: "+errorMsg);

        // update each required position in the board
        for (Position p : t.getShapeArrangement()) {
            gameBoard[p.getX()][p.getY()] = p.getS();
        }
    }
    public void updateBoardPosition(String piecePlacement) { // also accepts String input
        Tile t = new Tile(piecePlacement);
        updateBoardPosition(t);
    }

    // updates a board position given a tile, without checking if that placement is valid
    // WARNING: error prone
    public void updateBoardPositionForced(Tile t) {
        // update each required position in the board
        for (Position p : t.getShapeArrangement()) {
            gameBoard[p.getX()][p.getY()] = p.getS();
        }
    }
    public void updateBoardPositionForced(String piecePlacement) { // also accepts String input
        Tile t = new Tile(piecePlacement);
        updateBoardPositionForced(t);
    }

    // prints the array for easy debugging
    @Override
    public String toString() {
        String result = "";

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 9; x++) {
                char br1 = '(';
                char br2 = ')';

                // indicate challenge zone
                if (x > 2 && x < 6 && y > 0 && y <4) {
                    br1 = '{';
                    br2 = '}';
                }

                if (gameBoard[x][y] == EMP)
                    result += br1+"["+x+"]["+y+"]:   "+br2+" ";
                else if (gameBoard[x][y] == NLL)
                    result += br1+"["+x+"]["+y+"]:___"+br2+" ";
                else
                    result += br1+"["+x+"]["+y+"]:"+gameBoard[x][y]+br2+" ";
            }
            result += "\n";
        }

        return result;
    }
}
