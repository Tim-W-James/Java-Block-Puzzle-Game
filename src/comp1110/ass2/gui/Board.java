package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.*;

import static comp1110.ass2.Shape.*;

// a packaged executable called game.jar can be found at the root directory,
// to run this on IntelliJ go to Run -> Edit Configurations -> VM options,
// and add: --module-path ${PATH_TO_FX} --add-modules=javafx.controls,javafx.fxml,javafx.media
// Note: for changes to be reflected, use Build -> Build Artifacts -> Build

// TODO Position Tiles on the side of screen in a clear, clean way (make sure they are not touching each other)
// TODO Drag and drop
// TODO Drag and drop snap
// TODO Rotation
// TODO Drag and drop check valid
// TODO Drag and drop highlight
// TODO Generate challenge
// TODO Display challenge
// TODO Reset button
// TODO Check if the user has found a solution
// TODO Generating a new challenge should reset the board
// TODO Remove text box for letting the user enter placement strings (drag and drop only)

/*
Authorship: Nicholas Dale
*/

public class Board extends Application {

    /**
     * The GameBoardArray contains most of the logic behind the game
     */
    private GameBoardArray game = new GameBoardArray("a701b400c410d303e111f330g030h000i733j332");
//    private GameBoardArray game = new GameBoardArray();

    private GTile hint;
    private boolean isSLASHDown = false;

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
    private static final int OFFSET_Y = (BOARD_HEIGHT - GAME_GRID_HEIGHT + (BOARD_HEIGHT / 9)) / 2;

    private static final int SQUARE_SIZE = 60;
    private static final double OFFBOARD_SQUARE_SIZE = 0.5 * SQUARE_SIZE;

    private static final int TILE_AREA_STARTING_X = BOARD_WIDTH + 100;
    private static final int TILE_AREA_STARTING_Y = 5;  //I don't think this value actually does anything tbh

    private static final int TILE_AREA_FINISH_X = WINDOW_WIDTH;
    private static final int TILE_AREA_FINISH_Y = WINDOW_HEIGHT;

    private static final int CHALLENGE_AREA_WIDTH = SQUARE_SIZE * 3;
    private static final int CHALLENGE_AREA_HEIGHT = SQUARE_SIZE * 3;
    private static final int CHALLENGE_AREA_X = OFFSET_X + SQUARE_SIZE * 3; //Maybe make a universal margin constant
    private static final int CHALLENGE_AREA_Y = BOARD_HEIGHT + 60; //Maybe make a universal margin constant

    private static String challengeString;

    private static final String URI_BASE = "comp1110/ass2/gui/assets/";

    private final Group root = new Group();
    private final Group background = new Group();
    private final Group controls = new Group();
    private final Group gTiles = new Group();
    private final Group challenge = new Group();

    private TextField textField;

    private HashSet<DraggableTile> allTiles = new HashSet<>();

    //Difficulty slider
    //private final Slider difficulty = new Slider();

    //Challenge controls
    private final static ToggleGroup group = new ToggleGroup();
    private final static RadioButton b1 = new RadioButton("Starter");
    private final static RadioButton b2 = new RadioButton("Junior");
    private final static RadioButton b3 = new RadioButton("Expert");
    private final static RadioButton b4 = new RadioButton("Master");
    private final static RadioButton b5 = new RadioButton("Wizard");

    class GTile extends ImageView {
        private Tile t;
        boolean inGame = false;

        /**
         * Constructor for gTile with inGame value
         * @param t
         * @param inGame
         */
        GTile(Tile t, boolean inGame) {
            this(t);
            this.inGame = inGame;

            if (! inGame) {
                sendToDefaultPlacement();
            }

            //Scaling
            if (!inGame) {
                setScaleX(0.5);
                setScaleY(0.5);
            } else {
                setScaleX(1);
                setScaleY(1);
            }
        }

        /**
         * Constructor to build a PLAYING tile
         */
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
            System.out.println(t.getShape().getMaxReach(t.getDirection(), true) + ", " + t.getShape().getMaxReach(t.getDirection(), false));

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
         * Constructor to build individual square pieces that make up the CHALLENGE

        Using Position's int x and int y parameters to specify where they should be placed on the
        challenge board by treating them as coordinates:
            (0,0) (1,0) (2,0)
            (0,1) (1,1) (2,1)
            (0,2) (1,2) (2,2)
         */

