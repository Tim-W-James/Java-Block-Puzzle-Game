package comp1110.ass2.gui;

import comp1110.ass2.GameBoardArray;
import comp1110.ass2.Tile;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import comp1110.ass2.Position;

import java.awt.*;


/**
 * A very simple viewer for piece placements in the IQ-Focus game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */

/*
Authorship: Rebecca Gibson, Nicholas Dale
*/

public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 70;
    private static final int VIEWER_WIDTH = 720;
    private static final int VIEWER_HEIGHT = 480;

    private static final String URI_BASE = "comp1110/ass2/gui/assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group rootBackground = new Group();
    private TextField textField;

    /* GameBoard instance */
    GameBoardArray board = new GameBoardArray();

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     */
    void makePlacement(String placement) {


        Tile toPlace = new Tile(placement);
        var shapes = toPlace.getShapeArrangement();

        if (board.checkValidPosition(toPlace)) {
            board.updateBoardPosition(toPlace);

            for (Position shape:
                    shapes) {
                System.out.println(shape);

                Image image = new Image((URI_BASE + stateToString(shape)) + ".png");
                ImageView iv1 = new ImageView();
                iv1.setImage(image);
                iv1.setFitHeight(SQUARE_SIZE);
                iv1.setFitWidth(SQUARE_SIZE);

                iv1.setSmooth(true);
                iv1.setCache(true);

                iv1.setTranslateX(shape.getX() * SQUARE_SIZE + 48);
                iv1.setTranslateY(shape.getY() * SQUARE_SIZE + 83);

                rootBackground.getChildren().add(iv1);
            }
        } else {
            System.out.println("Position for " + toPlace + " Is invalid");
        }

    }

    //converts state of tile to corresponding string
    private String stateToString(Position s) {
        switch (s.getS()) {
            case WTE:
                return "sq-w";
            case RED:
                return "sq-r";
            case BLE:
                return "sq-b";
            case GRN:
                return "sq-g";
            default:
                return "";
        }

    }

    // returns dimensions of tile depending on tile type
    private int[] tileSize(String placement) {
        char shape = placement.charAt(0);
        switch (shape) {
            case 'a':
            case 'd':
            case 'e':
            case 'g': {
                int[] size = {3,2};
                return size;
            }
            case 'b':
            case 'c':
            case 'j': {
                int[] size = {4,2};
                return size;
            }
            case 'f': {
                int[] size = {3,1};
                return size;
            }
            case 'h': {
                int[] size = {3,3};
                return size;
            }
            case 'i': {
                int[] size = {2,2};
                return size;
            }
            default: {
                int[] size = {1,1};
                return size;
            }

        }
    }

    //returns orientation of tile in terms of degrees
    private int tileRotation(String placement) {
        int direction = placement.charAt(3);
        switch (direction) {
            case 0: {
                return 0;
            }
            case 1: {
                return 90;
            }
            case 2: {
                return 180;
            }
            default: {
                return 270;
            }

        }
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makePlacement(textField.getText());
                textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);

        controls.getChildren().add(hb);


    }

    /*

     */
    private void makeBackground() {
        Image image = new Image(URI_BASE + "board" + ".png");
        ImageView iv = new ImageView(image);
        iv.setFitWidth(720);
        iv.setFitHeight(463);

        StackPane hb = new StackPane();
        hb.getChildren().add(iv);
//        hb.prefHeight(VIEWER_HEIGHT);
//        hb.prefWidth(VIEWER_WIDTH);
        rootBackground.getChildren().add(hb);



    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FocusGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(rootBackground);
        makeBackground();
        root.getChildren().add(controls);
        makeControls();

        // Test placement
        makePlacement("a000");

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
