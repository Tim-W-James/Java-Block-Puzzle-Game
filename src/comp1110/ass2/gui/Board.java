package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.*;

// a packaged executable called game.jar can be found at the root directory,
// to run this on IntelliJ go to Run -> Edit Configurations -> VM options,
// and add: --module-path ${PATH_TO_FX} --add-modules=javafx.controls,javafx.fxml,javafx.media
// Note: for changes to be reflected, use Build -> Build Artifacts -> Build

/*
Authorship: Nicholas Dale, Rebecca Gibson, Timothy James
*/

public class Board extends Application {

    /**
     * The GameBoardArray contains most of the logic behind the game
     */

    private GameBoardArray game = new GameBoardArray();

    private GTile hint;
    private GTile preview;
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

    // Tile starting area
    private static final int TILE_AREA_STARTING_X = BOARD_WIDTH + 70;

    // Challenge area
    private static final int CHALLENGE_AREA_WIDTH = SQUARE_SIZE * 3;
    private static final int CHALLENGE_AREA_HEIGHT = SQUARE_SIZE * 3;
    private static final int CHALLENGE_AREA_X = OFFSET_X + SQUARE_SIZE * 3;
    private static final int CHALLENGE_AREA_Y = BOARD_HEIGHT + 60;

    //Challenge controls
    private final static ToggleGroup group = new ToggleGroup();
    private final static RadioButton b1 = new RadioButton("Starter");
    private final static RadioButton b2 = new RadioButton("Junior");
    private final static RadioButton b3 = new RadioButton("Expert");
    private final static RadioButton b4 = new RadioButton("Master");
    private final static RadioButton b5 = new RadioButton("Wizard");

    //The challenge string
    private static String challengeString;
    //The respective solution
    private static String solutionString;

    private static final String URI_BASE = "comp1110/ass2/gui/assets/";

    //Groups
    private final Group root = new Group();
    private final Group background = new Group();
    private final Group controls = new Group();
    private final Group gTiles = new Group();
    private final Group challenge = new Group();
    private final Group defaultChallenge = new Group();


    private final Text completionText = new Text("Challenge completed!");

    //Rotation
    private static final long ROTATION_THRESHOLD = 100;


    class GTile extends ImageView {
        Tile t;
        boolean inGame = false;

        /**
         * Constructor to build a PLAYING tile
         * @param t a logical Tile object
         */
        GTile(Tile t) {
            this.t = t;

            setImage(new Image(URI_BASE + t.getShape().toString().toLowerCase() + ".png"));

            // Sets the height and width of the tiles
            if (t.getDirection() == Direction.NORTH || t.getDirection() == Direction.SOUTH) {
                setFitWidth(t.getWidth() * SQUARE_SIZE);
                setFitHeight(t.getHeight() * SQUARE_SIZE);
            } else {
                setFitWidth(t.getHeight() * SQUARE_SIZE);
                setFitHeight(t.getWidth() * SQUARE_SIZE);
            }

            //Location
            setLayoutX((t.getPosition().getX()) * SQUARE_SIZE + OFFSET_X);
            setLayoutY((t.getPosition().getY()) * SQUARE_SIZE + OFFSET_Y);

            //Rotation
            Rotate rotation = new Rotate();
            rotation.setPivotX(0);
            rotation.setPivotY(0);
            rotation.setAngle(t.getDirection().toDegree());
            getTransforms().add(rotation);


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
        }

        /**
         * Constructor to build CHALLENGE tiles for the challenge area

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
                    throw new IllegalArgumentException("Square position " + square + " is invalid");
            }

            setImage(new Image(URI_BASE + objTileID + ".png"));
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);

            setLayoutX(CHALLENGE_AREA_X + (square.getX() * SQUARE_SIZE));
            setLayoutY(CHALLENGE_AREA_Y + (square.getY() * SQUARE_SIZE));
        }

        /**
         * Rotate the tile by 90 degrees and update any necessary coordinates
         * Defaults to 90 degrees if no argument provided
         */
        void rotate() {
            root.getChildren().remove(preview);

            Direction d = t.getDirection();
            Direction newDirection;
            switch (d) {
                case NORTH:
                    newDirection = Direction.EAST;
                    break;
                case EAST:
                    newDirection = Direction.SOUTH;
                    break;
                case SOUTH:
                    newDirection = Direction.WEST;
                    break;
                case WEST:
                    newDirection = Direction.NORTH;
                    break;
                default:
                    throw new IllegalStateException("Unexpected direction value: " + d);
            }

            this.t = new Tile(t.getShape(),t.getPosition(),newDirection);

            setRotate(t.getDirection().toDegree());

            System.out.println("The tile " + this + " is rotated 90 degrees");
        }

