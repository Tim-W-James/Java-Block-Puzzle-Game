package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

// do we need these imports?
//import com.sun.webkit.network.Util;
//import java.awt.*;
//import java.net.URI;

/**
 * A very simple viewer for piece placements in the IQ-Focus game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 60;
    private static final int VIEWER_WIDTH = 720;
    private static final int VIEWER_HEIGHT = 480;

    private static final String URI_BASE = "comp1110/ass2/gui/assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField textField;

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     */
    void makePlacement(String placement) {
        // FIXME Task 4: implement the simple placement viewer

        char shape = placement.charAt(0);
        int xPos = Character.getNumericValue(placement.charAt(1));
        int yPos = Character.getNumericValue(placement.charAt(2));
        char direction = placement.charAt(3);

        // Set the image based on the shape
        Image image = new Image((URI_BASE + shape + ".png").toString());

        // Create a view for the image
        ImageView iv1 = new ImageView();
        iv1.setImage(image);

        // Get the rotation for the piece
        // TODO
//        double rotation = tileRotation(placement);
//        iv1.setNodeOrientation();

        // Get the size of the piece
        int[] size = tileSize(placement);
        iv1.setFitWidth(SQUARE_SIZE*size[0]);
        iv1.setFitHeight(SQUARE_SIZE*size[1]);
//        iv1.setPreserveRatio(true);



        iv1.setSmooth(true);
        iv1.setCache(true);

        // Move the piece
        iv1.setTranslateX(xPos * SQUARE_SIZE);
        iv1.setTranslateY(yPos * SQUARE_SIZE);


        root.getChildren().add(iv1);
    }

    private int[] tileSize(String placement) {
        char shape = placement.charAt(0);
        switch (shape) {
            case 'a': {
                int[] size = {3,2};
                return size;
            }
            case 'b': {
                int[] size = {4,2};
                return size;
            }
            case 'c': {
                int[] size = {4,2};
                return size;
            }
            case 'd': {
                int[] size = {3,2};
                return size;
            }
            case 'e': {
                int[] size = {3,2};
                return size;
            }
            case 'f': {
                int[] size = {3,1};
                return size;
            }
            case 'g': {
                int[] size = {3,2};
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
            case 'j': {
                int[] size = {4,2};
                return size;
            }
            default: {
                int[] size = {1,1};
                return size;
            }

        }
    }

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

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FocusGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
