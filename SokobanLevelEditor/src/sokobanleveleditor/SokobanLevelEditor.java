package sokobanleveleditor;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This program serves as a level editor for the Sokoban game. It is capable of
 * making and saving new levels.
 *
 * Note that we have designed this level editor such that the entire program is
 * defined inside this one class using inner classes for all event handlers and
 * the renderer.
 *
 * @author Paul Fodor
 */
public class SokobanLevelEditor extends Application {
    // INITIALIZATION CONSTANTS

    // THESE CONSTANTS ARE FOR CUSTOMIZATION OF THE GRID
    private final int INIT_GRID_DIM = 10;
    private final int MIN_GRID_DIM = 1;
    private final int MAX_GRID_ROWS = 10;
    private final int MAX_GRID_COLUMNS = 20;

    // TEXTUAL CONSTANTS
    private final String APP_TITLE = "Sokoban Level Editor";
    private final String OPEN_BUTTON_TEXT = "Open level binary file";
    private final String SAVE_AS_BUTTON_TEXT = "Save level binary file";
    private final String UPDATE_GRID_BUTTON_TEXT = "Update Grid";
    private final String COLUMNS_LABEL_TEXT = "Columns: ";
    private final String ROWS_LABEL_TEXT = "Rows: ";
    private final String SOK_FILE_EXTENSION = ".sok";
    private final String OPEN_FILE_ERROR_FEEDBACK_TEXT = "File not loaded: .sok files only";
    private final String SAVE_AS_ERROR_FEEDBACK_TEXT = "File not saved: must use .sok file extension";
    private final String FILE_LOADING_SUCCESS_TEXT = " loaded successfully";
    private final String FILE_READING_ERROR_TEXT = "Error reading from ";
    private final String FILE_WRITING_ERROR_TEXT = "Error writing to ";

    private final String SOKOBAN_DATA_DIR = "../Sokoban_draft/data/";

    // CONSTANTS FOR FORMATTING THE GRID
    private final Font GRID_FONT = new Font("monospaced", 36);

    // INSTANCE VARIABLES
    // HERE ARE THE UI COMPONENTS
    Stage primaryStage;
    private BorderPane gamePane;
    private GridPane westPane;
    private Button openButton;
    private Button saveAsButton;
    private Label columnsLabel;
    private TextField columnsTextField;
    private Label rowsLabel;
    private TextField rowsTextField;
    private Button updateGridButton;

    // GRID Renderer
    GridRenderer gridRenderer;
    private GraphicsContext gc;

    // AND HERE IS THE GRID WE'RE MAKING
    private int gridColumns;
    private int gridRows;
    private int grid[][];

    // THIS WILL LET THE USER SELECT THE FILES TO READ AND WRITE
    private FileChooser fileChooser;

    @Override
    public void start(Stage initPrimaryStage) {
        primaryStage = initPrimaryStage;
        // INIT THE EDITOR APP'S CONTAINER
        initStage();
        // INITIALIZES THE GRID DATA
        initData();
        // LAYOUT THE INITIAL CONTROLS
        initGUI();
        // HOOK UP THE EVENT HANDLERS
        initHandlers();
        // INITIALIZE
        initFileControls();
    }

    public void initStage() {
        primaryStage.setX(10);
        primaryStage.setY(10);
        primaryStage.setWidth(900);
        primaryStage.setHeight(550);
    }

