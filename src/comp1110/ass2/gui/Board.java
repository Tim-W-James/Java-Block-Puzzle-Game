package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.HashSet;

// a packaged executable called game.jar can be found at the root directory,
// to run this on IntelliJ go to Run -> Edit Configurations -> VM options,
// and add: --module-path ${PATH_TO_FX} --add-modules=javafx.controls,javafx.fxml,javafx.media
// Note: for changes to be reflected, use Build -> Build Artifacts -> Build

// TODO GUI layout (board, pieces, challenge area)
// TODO Drag and drop
// TODO Drag and drop snap
// TODO Rotation
// TODO Drag and drop check valid
// TODO Drag and drop highlight
// TODO Generate challenge
// TODO Display challenge
// TODO Reset
// TODO Check solution

/*
Authorship: Nicholas Dale
*/

public class Board extends Application {

    /**
     * The GameBoardArray contains most of the logic behind the game
     */
    private GameBoardArray game = new GameBoardArray("a701b400c410d303e111f330g030h000i733j332");
//    private GameBoardArray game = new GameBoardArray();


    // Size of the board within the window
    private static final int BOARD_WIDTH = 617;
    private static final int BOARD_HEIGHT = 398;

    // Size of the grid within the board (SqSz * # squares)
    private static final int GAME_GRID_WIDTH = 540;
    private static final int GAME_GRID_HEIGHT = 300;

    // Overall Window Size
    private static final int WINDOW_WIDTH = 933;
    private static final int WINDOW_HEIGHT = 700;

    // Offset between board and game grid
    private static final int OFFSET_X = (BOARD_WIDTH - GAME_GRID_WIDTH) / 2;
    private static final int OFFSET_Y = (BOARD_HEIGHT - GAME_GRID_HEIGHT + (BOARD_HEIGHT/9)) / 2;

    private static final int SQUARE_SIZE = 60;

    private static final int TILE_AREA_STARTING_X = BOARD_WIDTH + 100;
    private static final int TILE_AREA_STARTING_Y = 10;

    private static final int TILE_AREA_FINISH_X = WINDOW_WIDTH;
    private static final int TILE_AREA_FINISH_Y = WINDOW_HEIGHT;

    private static final String URI_BASE = "comp1110/ass2/gui/assets/";

    private final Group root = new Group();
    private final Group background = new Group();
    private final Group controls = new Group();
    private final Group gTiles = new Group();

    private TextField textField;



    class GTile extends ImageView {
        private Tile t;

        GTile(Tile t) {
            this.t = t;

            setImage(new Image((URI_BASE + t.getShape().toString().toLowerCase() + ".png")));

            // Sets the height and width of the tiles
            if (t.getDirection() == Direction.NORTH || t.getDirection() == Direction.SOUTH) {
                setFitWidth(t.getWidth() * SQUARE_SIZE);
                setFitHeight(t.getHeight() * SQUARE_SIZE);
            } else {
                setFitWidth(t.getHeight() * SQUARE_SIZE);
                setFitHeight(t.getWidth() * SQUARE_SIZE);
            }

            System.out.println(t.getHeight() + ", " + t.getWidth());
            System.out.println(t.getShape().getMaxReach(t.getDirection(),true) + ", " + t.getShape().getMaxReach(t.getDirection(),false));

            Rotate rotation = new Rotate();
            rotation.setPivotX(0);
            rotation.setPivotY(0);
            rotation.setAngle(t.getDirection().toDegree());
            getTransforms().add(rotation);

            setLayoutX((t.getPosition().getX()) * SQUARE_SIZE + OFFSET_X);
            setLayoutY((t.getPosition().getY()) * SQUARE_SIZE + OFFSET_Y);


            if (t.getDirection() == Direction.EAST) {
                setLayoutX(getLayoutX() + SQUARE_SIZE * t.getWidth());
            }
            if (t.getDirection() == Direction.SOUTH) {
                setLayoutX(getLayoutX() + SQUARE_SIZE * t.getWidth());
                setLayoutY(getLayoutY() + SQUARE_SIZE * t.getHeight());
            }

            if (t.getDirection() == Direction.WEST) {
                setLayoutY(getLayoutY() + SQUARE_SIZE * t.getHeight());
            }

//            System.out.println(t.getPosition());

        }

        /**
         * Snaps tile to game grid
         */
        private void snapToGameGrid () {

        }

        /**
         * Move the tile back to its starting position
         */
        private void sendToHome() {

        }

        /**
         * Rotate the tile and update any necessary coordinates
         */
        private void rotate() {

        }

        private double distance(double x, double y) {
            double a_squared = (Math.pow(getLayoutX()-x, 2));
            double b_squared = (Math.pow(getLayoutY()-y, 2));
            double c_squared = a_squared + b_squared;
            return Math.sqrt(c_squared);
        }

        @Override
        public String toString() {
            return "Fit Width/Height: " + getFitWidth() + "," + getFitHeight() + " | " + "Position x,y: " + getLayoutX() + "," + getLayoutY() + "\n" + this.t + "\n";
        }
    }

    class DraggableTile extends GTile {
        private double mouseX;
        private double mouseY;
        private double homeX;
        private double homeY;
        private int offset;