        GTile(Position square) {
            String objTileID;

            switch (square.getS()) {
                case WTE:
                    objTileID = "sq-w";
                    break;
                case BLE:
                    objTileID = "sq-b";
                    break;
                case RED:
                    objTileID = "sq-r";
                    break;
                case GRN:
                    objTileID = "sq-g";
                    break;
                default:
                    objTileID = "sq-b";
            }

            setImage(new Image(URI_BASE + objTileID + ".png"));
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);

            setLayoutX(CHALLENGE_AREA_X + (square.getX() * SQUARE_SIZE));
            setLayoutY(CHALLENGE_AREA_Y + (square.getY() * SQUARE_SIZE));
        }

        /**
         * Snaps tile to game grid
         */
        private void snapToGameGrid() {

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

        private void sendToDefaultPlacement() {
            double offset;
            int previousTileHeight;
            double homeX;
            double homeY;

            switch (t.getShape()) {
                case A:
                    offset = 0;
                    previousTileHeight = 0;
                    break;
                case B:
                    offset = 1;
                    previousTileHeight = A.getMaxReach(Direction.NORTH, true);
                    break;
                case C:
                    offset = 2;
                    previousTileHeight = B.getMaxReach(Direction.NORTH, true);
                    break;
                case D:
                    offset = 3;
                    previousTileHeight = C.getMaxReach(Direction.NORTH, true);
                    break;
                case E:
                    offset = 4;
                    previousTileHeight = D.getMaxReach(Direction.NORTH, true);
                    break;
                case F:
                    offset = 5.5; //should be 5 but it gets overlapped for some reason
                    previousTileHeight = E.getMaxReach(Direction.NORTH, true);
                    break;
                case G:
                    offset = 12; //should be 6 but it gets overlapped because the maxReach for F is 1 so it goes all weird
                    previousTileHeight = F.getMaxReach(Direction.NORTH, true);
                    break;
                case H:
                    offset = 7;
                    previousTileHeight = G.getMaxReach(Direction.NORTH, true);
                    break;
                case I:
                    offset = 6.7; //should be 8
                    previousTileHeight = H.getMaxReach(Direction.NORTH, true);
                    break;
                default:
                    offset = 9;
                    previousTileHeight = I.getMaxReach(Direction.NORTH, true);
                    break;
            }

            homeX = TILE_AREA_STARTING_X;
            homeY = TILE_AREA_STARTING_Y + (offset * (previousTileHeight + 0.05) * OFFBOARD_SQUARE_SIZE);
            setLayoutX(homeX);
            setLayoutY(homeY);


        }

        /**
         * Returns distance between an x,y point and the top left of this tile
         * @param x horizontal coordinate
         * @param y vertical coordinate
         * @return distance
         */
        public double getDistance(double x, double y) {
            return Math.sqrt((Math.pow((getLayoutX() - x),2)) + (Math.pow((getLayoutY() - y),2)));
        }

        @Override
        public String toString() {
            return "Fit Width/Height: " + getFitWidth() + "," + getFitHeight() + " | " + "Position x,y: " + getLayoutX() + "," + getLayoutY() + "\n" + this.t + "\n";
        }
    }

    class DraggableTile extends GTile {
        private double mouseX;
        private double mouseY;

        DraggableTile(Tile t, boolean inGame) {
            super(t,inGame);

            this.setOnMousePressed(event -> {
                mouseX = event.getSceneX(); //gets X coordinates
                mouseY = event.getSceneY(); //gets Y coordinates
                this.toFront();

//                if (mouseX >= 0) //if mouse X and mouse Y falls within dimensions of off game board tile, switch to true
//                    this.inGame = true;
//
            });

            this.setOnMouseDragged(event -> {
                setLayoutX(event.getSceneX() - mouseX);
                setLayoutY(event.getSceneY() - mouseY);
            });

            this.setOnMouseReleased(event -> {
                setLayoutX(findNearestTile(getLayoutX(),getLayoutY()).getLayoutX());
                setLayoutY(findNearestTile(getLayoutX(),getLayoutY()).getLayoutY());

            });


        }


    }

    /**
     * Returns nearest tile to a given x,y position
     * @param x
     * @param y
     * @return
     */
    private DraggableTile findNearestTile(double x, double y) {
        ArrayList<DraggableTile> allTiles = new ArrayList<>();
        for (Node current :
                gTiles.getChildren()) {
            if (current instanceof DraggableTile) {
                allTiles.add((DraggableTile) current);
            }
        }

        DraggableTile candidate = null;
        double shortestDistance = 1000;

        for (DraggableTile t :
                allTiles) {
            double distance = t.getDistance(x,y);
            if (candidate == null) {
                candidate = t;
            } else {
                System.out.println("Changing distance of " + distance + " To distance of " + shortestDistance);
                if (distance < shortestDistance) {
                    candidate = t;
                    shortestDistance = distance;
                }
            }
        }

        if (candidate != null) {
            System.out.println(shortestDistance);
            System.out.println(candidate);
            return candidate;
        } else {
            throw new NoSuchElementException("No tiles exist in gTiles");
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

        Button reset = new Button("Reset");
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                game.resetGameBoardArray();
                allTiles.clear();
                resetGTiles();

                renderGameBoard(game);
                setupInitialTileArea();
                setupChallengeArea();
                System.out.println("GameBoard has been reset");
            }
        });
