package comp1110.ass2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.lang.String;

/**
 * This class provides the text interface for the IQ Focus Game
 * <p>
 * The game is based directly on Smart Games' IQ-Focus game
 * (https://www.smartgames.eu/uk/one-player-games/iq-focus)
 */
public class FocusGame {

    /**
     * Determine whether a piece placement is well-formed according to the
     * following criteria:
     * - it consists of exactly four characters
     * - the first character is in the range a .. j (shape)
     * - the second character is in the range 0 .. 8 (column)
     * - the third character is in the range 0 .. 4 (row)
     * - the fourth character is in the range 0 .. 3 (orientation)
     *
     * @param piecePlacement A string describing a piece placement
     * @return True if the piece placement is well-formed
     */

    static boolean isPiecePlacementWellFormed(String piecePlacement) {
        // check each character falls within valid ascii values
        return (piecePlacement.length() == 4 &&
            piecePlacement.charAt(0) >= 97 && piecePlacement.charAt(0) <= 106 &&
            piecePlacement.charAt(1) >= 48 && piecePlacement.charAt(1) <= 56 &&
            piecePlacement.charAt(2) >= 48 && piecePlacement.charAt(2) <= 52 &&
            piecePlacement.charAt(3) >= 48 && piecePlacement.charAt(3) <= 51);
    }

    /**
     * Determine whether a placement string is well-formed:
     * - it consists of exactly N four-character piece placements (where N = 1 .. 10);
     * - each piece placement is well-formed
     * - no shape appears more than once in the placement
     *
     * @param placement A string describing a placement of one or more pieces
     * @return True if the placement is well-formed
     */
    public static boolean isPlacementStringWellFormed(String placement) {
        // check length is valid
        if (placement.length() % 4 != 0 ||
                placement.length() == 0)
            return false;

        // iterate across placement string and check piece placements
        String substring = "";
        int acc = 0;
        for (char x : placement.toCharArray()) {
            substring += x;
            if ((acc+1) % 4 == 0) { // piece placements are composed of 4 characters

                // check that each individual piece placement is well formed,
                // and that the same piece shape does not occur more than once
                if (!isPiecePlacementWellFormed(substring) ||
                        placement.length() - placement.replace(substring.substring(0,1),"").length() > 1)
                    return false;

                substring = "";
            }
            acc++;
        }

        return true;
    }

    /**
     * Determine whether a placement string is valid.
     *
     * To be valid, the placement string must be:
     * - well-formed, and
     * - each piece placement must be a valid placement according to the
     *   rules of the game:
     *   - pieces must be entirely on the board
     *   - pieces must not overlap each other
     *
     * @param placement A placement string
     * @return True if the placement sequence is valid
     */
    public static boolean isPlacementStringValid(String placement) {
        // check placement is well-formed
        if (!isPlacementStringWellFormed(placement))
            return false;

        // check placement is valid to game rules
        GameBoardArray board = new GameBoardArray();
        for (Tile t : Tile.placementToTileArray(placement)) {
            if (!board.checkValidPosition(t))
                return false;
            else
                board.updateBoardPosition(t);
        }

        return true;
    }