        /**
         * Snaps tile to game grid
         * This method updates GameBoardArray
         * And also updates the game board rendering
         */
        void snapToGameGrid() {
            try {
                Position pos = getGameGridSquare();

                if (pos != null) {
                    Tile newTile = new Tile(this.t.getShape(),pos,this.t.getDirection());
                    setInGame(true);

                    game.removeFromBoardSafe(t);

                    if (game.checkValidPosition(newTile)) {
                        game.updateBoardPosition(newTile);
                        renderGameBoard(game);
                        setupInitialTiles();

                    } else {
                        sendToDefaultPlacement();
                    }
                } else {
                    sendToDefaultPlacement();
                }
                checkCompletion();
            }
            // when invalid placements are attempted, send to default placement
            catch (IllegalArgumentException ex) {
                sendToDefaultPlacement();
            }
        }

        /**
         * get the game grid square for a this tile
         * @return returns a position for the top left of the tile
         */
        Position getGameGridSquare() {
            return Board.getGameGridSquare(getLayoutX(),getLayoutY());
        }

        /**
         * Sends the playing tiles to its starting position
         */
        void sendToDefaultPlacement() {
            double homeX;
            double homeY;

            switch (t.getShape()) {
                case A:
                    homeY = 0;
                    homeX = TILE_AREA_STARTING_X;
                    break;
                case B:
                    homeY = OFFBOARD_SQUARE_SIZE * 3 - 15;
                    homeX = TILE_AREA_STARTING_X;
                    break;
                case C:
                    homeY = OFFBOARD_SQUARE_SIZE * 6 - 35;
                    homeX = TILE_AREA_STARTING_X;
                    break;
                case D:
                    homeY = OFFBOARD_SQUARE_SIZE * 9 - 55;
                    homeX = TILE_AREA_STARTING_X;
                    break;
                case E:
                    homeY = OFFBOARD_SQUARE_SIZE * 12 - 70;
                    homeX = TILE_AREA_STARTING_X;
                    break;
                case F:
                    homeY = OFFBOARD_SQUARE_SIZE * 15 - 75;
                    homeX = TILE_AREA_STARTING_X;
                    break;
                case G:
                    homeY = OFFBOARD_SQUARE_SIZE * 17 - 100;
                    homeX = TILE_AREA_STARTING_X;
                    break;
                case H:
                    homeY = OFFBOARD_SQUARE_SIZE * 20 - 125;
                    homeX = TILE_AREA_STARTING_X;
                    break;
                case I:
                    homeY = OFFBOARD_SQUARE_SIZE * 23 - 150;
                    homeX = TILE_AREA_STARTING_X + OFFBOARD_SQUARE_SIZE * 2;
                    break;
                default:
                    homeY = OFFBOARD_SQUARE_SIZE * 26 - 175;
                    homeX = TILE_AREA_STARTING_X;
                    break;
            }

            //Remove from starting area if tile on game board
            if (game.getPlacementString().contains(String.valueOf(t.getShape().toChar()))) {
                setOpacity(0);
            }

            setInGame(false);
            setLayoutX(homeX);
            setLayoutY(homeY-25);
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

        /**
         * Scales tile if it's active
         * @param inGame whether the tile is in the game grid
         */
        void setInGame(boolean inGame) {
            this.inGame = inGame;
            if (!inGame) {
                setScaleX(0.5);
                setScaleY(0.5);
            } else {
                setScaleX(1);
                setScaleY(1);
            }
        }

        @Override
        public String toString() {
            return "Fit Width/Height: " + getFitWidth() + "," + getFitHeight() + " | " + "Position x,y: " +
                    getLayoutX() + "," + getLayoutY() + "\n" + this.t + "\n";
        }
    }

