package comp1110.ass2.gui;

import comp1110.ass2.GameBoardArray;
import comp1110.ass2.Position;
import comp1110.ass2.State;
import comp1110.ass2.Tile;
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
import javafx.stage.Stage;

import java.util.HashSet;

public class Board extends Application {

    // Gameboard
    private GameBoardArray game = new GameBoardArray();

    // Size of the board within the window
    private static final int BOARD_WIDTH = 823;
    private static final int BOARD_HEIGHT = 530;

    // Size of the grid within the board (SqSz * # squares)
    private static final int GAME_GRID_WIDTH = 720;
    private static final int GAME_GRID_HEIGHT = 400;

    // Overall Window Size
    private static final int WINDOW_WIDTH = 823;
    private static final int WINDOW_HEIGHT = 600;

    // Offset between board and game grid
    private static final int OFFSET_X = (BOARD_WIDTH - GAME_GRID_WIDTH) / 2;
    private static final int OFFSET_Y = 91;

    private static final int SQUARE_SIZE = 80;


    private static final String URI_BASE = "comp1110/ass2/gui/assets/";

    private final Group root = new Group();
    private final Group background = new Group();
    private final Group controls = new Group();
    private final Group gTiles = new Group();

    private TextField textField;



    class GTile extends ImageView {
        private String ID;
        private int direction;


        GTile(Tile t) {
            ID = t.getPlacement();

            System.out.println(URI_BASE + t.getShape().toString().toLowerCase() + ".png");

            setImage(new Image((URI_BASE + t.getShape().toString().toLowerCase() + ".png")));

            setRotate(t.getDirection().toDegree());

            // Sets the height and width of the tiles
            setFitHeight(t.getHeight() * SQUARE_SIZE);
            setFitWidth(t.getWidth() * SQUARE_SIZE);


            setLayoutX((t.getPosition().getX() - 1) * SQUARE_SIZE + OFFSET_X);
            setLayoutY((t.getPosition().getY() - 1) * SQUARE_SIZE + OFFSET_Y);

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

    private void makeTiles(HashSet<Tile> tiles) {
        gTiles.getChildren().clear();
        for (Tile t :
                tiles) {
            System.out.println("Rendering Tile " + t);
            gTiles.getChildren().add(new GTile(t));
        }
        gTiles.toFront();
        System.out.println(gTiles.getChildren());
    }

    private void addTile(Tile t) {
        gTiles.getChildren().add(new GTile(t));
    }

    private void removeTile(Tile t) {

    }


    void makePlacementFromString(String placement) {
        Tile toPlace = new Tile(placement);
        var shapes = toPlace.getShapeArrangement();

        if (game.checkValidPosition(toPlace)) {
            game.updateBoardPosition(toPlace);
            renderGameBoard();
            }
        else {
            System.out.println("Position for " + toPlace + " Is invalid");
        }

    }




    // FIXME Task 7: Implement a basic playable Focus Game in JavaFX that only allows pieces to be placed in valid places

    // FIXME Task 8: Implement challenges (you may use challenges and assets provided for you in comp1110.ass2.gui.assets: sq-b.png, sq-g.png, sq-r.png & sq-w.png)

    // FIXME Task 10: Implement hints

    // FIXME Task 11: Generate interesting challenges (each challenge may have just one solution)

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("IQ Focus by thu11g");
        Scene scene = new Scene(root,WINDOW_WIDTH,WINDOW_HEIGHT);

        root.getChildren().add(background);
        root.getChildren().add(controls);
        root.getChildren().add(gTiles);

        setupBackground();
        setupTestControls();

        makePlacementFromString("a110");

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