    /**
     * Given a string describing a placement of pieces and a string describing
     * a challenge, return a set of all possible next viable piece placements
     * which cover a specific board cell.
     *
     * For a piece placement to be viable
     * - it must be valid
     * - it must be consistent with the challenge
     *
     * @param placement A viable placement string
     * @param challenge The game's challenge is represented as a 9-character string
     *                  which represents the color of the 3*3 central board area
     *                  squares indexed as follows:
     *                  [0] [1] [2]
     *                  [3] [4] [5]
     *                  [6] [7] [8]
     *                  each character may be any of
     *                  - 'R' = RED square
     *                  - 'B' = Blue square
     *                  - 'G' = Green square
     *                  - 'W' = White square
     * @param col      The cell's column.
     * @param row      The cell's row.
     * @return A set of viable piece placements, or null if there are none.
     */
    static Set<String> getViablePiecePlacements(String placement, String challenge, int col, int row) {
        // TODO optimize to avoid timeout

        // store piece placements in sets
        Set <String> possiblePieces = new HashSet<>();
        Set <String> viablePieces = new HashSet<>();

        // store information about the game
        Challenge ch = new Challenge(challenge);

        // build a set of all possible values
        for (Shape s : Shape.values()) { // iterate across Shapes
            // don't add shapes that are already in the placement
            if (!placement.contains(s.toString().toLowerCase())) {
                for (Direction d : Direction.values()) { // iterate across Directions
                    for (int x = Math.max(0, col - s.getMaxReach()); x <= col; x++) { // iterate across relevant x Positions
                        for (int y = Math.max(0, row - s.getMaxReach()); y <= row; y++) { // iterate across relevant x Positions
                            Tile t = new Tile(s, x, y, d);
                            // check that the placement is actually valid
                            if (isPlacementStringValid(t.getPlacement()+placement)) {
                                // check the relevant Position is not empty
                                GameBoardArray gb = new GameBoardArray(t.getPlacement());
                                if (gb.getStateAt(col, row) != State.EMP)
                                    possiblePieces.add(t.getRawPlacement()); // add to the set
                            }
                        }
                    }
                }
            }
        }

        // build a set of all valid values from possible values
        for (String p : possiblePieces) {
            GameBoardArray gb = new GameBoardArray(p+placement);
            if (ch.isChallengeVld(gb, true)) // check the placement accepts the challenge condition
                viablePieces.add(p);
        }

        // return null if empty
        if (viablePieces.isEmpty())
            return null;
        else
            return viablePieces;

        // Rebbecca's code
//        Set<String> viablePieces = new HashSet<>();
//
//        if (isPlacementStringValid(placement) && Challenge.isChallengeWFormed(challenge)) {
//            Shape[] unused = Tile.returnUnusedTileShapes(placement);
//            GameBoardArray currentBoard = new GameBoardArray(placement);
//
//
//            //1. Look at every single space on the game board array.
//            for (int y = 0; y < 5; y++) {
//                for (int x = 0; x < 9; x++) {
//
//                    //2. If it is empty, for each unused shape, check every orientation from that position
//                    if (currentBoard.getStateAt(x,y) == State.EMP) {
//                        for (Shape s : unused) {
//                            Direction d = Direction.NORTH;
//                            /*N.B. direction shouldn't be fixed as you need to check all
//                            directions of the piece.
//                            Switch statement probably easier?*/
//
//                            Tile candidate = new Tile(s,x,y,d);
//                            /*N.B. created new things on Tile document to get this.
//                            Delete respective functions if scrapping this.*/
//
//                            //3. Check if tile is valid on game board using checkValidPosition
//                            if (currentBoard.checkValidPosition(candidate)) {
//                                //4. If valid, check Shape arrangement of the tile
//                                Position[] posState = candidate.getShapeArrangement();
//                                for (int i = 0; i < posState.length; i++) {
//                                    //5. If tile fulfils even just one square of the challenge area, add it to the set
//                                        /*Probably a better way for this tho*/
//
//                                    if (challenge.contains(String.valueOf(posState[i].getS().toChar()))) {
//
//                                        //6. Convert Tile to pieceplacement and add to Set
//                                        viablePieces.add(Tile.tileToPiecePlacement(candidate));
//                                    }
//                                }
//                            }
//                        }
//                }
//            }
//            }
//        }
//        else {
//            return null;
//        }
//
//
//        return viablePieces;
    }

    public static void main(String[] args) {
        System.out.println(isPlacementStringValid(new Tile("a000").getPlacement()));
    }

    /**
     * Return the canonical encoding of the solution to a particular challenge.
     *
     * A given challenge can only solved with a single placement of pieces.
     *
     * Since some piece placements can be described two ways (due to symmetry),
     * you need to use a canonical encoding of the placement, which means you
     * must:
     * - Order the placement sequence by piece IDs
     * - If a piece exhibits rotational symmetry, only return the lowest
     *   orientation value (0 or 1)
     *
     * @param challenge A challenge string.
     * @return A placement string describing a canonical encoding of the solution to
     * the challenge.
     */
    public static String getSolution(String challenge) {
        // FIXME Task 9: determine the solution to the game, given a particular challenge
        return null;
    }
}
