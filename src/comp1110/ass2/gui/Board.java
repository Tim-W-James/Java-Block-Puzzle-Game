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
import javafx.scene.input.MouseEvent;
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
import javafx.scene.effect.DropShadow;

import java.lang.reflect.Array;
import java.util.*;

import static comp1110.ass2.Shape.*;

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
   // private GameBoardArray game = new GameBoardArray("a701b400c410d303e111f330g030h000i733j332");
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

    // Area where the tiles are placed at the start of a game
    private static final int TILE_AREA_STARTING_X = BOARD_WIDTH + 70;
    private static final int TILE_AREA_STARTING_Y = 5;  //I don't think this value actually does anything tbh

    private static final int TILE_AREA_FINISH_X = WINDOW_WIDTH;
    private static final int TILE_AREA_FINISH_Y = WINDOW_HEIGHT;

    // Area where the challenge stuff lives
    private static final int CHALLENGE_AREA_WIDTH = SQUARE_SIZE * 3;
    private static final int CHALLENGE_AREA_HEIGHT = SQUARE_SIZE * 3;
    private static final int CHALLENGE_AREA_X = OFFSET_X + SQUARE_SIZE * 3; //Maybe make a universal margin constant
    private static final int CHALLENGE_AREA_Y = BOARD_HEIGHT + 60; //Maybe make a universal margin constant

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

    private final Group root = new Group();
    private final Group background = new Group();
    private final Group controls = new Group();
    private final Group gTiles = new Group();
    private final Group challenge = new Group();
    private final Group defaultChallenge = new Group();

    private TextField textField;

    private final Text completionText = new Text("Challenge completed!");

    private HashSet<DraggableTile> allTiles = new HashSet<>();

    //Rotation
    private static final long ROTATION_THRESHOLD = 100;



    class GTile extends ImageView {
        Tile t;
        boolean inGame = false;


        //What's the difference between this GTile and the next?? Which one are we actually using?

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

            // Scaling
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

            setImage(new Image(URI_BASE + t.getShape().toString().toLowerCase() + ".png"));

            // Sets the height and width of the tiles
            if (t.getDirection() == Direction.NORTH || t.getDirection() == Direction.SOUTH) {
                setFitWidth(t.getWidth() * SQUARE_SIZE);
                setFitHeight(t.getHeight() * SQUARE_SIZE);
            } else {
                setFitWidth(t.getHeight() * SQUARE_SIZE);
                setFitHeight(t.getWidth() * SQUARE_SIZE);
            }

//            System.out.println(t.getHeight() + ", " + t.getWidth());
//            System.out.println(t.getShape().getMaxReach(t.getDirection(), true) + ", " + t.getShape().getMaxReach(t.getDirection(), false));

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
         * Rotate the tile by 90 degrees and update any necessary coordinates
         */
        void rotate(int rotation) {
            root.getChildren().remove(preview);
            setRotate((getRotate() + rotation) % 360);
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
//            System.out.println("The tile " + this.t + " is rotated " + rotation + " degrees");
        }

        void rotate() {
            rotate(90);
        }

        /**
         * Snaps tile to game grid
         * This method updates GameBoardArray
         */
        void snapToGameGrid() {
            try {
                double x = getLayoutX();
                double y = getLayoutY();
                Position pos = null;

                // Figure out where the game square is
                if (x < BOARD_WIDTH && y < BOARD_HEIGHT) {
                    double ajX = x - OFFSET_X + 10;
                    double ajY = y - OFFSET_Y + 10;

                    int tileX = (int)Math.floor(ajX / SQUARE_SIZE);
                    int tileY = (int)Math.floor(ajY / SQUARE_SIZE);

/*
                System.out.println("Guessing tile is at: " + tileX + "," + tileY);
                System.out.println("Based on input coords: " + x + "," + y);
                System.out.println("And adjusted coords: " + ajX + "," + ajY);
*/
                    pos = new Position(tileX,tileY);
                }
//                System.out.println("Adding tile " + t + " at position " + pos);

                if (pos != null) {
                    Tile newTile = new Tile(this.t.getShape(),pos,this.t.getDirection());
                    this.inGame = true;
                    if (inGame) {
                        game.removeFromBoardSafe(t);

                        if (game.checkValidPosition(newTile)) {
                            game.updateBoardPosition(newTile);
                            renderGameBoard(game);
                            setupInitialTileArea();

                        } else {
                            sendToDefaultPlacement();
                        }
                    }
                } else {
                    sendToDefaultPlacement();
                }
                checkCompletion();
            }
            // when invalid placements are attempted, send to default placement - Tim
            catch (IllegalArgumentException ex) {
                sendToDefaultPlacement();
            }
        }

        /**
         * Returns the grid position of the gamegrid
         * @param x
         * @param y
         */
         Position getGameGridSquare(double x, double y){
            if (x < BOARD_WIDTH && y < BOARD_HEIGHT) {
                double ajX = x - OFFSET_X + 10;
                double ajY = y - OFFSET_Y + 10;

                int tileX = (int)Math.floor(ajX / SQUARE_SIZE);
                int tileY = (int)Math.floor(ajY / SQUARE_SIZE);

//                System.out.println("Guessing tile is at: " + tileX + "," + tileY);
//                System.out.println("Based on input coords: " + x + "," + y);
//                System.out.println("And adjusted coords: " + ajX + "," + ajY);

                return (new Position(tileX, tileY));
            }
            else
                return (new Position(0,0));
        }

        Position getGameGridSquare() {
             return getGameGridSquare(getLayoutX(),getLayoutY());
        }



        /**
         * Sends the tile to its starting position
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
                //F is derpy for some reason
                //Scratch that: they're all derpy
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

            if (game.getPlacementString().contains(String.valueOf(t.getShape().toChar()))) {
                setOpacity(0);
            }

            // now resets scale when sent back - Tim
            setScaleX(0.5);
            setScaleY(0.5);

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
            return "Fit Width/Height: " + getFitWidth() + "," + getFitHeight() + " | " + "Position x,y: " + getLayoutX() + "," + getLayoutY() + "\n" + this.t + "\n";
        }
    }

    class DraggableTile extends GTile {
        private double mouseX, mouseY;
        long lastRotationTime = System.currentTimeMillis();
        boolean isHover = false;
        boolean isDrag = false;


        DraggableTile(Tile t, boolean inGame) {
            super(t, inGame);

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
                setInGame(true);    //resizes when dragged for easier placement
            });

            this.setOnMouseReleased(event -> {
                isDrag = false;
                // If out of GameGrid send to default placement, otherwise snap
                if (getLayoutX() > GAME_GRID_WIDTH || getLayoutY() > BOARD_HEIGHT) {
                    setInGame(false);
                    sendToDefaultPlacement();
                } else {
                    snapToGameGrid();
                }
            });

            this.setOnMouseEntered(event -> {
                isHover = true;
            });

            this.setOnMouseExited(event -> {
                isHover = false;
            });

            //doesn't work because it can't tell if you're referring to 'this' tile when you press R
            //Can anyone figure this out??
//            this.setOnMouseEntered(event -> {
//                System.out.println("OI");
//                isHover = true;
//                setOnKeyPressed(e -> {
//                    if (e.getCode() == KeyCode.R) {
//                        rotate();
//                    }
//                });
//            });

            //Scroll to Rotate
            /*
            N.B. the first two lines (and their respective variables)
            were taken from Dinosaurs assignment. Do we need to state that in SoO???
             */
            this.setOnScroll(event -> {
                if (System.currentTimeMillis() - lastRotationTime > ROTATION_THRESHOLD) {
                    lastRotationTime = System.currentTimeMillis();
                    rotate();
                    event.consume();
                }
            });

        }

        // modification of snapToGameGrid() which creates a preview of where snapping will occur
        void placementPreview() {
            try {
                double x = getLayoutX();
                double y = getLayoutY();
                Position pos = null;

                // Figure out where the game square is
                if (x < BOARD_WIDTH && y < BOARD_HEIGHT) {
                    double ajX = x - OFFSET_X + 10;
                    double ajY = y - OFFSET_Y + 10;

                    int tileX = (int)Math.floor(ajX / SQUARE_SIZE);
                    int tileY = (int)Math.floor(ajY / SQUARE_SIZE);

                    pos = new Position(tileX,tileY);
                }

                if (pos != null) {
                    Tile newTile = new Tile(this.t.getShape(),pos,this.t.getDirection());
                    this.inGame = true;
                    if (inGame) {
                        if (game.checkValidPosition(newTile)) {
                            createPreview(newTile);
                        }
                        else {
                            root.getChildren().remove(preview);
                        }
                    }
                }
            }
            // when invalid placements are attempted, send to default placement - Tim
            catch (IllegalArgumentException ex) {
                root.getChildren().remove(preview);
            }
        }

        //N.B. maybe use a key 'r' and every time that is pressed, the tile is rotated 90 degrees clockwise
    }

    // creates a preview image of a tile
    private void createPreview(Tile t) {
        root.getChildren().remove(preview);
        preview = new GTile(t);
        preview.setOpacity(0.25);
        root.getChildren().add(preview);
    }

    /**
     * Returns nearest tile to a given x,y position. Positions are in GUI NOT GameBoard
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
//                System.out.println("Changing shortest distance of " + shortestDistance + " To distance of " + distance);
                if (distance < shortestDistance && distance != 0) {
                    candidate = t;
                    shortestDistance = distance;
                }
            }
        }

        if (candidate != null) {
//            System.out.println(shortestDistance);
//            System.out.println(candidate);
            return candidate;
        } else {
            throw new NoSuchElementException("No tiles exist in gTiles");
        }
    }

    private void setupBackground() {
        background.getChildren().clear();
        //Board background
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

    /* Create controls for testing */
    private void setupTestControls() {
        Button reset = new Button("Reset");
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                game.resetGameBoardArray();
                allTiles.clear();
                resetGTiles();
                hideCompletion();

                renderGameBoard(game);
                setupInitialTileArea();
//                System.out.println("GameBoard has been reset");
            }
        });