    class DraggableTile extends GTile {
        private double mouseX, mouseY;
        long lastRotationTime = System.currentTimeMillis();
        boolean isHover = false;
        boolean isDrag = false;

        /**
         * Constructor to build a draggable gTile
         * @param t A logical tile
         * @param inGame whether the tile is starting in the game or not
         */
        DraggableTile(Tile t, boolean inGame) {
            super(t);
            setInGame(inGame);
            if (! inGame) {
                sendToDefaultPlacement();
            }

            this.setOnMousePressed(event -> {
                mouseX = event.getX(); //gets X coordinates
                mouseY = event.getY(); //gets Y coordinates
                this.toFront();
            });

            this.setOnMouseDragged(event -> {
                setLayoutX(event.getSceneX() - mouseX);
                setLayoutY(event.getSceneY() - mouseY);
                isDrag = true;
                placementPreview();
                setInGame(true); //resizes when dragged for easier placement
            });

            this.setOnMouseReleased(event -> {
                isDrag = false;
                //If out of GameGrid send to default placement, otherwise snap
                if (getLayoutX() > GAME_GRID_WIDTH || getLayoutY() > BOARD_HEIGHT) {
                    setInGame(false);
                    sendToDefaultPlacement();
                } else {
                    snapToGameGrid();
                }
            });

            this.setOnMouseEntered(event -> isHover = true);

            this.setOnMouseExited(event -> isHover = false);

            //Scroll to rotate
            this.setOnScroll(event -> {
                if (System.currentTimeMillis() - lastRotationTime > ROTATION_THRESHOLD) {
                    lastRotationTime = System.currentTimeMillis();
                    rotate();
                    event.consume();
                }
            });

        }


        /**
         * Finds placement of preview tile to show where snapping will occur
         * (Modification of snapToGameGrid() function)
         */
        void placementPreview() {
            try {
                Position pos = getGameGridSquare();

                if (pos != null) {
                    Tile newTile = new Tile(this.t.getShape(),pos,this.t.getDirection());
                    if (game.checkValidPosition(newTile)) {
                        createPreview(newTile);
                    }
                    else {
                        root.getChildren().remove(preview);
                    }
                }
            }
            // when invalid placements are attempted, send to default placement
            catch (IllegalArgumentException ex) {
                root.getChildren().remove(preview);
            }
        }
    }


    /**
     * Creates the tile preview image
     * @param t Create a preview tile representing the logical tile t
     */
    private void createPreview(Tile t) {
        root.getChildren().remove(preview);
        preview = new GTile(t);
        preview.setOpacity(0.25);
        root.getChildren().add(preview);
    }

    /**
     * Returns nearest tile to a given x,y position. Positions are in GUI, NOT GameBoard
     * @param x an arbitrary x position on the GUI. If called from within a tile, call it with getLayoutX()
     * @param y an arbitrary y position on the GUI. If called from within a tile, call it with getLayoutY()
     * @return a DraggableTile object representing the closest tile to the given coordinates
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
                if (distance < shortestDistance && distance != 0) {
                    candidate = t;
                    shortestDistance = distance;
                }
            }
        }

        if (candidate != null) {
            return candidate;
        } else {
            throw new NoSuchElementException("No tiles exist in gTiles");
        }
    }

    /**
     * Returns the grid position of the gamegrid
     * @param x Optional x
     * @param y Optional y
     * The function can either take a specified x,y or be called from a given tile
     * @return Position or null if position is invalid
     */
    static Position getGameGridSquare(double x, double y){
        if (x < BOARD_WIDTH && y < BOARD_HEIGHT) {
            double ajX = x - OFFSET_X + 5;
            double ajY = y - OFFSET_Y + 5;

            int tileX = (int)Math.floor(ajX / SQUARE_SIZE);
            int tileY = (int)Math.floor(ajY / SQUARE_SIZE);
            return (new Position(tileX, tileY));
        }
        else
            return null;
//                throw new IllegalArgumentException("Tile at position " + x + "," + y + " is not on game board");
    }