        DraggableTile(Tile t){
            super(t);

            switch (t.getShape()) {
                case A:
                    offset = 0;
                    break;
                case B:
                    offset = 1;
                    break;
                case C:
                    offset = 2;
                    break;
                case D:
                    offset = 3;
                    break;
                case E:
                    offset = 4;
                    break;
                case F:
                    offset = 10;
                    break;
                case G:
                    offset = 6;
                    break;
                case H:
                    offset = 7;
                    break;
                case I:
                    offset = 8;
                    break;
                case J:
                    offset = 9;
                    break;
            }

            homeX = TILE_AREA_STARTING_X;
            homeY = TILE_AREA_STARTING_Y + offset*t.getHeight()*SQUARE_SIZE;
            //You need to get height of the previuos tile so that it's ordered properly


            switch (t.getDirection().toChar()) {
                case 0:
                    setRotate(0);
                    break;
                case 1:
                    setRotate(90);
                    break;
                case 2:
                    setRotate(180);
                    break;
                case 3:
                    setRotate(270);
                    break;
            }

            setLayoutX(homeX);
            setLayoutY(homeY);

        }

    }

    private void setupBackground() {
        background.getChildren().clear();

        ImageView board = new ImageView();
        board.setImage(new Image(URI_BASE + "board" + ".png"));
        board.setFitWidth(BOARD_WIDTH);
        board.setFitHeight(BOARD_HEIGHT);

        background.getChildren().add(board);
        board.toBack();



    }

    /* Create controls for testing */
    private void setupTestControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makePlacementFromString(textField.getText());
                textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(BOARD_HEIGHT + 10);

        controls.getChildren().add(hb);
        controls.toFront();


    }

    /**
     * Place initial tiles
     */
    private void setupInitialTileArea() {
        HashSet<DraggableTile> allTiles = new HashSet<>();   //maybe include all the tiles here then? refer to Tile docco

        //NOTE: Position values for tile shouldn't matter while it is in setup
        /*
        Converting each Tile A to J into a GTile
         */
        DraggableTile A = new DraggableTile(new Tile("a000"));
        allTiles.add(A);
        DraggableTile B = new DraggableTile(new Tile("b000"));
        allTiles.add(B);
        DraggableTile C = new DraggableTile(new Tile("c000"));
        allTiles.add(C);
        DraggableTile D = new DraggableTile(new Tile("d000"));
        allTiles.add(D);
        DraggableTile E = new DraggableTile(new Tile("e000"));
        allTiles.add(E);
        DraggableTile F = new DraggableTile(new Tile("f000"));
        allTiles.add(F);
        DraggableTile G = new DraggableTile(new Tile("g000"));
        allTiles.add(G);
        DraggableTile H = new DraggableTile(new Tile("h000"));
        allTiles.add(H);
        DraggableTile I = new DraggableTile(new Tile("i000"));
        allTiles.add(I);
        DraggableTile J = new DraggableTile(new Tile("j000"));
        allTiles.add(J);

        //gTiles.getChildren().addAll(A);
        gTiles.getChildren().addAll(allTiles);


        //the point is to use the method makeTiles so you don't have to do this tediousness?!!
        //makeTiles(allTiles);

        /*
        To make the tile appear, convert it into instances of a DraggableTile and add it to
        gTile group.

        Write methods in GTIle if you want to manipulate the graphic tile
         */

    }

    void renderGameBoard() {
        State[][] ToDraw = game.getBoardState();

        var tilesToRender = new HashSet<Tile>();

        for (int i = 0; i < ToDraw.length; i++) {
            State[] row = ToDraw[i];

            for (int j = 0; j < row.length; j++) {
                State state = row[j];

                if (state != State.EMP && state != State.NLL) {
                    tilesToRender.add(game.getTileAt(i, j));
                }
            }
        }

        makeTiles(tilesToRender);
    }

    /**
     * Adds each tile to the RHS of the game board ready for placement
     */
    private void makeTiles(HashSet<Tile> tiles) {
        /*

        For every associated tile on GBA, it converts to Graphic TIle
         */

        gTiles.getChildren().clear();
        for (Tile t :
                tiles) {
            gTiles.getChildren().add(new GTile(t));
        }
        gTiles.toFront();

        System.out.println(gTiles.getChildren());
    }

    private void addTile(Tile t) {
        gTiles.getChildren().add(new GTile(t));
    }

    private void removeTile(Tile t) {
        gTiles.getChildren().remove(t);
    }



    /**
     * Reset board to the beginning state
     */
    private void resetBoard() {
        gTiles.getChildren().removeAll(gTiles);     //lol mate this removes all the tiles

    }


    void makePlacementFromString(String placement) {
        game.updateBoardPosition(placement);
        renderGameBoard();
    }




    // FIXME Task 7: Implement a basic playable Focus Game in JavaFX that only allows pieces to be placed in valid places

    // FIXME Task 8: Implement challenges (you may use challenges and assets provided for you in comp1110.ass2.gui.assets: sq-b.png, sq-g.png, sq-r.png & sq-w.png)

    // FIXME Task 10: Implement hints

    // FIXME Task 11: Generate interesting challenges (each challenge may have just one solution)

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("IQ Focus by thu11g");
        Scene scene = new Scene(root,WINDOW_WIDTH,WINDOW_HEIGHT);
        scene.setFill(Color.LIGHTGRAY);

        root.getChildren().add(background);
        root.getChildren().add(controls);
        root.getChildren().add(gTiles);

        setupBackground();
        setupTestControls();
        renderGameBoard();
        setupInitialTileArea();

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
