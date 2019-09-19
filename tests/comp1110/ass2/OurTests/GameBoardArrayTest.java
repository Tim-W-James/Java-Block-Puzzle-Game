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
                        {GRN, EMP, EMP, EMP, NLL}   //The correct value
                },
                gb_TWO.getBoardState() //Method that's being tested
                );

        assertEquals("Method .getPlacementString must return accompanying placementString",
                "a000b013c113d302e323f400g420h522i613j701", //The correct value
                gb_FOUR.getPlacementString()    //Method that's being tested
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

    /*
    Tests to make:
    - getTileAt(Position & int x, int y)
    - checkValidPosition(Tile & String piecePlacement)
    - updateBoardPosition (Tile t & piece placement)
    - updateBoardPositionForced (Tile & piece placement)
        --Give a tile that is invalid but force it onto the board anyway
          (assuming the tiles that fall off the board simply
          don't appear on the game board?)
    - removeFromBoard( Tile & piece placement)

     */
}