    /**
     * Sets up background of game
     */
    private void setupBackground() {
        background.getChildren().clear();
        //Board background image
        ImageView board = new ImageView();
        board.setImage(new Image(URI_BASE + "board" + ".png"));
        board.setFitWidth(BOARD_WIDTH);
        board.setFitHeight(BOARD_HEIGHT);
        board.toBack();

        //Challenge area on board
        Rectangle challengeArea = new Rectangle(OFFSET_X+SQUARE_SIZE*3,OFFSET_Y+SQUARE_SIZE+3,SQUARE_SIZE*3,SQUARE_SIZE*3);
        challengeArea.setOpacity(0.5);
        challengeArea.setFill(Color.BLACK);

        //Tiles background
        Rectangle tileBg = new Rectangle(BOARD_WIDTH,0,WINDOW_WIDTH-BOARD_WIDTH,WINDOW_HEIGHT);
        tileBg.setFill(Color.AQUAMARINE);
        tileBg.toBack();

        background.getChildren().addAll(board,tileBg,challengeArea);
    }

    /***
     * Create controls for testing
     */
    private void setupResetButton() {
        Button reset = new Button("Reset");
        reset.setOnAction(actionEvent -> {
            game.resetGameBoardArray();
            resetGTiles();
            hideCompletion();

            renderGameBoard(game);
            setupInitialTiles();
        });
        reset.toFront();

        HBox hb = new HBox();
        hb.getChildren().add(reset);
        hb.setSpacing(10);
        hb.setLayoutX(GAME_GRID_WIDTH/2.0+10);
        hb.setLayoutY(15);

        controls.getChildren().add(hb);
        controls.toFront();
    }

    /**
     * Setting up initial tiles and adding them to root
     */
    private void setupInitialTiles() {
        //Position values for tile shouldn't matter while it is in setup
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
    }

    /**
     * Renders game board
     * @param g the GameBoardArray that will be rendered
     */
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
     * Converts tiles into gTiles for rendering
     */
    private void makeTiles(HashSet<Tile> tiles) {
        gTiles.getChildren().clear();
        for (Tile t :
                tiles) {
            gTiles.getChildren().add(new GTile(t));
        }
        gTiles.toFront();
    }

    /**
     * Reset board to the beginning state
     */
    private void resetGTiles() {
        gTiles.getChildren().removeAll(gTiles);
        root.getChildren().remove(preview);
    }

    /**
     * Setup instruction box gui
     */
    private void setupInstructions() {
        Button button = new Button("How to Play");
        button.setLayoutX(15);
        button.setLayoutY(BOARD_HEIGHT + 5);

        Stage popup = new Stage();

        popup.setTitle("How to Play");
        button.setOnAction(event -> popup.show());

        // update with more info, formatting
        VBox helpBox = new VBox();
        helpBox.getChildren().add(new Text(
                "Welcome to virtual IQ-Focus!\n"+
                        "\n"+
                        "Your challenge is to place all the pieces of the game onto the \n" +
                        "board in a such a way that the shaded area in the middle\n"+
                        "corresponds to the challenge grid at the bottom of the screen\n" +
                        "\n"+
                 "Controls:\n" +
                "- To Move: Click and drag\n" +
                "- To Rotate: Press \"R\" while hovering over a shape or scroll over\n"+"a tile\n" +
                "- To Remove a Piece: Click over a tile on the board\n"+
                "- Hints: Hold \"/\""));
        Scene helpScene = new Scene(helpBox, 400, 300);
        popup.setScene(helpScene);

        button.toBack();
        root.getChildren().addAll(button);

    }

    /**
     * Set up challenge area gui
     */
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

        //Formatting squares
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

    /**
     * Ensures that default challenge shown is the first starter challenge
     */

    private void forceDefaultChallenge() {
        challengeString = "RRRBWBBRB";
        solutionString = "a000b013c113d302e323f400g420h522i613j701";
        for (int i = 0; i < challengeString.length(); i++) {
            if (i >= 0 && i <= 2) {
                GTile challengeSquare = new GTile(new Position(i % 3, 0, State.charToState(challengeString.charAt(i))));
                defaultChallenge.getChildren().add(challengeSquare);
            } else if (i >= 3 && i <= 5) {
                GTile challengeSquare = new GTile(new Position(i % 3, 1, State.charToState(challengeString.charAt(i))));
                defaultChallenge.getChildren().add(challengeSquare);
            } else {
                GTile challengeSquare = new GTile(new Position(i % 3, 2, State.charToState(challengeString.charAt(i))));
                defaultChallenge.getChildren().add(challengeSquare);
            }
        }
    }