//        reset.setLayoutX(20);
//        reset.setLayoutY(BOARD_HEIGHT + 20);
        reset.toFront();


        HBox hb = new HBox();
        //Could possibly get rid of this now that we don't need textfield stuff

        //hb.getChildren().addAll(label1, textField, button,reset);
        hb.getChildren().add(reset);
        hb.setSpacing(10);
        hb.setLayoutX(GAME_GRID_WIDTH/2+10);
        hb.setLayoutY(15);

        controls.getChildren().add(hb);
        controls.toFront();


    }

    /**
     * Place initial tiles
     */
    private void setupInitialTileArea() {
        //NOTE: Position values for tile shouldn't matter while it is in setup
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

//        System.out.println(gTiles.getChildren());
    }

    // make sure these accommodate for the gameboardarray if you are using them in that manner - Tim
    private void addTile(Tile t) {
        gTiles.getChildren().add(new GTile(t));
    }

    private void removeTile(Tile t) { // this does not work, as it is attempting to remove a Tile, not GTile - Tim


        gTiles.getChildren().remove(new GTile(t));
    }


    /**
     * Reset board to the beginning state
     */
    private void resetGTiles() {
        gTiles.getChildren().removeAll(gTiles);
        root.getChildren().remove(preview);

    }


    void makePlacementFromString(String placement) {
        game.updateBoardPosition(placement);
        renderGameBoard(game);
    }


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
                "Controls:\n" +
                "- To Move: Click and drag\n" +
                "- To Rotate: Press \"R\" while hovering over a shape\n" +
                "- Hints: Hold \"/\""));
        Scene helpScene = new Scene(helpBox, 300, 80);
        popup.setScene(helpScene);

        button.toBack();
        root.getChildren().addAll(button);

    }



