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
    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;

    // Window size
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 750;

    // Offset of the board within the window
    private static final int BACKGROUND_X = 33;
    private static final int BACKGROUND_Y = 25;

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


            var pos = t.getShapeArrangement();
            int minX;
            int minY;
            int maxX;
            int maxY;

            minX = pos[0].getX();
            maxX = pos[0].getX();
            minY = pos[0].getY();
            maxY = pos[0].getY();

            for (Position s :
                    pos) {
                System.out.println(s);
                int x = s.getX();
                int y = s.getY();

                if (x < minX)
                    minX = x;
                if (x > maxX)
                    maxX = x;
                if (y < minY)
                    minY = y;
                if (y > maxY)
                    maxY = y;

            }

            int shapeX = maxX - minX + 1;
            int shapeY = maxY - minY + 1;
            System.out.println(shapeX + "," + shapeY);


            setFitHeight(shapeY * SQUARE_SIZE);
            setFitWidth(shapeX * SQUARE_SIZE);


            setLayoutX(t.getPosition().getX() * SQUARE_SIZE);
            setLayoutY(t.getPosition().getY() * SQUARE_SIZE);

        }
    }

    private void setupBackground() {
        background.getChildren().clear();

        ImageView board = new ImageView();
        board.setImage(new Image(URI_BASE + "board" + ".png"));
        board.setFitWidth(BOARD_WIDTH);
        board.setFitHeight(BOARD_HEIGHT);
        board.setLayoutX(BACKGROUND_X);
        board.setLayoutY(BACKGROUND_Y);

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
        // Debugging
        System.out.println("Found " + tilesToRender.size() + " Tiles");
        for (Tile t :
                tilesToRender) {
            System.out.println(t);

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

//        root.getChildren().add(background);
        root.getChildren().add(controls);
        root.getChildren().add(gTiles);

        setupBackground();
        setupTestControls();

        makePlacementFromString("a110");

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
