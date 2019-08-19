package comp1110.ass2;

import java.util.EmptyStackException;

import static comp1110.ass2.State.*;

public class GameBoardArray {

    // initially all states are empty, except (0,4) and (8,4) which are unused
    private State[][] gameBoard = {
                {EMPTY, EMPTY, EMPTY, EMPTY, UNUSED},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, UNUSED}
        };

     // Get and Set methods for the class

    public State[][] getBoardState() { return gameBoard; }

    // find the state at a given position
    public State getStateAt(Position pos) { return gameBoard[pos.getX()][pos.getY()]; }
    public State getStateAt(int x, int y) { return gameBoard[x][y]; }

    public void updateBoardPosition(Tile t) {
        // Check position is valid
        if (checkValidPosition(t))
            throw new IllegalArgumentException("Invalid Tile Input");

        // TODO updateBoardPosition should update the board with a tile
    }

    public boolean checkValidPosition(Tile t) {
        // TODO checkValidPosition should check if a tile can go in a given board position
        return false;
    }

    @Override
    public String toString() {
        // TODO toString should pretty print the array for easy debugging
        return super.toString();
    }
}
