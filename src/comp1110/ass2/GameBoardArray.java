package comp1110.ass2;

import java.util.EmptyStackException;
import java.util.Random;

import static comp1110.ass2.State.*;

public class GameBoardArray {

    // initially all states are empty, except (0,4) and (8,4) which are unused
    // 9*5, 43 used squares
    private State[][] gameBoard;

    public GameBoardArray() { // zero arg constructor
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

    public GameBoardArray(String placements) {  // build board from placements
        // TODO placements to GameBoardArray
    }


    /*
     get methods for the class
      */

    public State[][] getBoardState() { return gameBoard; }

    // find the state at a given position
    public State getStateAt(Position pos) { return gameBoard[pos.getX()][pos.getY()]; }
    public State getStateAt(int x, int y) { return gameBoard[x][y]; }


    /*
     useful methods to handle changes in the game board
     */

    public boolean checkValidPosition(Tile t) {
        // TODO checkValidPosition should check if a tile can go in a given board position
        int x = t.getPosition().getX();
        int y = t.getPosition().getY();

        if (!(x >= 1 && x <= 9 && y >= 1 && y <= 5))
            return false;
        else
        //Factoring in the two columns with nulls at the end
            if ((x == 1 || x == 9) && y == 5)
                return false;
            else
                return true;
    }

    public boolean checkValidPosition(String piecePlacement) { // also accepts String input
        Tile t = new Tile(piecePlacement);
        return checkValidPosition(t);
    }

    public void updateBoardPosition(Tile t) {
        int x = t.getPosition().getX();
        int y = t.getPosition().getY();
        Shape s = t.getShape();
        // check position is valid
        if (!checkValidPosition(t))
            throw new IllegalArgumentException("Invalid Tile Input");

        switch (t.getDirection()) {
            case NORTH:
                if (s == Shape.A)
                    gameBoard[9-x][y-1] = GRN;
                    gameBoard[8-x][y-1] = WTE;
                    gameBoard[7-x][y-1] = RED;
                    gameBoard[8-x][y] = RED;
                if (s == Shape.B)
                    gameBoard[8-x][y-1] = BLE;
                    gameBoard[7-x][y-1] = GRN;
                    gameBoard[6-x][y-1] = GRN;
                    gameBoard[9-x][y] = WTE;
                    gameBoard[8-x][y] = WTE;
                if (s == Shape.C)
                    gameBoard[7-x][y-1] = GRN;
                    gameBoard[9-x][y] = RED;
                    gameBoard[8-x][y] = RED;
                    gameBoard[7-x][y] = WTE;
                    gameBoard[6-x][y] = BLE;
                if (s == Shape.D)
                    gameBoard[9-x][y-1] = RED;
                    gameBoard[8-x][y-1] = RED;
                    gameBoard[7-x][y-1] = RED;
                    gameBoard[7-x][y] = BLE;
                if (s == Shape.E)
                    gameBoard[9-x][y-1] = BLE;
                    gameBoard[8-x][y-1] = BLE;
                    gameBoard[7-x][y-1] = BLE;
                    gameBoard[9-x][y] = RED;
                    gameBoard[8-x][y] = RED;
                if (s == Shape.F)
                    gameBoard[9-x][y-1] = WTE;
                    gameBoard[8-x][y-1] = WTE;
                    gameBoard[7-x][y-1] = WTE;
                if (s == Shape.G)
                    gameBoard[9-x][y-1] = WTE;
                    gameBoard[8-x][y-1] = BLE;
                    gameBoard[8-x][y] = BLE;
                    gameBoard[7-x][y] = WTE;
                if (s == Shape.H)
                    gameBoard[9-x][y-1] = RED;
                    gameBoard[8-x][y-1] = GRN;
                    gameBoard[7-x][y-1] = GRN;
                    gameBoard[9-x][y] = WTE;
                    gameBoard[9-x][y+1] = WTE;
                if (s == Shape.I)
                    gameBoard[9-x][y-1] = BLE;
                    gameBoard[8-x][y-1] = BLE;
                    gameBoard[8-x][y] = WTE;
                if (s == Shape.J)
                    gameBoard[9-x][y-1] = GRN;
                    gameBoard[8-x][y-1] = GRN;
                    gameBoard[7-x][y-1] = WTE;
                    gameBoard[6-x][y-1] = RED;
                    gameBoard[9-x][y] = GRN;
        }

        // TODO updateBoardPosition should update the board with a tile
    }
    public void updateBoardPosition(String piecePlacement) { // also accepts String input
        Tile t = new Tile(piecePlacement);
        updateBoardPosition(t);
    }

    // prints the array for easy debugging
    @Override
    public String toString() {
        String result = "";

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 9; x++) {
                result += "(["+x+"]["+y+"]:"+gameBoard[x][y]+") ";
            }
            result += "\n";
        }

        return result;
    }
}
