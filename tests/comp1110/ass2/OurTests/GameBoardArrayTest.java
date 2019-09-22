package comp1110.ass2.OurTests;

import comp1110.ass2.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static comp1110.ass2.State.*;
import static org.junit.Assert.*;

public class GameBoardArrayTest  {

    @Rule
    public Timeout globalTimeout = Timeout.millis(120000);

    private GameBoardArray gb_ONE = new GameBoardArray();   //Empty game board
    private GameBoardArray gb_TWO = new GameBoardArray("a000g210e630"); //String placement constructor

    private Position p_ONE = new Position(0,0);
    private Position p_TWO = new Position(1,1);
    private Position p_THREE = new Position(0,3);
    private GameBoardArray gb_THREE = new GameBoardArray(
            new Tile[]{
                    new Tile(Shape.H, p_ONE, Direction.NORTH),
                    new Tile(Shape.I, p_TWO, Direction.WEST),
                    new Tile(Shape.F, p_THREE, Direction.NORTH)
            }
    ); //Tile[] (Shape,Position,Direction) constructor

    private  GameBoardArray gb_FOUR = new GameBoardArray("a000b013c113d302e323f400g420h522i613j701");
    //Full game board (based off of .idea/assets/a00.png)

    @Test
    public void checkConstructors() {
    /*
        for some reason, these tests are comparing answers to
        string inputs: e.g. [[Lcomp1110.ass2.State;@2a9b652a ??
     */

        //No constructor
        assertEquals("Game board must reflect no constructor",
                gb_ONE,
                new State[][]{
                        {EMP, EMP, EMP, EMP, NLL},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, NLL}
                });
        //String placement constructor
        assertEquals("Game board must correspond to String Placements",
                gb_FOUR,
                new State[][]{
                        {GRN, GRN, WTE, RED, NLL},
                        {GRN, WTE, WTE, WTE, RED},
                        {WTE, BLE, BLE, WTE, GRN},
                        {WTE, RED, BLE, BLE, GRN},
                        {WTE, RED, WTE, RED, RED},
                        {BLE, RED, BLE, BLE, BLE},
                        {RED, BLE, WTE, RED, RED},
                        {WTE, RED, GRN, WTE, WTE},
                        {GRN, GRN, GRN, BLE, NLL}
                });
        //Tile[] constructor test
        assertEquals("Game board must correspond to Tile[] placements",
                gb_THREE,
                new State[][]{
                        {EMP, EMP, EMP, EMP, NLL},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {GRN, WTE, EMP, WTE, EMP},
                        {GRN, BLE, BLE, WTE, EMP},
                        {RED, WTE, WTE, WTE, NLL}
                });
    }

    @Test
    public void checkGetFieldMethods() {
        /*

        Why is there a strikethrough??

        WARNING: This test fails.
         */
        assertEquals("Method .getBoardState must return correct game board",
                new State[][]{
                        {EMP, EMP, EMP, BLE, NLL},
                        {EMP, EMP, EMP, BLE, RED},
                        {EMP, EMP, EMP, BLE, RED},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, WTE, EMP, EMP},
                        {EMP, BLE, BLE, EMP, EMP},
                        {RED, WTE, EMP, EMP, EMP},
                        {WTE, RED, EMP, EMP, EMP},
                        {GRN, EMP, EMP, EMP, NLL}   //Expected
                },
                gb_TWO.getBoardState() //Actual
                );
        /*
        It seems like the game board returned is being flipped?
         */

        assertEquals("Method .getPlacementString must return accompanying placementString",
                "a000b013c113d302e323f400g420h522i613j701", //Expected
                gb_FOUR.getPlacementString()    //Actual
                );
        assertEquals("Method .getStateAt must return correct state at given position (Position pos)",
                RED,
                gb_THREE.getStateAt(p_ONE)
                );
        assertEquals("Method .getStateAt must return correct state at given position (int x, int y)",
                BLE,
                gb_THREE.getStateAt(1,1)
                );
    }

    @Test
    public void checkGetTileAt() {
        assertEquals("Method .getTileAt must return corresponding Tile from a Position",
                new Tile("b013"),
                gb_FOUR.getTileAt(new Position(0,1))
                );
        assertEquals("Method .getTileAt must return corresponding Tile from x y coordinates",
                new Tile("a000"),
                gb_TWO.getTileAt(1,1)
                );
    }


    @Test
    public void checkValidPosition() {
        assertTrue("Method .checkValidPosition must correctly reveal if tile can go in a given board position",
                gb_ONE.checkValidPosition(new Tile("i030")));
        assertFalse("Method .checkValidPosition must correctly reveal if tile can go in a given board position",
                gb_ONE.checkValidPosition("i031"));
        assertFalse("Method .checkValidPosition must correctly reveal if tile can go in a given board position",
                gb_FOUR.checkValidPosition("f000"));
        assertTrue("Method .checkValidPosition must correctly reveal if tile can go in a given board position",
                gb_THREE.checkValidPosition("d601"));
    }

    @Test
    public void checkUpdateBoardPosition() {
        assertEquals("Method .updateBoardPosition must return the correct game board array given a tile to place",
                new State[][] {
                        {EMP, EMP, EMP, EMP, NLL},
                        {EMP, EMP, EMP, EMP, GRN},
                        {EMP, EMP, EMP, RED, WTE},
                        {EMP, EMP, EMP, EMP, RED},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {GRN, WTE, EMP, WTE, EMP},
                        {GRN, BLE, BLE, WTE, EMP},
                        {RED, WTE, WTE, WTE, NLL}
                },
                gb_THREE.updateBoardPosition(new Tile("a532"))
                );
        assertEquals("Method .updateBoardPosition must return the correct game board array given a tile to place",
                new State[][] {
                        {EMP, EMP, EMP, EMP, NLL},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, EMP, EMP, EMP},
                        {BLE, WTE, EMP, EMP, EMP},
                        {BLE, EMP, EMP, EMP, NLL}
                },
                gb_ONE.updateBoardPosition("i000")
        );
        assertEquals("Method .updateBoardPosition must return the correct game board array given a tile to place",
                new State[][] {
                        {BLE, WTE, EMP, BLE, NLL},
                        {BLE, EMP, EMP, BLE, RED},
                        {EMP, EMP, EMP, BLE, RED},
                        {EMP, EMP, EMP, EMP, EMP},
                        {EMP, EMP, WTE, EMP, EMP},
                        {EMP, BLE, BLE, EMP, EMP},
                        {RED, WTE, EMP, EMP, EMP},
                        {WTE, RED, EMP, EMP, EMP},
                        {GRN, EMP, EMP, EMP, NLL}
                },
                gb_TWO.updateBoardPosition("i700")
        );
    }

    @Test
    public void checkRemoveFromBoard() {
        assertEquals("Method .removeFromBoard must remove the right tile from both the gameboard and placement string",
                "f030i113",
                gb_THREE.removeFromBoard(new Tile("h000"))
                );
        assertEquals("Method .removeFromBoard must remove the right tile from both the gameboard and placement string",
                "g210e630",
                gb_TWO.removeFromBoard("a000"));
        assertEquals("Method .removeFromBoard must remove the right tile from both the gameboard and placement string",
                "a000b013c113d302e323f400g420h522i613",
                gb_FOUR.removeFromBoard("j701"));
    }



    /*
    Tests to make:
    - updateBoardPositionForced (Tile & piece placement)
        --Give a tile that is invalid but force it onto the board anyway
          (assuming the tiles that fall off the board simply
          don't appear on the game board?)
     */
}