//        reset.setLayoutX(20);
//        reset.setLayoutY(BOARD_HEIGHT + 20);
        reset.toFront();


        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button,reset);
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
        //NOTE: Position values for tile shouldn't matter while it is in setup
        /*
        Converting each Tile A to J into a GTile
         */
        DraggableTile A = new DraggableTile(new Tile("a000"), false);
        DraggableTile B = new DraggableTile(new Tile("b000"), false);
        DraggableTile C = new DraggableTile(new Tile("c000"), false);
        DraggableTile D = new DraggableTile(new Tile("d000"), false);
        DraggableTile E = new DraggableTile(new Tile("e000"), false);
        DraggableTile F = new DraggableTile(new Tile("f000"), false);
        DraggableTile G = new DraggableTile(new Tile("g000"), false);
        DraggableTile H = new DraggableTile(new Tile("h000"), false);
        DraggableTile I = new DraggableTile(new Tile("i000"), false);
        DraggableTile J = new DraggableTile(new Tile("j000"), false);

        gTiles.getChildren().addAll(A,B,C,D,E,F,G,H,I,J);


        //the point is to use the method makeTiles so you don't have to do this tediousness?!!
        //makeTiles(allTiles);


    }

    void renderGameBoard(GameBoardArray g) {
        State[][] ToDraw = g.getBoardState();

        var tilesToRender = new HashSet<Tile>();

        for (int i = 0; i < ToDraw.length; i++) {
            State[] row = ToDraw[i];

            for (int j = 0; j < row.length; j++) {
                State state = row[j];

                if (state != State.EMP && state != State.NLL) {
                    tilesToRender.add(g.getTileAt(i, j));
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

    // make sure these accommodate for the gameboardarray if you are using them in that manner - Tim
    private void addTile(Tile t) {
        gTiles.getChildren().add(new GTile(t));
    }

    private void removeTile(Tile t) { // this does not work, as it is attempting to remove a Tile, not GTile - Tim
        gTiles.getChildren().remove(t);
    }


    /**
     * Reset board to the beginning state
     */
    private void resetGTiles() {
        gTiles.getChildren().removeAll(gTiles);     //lol mate this removes all the tiles

    }


    void makePlacementFromString(String placement) {
        game.updateBoardPosition(placement);
        renderGameBoard(game);
    }


    // FIXME Task 7: Implement a basic playable Focus Game in JavaFX that only allows pieces to be placed in valid places

    // FIXME Task 8: Implement challenges (you may use challenges and assets provided for you in comp1110.ass2.gui.assets: sq-b.png, sq-g.png, sq-r.png & sq-w.png)

    private void setupChallengeArea() {

        Rectangle border = new Rectangle(CHALLENGE_AREA_X-5, CHALLENGE_AREA_Y-5, CHALLENGE_AREA_WIDTH+10, CHALLENGE_AREA_HEIGHT+10);
        Rectangle challengeArea = new Rectangle(CHALLENGE_AREA_X, CHALLENGE_AREA_Y, CHALLENGE_AREA_WIDTH, CHALLENGE_AREA_HEIGHT);
        challengeArea.setFill(Color.GRAY);
        challengeArea.setStroke(Color.BLACK);

        background.getChildren().add(challengeArea);
        background.getChildren().add(border);
        challengeArea.toBack();

        challenge.getChildren().clear();

        //randomly generate challenge when 'new challenge' button is hit
        challengeString = generateChallenge();
        //TODO there's a bug where the default challenge that is shown does not comply to the following rules
        //i.e. default selected Button is Starter, but it can generate other difficulty challenges instead (upon setup)
        //Possible solution: just set a particular string as the default (but that overrides it)


        for (int i = 0; i < challengeString.length(); i++) {
            if (i >= 0 && i <= 2) {
                GTile challengeSquare = new GTile(new Position(i % 3, 0, State.charToState(challengeString.charAt(i))));
                challenge.getChildren().add(challengeSquare);
            } else if (i >= 3 && i <= 5) {
                GTile challengeSquare = new GTile(new Position(i % 3, 1, State.charToState(challengeString.charAt(i))));
                challenge.getChildren().add(challengeSquare);
            } else {
                GTile challengeSquare = new GTile(new Position(i % 3, 2, State.charToState(challengeString.charAt(i))));
                challenge.getChildren().add(challengeSquare);
            }
        }


    }

    private String generateChallenge() {
        Random r = new Random();
        //int random = r.nextInt(Solution.SOLUTIONS.length);
        int random;


        /*
        N.B. need to decrement by one since you will be indexing from array
         */
        //double check calculations
        if (group.getSelectedToggle() == b1) {
            random = r.nextInt (23);
        } else if (group.getSelectedToggle() == b2) {
            random = 24+(int)(Math.random()*(47-24+1));
            //this bit would need to be a range
        } else if (group.getSelectedToggle() == b3) {
            random = 48+(int)(Math.random()*(71-48+1));
        } else if (group.getSelectedToggle() == b4) {
            random = 72+(int)(Math.random()*(95-72+1));
        } else {
            random = 95+(int)(Math.random()*(119-95+1));
        }


        //CHECK: I'm doing recursion properly here right??
        //if the current challenge is the same as the newly generated one, generate a new challenge again
        if (challengeString == Solution.SOLUTIONS[random].objective)
            return generateChallenge();
        else
            return Solution.SOLUTIONS[random].objective;

    }

    private void makeChallengeControls() {
        Button button = new Button("New Challenge");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                challengeString = generateChallenge();
                setupChallengeArea();
                System.out.println("The new challenge is a " + group.getSelectedToggle() + " challenge: "+ challengeString);
                //TODO add toString() for the ToggleGroup


            }
        });
        button.setLayoutX(CHALLENGE_AREA_X+SQUARE_SIZE*4);
        button.setLayoutY(CHALLENGE_AREA_Y);
        button.toFront();

        Set<RadioButton> difficulties = new HashSet<>();
        //ToggleGroup group = new ToggleGroup();


        difficulties.add(b1);
        b1.setToggleGroup(group);
        b1.setLayoutX(CHALLENGE_AREA_X+SQUARE_SIZE*4);
        b1.setLayoutY(CHALLENGE_AREA_Y+40);
        //EVERY TIME A NEW GAME OCCURS set Starter as default
        b1.setSelected(true);
        //generateChallenge();

        difficulties.add(b2);
        b2.setToggleGroup(group);
        b2.setLayoutX(CHALLENGE_AREA_X+SQUARE_SIZE*4);
        b2.setLayoutY(CHALLENGE_AREA_Y+65);


        difficulties.add(b3);
        b3.setToggleGroup(group);
        b3.setLayoutX(CHALLENGE_AREA_X+SQUARE_SIZE*4);
        b3.setLayoutY(CHALLENGE_AREA_Y+90);


        difficulties.add(b4);
        b4.setToggleGroup(group);
        b4.setLayoutX(CHALLENGE_AREA_X+SQUARE_SIZE*4);
        b4.setLayoutY(CHALLENGE_AREA_Y+115);


        difficulties.add(b5);
        b5.setToggleGroup(group);
        b5.setLayoutX(CHALLENGE_AREA_X+SQUARE_SIZE*4);
        b5.setLayoutY(CHALLENGE_AREA_Y+140);

        controls.getChildren().add(button);
        controls.getChildren().addAll(difficulties);



    }

    // FIXME Task 11: Generate interesting challenges (each challenge may have just one solution)

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("IQ Focus by thu11g");
        Scene scene = new Scene(root,WINDOW_WIDTH,WINDOW_HEIGHT);
        scene.setFill(Color.LIGHTGRAY);

        root.getChildren().add(background);
        root.getChildren().add(controls);
        root.getChildren().add(gTiles);
        root.getChildren().add(challenge);

        setupBackground();
        setupTestControls();
        renderGameBoard(game);
        setupInitialTileArea();
        setupChallengeArea();
        makeChallengeControls();

        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SLASH && !isSLASHDown) { // update hints while "/" is pressed
                String h = new Challenge(challengeString).generateHint(game.getPlacementString());
                if (h != null) {
                    hint = new GTile(new Tile(h));
                    hint.setOpacity(0.5);
                    root.getChildren().add(hint);
                }
                isSLASHDown = true;
            }
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SLASH) { // remove hints when "/" is released
                root.getChildren().remove(hint);
                isSLASHDown = false;
            }
        });
    }
}
