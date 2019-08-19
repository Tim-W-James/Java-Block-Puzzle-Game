package comp1110.ass2;

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
        // Rebecca's implementation
//        int l = placement.length();
//        //20
//        //5 4's
//        if (l % 4 == 0 && l/4 <= 10) {
//            for (int i = 0; l == 0; i++) {
//                return isPiecePlacementWellFormed(i );
//                if (i == 3) {
//                    i = 0;
//                    l -= l / 4;
//                }
//                else
//                    i++;
//
//            }
//
//        }
//        else
//            return false;
//            //get rid of that section of the pieceplacement and use contains to see if the shape
//            //appears in the stirng again


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
                String tempPlacement = placement;
                if (!isPiecePlacementWellFormed(substring) ||
                        tempPlacement.length() - tempPlacement.replace(substring.substring(0,1),"").length() > 1)
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
        // FIXME Task 5: determine whether a placement string is valid
        return false;
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
        // FIXME Task 6: determine the set of all viable piece placements given existing placements and a challenge
        return null;
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
