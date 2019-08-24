package comp1110.ass2;

import java.util.EmptyStackException;
import java.util.Random;

import static comp1110.ass2.State.*;

public class GameBoardArray {

    // initially all states are empty, except (0,4) and (8,4) which are unused
    // 9*5, 43 used squares
    private State[][] gameBoard;

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

    // checkValidPosition should check if a tile can go in a given board position
    public boolean checkValidPosition(Tile t) {
        int x = t.getPosition().getX();
        int y = t.getPosition().getY();

        // check origin is free
        // ONLY TEMPORARY - ORIGIN MAY NOT NECESSARILY BE FILLED
        // TODO basically task 5
        return ((x >= 0 && x <= 8 && y >= 0 && y <= 3) ||
                (x >= 1 && x <= 7 && y == 4)); // factoring in the two columns with nulls at the end
    }

    public boolean checkValidPosition(String piecePlacement) { // also accepts String input
        Tile t = new Tile(piecePlacement);
        return checkValidPosition(t);
    }

    public void updateBoardPosition(Tile t) {
//        int x = t.getPosition().getX();
//        int y = t.getPosition().getY();
//        Shape s = t.getShape();


        //There's probably a more efficient way...
//        switch (t.getDirection()) {
//            case NORTH:
//                if (s == Shape.A)
//                    gameBoard[9-x][y-1] = GRN;
//                    gameBoard[8-x][y-1] = WTE;
//                    gameBoard[7-x][y-1] = RED;
//                    gameBoard[8-x][y] = RED;
//                if (s == Shape.B)
//                    gameBoard[8-x][y-1] = BLE;
//                    gameBoard[7-x][y-1] = GRN;
//                    gameBoard[6-x][y-1] = GRN;
//                    gameBoard[9-x][y] = WTE;
//                    gameBoard[8-x][y] = WTE;
//                if (s == Shape.C)
//                    gameBoard[7-x][y-1] = GRN;
//                    gameBoard[9-x][y] = RED;
//                    gameBoard[8-x][y] = RED;
//                    gameBoard[7-x][y] = WTE;
//                    gameBoard[6-x][y] = BLE;
//                if (s == Shape.D)
//                    gameBoard[9-x][y-1] = RED;
//                    gameBoard[8-x][y-1] = RED;
//                    gameBoard[7-x][y-1] = RED;
//                    gameBoard[7-x][y] = BLE;
//                if (s == Shape.E)
//                    gameBoard[9-x][y-1] = BLE;
//                    gameBoard[8-x][y-1] = BLE;
//                    gameBoard[7-x][y-1] = BLE;
//                    gameBoard[9-x][y] = RED;
//                    gameBoard[8-x][y] = RED;
//                if (s == Shape.F)
//                    gameBoard[9-x][y-1] = WTE;
//                    gameBoard[8-x][y-1] = WTE;
//                    gameBoard[7-x][y-1] = WTE;
//                if (s == Shape.G)
//                    gameBoard[9-x][y-1] = WTE;
//                    gameBoard[8-x][y-1] = BLE;
//                    gameBoard[8-x][y] = BLE;
//                    gameBoard[7-x][y] = WTE;
//                if (s == Shape.H)
//                    gameBoard[9-x][y-1] = RED;
//                    gameBoard[8-x][y-1] = GRN;
//                    gameBoard[7-x][y-1] = GRN;
//                    gameBoard[9-x][y] = WTE;
//                    gameBoard[9-x][y+1] = WTE;
//                if (s == Shape.I)
//                    gameBoard[9-x][y-1] = BLE;
//                    gameBoard[8-x][y-1] = BLE;
//                    gameBoard[8-x][y] = WTE;
//                if (s == Shape.J)
//                    gameBoard[9-x][y-1] = GRN;
//                    gameBoard[8-x][y-1] = GRN;
//                    gameBoard[7-x][y-1] = WTE;
//                    gameBoard[6-x][y-1] = RED;
//                    gameBoard[9-x][y] = GRN;
//
//            case SOUTH:
//                if (s == Shape.A)
//                    gameBoard[9-x][y] = RED;
//                    gameBoard[8-x][y] = WTE;
//                    gameBoard[7-x][y] = GRN;
//                    gameBoard[8-x][y-1] = RED;
//                if (s == Shape.B)
//                    gameBoard[7-x][y-1] = WTE;
//                    gameBoard[6-x][y-1] = WTE;
//                    gameBoard[9-x][y] = GRN;
//                    gameBoard[8-x][y] = GRN;
//                    gameBoard[7-x][y] = BLE;
//                if (s == Shape.C)
//                    gameBoard[9-x][y-1] = BLE;
//                    gameBoard[8-x][y-1] = WTE;
//                    gameBoard[7-x][y-1] = RED;
//                    gameBoard[6-x][y-1] = RED;
//                    gameBoard[8-x][y] = GRN;
//                if (s == Shape.D)
//                    gameBoard[9-x][y-1] = BLE;
//                    gameBoard[9-x][y] = RED;
//                    gameBoard[8-x][y] = RED;
//                    gameBoard[7-x][y] = RED;
//                if (s == Shape.E)
//                    gameBoard[8-x][y-1] = RED;
//                    gameBoard[7-x][y-1] = RED;
//                    gameBoard[9-x][y] = BLE;
//                    gameBoard[8-x][y] = BLE;
//                    gameBoard[7-x][y] = BLE;
//                if (s == Shape.F)
//                    gameBoard[9-x][y-1] = WTE;
//                    gameBoard[8-x][y-1] = WTE;
//                    gameBoard[7-x][y-1] = WTE;
//                if (s == Shape.G)
//                    gameBoard[9-x][y-1] = WTE;
//                    gameBoard[8-x][y-1] = BLE;
//                    gameBoard[8-x][y] = BLE;
//                    gameBoard[7-x][y] = WTE;
//                if (s == Shape.H)
//                    gameBoard[7-x][y-1] = WTE;
//                    gameBoard[7-x][y] = WTE;
//                    gameBoard[9-x][y+1] = GRN;
//                    gameBoard[8-x][y+1] = GRN;
//                    gameBoard[7-x][y+1] = RED;
//                if (s == Shape.I)
//                    gameBoard[9-x][y-1] = WTE;
//                    gameBoard[9-x][y] = BLE;
//                    gameBoard[8-x][y] = BLE;
//                if (s == Shape.J)
//                    gameBoard[9-x][y] = RED;
//                    gameBoard[8-x][y] = WTE;
//                    gameBoard[7-x][y] = GRN;
//                    gameBoard[6-x][y] = GRN;
//                    gameBoard[9-x][y-1] = GRN;
//
//            case EAST:
//                if (s == Shape.A)
//                    gameBoard[8-x][y-1] = GRN;
//                    gameBoard[9-x][y] = RED;
//                    gameBoard[8-x][y] = WTE;
//                    gameBoard[8-x][y+1] = RED;
//                if (s == Shape.B)
//                    gameBoard[9-x][y-1] = WTE;
//                    gameBoard[9-x][y] = WTE;
//                    gameBoard[8-x][y] = BLE;
//                    gameBoard[8-x][y+1] = GRN;
//                    gameBoard[8-x][y+2] = GRN;
//                if (s == Shape.C)
//                    gameBoard[9-x][y-1] = RED;
//                    gameBoard[9-x][y] = RED;
//                    gameBoard[9-x][y+1] = WTE;
//                    gameBoard[8-x][y+1] =GRN;
//                    gameBoard[9-x][y+2] = BLE;
//                if (s == Shape.D)
//                    gameBoard[8-x][y-1] = RED;
//                    gameBoard[8-x][y] = RED;
//                    gameBoard[9-x][y+1] = BLE;
//                    gameBoard[8-x][y+1] = RED;
//                if (s == Shape.E)
//                    gameBoard[9-x][y-1] = RED;
//                    gameBoard[8-x][y-1] = BLE;
//                    gameBoard[9-x][y] = RED;
//                    gameBoard[8-x][y] = BLE;
//                    gameBoard[8-x][y+1] = BLE;
//                if (s == Shape.F)
//                    gameBoard[9-x][y-1] = WTE;
//                    gameBoard[9-x][y] = WTE;
//                    gameBoard[9-x][y+1] = WTE;
//                if (s == Shape.G)
//                    gameBoard[8-x][y-1] = WTE;
//                    gameBoard[9-x][y] = BLE;
//                    gameBoard[8-x][y] = BLE;
//                    gameBoard[9-x][y+1] = WTE;
//                if (s == Shape.H)
//                    gameBoard[9-x][y-1] = WTE;
//                    gameBoard[8-x][y-1] = WTE;
//                    gameBoard[7-x][y-1] = RED;
//                    gameBoard[7-x][y] = GRN;
//                    gameBoard[7-x][y+1] = GRN;
//                if (s == Shape.I)
//                    gameBoard[8-x][y-1] = BLE;
//                    gameBoard[9-x][y] = WTE;
//                    gameBoard[8-x][y] = BLE;
//                if (s == Shape.J)
//                    gameBoard[9-x][y-1] = GRN;
//                    gameBoard[8-x][y-1] = GRN;
//                    gameBoard[8-x][y] = GRN;
//                    gameBoard[8-x][y+1] = WTE;
//                    gameBoard[8-x][y+2] = RED;
//
//            default:
//                if (s == Shape.A)
//                    gameBoard[9-x][y-1] = RED;
//                    gameBoard[9-x][y] = WTE;
//                    gameBoard[8-x][y] = RED;
//                    gameBoard[9-x][y+1] = GRN;
//                if (s == Shape.B)
//                    gameBoard[9-x][y-1] = GRN;
//                    gameBoard[9-x][y] = GRN;
//                    gameBoard[9-x][y+1] = BLE;
//                    gameBoard[8-x][y+1] = WTE;
//                    gameBoard[8-x][y+2] = WTE;
//                if (s == Shape.C)
//                    gameBoard[8-x][y-1] = BLE;
//                    gameBoard[9-x][y] = GRN;
//                    gameBoard[8-x][y] = WTE;
//                    gameBoard[8-x][y+1] =RED;
//                    gameBoard[8-x][y+2] = RED;
//                if (s == Shape.D)
//                    gameBoard[9-x][y-1] = RED;
//                    gameBoard[8-x][y-1] = BLE;
//                    gameBoard[9-x][y] = RED;
//                    gameBoard[9-x][y+1] = RED;
//                if (s == Shape.E)
//                    gameBoard[9-x][y-1] = BLE;
//                    gameBoard[9-x][y] = BLE;
//                    gameBoard[8-x][y] = RED;
//                    gameBoard[9-x][y+1] = BLE;
//                    gameBoard[8-x][y+1] = RED;
//                if (s == Shape.F)
//                    gameBoard[9-x][y-1] = WTE;
//                    gameBoard[9-x][y] = WTE;
//                    gameBoard[9-x][y+1] = WTE;
//                if (s == Shape.G)
//                    gameBoard[8-x][y-1] = WTE;
//                    gameBoard[9-x][y] = BLE;
//                    gameBoard[8-x][y] = BLE;
//                    gameBoard[9-x][y+1] = WTE;
//                if (s == Shape.H)
//                    gameBoard[9-x][y-1] = GRN;
//                    gameBoard[9-x][y] = GRN;
//                    gameBoard[9-x][y+1] = RED;
//                    gameBoard[8-x][y+1] = WTE;
//                    gameBoard[7-x][y+1] = WTE;
//                if (s == Shape.I)
//                    gameBoard[9-x][y-1] = BLE;
//                    gameBoard[8-x][y-1] = WTE;
//                    gameBoard[9-x][y] = BLE;
//                if (s == Shape.J)
//                    gameBoard[9-x][y-1] = RED;
//                    gameBoard[9-x][y] = WTE;
//                    gameBoard[9-x][y+1] = GRN;
//                    gameBoard[9-x][y+2] = GRN;
//                    gameBoard[8-x][y+2] = GRN;
//        }



        // TODO updateBoardPosition should update the board with a tile

        // check position is valid
//        if (!checkValidPosition(t))
//            throw new IllegalArgumentException("Invalid Tile Input");

        // update each required position in the board
        for (Position p : t.getShapeArrangement()) {
            gameBoard[p.getX()][p.getY()] = p.getS();
        }
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
                if (gameBoard[x][y] == EMP)
                    result += "(["+x+"]["+y+"]:   ) ";
                else if (gameBoard[x][y] == NLL)
                    result += "(["+x+"]["+y+"]:___) ";
                else
                    result += "(["+x+"]["+y+"]:"+gameBoard[x][y]+") ";
            }
            result += "\n";
        }

        return result;
    }
}