    private String generateChallenge() {
        Random r = new Random();
        int random;

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
        //get respective solution
        solutionString = FocusGame.getSolution(Solution.SOLUTIONS[random].objective);

        //if current challenge is the same as the newly generated one, generate a new challenge again
        if (challengeString == Solution.SOLUTIONS[random].objective)
            return generateChallenge();
        else
            return Solution.SOLUTIONS[random].objective;
    }

    private void makeChallengeControls() {
        Button button = new Button("New Challenge");
        button.setOnAction(event -> {
            //Generates new challenge
            challengeString = generateChallenge();
            setupChallengeArea();
            defaultChallenge.getChildren().clear();
            hideCompletion();

            //Resets game board
            game.resetGameBoardArray();
            resetGTiles();
            renderGameBoard(game);
            setupInitialTiles();

        });
        button.setLayoutX(CHALLENGE_AREA_X+SQUARE_SIZE*4);
        button.setLayoutY(CHALLENGE_AREA_Y);
        button.toFront();

        Set<RadioButton> difficulties = new HashSet<>();

        difficulties.add(b1);
        b1.setToggleGroup(group);
        b1.setLayoutX(CHALLENGE_AREA_X+SQUARE_SIZE*4);
        b1.setLayoutY(CHALLENGE_AREA_Y+40);

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

    /**
     * Creates completion message
     */
    private void setupCompletion() {
        completionText.setFont(Font.font("Comic Sans", FontWeight.EXTRA_BOLD, 30));
        completionText.setLayoutX(BOARD_WIDTH/2.0-140);
        completionText.setLayoutY(BOARD_HEIGHT+30);
        completionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(completionText);
    }

    /**
     * Hides completion message
     */
    private void hideCompletion() {
        completionText.setOpacity(0);
    }

    /**
     * Shows completion message
     */
    private void showCompletion() {
        completionText.setOpacity(1);
        completionText.toFront();
    }

    /**
     * Checks if game is completed
     */
    private void checkCompletion() {
        if (game.getPlacementString().equals(solutionString)) {
            showCompletion();
        }
    }

    /**
     * Main method
     * @param primaryStage Starting stage for the GUI
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("IQ Focus by thu11g");
        Scene scene = new Scene(root,WINDOW_WIDTH,WINDOW_HEIGHT);
        scene.setFill(Color.LIGHTGRAY);

        root.getChildren().add(background);
        root.getChildren().add(controls);
        root.getChildren().add(gTiles);
        root.getChildren().add(challenge);
        root.getChildren().add(defaultChallenge);

        setupBackground();
        setupResetButton();
        renderGameBoard(game);

        setupInitialTiles();

        setupChallengeArea();
        makeChallengeControls();
        forceDefaultChallenge();
        hideCompletion();

        setupInstructions();

        setupCompletion();

        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnMousePressed(e -> {
            double x = e.getX();
            double y = e.getY();
            Position p = getGameGridSquare(x,y);

            // remove a piece on click
            if (x < BOARD_WIDTH && y < BOARD_HEIGHT) {
                try {
                    Tile t = game.getTileAt(p);

                    game.removeFromBoard(t);
                    root.getChildren().remove(preview);
                    renderGameBoard(game);
                    setupInitialTiles();
                }
                catch (IllegalArgumentException ex) {
                    System.out.println("IllegalArgumentException while trying to remove tile at " + p);
                }
                catch(ArrayIndexOutOfBoundsException ex) {
                    System.out.println("ArrayIndexOutOfBoundsException while trying to remove tile at " + p);
                }
            }
        });

        //Implementing hints via "/"
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
            // rotate any piece being hovered or dragged via "R"
            else if (e.getCode() == KeyCode.R) {
                for (Node current : gTiles.getChildren()) {
                    if (current instanceof DraggableTile) {
                        if (((DraggableTile) current).isHover || ((DraggableTile) current).isDrag) {
                            ((DraggableTile) current).rotate();
                            ((DraggableTile) current).placementPreview();
                        }
                    }
                }
            }
        });
        // remove hints when "/" is released
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SLASH) {
                root.getChildren().remove(hint);
                isSLASHDown = false;
            }
        });
    }
}
