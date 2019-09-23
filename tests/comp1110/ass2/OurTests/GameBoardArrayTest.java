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

    //Full game board (based off of .idea/assets/a00.png)
    private  GameBoardArray gb_FOUR = new GameBoardArray("a000b013c113d302e323f400g420h522i613j701");

    // matches gb_THREE but to be updated by updateBoardPosition
    private GameBoardArray gb_FIVE = new GameBoardArray(
            new Tile[]{
                    new Tile(Shape.H, p_ONE, Direction.NORTH),
                    new Tile(Shape.I, p_TWO, Direction.WEST),
                    new Tile(Shape.F, p_THREE, Direction.NORTH)
            }
    );

    // to be updated by updateBoardPositionForced
    private GameBoardArray gb_SIX = new GameBoardArray("a000");

    // empty state - matches gb_ONE
    private State[][] s_ONE = new State[][]{ // note that positions are inverted when printed in this form
        //y=0 y=1  y=2  y=3  y=4
        {EMP, EMP, EMP, EMP, NLL}, // x = 0
        {EMP, EMP, EMP, EMP, EMP}, // x = 1
        {EMP, EMP, EMP, EMP, EMP}, // x = 2
        {EMP, EMP, EMP, EMP, EMP}, // x = 3
        {EMP, EMP, EMP, EMP, EMP}, // x = 4
        {EMP, EMP, EMP, EMP, EMP}, // x = 5
        {EMP, EMP, EMP, EMP, EMP}, // x = 6
        {EMP, EMP, EMP, EMP, EMP}, // x = 7
        {EMP, EMP, EMP, EMP, NLL}  // x = 8
    };
    // partially filled state -- matches gb_TWO
    //a000g210e630
    private State[][] s_TWO = new State[][]{
            //y=0 y=1  y=2  y=3  y=4
            {EMP, EMP, EMP, BLE, NLL}, // x = 0
            {EMP, EMP, EMP, BLE, RED}, // x = 1
            {EMP, EMP, EMP, BLE, RED}, // x = 2
            {EMP, EMP, EMP, EMP, EMP}, // x = 3
            {EMP, EMP, WTE, EMP, EMP}, // x = 4
            {EMP, BLE, BLE, EMP, EMP}, // x = 5
            {RED, WTE, EMP, EMP, EMP}, // x = 6
            {WTE, RED, EMP, EMP, EMP}, // x = 7
            {GRN, EMP, EMP, EMP, NLL}  // x = 8
    };

    // partially filled state - matches gb_THREE
    private State[][] s_THREE = new State[][]{
            {RED, WTE, WTE, WTE, NLL},
            {GRN, BLE, BLE, WTE, EMP},
            {GRN, WTE, EMP, WTE, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, NLL}
    };

    // filled state - matches gb_FOUR
    private State[][] s_FOUR = new State[][]{
            {GRN, GRN, GRN, BLE, NLL},
            {WTE, RED, GRN, WTE, WTE},
            {RED, BLE, WTE, RED, RED},
            {BLE, RED, BLE, BLE, BLE},
            {WTE, RED, WTE, RED, RED},
            {WTE, RED, BLE, BLE, GRN},
            {WTE, BLE, BLE, WTE, GRN},
            {GRN, WTE, WTE, WTE, RED},
            {GRN, GRN, WTE, RED, NLL}
    };

    // partially filled state - matches gb_FIVE after update "a532"
    private State[][] s_FIVE = new State[][]{
            {RED, WTE, WTE, WTE, NLL},
            {GRN, BLE, BLE, WTE, EMP},
            {GRN, WTE, EMP, WTE, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, RED},
            {EMP, EMP, EMP, RED, WTE},
            {EMP, EMP, EMP, EMP, GRN},
            {EMP, EMP, EMP, EMP, NLL}
    };

    // partially filled state - matches gb_FIVE after update "a532" and "e600"
    private State[][] s_SIX = new State[][]{
            {RED, WTE, WTE, WTE, NLL},
            {GRN, BLE, BLE, WTE, EMP},
            {GRN, WTE, EMP, WTE, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, RED},
            {BLE, RED, EMP, RED, WTE},
            {BLE, RED, EMP, EMP, GRN},
            {BLE, EMP, EMP, EMP, NLL}
    };

    // partially filled state - matches gb_FIVE after update "a532", "e600" and "d711"
    private State[][] s_SEVEN = new State[][]{
            {RED, WTE, WTE, WTE, NLL},
            {GRN, BLE, BLE, WTE, EMP},
            {GRN, WTE, EMP, WTE, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, RED},
            {BLE, RED, EMP, RED, WTE},
            {BLE, RED, EMP, BLE, GRN},
            {BLE, RED, RED, RED, NLL}
    };

    // after updateForced on gb_SIX
    private State[][] s_EIGHT = new State[][]{
            {WTE, EMP, EMP, EMP, NLL},
            {WTE, RED, EMP, EMP, EMP},
            {WTE, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, EMP},
            {EMP, EMP, EMP, EMP, NLL}
    };

    @Test
    public void checkConstructors() {

        //No constructor
        assertArrayEquals("Game board must reflect no constructor",
                s_ONE, // expected
                gb_ONE.getBoardState());    //actual
        //String placement constructor
        assertArrayEquals("Game board must correspond to String Placements",
                s_FOUR,
                gb_FOUR.getBoardState());
        //Tile[] constructor test
        assertArrayEquals("Game board must correspond to Tile[] placements",
                s_THREE,
                gb_THREE.getBoardState());
    }

    @Test
    public void checkGetFieldMethods() {
        assertArrayEquals("Method .getBoardState must return correct game board",
                s_FOUR, //expected
                gb_FOUR.getBoardState() //Actual
                );

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
        assertEquals("Method .getTileAt must return corresponding Tile",
                new Tile("h000"),
                gb_THREE.getTileAt(p_ONE));
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
        assertArrayEquals("Method .updateBoardPosition must return the correct game board array given a tile to place",
                s_FIVE,
                gb_FIVE.updateBoardPosition(new Tile("a532")).getBoardState()
                );
        assertArrayEquals("Method .updateBoardPosition must return the correct game board array given a tile to place",
                s_SIX,
                gb_FIVE.updateBoardPosition(new Tile("e600")).getBoardState()
        );
        assertArrayEquals("Method .updateBoardPosition must return the correct game board array given a tile to place",
                s_SEVEN,
                gb_FIVE.updateBoardPosition("d711").getBoardState()
        );
        assertEquals("Method .updateBoardPosition must return correct game board placement after updating tile",
                "a000",
                gb_ONE.updateBoardPosition("a000").getPlacementString());
    }

    @Test
    public void checkUpdateBoardPositionForced() {
        assertArrayEquals("Method .updateBoardPositionForced must return the correct game board array given a "+
                "tile to place, even if it is invalid",
                s_EIGHT,
                gb_SIX.updateBoardPositionForced("f000").getBoardState()
                );
        assertEquals("Method .updateBoardPositionForced must return the correct game board array placement even if tile "+
                        "to place is invalid",
                "a000f000i000",
                gb_SIX.updateBoardPositionForced("i000").getPlacementString()
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
}