@SuppressWarnings("Duplicates")
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

    //Really awful way of going about this bug but oh well

    //Ensures that default challenge shown is the first starter challenge
    @SuppressWarnings("Duplicates")
    private void showDefaultChallenge() {
        String defChal = "RRRBWBBRB";
        for (int i = 0; i < defChal.length(); i++) {
            if (i >= 0 && i <= 2) {
                GTile challengeSquare = new GTile(new Position(i % 3, 0, State.charToState(defChal.charAt(i))));
                defaultChallenge.getChildren().add(challengeSquare);
            } else if (i >= 3 && i <= 5) {
                GTile challengeSquare = new GTile(new Position(i % 3, 1, State.charToState(defChal.charAt(i))));
                defaultChallenge.getChildren().add(challengeSquare);
            } else {
                GTile challengeSquare = new GTile(new Position(i % 3, 2, State.charToState(defChal.charAt(i))));
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
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Generates new challenge
                challengeString = generateChallenge();
                setupChallengeArea();
                defaultChallenge.toBack();
                hideCompletion();

                //Resets game board
                game.resetGameBoardArray();
                allTiles.clear();
                resetGTiles();
                renderGameBoard(game);
                setupInitialTileArea();

                /*
                Rip my comp, literally everytime the game's reset it's just layering images over images
                and my laptop can't handle all that very well lol
                 */

//                System.out.println("The new challenge is a " + group.getSelectedToggle() + " challenge: "+ challengeString);
                //System.out.println("The corresponding solution is " + solutionString);
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

    /**
     * Completion message
     */
    //Creating completion message
    private void setupCompletion() {
        completionText.setFont(Font.font("Comic Sans", FontWeight.EXTRA_BOLD, 30));
        completionText.setLayoutX(BOARD_WIDTH/2-140);
        completionText.setLayoutY(BOARD_HEIGHT+30);
        completionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(completionText);
    }
    //Hiding completion message
    private void hideCompletion() {
        completionText.setOpacity(0);
    }
    //Showing completion message
    private void showCompletion() {
        completionText.setOpacity(1);
        completionText.toFront();
    }
    //Checking if game is completed
    private void checkCompletion() {
        if (game.getPlacementString().equals(solutionString)) {
            showCompletion();
//            System.out.println("Game is completed");
//            System.out.println(game.getPlacementString() + " matches " + solutionString);
        }
    }

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
        setupTestControls();
        renderGameBoard(game);

        setupInitialTileArea();

        setupChallengeArea();
        makeChallengeControls();
        showDefaultChallenge();
        hideCompletion();

        setupInstructions();

        setupCompletion();

        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnMousePressed(e -> {
            double x = e.getX();
            double y = e.getY();

            // remove a piece on click
            if (x < BOARD_WIDTH && y < BOARD_HEIGHT) {
                try {
                    double ajX = x - OFFSET_X + 10;
                    double ajY = y - OFFSET_Y + 10;

                    int tileX = (int) Math.floor(ajX / SQUARE_SIZE);
                    int tileY = (int) Math.floor(ajY / SQUARE_SIZE);

                    Position p = new Position(tileX, tileY);
                    Tile t = game.getTileAt(p);

                    game.removeFromBoard(t);
                    root.getChildren().remove(preview);
                    renderGameBoard(game);
                    setupInitialTileArea();
//                    System.out.println(game.toString());
                }
                catch (IllegalArgumentException ex) {
//                    System.out.println("No Tile Found");
                }
                catch(ArrayIndexOutOfBoundsException ex)
                {
//                    System.out.println("Outside Board");
                }
            }
        });

        //Is it possible to put all this in another function just for a cleaner look in the main method?
        // it is useful to keep general events within the main method - Tim
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
            // rotate any piece being hovered or dragged
            else if (e.getCode() == KeyCode.R) {
                for (Node current : gTiles.getChildren()) {
                    if (current instanceof DraggableTile) {
                        if (((DraggableTile) current).isHover || ((DraggableTile) current).isDrag) {
                            ((DraggableTile) current).rotate(90);
//                            System.out.println("Tile is rotated");
                        }
                    } else {
//                        System.out.println("Tile " + current + " is not rotated");
                    }
                }
            }
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SLASH) { // remove hints when "/" is released
                root.getChildren().remove(hint);
                isSLASHDown = false;
            }
        });


        //Ughhh still doesn't work
        //Screw it - low priority atm.
//        scene.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.R) {
//                gTiles.setOnMouseEntered(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent mouseEvent) {
//                        for (Node current : gTiles.getChildren()) {
//                            if (mouseEvent.getTarget() == current && current instanceof DraggableTile) {
//                                ((DraggableTile) current).rotate(90);
//                                System.out.println("Tile is rotated");
//                            } else {
//                                System.out.println("Tile " + current + " is not rotated");
//                            }
//                    }
//                }
//            });
//        }});


        for (Node current :
                gTiles.getChildren()) {
            if (current instanceof DraggableTile) {
                allTiles.add((DraggableTile) current);
            }
        }

    }
}
