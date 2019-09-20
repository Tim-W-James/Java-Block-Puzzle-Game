package comp1110.ass2;

import static comp1110.ass2.State.*;

public class GameBoardArray {

    // initially all states are empty, except (0,4) and (8,4) which are unused
    // 9*5, 43 used squares
    private State[][] gameBoard;
    private String placementString = ""; // stores and updates placementString
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
        if (placements.length() > 0) {
            gameBoard = new GameBoardArray().gameBoard;
            for (Tile t : Tile.placementToTileArray(placements)) {
                updateBoardPosition(t);
            }
            placementString = placements;
        }
        else { // empty String input
            gameBoard = new GameBoardArray().getBoardState();
        }
    }

    // build board from placements
    public GameBoardArray (Tile[] placements) {
        gameBoard = new GameBoardArray().gameBoard;
        for (Tile t : placements) {
            updateBoardPosition(t);
        }
        placementString = Tile.tileArrayToPlacement(placements);
    }


    /**
     get methods for the class
      */

    public State[][] getBoardState() { return gameBoard; }
    public String getPlacementString() { return placementString; }

    // find the state at a given position
    public State getStateAt(Position pos) { return gameBoard[pos.getX()][pos.getY()]; }
    public State getStateAt(int x, int y) { return gameBoard[x][y]; }



    // based on the placementString, returns the appropriate Tile of a given Position
    // TODO Method needs to be tested for correctness
    public Tile getTileAt (Position p) {
        // check position has a tile
        if (getStateAt(p) == EMP || getStateAt(p) == NLL)
            throw new IllegalArgumentException("No Tile At: "+p);

        //1. split placementString into piece placements
        String[] piecePlacements = Tile.placementToPieceArray(getPlacementString());

        //2. Check position of each piece placement string
        String correctTile = "";
        for (String pp : piecePlacements) {
            if (checkValidPosition(pp) && pp.charAt(1) == p.getX() && pp.charAt(2) == p.getY()) {
                correctTile = pp;
            }
        }
        //TODO
        //I think this needs to be extended so that you look at all positions that
        //the piece placement covers (so u need to utilise Tile.getShapeArrangement)
        return new Tile(correctTile);
    }

    // returns appropriate Tile given X and Y coordinates
    public Tile getTileAt (int x, int y) {
        return getTileAt(new Position(x, y));
    }


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
    public String updateBoardPosition(Tile t) {
        // check position is valid
        if (!checkValidPosition(t))
            throw new IllegalArgumentException("Invalid Tile Input: "+errorMsg);

        // update each required position in the board
        for (Position p : t.getShapeArrangement()) {
            gameBoard[p.getX()][p.getY()] = p.getS();
        }

        // add new tile to the placement string,
        // note that Tile.pieceArrayToPlacement ensures placementString is correctly sorted
        String temp = placementString += t.getPlacement();
        placementString = Tile.pieceArrayToPlacement(Tile.placementToPieceArray(temp));

        return placementString;
    }
    public String updateBoardPosition(String piecePlacement) { // also accepts String input
        Tile t = new Tile(piecePlacement);
        updateBoardPosition(t);

        return placementString;
    }

    // updates a board position given a tile, without checking if that placement is valid
    // WARNING: error prone, also does not update the placement string
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

    // removes a given tile from the board
    // TODO method needs to be checked for correctness
    public String removeFromBoard (Tile t) {
        // 1. check that the Tile actually exists on the board before it can be removed
        if (t != getTileAt(t.getPosition())) {
            throw new IllegalArgumentException("Tile "+t+" does not exist on the current game board");
        }
        // 2. update the gameBoard by removing the Tile
        for (Position p : t.getShapeArrangement()) {
            gameBoard[p.getX()][p.getY()] = EMP;
        }
        // 3. update placementString by removing the piecePlacement
        placementString.replace(t.getPlacement(), "");
        return placementString;
    }

    public String removeFromBoard (String piecePlacement) { // also supports String input
        removeFromBoard(new Tile(piecePlacement));
        return placementString;
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
                    br1 = '*';
                    br2 = '*';
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameBoardArray) // must match instance variables
            return placementString.equals(((GameBoardArray) obj).placementString);
        else
            return false;
    }
}
