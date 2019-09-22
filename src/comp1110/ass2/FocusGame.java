package comp1110.ass2;

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
     * which cover a specific board location.
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
     * @param col      The location's column.
     * @param row      The location's row.
     * @return A set of viable piece placements, or null if there are none.
     */
    static Set<String> getViablePiecePlacements(String placement, String challenge, int col, int row) {
        // store piece placements in sets
        Set <String> viablePieces = new HashSet<>();

        for (Shape s : Shape.values()) { // iterate across Shapes
            // add viable pieces
            // uses getViablePiecePlacements with a Shape input for each Shape
            viablePieces.addAll(getViablePiecePlacements(placement, challenge, col, row, s, true));
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
    // getViablePiecePlacements can check for individual shapes
    private static Set<String> getViablePiecePlacements(String placement, String challenge, int col, int row, Shape s, boolean includeSymmetry) {
        // store piece placements in sets
        Set<String> possiblePieces = new HashSet<>();
        Set<String> viablePieces = new HashSet<>();

        // build a set of all possible values
        // don't add shapes that are already in the placement
        if (!placement.contains(s.toString().toLowerCase())) {
            for (Direction d : Direction.values()) { // iterate across Directions
                // don't consider symmetric duplicates
                if (!includeSymmetry && (s == Shape.F || s == Shape.G) && d == Direction.SOUTH)
                    break;
                for (int x = Math.max(0, col - s.getMaxReach(d, false)); x <= col; x++) { // iterate across relevant x Positions
                    for (int y = Math.max(0, row - s.getMaxReach(d, true)); y <= row; y++) { // iterate across relevant y Positions
                        Tile t = new Tile(s, x, y, d);
                        // check the tile contains the relevant Position
                        if (t.doesTileContainPosition(new Position(col, row))) {
                            // check that the placement is actually valid
                            if (isPlacementStringValid(t.getPlacement() + placement)) {
                                possiblePieces.add(t.getRawPlacement()); // add to the set
                            }
                        }
                    }
                }
            }
        }

        // store information about the game
        Challenge ch = new Challenge(challenge);

        // build a set of all valid values from possible values
        for (String p : possiblePieces) {
            // check the placement accepts the challenge condition
            // and if outside of the range of the challenge
            if (col > 5 || row > 3 || ch.isChallengeVld(new GameBoardArray(p), true))
                viablePieces.add(p);
        }

        return viablePieces;
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
        // recursively test possible placements until an accepted placement is found
        return getPossibleSolution(new GameBoardArray(), challenge, 0);
    }
    // recursive function with an int accumulator and game board which is incrementally built
    private static String getPossibleSolution(GameBoardArray gb, String ch, int acc) {
        int x;
        int y;

        // calculate position on board for a respective accumulator
        // first passes evaluate the challenge area, greatly reducing possible combinations
        if (acc < 9) {
            x = acc%3 + 3;
            y = (int) Math.floor((double) acc/3) + 1;
        }
        // skip the challenge area when it has already been evaluated
        else if (acc > 20 && acc < 24 || acc > 29 && acc < 33 || acc > 38 && acc < 42) {
            return getPossibleSolution(gb, ch, acc+3);
        }
        // assign a x, y value to the accumulator
        else {
            x = acc % 9;
            y = (int) Math.floor((double) acc / 9) - 1;
        }

        if (acc > 52) // base case, terminates when every square has been checked
            return gb.getPlacementString();

        if (gb.getStateAt(x, y) != State.EMP) // empty positions are skipped
            return getPossibleSolution(gb, ch, acc+1);

        for (Shape s : Shape.values()) { // step case, check every square
            // only check Shapes that aren't already on the game board
            if (gb.getPlacementString().contains(s.toString().toLowerCase()))
                continue;

            Set <String> possiblePieces = getViablePiecePlacements(gb.getPlacementString(), ch, x, y, s, false);
            // skip iteration if no viable pieces are found
            if (possiblePieces.isEmpty())
                continue;

            for (String p : possiblePieces) {
                // return part of a solution if it could lead to the solution
                if (isPlacementStringValid(gb.getPlacementString()+p)) { // placement must be valid
                    GameBoardArray tempGB = GameBoardArray.copy(gb);
                    String possibleSolution = getPossibleSolution(tempGB.updateBoardPositionForced(p), ch, acc+1);

                    // don't consider the placement if it has no possible solutions
                    if (possibleSolution == null || possibleSolution.length() == 0)
                        continue;

                    return possibleSolution;
                }
            }
        }

        // when no solutions are found, return null
        return null;
    }
}