    /**
     * Initializes the app data.
     */
    public void initData() {
        // START OUT OUR GRID WITH DEFAULT DIMENSIONS
        gridColumns = INIT_GRID_DIM;
        gridRows = INIT_GRID_DIM;

        // NOW MAKE THE INITIALLY EMPTY GRID
        grid = new int[gridColumns][gridRows];
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                grid[i][j] = 0;
            }
        }
    }

    /**
     * Constructs and lays out all UI controls.
     */
    public void initGUI() {
        // THE PARENT PANE IS A BORDERPANE
        gamePane = new BorderPane();

        // ALL THE GRID DIMENSIONS CONTROLS GO IN THE WEST
        westPane = new GridPane();

        // WE HAVE 2 TEXTFIELDS FOR UPDATING THE GRID DIMENSIONS, THESE
        // MODELS SPECIFY HOW THEY GET INITIALIZED AND THEIR VALUE BOUNDARIES
        columnsTextField = new TextField("10");
        rowsTextField = new TextField("10");

        // CONSTRUCT ALL THE WEST TOOLBAR COMPONENTS
        openButton = new Button(OPEN_BUTTON_TEXT);
        saveAsButton = new Button(SAVE_AS_BUTTON_TEXT);
        columnsLabel = new Label(COLUMNS_LABEL_TEXT);
        rowsLabel = new Label(ROWS_LABEL_TEXT);
        updateGridButton = new Button(UPDATE_GRID_BUTTON_TEXT);

        // NOW PUT ALL THE CONTROLS IN THE WEST TOOLBAR
        westPane.add(openButton, 0, 0);
        westPane.add(columnsLabel, 0, 2);
        westPane.add(columnsTextField, 1, 2);
        westPane.add(rowsLabel, 0, 3);
        westPane.add(rowsTextField, 1, 3);
        westPane.add(updateGridButton, 1, 4);
        westPane.add(saveAsButton, 0, 6);

        //westPane.add(updateGridButton, 0, 3);
        // THIS GUY RENDERS OUR GRID
        gridRenderer = new GridRenderer();

        // PUT EVERYTHING IN THE FRAME
        gamePane.setLeft(westPane);
        gamePane.setRight(gridRenderer);

        Scene scene = new Scene(gamePane);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * This class renders the grid for us. Note that we also listen for mouse
     * clicks on it.
     */
    class GridRenderer extends Canvas {

        // PIXEL DIMENSIONS OF EACH CELL
        int cellWidth;
        int cellHeight;

        // images
        Image wallImage = new Image("file:images/wall.png");
        Image boxImage = new Image("file:images/box.png");
        Image placeImage = new Image("file:images/place.png");
        Image sokobanImage = new Image("file:images/Sokoban.png");

        /**
         * Default constructor.
         */
        public GridRenderer() {
            this.setWidth(500);
            this.setHeight(500);
            repaint();
        }

        public void repaint() {
            gc = this.getGraphicsContext2D();
            gc.clearRect(0, 0, this.getWidth(), this.getHeight());

            // CALCULATE THE GRID CELL DIMENSIONS
            double w = this.getWidth() / gridColumns;
            double h = this.getHeight() / gridRows;

            gc = this.getGraphicsContext2D();

            // NOW RENDER EACH CELL
            int x = 0, y = 0;
            for (int i = 0; i < gridColumns; i++) {
                y = 0;
                for (int j = 0; j < gridRows; j++) {
                    // DRAW THE CELL
                    gc.setFill(Color.LIGHTBLUE);
                    gc.strokeRoundRect(x, y, w, h, 10, 10);

                    switch (grid[i][j]) {
                        case 0:
                            gc.strokeRoundRect(x, y, w, h, 10, 10);
                            break;
                        case 1:
                            gc.drawImage(wallImage, x, y, w, h);
                            break;
                        case 2:
                            gc.drawImage(boxImage, x, y, w, h);
                            break;
                        case 3:
                            gc.drawImage(placeImage, x, y, w, h);
                            break;
                        case 4:
                            gc.drawImage(sokobanImage, x, y, w, h);
                            break;
                    }

                    // THEN RENDER THE TEXT
                    String numToDraw = "" + grid[i][j];
                    double xInc = (w / 2) - (10 / 2);
                    double yInc = (h / 2) + (10 / 4);
                    x += xInc;
                    y += yInc;
                    gc.setFill(Color.RED);
                    gc.fillText(numToDraw, x, y);
                    x -= xInc;
                    y -= yInc;

                    // ON TO THE NEXT ROW
                    y += h;
                }
                // ON TO THE NEXT COLUMN
                x += w;
            }
        }

    }

    /**
     * This method initializes all the event handlers needed by this
     * application.
     */
    private void initHandlers() {
        // WE'LL UPDATE THE CELL-TILE COUNTS WHEN THE
        // USER CLICKS THE MOUSE ON THE RENDERING PANEL
        gridRenderer.setOnMouseClicked(mouseEvent -> {
            // FIGURE OUT THE CORRESPONDING COLUMN & ROW
            double w = gridRenderer.getWidth() / gridColumns;
            double col = mouseEvent.getX() / w;
            double h = gridRenderer.getHeight() / gridRows;
            double row = mouseEvent.getY() / h;
            // GET THE VALUE IN THAT CELL
            int value = grid[(int) col][(int) row];
            if (value < 5) {
                grid[(int) col][(int) row]++;
            } else {
                grid[(int) col][(int) row] = 0;
            }
            gridRenderer.repaint();
        });

        // Open Level
        openButton.setOnAction(event -> {
            File fileToOpen = fileChooser.showOpenDialog(primaryStage);
            String fileName = fileToOpen.getPath();
            try {
                if (fileToOpen != null) {
                    // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
                    // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
                    // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
                    byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    FileInputStream fis = new FileInputStream(fileToOpen);
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    // HERE IT IS, THE ONLY READY REQUEST WE NEED
                    bis.read(bytes);
                    bis.close();

                    // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
                    DataInputStream dis = new DataInputStream(bais);

                    // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
                    // ORDER AND FORMAT AS WE SAVED IT
                    // FIRST READ THE GRID DIMENSIONS
                    int initGridColumns = dis.readInt();
                    int initGridRows = dis.readInt();
                    int[][] newGrid = new int[initGridColumns][initGridRows];

                    // AND NOW ALL THE CELL VALUES
                    for (int i = 0; i < initGridColumns; i++) {
                        for (int j = 0; j < initGridRows; j++) {
                            newGrid[i][j] = dis.readInt();
                        }
                    }

                    grid = newGrid;
                    gridColumns = initGridColumns;
                    gridRows = initGridRows;
                    gridRenderer.repaint();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Save Level
        saveAsButton.setOnAction(event -> {
            File selectedFile = fileChooser.showSaveDialog(primaryStage);
            String fileName = selectedFile.getPath();
            try {
                if (selectedFile != null) {
                    // WE'LL WRITE EVERYTHING IN BINARY. NOTE THAT WE
                    // NEED TO MAKE SURE WE SAVE THE DATA IN THE SAME
                    // FORMAT AND ORDER WITH WHICH WE READ IT LATER
                    FileOutputStream fos = new FileOutputStream(fileName);
                    DataOutputStream dos = new DataOutputStream(fos);
                    // FIRST WRITE THE DIMENSIONS
                    dos.writeInt(gridColumns);
                    dos.writeInt(gridRows);
                    // AND NOW ALL THE CELL VALUES
                    for (int i = 0; i < gridColumns; i++) {
                        for (int j = 0; j < gridRows; j++) {
                            dos.writeInt(grid[i][j]);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Update the grid
        updateGridButton.setOnAction(event -> {
            gridColumns = Integer.parseInt(columnsTextField.getText());
            gridRows = Integer.parseInt(rowsTextField.getText());
            // NOW MAKE THE INITIALLY EMPTY GRID
            grid = new int[gridColumns][gridRows];
            for (int i = 0; i < gridColumns; i++) {
                for (int j = 0; j < gridRows; j++) {
                    grid[i][j] = 0;
                }
            }
            gridRenderer.repaint();
        });

    }

    /**
     * This method initializes the file chooser and the file filter for that
     * control so that the user may select files for saving and loading.
     */
    public void initFileControls() {
        // INIT THE FILE CHOOSER CONTROL
        fileChooser = new FileChooser();

        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Sokoban Files", "*.sok"));

        //File selectedFile = fileChooser.showOpenDialog(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
