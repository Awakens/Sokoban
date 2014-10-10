package sokoban.ui;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JEditorPane;
import sokoban.game.SokobanGameStateManager;
import application.Main.SokobanPropertyType;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingNode;
import properties_manager.PropertiesManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.U;
import static javafx.scene.input.KeyCode.UP;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JScrollPane;
       import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import sokoban.file.SokobanFileLoader;

public class SokobanUI extends Pane {
    
    /**
     * The SokobanUIState represents the four screen states that are possible
     * for the Sokoban game application. Depending on which state is in current
     * use, different controls will be visible.
     */
    public enum SokobanUIState {

        SPLASH_SCREEN_STATE, PLAY_GAME_STATE, VIEW_STATS_STATE, VIEW_HELP_STATE,
        HANG1_STATE, HANG2_STATE, HANG3_STATE, HANG4_STATE, HANG5_STATE, HANG6_STATE,
    }

    // mainStage
    private Stage primaryStage;

    // mainPane
    private BorderPane mainPane;
    private BorderPane hmPane;

    // SplashScreen
    private ImageView splashScreenImageView;
    private Pane splashScreenPane;
    private Label splashScreenImageLabel;
    private HBox levelSelectionPane;
    private ArrayList<Button> levelButtons;

    // NorthToolBar
    private HBox northToolbar;
    private Button gameButton;
    private Button statsButton;
    private Button helpButton;
    private Button exitButton;
    private Button backButton = new Button("Back");   //check
    private Button undoButton = new Button("Undo");
    private HBox TheBox = new HBox();
    private Label winLabel = new Label("You won!");
    private Label loseLabel = new Label("You Lose!");
    private Button OK = new Button("OK");
     ArrayList<String> levels;
    private GraphicsContext gc;
    int sokoI;
    int sokoJ;
    int SokoI; int SokoJ;
  int placesNeeded; int numBoxes; int numBoxesBlocked;
  SwingNode statsSwingNode = new SwingNode();
  ArrayList<int[][]> moveStack;
  GridRenderer gridRenderer;
  MediaPlayer mediaPlayer;
  MediaPlayer mediaPlayerSplash;
    private File file = new File("move.wav");         //creating file for sound
    private final String MEDIA_URL = file.toURI().toString();     //turning it into a string url
    private File filer = new File("Counting+Stars+Background+Audio.wav");
    private final String splashSounds = filer.toURI().toString();
    private File fileWin = new File("win.wav");
    private final String wonMe = fileWin.toURI().toString();
    private File fileLose = new File("lose.wav");
    private final String loseMe = fileLose.toURI().toString();
    private File fileBlocked = new File("blocked.wav");
    private final String blockedSounds = fileBlocked.toURI().toString();
    private Timeline timeline;
    private Timer time;
    private double level1wins; private double level1loses; private double level1percent; private String level1FastestWin = "-";
    private double level2wins; private double level2loses; private double level2percent; private String level2FastestWin = "-";
    private double level3wins; private double level3loses; private double level3percent; private String level3FastestWin = "-";
    private double level4wins; private double level4loses; private double level4percent; private String level4FastestWin = "-";
    private double level5wins; private double level5loses; private double level5percent; private String level5FastestWin = "-";
    private double level6wins; private double level6loses; private double level6percent; private String level6FastestWin = "-";
    private double level7wins; private double level7loses; private double level7percent; private String level7FastestWin = "-";
    
    
    
            // images
        Image wallImage = new Image("file:images/wall.png");
        Image boxImage = new Image("file:images/box.png");
        Image placeImage = new Image("file:images/place.png");
        Image sokobanImage = new Image("file:images/Sokoban.png");

    // AND HERE IS THE GRID WE'RE MAKING
    private int gridColumns;
    private int gridRows;
    private int grid[][];

    // GamePane
    private Label SokobanLabel;
    private Button newGameButton;
    private HBox letterButtonsPane;
    private HashMap<Character, Button> letterButtons;
    private BorderPane gamePanel = new BorderPane();


    //StatsPane
    private ScrollPane statsScrollPane;
    private JEditorPane statsPane;

    //HelpPane
    private BorderPane helpPanel;
    private JScrollPane helpScrollPane;
    private JEditorPane helpPane;
    private Button homeButton;
    private Pane workspace;

    // Padding
    private Insets marginlessInsets;

    // Image path
    private String ImgPath = "file:images/";

    // mainPane weight && height
    private int paneWidth;
    private int paneHeigth;

    // THIS CLASS WILL HANDLE ALL ACTION EVENTS FOR THIS PROGRAM
    private SokobanEventHandler eventHandler;
    private SokobanErrorHandler errorHandler;
    private SokobanDocumentManager docManager;

    SokobanGameStateManager gsm;

   public void Media(String x){return; }
   
    public SokobanUI() {
        gsm = new SokobanGameStateManager(this);
        eventHandler = new SokobanEventHandler(this);
        errorHandler = new SokobanErrorHandler(primaryStage);
        docManager = new SokobanDocumentManager(this);
        initMainPane();
        initNorthToolbar();//check
         Media media = new Media(splashSounds);
mediaPlayerSplash = new MediaPlayer(media);
        initSplashScreen();
        
      //  Media media = new Media("sample.mp4"); 
 //MediaPlayer mediaPlayer = new MediaPlayer(media); 
 //MediaView mediaView = new MediaView(mediaPlayer); 
    }


    public void SetStage(Stage stage) {
        primaryStage = stage;
    }

    public BorderPane GetMainPane() {
        return this.mainPane;
    }

    public SokobanGameStateManager getGSM() {
        return gsm;
    }

    public SokobanDocumentManager getDocManager() {
        return docManager;
    }

    public SokobanErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public JEditorPane getHelpPane() {
        return helpPane; 
    }

    public void initMainPane() {
        marginlessInsets = new Insets(5, 5, 5, 5);
        mainPane = new BorderPane();

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        paneWidth = Integer.parseInt(props
                .getProperty(SokobanPropertyType.WINDOW_WIDTH));
        paneHeigth = Integer.parseInt(props
                .getProperty(SokobanPropertyType.WINDOW_HEIGHT));
        mainPane.resize(paneWidth, paneHeigth);
        mainPane.setPadding(marginlessInsets);
    } 
   
    public void initSplashScreen() {
        try {
            splashSound();
        } catch (Exception ex) {
            Logger.getLogger(SokobanUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        // INIT THE SPLASH SCREEN CONTROLS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String splashScreenImagePath = props
                .getProperty(SokobanPropertyType.SPLASH_SCREEN_IMAGE_NAME);
        props.addProperty(SokobanPropertyType.INSETS, "5");
        String str = props.getProperty(SokobanPropertyType.INSETS);

        splashScreenPane = new FlowPane();
         splashScreenPane.getChildren().add(northToolbar);  //check
        Image splashScreenImage = loadImage(splashScreenImagePath);
        splashScreenImageView = new ImageView(splashScreenImage);
        splashScreenImageView.setFitHeight(400);        //check
        
        splashScreenImageLabel = new Label();
        splashScreenImageLabel.setGraphic(splashScreenImageView);
        // move the label position to fix the pane
        //splashScreenImageLabel.setLayoutX(-45);
       
        splashScreenPane.getChildren().add(splashScreenImageLabel);

        // GET THE LIST OF LEVEL OPTIONS
        levels = props
                .getPropertyOptionsList(SokobanPropertyType.LEVEL_OPTIONS);
        ArrayList<String> levelImages = props
                .getPropertyOptionsList(SokobanPropertyType.LEVEL_IMAGE_NAMES);
        //ArrayList<String> levelFiles = props
        //        .getPropertyOptionsList(SokobanPropertyType.LEVEL_FILES);

        levelSelectionPane = new HBox();
        levelSelectionPane.setSpacing(10.0);
        levelSelectionPane.setAlignment(Pos.CENTER);
        // add key listener
        levelButtons = new ArrayList<Button>();
        for (int i = 0; i < levels.size(); i++) {

            // GET THE LIST OF LEVEL OPTIONS
            String leveler = levels.get(i);
            String levelImageName = levelImages.get(i);
            Image levelImage = loadImage(levelImageName);
            ImageView levelImageView = new ImageView(levelImage);
              levelImageView.setFitHeight(200);  //check
              levelImageView.setFitWidth(50);
            // AND BUILD THE BUTTON
            Button levelButton = new Button();
            levelButton.setText(leveler);
            levelButton.setGraphic(levelImageView);
            // CONNECT THE BUTTON TO THE EVENT HANDLER
            levelButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    // TODO
                   
                    changeWorkspace(SokobanUIState.PLAY_GAME_STATE);   //check
                  respondToSelectLevelRequest(levelButton.getText());  //check
                   
                }
            });
             levelButton.setOnKeyPressed(e -> {
                  switch (e.getCode()) {
                      case ENTER:     
                    changeWorkspace(SokobanUIState.PLAY_GAME_STATE);   //check
                  respondToSelectLevelRequest(levelButton.getText());  //check
                  }
             });
            // TODO
            levelSelectionPane.getChildren().add(levelButton);   //check
           
        }

        mainPane.setCenter(splashScreenPane);
        splashScreenPane.getChildren().add(levelSelectionPane);  
       //mainPane.setBottom(levelSelectionPane);  
        
    }
    
    public void moveSound() throws Exception 
    {  Media media = new Media(MEDIA_URL);
mediaPlayer = new MediaPlayer(media);
mediaPlayer.play();
    MediaView mediaView = new MediaView(mediaPlayer);
    mainPane.setTop(mediaView);}
    public void splashSound() throws Exception 
    {  Media media = new Media(splashSounds);
    mediaPlayerSplash.stop();
mediaPlayerSplash = new MediaPlayer(media);
mediaPlayerSplash.play();   mediaPlayerSplash.setCycleCount(500); mediaPlayerSplash.setAutoPlay(true); 
    MediaView mediaView = new MediaView(mediaPlayer);
    mainPane.setTop(mediaView);}
    public void winSound() throws Exception 
    {  mediaPlayerSplash.stop();
        Media media = new Media(wonMe);
mediaPlayer = new MediaPlayer(media);
mediaPlayer.play();
    MediaView mediaView = new MediaView(mediaPlayer);
    mainPane.setTop(mediaView);
    }
    public void loseSound() throws Exception 
    { mediaPlayerSplash.stop();
        Media media = new Media(loseMe);
mediaPlayer = new MediaPlayer(media);
mediaPlayer.play();
    MediaView mediaView = new MediaView(mediaPlayer);
    mainPane.setTop(mediaView);}
       public void blockedSound() throws Exception 
    {  Media media = new Media(blockedSounds);
mediaPlayer = new MediaPlayer(media);
mediaPlayer.play();
    MediaView mediaView = new MediaView(mediaPlayer);
    mainPane.setTop(mediaView);}
  
public void respondToSelectLevelRequest(String level) {    //check
    /*   fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Sokoban Files", "*.sok"));
     File fileToOpen = fileChooser.showOpenDialog(primaryStage);  
            String fileName = fileToOpen.getPath();
    */
     File fileToOpen = new File("data/" + level + ".sok");
            String fileName = fileToOpen.toURI().toString(); System.out.println(fileName);
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
                    moveStack = new ArrayList<int[][]>(0); //resets moveStack since new game
                    // AND NOW ALL THE CELL VALUES
                    for (int i = 0; i < initGridColumns; i++) {
                        for (int j = 0; j < initGridRows; j++) {
                            newGrid[i][j] = dis.readInt();
                        }
                    }

                    grid = newGrid;
                    gridColumns = initGridColumns;
                    gridRows = initGridRows;
                    
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
          gridRenderer = null;
          gridRenderer = new GridRenderer();  
         gridRenderer.setWidth(500);
         gridRenderer.setHeight(500);
         gamePanel.getChildren().clear();
         gameButton.setVisible(true);      
         gamePanel.setTop(northToolbar);
         gamePanel.setLeft(TheBox);
         gamePanel.setCenter(gridRenderer);
         TheBox.getChildren().clear();
         time = new Timer();
         TheBox.getChildren().addAll(backButton, undoButton, time.getTimerLabel(), winLabel, loseLabel, OK);
         winLabel.setVisible(false); loseLabel.setVisible(false); OK.setVisible(false); 
         OK.setMinSize(100,50);
         OK.setOnAction(Enter -> {
           OK.setFocusTraversable(false);
           OK.setVisible(false);
           initSplashScreen();
           save();
            });
         OK.setOnKeyPressed(emp -> {
             if(emp.getCode().equals(ENTER))
             { OK.setFocusTraversable(false);
           OK.setVisible(false);
           initSplashScreen();
           save();              //save data 
             }});
         backButton.setFocusTraversable(false);    
         backButton.setMinSize(50,50);
         time.start();        //start timer when game level selected
         
         backButton.setOnAction(e ->   
          {  
  
        initSplashScreen();
         });
          undoButton.setFocusTraversable(false);
          undoButton.setMinSize(50,50);
         undoButton.setOnAction(e -> {
                // TODO Auto-generated method stub
              //  eventHandler.UNDO(something)   //check
                System.out.println("moveStack is size " + moveStack.size());
                if(moveStack.size() > 0)
                {grid = moveStack.get(moveStack.size()-1);    //updates grid from top of stack
                moveStack.remove(moveStack.size()-1);
                gridRenderer.repaint();}
        });
         

    
         gridRenderer.repaint();
         
          /* gridRenderer.setOnMouseDragged(mouseEvent ->  {
             double w = gridRenderer.getWidth() / gridColumns;
            double col = mouseEvent.getX() / w;
            double h = gridRenderer.getHeight() / gridRows;
            double row = mouseEvent.getY() / h;
            mouseEvent.getX();
            gridRenderer.setOnMouseDragged(e -> {});
                gridRenderer.getGraphicsContext2D();}
         */
         //come
          gridRenderer.setOnMouseClicked(mouseEvent -> {
            // FIGURE OUT THE CORRESPONDING COLUMN & ROW
            double w = gridRenderer.getWidth() / gridColumns;
            //double col = mouseEvent.getX() / w;
            double h = gridRenderer.getHeight() / gridRows;
            double row = mouseEvent.getY() / h;
            
                    
                sokoI = gridRenderer.getSokoI();    //check sokobon placement
                sokoJ = gridRenderer.getSokoJ();
                
          });    
             gridRenderer.setFocusTraversable(true);  //yay need this check
             gridRenderer.focusedProperty();
             gridRenderer.isFocused();
             gridRenderer.minWidth(mainPane.getWidth());
      
         gridRenderer.setOnKeyPressed(e -> {
            boolean blocked = false;  //track if sokoban was blocked
            if(time.getCount() == 0)       //check time renderer
            { if(level.equals("Level 1"))
            {level1loses++; }
            if(level.equals("Level 2"))
            {level2loses++; }
            if(level.equals("Level 3"))
            {level3loses++; }
            if(level.equals("Level 4"))
            {level4loses++; }
            if(level.equals("Level 5"))
            {level5loses++; }
            if(level.equals("Level 6"))
            {level6loses++; }
            if(level.equals("Level 7"))
            {level7loses++; }
                gridRenderer.setFocusTraversable(false);   //make gridRenderer unkeyable
                gridRenderer.setDisable(true);
                if(winLabel.isVisible() == false) loseLabel.setVisible(true);
              OK.setVisible(true); OK.setFocusTraversable(true);
             OK.focusedProperty(); OK.isFocused(); 
             try{ loseSound();}catch(Exception echo){System.out.println("loseSound error");}
             return;}
            //We make a new array that copies over the grid[][] at this moment 
            // saving grid just copies address
              int[][] newArray = new int[grid.length][grid[0].length];
              for(int o = 0; o < grid.length; o++)   //grid.length = columns   grid[0].length = rows
              { for(int p = 0; p < grid[0].length; p++)
              { newArray[o][p] = grid[o][p];
              }}       
              switch (e.getCode()) {
                  case U:   System.out.println("moveStack is size " + moveStack.size());    //add shortcut for undo when U is pressed
                if(moveStack.size() > 0)
                {grid = moveStack.get(moveStack.size()-1);    //updates grid from top of stack
                moveStack.remove(moveStack.size()-1);
                gridRenderer.repaint();}  return;    
                  default: ;
              }
                sokoI = gridRenderer.getSokoI();    //check sokobon placement
                sokoJ = gridRenderer.getSokoJ();
                switch (e.getCode()) { 
                
                case DOWN: moveStack.add(newArray);  //save original level grid 
                    if(grid[sokoI][sokoJ+1] == 0)     //check if panel below is empty
                {   grid[sokoI][sokoJ+1] = 4;   //move sokoban by 1 row if panel below is empty
                   if(grid[sokoI][sokoJ] == 6)
                   {    grid[sokoI][sokoJ] = 3;
                   grid[sokoI][sokoJ+1] = 4;  }     //turn new soko spot into regular soko
                   else
                    grid[sokoI][sokoJ] = 0;           //reset where sokoban moved from                   
                    SokoJ+=1; gridRenderer.repaint();               //incre soko placement
                    break;}
        if(grid[sokoI][sokoJ+1] == 1) {blocked = true; break;}  //exit if wall
      
        if(grid[sokoI][sokoJ+1] == 2)    //if box in panel below
      {if(grid[sokoI][sokoJ+2] == 1 || grid[sokoI][sokoJ+2] == 5 || grid[sokoI][sokoJ+2] == 2) {blocked = true; break;}   //continue if there box in panel below and the panel below box is not a wall or box or box with place
        if(grid[sokoI][sokoJ+2] == 3)    //if is a place
      {  grid[sokoI][sokoJ+2] = 5;      //place box on top of place
           placesNeeded--;}
        else  grid[sokoI][sokoJ+2] = 2;       //move box  
        grid[sokoI][sokoJ+1] = 4;       //then move sokoban
        if(grid[sokoI][sokoJ] == 6)
        {   grid[sokoI][sokoJ] = 3;
           grid[sokoI][sokoJ+1] = 4;  }     //turn new soko spot into regular soko
        else
      grid[sokoI][sokoJ] = 0;           //empty out the space where sokoban was
        SokoJ+=1;  gridRenderer.repaint(); 
             break;  } //incre sokoban place
        
         if(grid[sokoI][sokoJ+1] == 5)   //if box with place
      {if(grid[sokoI][sokoJ+2] == 1 || grid[sokoI][sokoJ+2] == 5 || grid[sokoI][sokoJ+2] == 2) {blocked = true; break;}   //continue if there box in panel below and the panel below box is not a wall or box or box with place
        if(grid[sokoI][sokoJ+2] == 3)    //if is a place
      {  grid[sokoI][sokoJ+2] = 5;      //place box on top of place
           placesNeeded--;}
        else  grid[sokoI][sokoJ+2] = 2;       //move box  
        grid[sokoI][sokoJ+1] = 6;       //then move sokoban on top of place
        if(grid[sokoI][sokoJ] == 6)
           grid[sokoI][sokoJ] = 3;           
        else
      grid[sokoI][sokoJ] = 0;           //empty out the space where sokoban was
        SokoJ+=1;  gridRenderer.repaint(); 
             break;  } //incre sokoban place
         
 if(grid[sokoI][sokoJ+1] == 3){     //ifplace
      grid[sokoI][sokoJ+1] = 6;   //move by row
      if(grid[sokoI][sokoJ] == 6)
          grid[sokoI][sokoJ] = 3;
      else
          grid[sokoI][sokoJ] = 0;
        SokoJ+=1; gridRenderer.repaint();  break; }   
      
 case UP: moveStack.add(newArray);  //save original level grid 
     if(grid[sokoI][sokoJ-1] == 0)     //check if panel above is empty
    {  grid[sokoI][sokoJ-1] = 4;   //move sokoban up by 1 row if panel above is empty
    if(grid[sokoI][sokoJ] == 6)
                       grid[sokoI][sokoJ] = 3;
                   else
       grid[sokoI][sokoJ] = 0;           //reset where sokoban moved from
      SokoJ-=1;  gridRenderer.repaint(); 
            break;}
      if(grid[sokoI][sokoJ-1] == 1) {blocked = true; break;}  //exit if wall
      
      if(grid[sokoI][sokoJ-1] == 2)    //if box in panel above
      {if(grid[sokoI][sokoJ-2] == 1 || grid[sokoI][sokoJ-2] == 5 || grid[sokoI][sokoJ-2] == 2) {blocked = true; break;}    //break if there box in panel below and the panel above box is not a wall or null
       if(grid[sokoI][sokoJ-2] == 3)    //if is a place
      {  grid[sokoI][sokoJ-2] = 5;      //place box on top of place
           placesNeeded--;}
        else  grid[sokoI][sokoJ-2] = (2);       //move box
        grid[sokoI][sokoJ-1] = 4;       //then move sokoban
        if(grid[sokoI][sokoJ] == 6)
                       grid[sokoI][sokoJ] = 3;
                   else
      grid[sokoI][sokoJ] = 0; 
        SokoJ-=1;  gridRenderer.repaint();  break;  } //empty out the space where sokoban was
      
               if(grid[sokoI][sokoJ-1] == 5)   //if box with place
      {if(grid[sokoI][sokoJ-2] == 1 || grid[sokoI][sokoJ-2] == 5 || grid[sokoI][sokoJ-2] == 2) {blocked = true; break;}   //continue if there box in panel below and the panel below box is not a wall or box or box with place
        if(grid[sokoI][sokoJ-2] == 3)    //if is a place
      {  grid[sokoI][sokoJ-2] = 5;      //place box on top of place
           placesNeeded--;}
        else  grid[sokoI][sokoJ-2] = 2;       //move box  
        grid[sokoI][sokoJ-1] = 6;       //then move sokoban on top of place
        if(grid[sokoI][sokoJ] == 6)
           grid[sokoI][sokoJ] = 3;           
        else
      grid[sokoI][sokoJ] = 0;           //empty out the space where sokoban was
        SokoJ-=1;  gridRenderer.repaint(); 
             break;  } //incre sokoban place
               
 if(grid[sokoI][sokoJ-1] == 3){     //if place
      grid[sokoI][sokoJ-1] = 6;   //move by row
      if(grid[sokoI][sokoJ] == 6)
                       grid[sokoI][sokoJ] = 3;
                   else
      grid[sokoI][sokoJ] = 0;
        SokoJ-=1;  gridRenderer.repaint(); break;
           }
 
 case LEFT:  moveStack.add(newArray);  //save original level grid 
     if(grid[sokoI-1][sokoJ] == 0)     //check if left panel  is empty
    {  grid[sokoI-1][sokoJ] = 4;   //move sokoban left by 1  if left panel is empty
    if(grid[sokoI][sokoJ] == 6)
                       grid[sokoI][sokoJ] = 3;
                   else
       grid[sokoI][sokoJ] = 0;           //reset where sokoban moved from
       SokoI-=1; gridRenderer.repaint();  
       break;}
       if(grid[sokoI-1][sokoJ] == 1) {blocked = true; break;}  //exit if wall  
      if(grid[sokoI-1][sokoJ] == 2)    //if box in left panel 
      {if(grid[sokoI-2][sokoJ] == 1 || grid[sokoI-2][sokoJ] == 5 || grid[sokoI-2][sokoJ] == 2) {blocked = true; break;}    //continue if there box in left panel and the left panel box is not a wall
      { if(grid[sokoI-2][sokoJ] == 3)    //if is a place
      {  grid[sokoI-2][sokoJ] = 5;      //place box on top of place
           placesNeeded--;}
        else  grid[sokoI-2][sokoJ] = (grid[sokoI-1][sokoJ]);       //move box
        grid[sokoI-1][sokoJ] = 4;       //then move sokoban
        if(grid[sokoI][sokoJ] == 6)
                       grid[sokoI][sokoJ] = 3;
                   else
      grid[sokoI][sokoJ] = 0; SokoI-=1; gridRenderer.repaint();  break;  }} //empty out the space where sokoban was
      
               if(grid[sokoI-1][sokoJ] == 5)   //if box with place
      {if(grid[sokoI-2][sokoJ] == 1 || grid[sokoI-2][sokoJ] == 5 || grid[sokoI-2][sokoJ] == 2) {blocked = true; break;}   //continue if there box in panel below and the panel below box is not a wall or box or box with place
        if(grid[sokoI-2][sokoJ] == 3)    //if is a place
      {  grid[sokoI-2][sokoJ] = 5;      //place box on top of place
           placesNeeded--;}
        else  grid[sokoI-2][sokoJ] = 2;       //move box  
        grid[sokoI-1][sokoJ] = 6;       //then move sokoban on top of place
        if(grid[sokoI][sokoJ] == 6)
           grid[sokoI][sokoJ] = 3;           
        else
      grid[sokoI][sokoJ] = 0;           //empty out the space where sokoban was
        SokoI-=1;  gridRenderer.repaint(); 
             break;  } //incre sokoban place
               
 if(grid[sokoI-1][sokoJ] == 3){     //if place
      grid[sokoI-1][sokoJ] = 6;   //move left
      if(grid[sokoI][sokoJ] == 6)
                       grid[sokoI][sokoJ] = 3;
                   else
      grid[sokoI][sokoJ] = 0;
        SokoI-=1; gridRenderer.repaint(); break;
           }
  /*  if(grid[sokoI-1][sokoJ] == 5)    //if box in left panel 
  {if(grid[sokoI-2][sokoJ] == 1 || grid[sokoI-2][sokoJ] == 5 || grid[sokoI-2][sokoJ] == 2) {blocked = true; break;}   //continue if there box in left panel and the left panel box is not a wall
       if(grid[sokoI-2][sokoJ] == 3)    //if is a place
      {  grid[sokoI-2][sokoJ] = 5;      //place box on top of place
           placesNeeded--;}
        else  grid[sokoI-2][sokoJ] = (grid[sokoI-1][sokoJ]);       //move box
        grid[sokoI-1][sokoJ] = 6;       //then move sokoban
        if(grid[sokoI][sokoJ] == 6)
                       grid[sokoI][sokoJ] = 3;
                   else
      grid[sokoI][sokoJ] = 0; SokoI-=1; gridRenderer.repaint();  break;  } //empty out the space where sokoban was
 */
 case RIGHT: moveStack.add(newArray);  //save original level grid 
     if(grid[sokoI+1][sokoJ] == 0)     //check if right panel  is empty
    {  grid[sokoI+1][sokoJ] = 4;   //move sokoban right by 1  if rigbht panel is empty
    if(grid[sokoI][sokoJ] == 6)
                       grid[sokoI][sokoJ] = 3;
                   else
       grid[sokoI][sokoJ] = 0;           //reset where sokoban moved from
       SokoI+=1; gridRenderer.repaint(); 
       break;}
        if(grid[sokoI+1][sokoJ] == 1) {blocked = true; break;}  //exit if wall  
      if(grid[sokoI+1][sokoJ] == 2)    //if box in right panel 
{if(grid[sokoI+2][sokoJ] == 1 || grid[sokoI+2][sokoJ] == 5 || grid[sokoI+2][sokoJ] == 2) {blocked = true; break;}    //continue if there box in right panel and the right panel box is not a wall
       if(grid[sokoI+2][sokoJ] == 3)    //if is a place
      {  grid[sokoI+2][sokoJ] = 5;      //place box on top of place
           placesNeeded--;}
        else  grid[sokoI+2][sokoJ] = (2);       //move box
        grid[sokoI+1][sokoJ] = 4;       //then move sokoban
         SokoI+=1; 
         if(grid[sokoI][sokoJ] == 6)
                       grid[sokoI][sokoJ] = 3;
                   else
             grid[sokoI][sokoJ] = 0; gridRenderer.repaint();  break;  } //empty out the space where sokoban was
 if(grid[sokoI+1][sokoJ] == 3){     //if place
      grid[sokoI+1][sokoJ] = 6;   //move right   
      if(grid[sokoI][sokoJ] == 6)
                       grid[sokoI][sokoJ] = 3;
                   else
      grid[sokoI][sokoJ] = 0;
          SokoI+=1; gridRenderer.repaint();  break;
           }
 /* if(grid[sokoI+1][sokoJ] == 5)    //if box with place in right panel 
  {if(grid[sokoI-2][sokoJ] == 1 || grid[sokoI-2][sokoJ] == 5 || grid[sokoI-2][sokoJ] == 2) {blocked = true; break;}    //continue if there box in right panel and the right panel box is not a wall
       if(grid[sokoI+2][sokoJ] == 3)    //if is a place
      {  grid[sokoI+2][sokoJ] = 5;      //place box on top of place
           placesNeeded--;}
        else  grid[sokoI+2][sokoJ] = (grid[sokoI+1][sokoJ]);       //move box
        grid[sokoI+1][sokoJ] = 6;       //then move sokoban ontop of place
         SokoI+=1; 
         if(grid[sokoI][sokoJ] == 6)
                       grid[sokoI][sokoJ] = 3;
                   else
         grid[sokoI][sokoJ] = 0; gridRenderer.repaint();  break;  } //empty out the space where sokoban was
 */
                 if(grid[sokoI+1][sokoJ] == 5)   //if box with place
      {if(grid[sokoI+2][sokoJ] == 1 || grid[sokoI+2][sokoJ] == 5 || grid[sokoI+2][sokoJ] == 2) {blocked = true; break;}   //continue if there box in panel below and the panel below box is not a wall or box or box with place
        if(grid[sokoI+2][sokoJ] == 3)    //if is a place
      {  grid[sokoI+2][sokoJ] = 5;      //place box on top of place
           placesNeeded--;}
        else  grid[sokoI+2][sokoJ] = 2;       //move box  
        grid[sokoI+1][sokoJ] = 6;       //then move sokoban on top of place
        if(grid[sokoI][sokoJ] == 6)
           grid[sokoI][sokoJ] = 3;           
        else
      grid[sokoI][sokoJ] = 0;           //empty out the space where sokoban was
        SokoI+=1;  gridRenderer.repaint(); 
             break;  } //incre sokoban place
          
 } 
            if(blocked == true)
            { try{blockedSound(); blocked = false;}
            catch(Exception eoi){System.out.println("blocked no exist");}}
            
            else if(placesNeeded == 0)  //if won
            {            { if(level.equals("Level 1"))
            {level1wins++;  level1FastestWin = time.getCount() + ""; }
            if(level.equals("Level 2"))
            {level2wins++;  level2FastestWin = time.getCount() + ""; }
            if(level.equals("Level 3"))
            {level3wins++;  level3FastestWin = time.getCount() + ""; }
            if(level.equals("Level 4"))
            {level4wins++;  level4FastestWin = time.getCount() + ""; }
            if(level.equals("Level 5"))
            {level5wins++;  level5FastestWin = time.getCount() + ""; }
            if(level.equals("Level 6"))
            {level6wins++;  level6FastestWin = time.getCount() + "";  }
            if(level.equals("Level 7"))
            {level7wins++;  level7FastestWin = time.getCount() + ""; }
                winLabel.setVisible(true); OK.setVisible(true);
             gridRenderer.setFocusTraversable(false);
             gridRenderer.setDisable(true);
           OK.setFocusTraversable(true);
           OK.focusTraversableProperty();
           OK.focusedProperty();}
             try{ winSound();}catch(Exception echo){System.out.println("winSound error");}}
            else if(numBoxesBlocked == numBoxes && placesNeeded != 0)
            {        if(level.equals("Level 1"))
            {level1loses++; }
            if(level.equals("Level 2"))
            {level2loses++; }
            if(level.equals("Level 3"))
            {level3loses++; }
            if(level.equals("Level 4"))
            {level4loses++; }
            if(level.equals("Level 5"))
            {level5loses++; }
            if(level.equals("Level 6"))
            {level6loses++;}
            if(level.equals("Level 7"))
            {level7loses++; }
                loseLabel.setVisible(true); OK.setVisible(true);
             gridRenderer.setFocusTraversable(false);
             gridRenderer.setDisable(true);
           OK.setFocusTraversable(true);
           OK.focusTraversableProperty();
           OK.focusedProperty();
            try{ loseSound();}catch(Exception echo){System.out.println("loseSound error");}}
            else
            {      try{ moveSound();}
            catch(Exception ex){}
         }
           
           
          
        });
}
public void save(){  
      if(level1loses == 0) level1percent = 100; else level1percent = level1wins / (level1wins + level1loses) * 100; 
       if(level2loses == 0) level2percent = 100; else level2percent = level2wins / (level2wins + level2loses) * 100; 
        if(level3loses == 0) level3percent = 100; else level3percent = level3wins / (level3wins + level3loses) * 100; 
         if(level4loses == 0) level4percent = 100; else level4percent = level4wins / (level4wins + level4loses) * 100; 
          if(level5loses == 0) level5percent = 100; else level5percent = level5wins / (level5wins + level5loses) * 100; 
           if(level6loses == 0) level6percent = 100; else level6percent = level6wins / (level6wins + level6loses) * 100; 
            if(level7loses == 0) level7percent = 100; else level7percent = level7wins / (level7wins + level7loses) * 100; 
    String winText = "Level 1 Number of wins: " + level1wins + "   Number of loses: " + level1loses + "   Winning percentage: " + level1percent + "%   Fastest win: " + level1FastestWin + " seconds" + "\n" +
                     "Level 2 Number of wins: " + level2wins + "   Number of loses: " + level2loses + "   Winning percentage: " + level2percent + "%   Fastest win: " + level2FastestWin + " seconds" + "\n" +
                       "Level 3 Number of wins: " + level3wins + "   Number of loses: " + level3loses + "   Winning percentage: " + level3percent + "%   Fastest win: " + level3FastestWin + " seconds" + "\n" +
                       "Level 4 Number of wins: " + level4wins + "   Number of loses: " + level4loses + "   Winning percentage: " + level4percent + "%   Fastest win: " + level4FastestWin + " seconds" + "\n" +
                       "Level 5 Number of wins: " + level5wins + "   Number of loses: " + level5loses + "   Winning percentage: " + level5percent + "%   Fastest win: " + level5FastestWin + " seconds" + "\n" +
                       "Level 6 Number of wins: " + level6wins + "   Number of loses: " + level6loses + "   Winning percentage: " + level6percent + "%   Fastest win: " + level6FastestWin + " seconds" + "\n" +
                       "Level 7 Number of wins: " + level7wins + "   Number of loses: " + level7loses + "   Winning percentage: " + level7percent + "%   Fastest win: " + level7FastestWin + " seconds" + "\n";
    Text winTexter = TextBuilder.create()
                .text(winText)
                .build();                
      try { File fileIt = new File("stats.txt");
            FileWriter fileWriter = null;
             
            fileWriter = new FileWriter(fileIt);
            fileWriter.write(winText);
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println("unable to write");
        }  
      }
    
    /**
     *
     * @throws Exception
     */
//come platform.runlater {update stuff}
    public void loadPage(JEditorPane jep, SokobanPropertyType fileProperty) {
		// GET THE FILE NAME
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		String fileName = props.getProperty(fileProperty);
		try {
			// LOAD THE HTML INTO THE EDITOR PANE
			String fileHTML = SokobanFileLoader.loadTextFile(fileName);
			jep.setText(fileHTML);
		} catch (IOException ioe) {
			errorHandler.processError(SokobanPropertyType.INVALID_URL_ERROR_TEXT);
                        System.out.println("error loading");
		}
	} 
           

private void initStatsPane()
    {
        // WE'LL DISPLAY ALL STATS IN A JEditorPane
        statsPane = new JEditorPane();
        statsPane.setEditable(false);
       
         try {
             String content = new String(Files.readAllBytes(Paths.get("stats.txt"))); 
            statsPane.setText(content); 
         }
             catch (IOException ex) {
             Logger.getLogger(SokobanUI.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("error reading");
         }
         
           
        statsSwingNode.setContent(statsPane); 
       mainPane.setCenter(statsSwingNode);  //done
       mainPane.setTop(northToolbar);
        statsScrollPane = new ScrollPane();
        statsScrollPane.setPrefWidth(mainPane.getWidth());   //check
        statsScrollPane.setPrefHeight(mainPane.getHeight());  
        statsScrollPane.setFitToWidth(true);
         statsScrollPane.setFitToHeight(true);
         //read from file
         
        
        // NOW ADD IT TO THE WORKSPACE, MEANING WE CAN SWITCH TO IT
        //workspace.add(statsScrollPane, HangManUIState.VIEW_STATS_STATE.toString());

    }


class GridRenderer extends Canvas {

        // PIXEL DIMENSIONS OF EACH CELL
        int cellWidth;
        int cellHeight;


        /**
         * Default constructor.
         */
        public GridRenderer() {
            this.setWidth(500);
            this.setHeight(500);
            repaint();
        }

        public void repaint() {
            placesNeeded = 0;
            numBoxes = 0;
            numBoxesBlocked = 0;
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
                            numBoxes++;
                            //if box is cornered
                            if((grid[i+1][j] == 1 && grid[i][j+1] == 1) || (grid[i+1][j] == 1 && grid[i][j-1] == 1)
                                    || (grid[i-1][j] == 1 && grid[i][j+1] == 1) || (grid[i-1][j] == 1 && grid[i][j-1] == 1)
                                    || (grid[i+1][j] == 1 && grid[i][j+1] == 2) || (grid[i+1][j] == 1 && grid[i][j-1] == 2)
                                    || (grid[i-1][j] == 1 && grid[i][j+1] == 2) || (grid[i-1][j] == 1 && grid[i][j-1] == 2)
                                    || (grid[i+1][j] == 2 && grid[i][j+1] == 1) || (grid[i+1][j] == 2 && grid[i][j-1] == 1)
                                    || (grid[i-1][j] == 2 && grid[i][j+1] == 1) || (grid[i-1][j] == 2 && grid[i][j-1] == 1)
                                    || (grid[i+1][j] == 2 && grid[i][j+1] == 2) || (grid[i+1][j] == 2 && grid[i][j-1] == 2)
                                    || (grid[i-1][j] == 2 && grid[i][j+1] == 2) || (grid[i-1][j] == 2 && grid[i][j-1] == 2)
                                    || (grid[i+1][j] == 1 && grid[i][j+1] == 5) || (grid[i+1][j] == 1 && grid[i][j-1] == 5)
                                    || (grid[i-1][j] == 1 && grid[i][j+1] == 5) || (grid[i-1][j] == 1 && grid[i][j-1] == 5)
                                    || (grid[i+1][j] == 5 && grid[i][j+1] == 1) || (grid[i+1][j] == 5 && grid[i][j-1] == 1)
                                    || (grid[i-1][j] == 5 && grid[i][j+1] == 1) || (grid[i-1][j] == 5 && grid[i][j-1] == 1)
                                    || (grid[i+1][j] == 2 && grid[i][j+1] == 5) || (grid[i+1][j] == 2 && grid[i][j-1] == 5)
                                    || (grid[i-1][j] == 2 && grid[i][j+1] == 5) || (grid[i-1][j] == 2 && grid[i][j-1] == 5)
                                    || (grid[i+1][j] == 5 && grid[i][j+1] == 2) || (grid[i+1][j] == 5 && grid[i][j-1] == 2)
                                    || (grid[i-1][j] == 5 && grid[i][j+1] == 2) || (grid[i-1][j] == 5 && grid[i][j-1] == 2)
                                    || (grid[i+1][j] == 5 && grid[i][j+1] == 5) || (grid[i+1][j] == 5 && grid[i][j-1] == 5)
                                    || (grid[i-1][j] == 5 && grid[i][j+1] == 5) || (grid[i-1][j] == 5 && grid[i][j-1] == 5)
                                    )
                              numBoxesBlocked++;
                                break;
                        case 3:
                            gc.drawImage(placeImage, x, y, w, h);
                            placesNeeded++;    //number of places
                            break;
                        case 4:
                            gc.drawImage(sokobanImage, x, y, w, h);
                            SokoI = i;   //save coordinates
                            SokoJ = j;
                            break;
                        case 5:
                            gc.drawImage(boxImage, x, y, w, h);    //box over place
                            numBoxes++;
                            //if box is cornered
                            if((grid[i+1][j] == 1 && grid[i][j+1] == 1) || (grid[i+1][j] == 1 && grid[i][j-1] == 1)
                                    || (grid[i-1][j] == 1 && grid[i][j+1] == 1) || (grid[i-1][j] == 1 && grid[i][j-1] == 1)
                                    || (grid[i+1][j] == 1 && grid[i][j+1] == 2) || (grid[i+1][j] == 1 && grid[i][j-1] == 2)
                                    || (grid[i-1][j] == 1 && grid[i][j+1] == 2) || (grid[i-1][j] == 1 && grid[i][j-1] == 2)
                                    || (grid[i+1][j] == 2 && grid[i][j+1] == 1) || (grid[i+1][j] == 2 && grid[i][j-1] == 1)
                                    || (grid[i-1][j] == 2 && grid[i][j+1] == 1) || (grid[i-1][j] == 2 && grid[i][j-1] == 1)
                                    || (grid[i+1][j] == 2 && grid[i][j+1] == 2) || (grid[i+1][j] == 2 && grid[i][j-1] == 2)
                                    || (grid[i-1][j] == 2 && grid[i][j+1] == 2) || (grid[i-1][j] == 2 && grid[i][j-1] == 2)
                                    || (grid[i+1][j] == 1 && grid[i][j+1] == 5) || (grid[i+1][j] == 1 && grid[i][j-1] == 5)
                                    || (grid[i-1][j] == 1 && grid[i][j+1] == 5) || (grid[i-1][j] == 1 && grid[i][j-1] == 5)
                                    || (grid[i+1][j] == 5 && grid[i][j+1] == 1) || (grid[i+1][j] == 5 && grid[i][j-1] == 1)
                                    || (grid[i-1][j] == 5 && grid[i][j+1] == 1) || (grid[i-1][j] == 5 && grid[i][j-1] == 1)
                                    || (grid[i+1][j] == 2 && grid[i][j+1] == 5) || (grid[i+1][j] == 2 && grid[i][j-1] == 5)
                                    || (grid[i-1][j] == 2 && grid[i][j+1] == 5) || (grid[i-1][j] == 2 && grid[i][j-1] == 5)
                                    || (grid[i+1][j] == 5 && grid[i][j+1] == 2) || (grid[i+1][j] == 5 && grid[i][j-1] == 2)
                                    || (grid[i-1][j] == 5 && grid[i][j+1] == 2) || (grid[i-1][j] == 5 && grid[i][j-1] == 2)
                                    || (grid[i+1][j] == 5 && grid[i][j+1] == 5) || (grid[i+1][j] == 5 && grid[i][j-1] == 5)
                                    || (grid[i-1][j] == 5 && grid[i][j+1] == 5) || (grid[i-1][j] == 5 && grid[i][j-1] == 5)
                                    )
                              numBoxesBlocked++;
                            break;
                        case 6:
                            gc.drawImage(sokobanImage, x, y, w, h);   //sokoban over place
                            placesNeeded++;
                            SokoI = i;   //save coordinates
                            SokoJ = j;
                            break;
                    }

               /*     // THEN RENDER THE TEXT
                    String numToDraw = "" + grid[i][j];
                    double xInc = (w / 2) - (10 / 2);
                    double yInc = (h / 2) + (10 / 4);
                    x += xInc;
                    y += yInc;
                    gc.setFill(Color.RED);
                    gc.fillText(numToDraw, x, y);
                    x -= xInc;
                    y -= yInc;
                */    
                    // ON TO THE NEXT ROW
                    y += h;
                }
                // ON TO THE NEXT COLUMN
                x += w;
            }
        }
        public int getSokoI()
        {  return SokoI;}
        public int getSokoJ()
        {  return SokoJ;}
    }





    /**
     * This method initializes the language-specific game controls, which
     * includes the three primary game screens.
     */
    public void initSokobanUI() {
        // FIRST REMOVE THE SPLASH SCREEN
        mainPane.getChildren().clear();

        // GET THE UPDATED TITLE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String title = props.getProperty(SokobanPropertyType.GAME_TITLE_TEXT);
        primaryStage.setTitle(title);

        // THEN ADD ALL THE STUFF WE MIGHT NOW USE
        initNorthToolbar();
        // OUR WORKSPACE WILL STORE EITHER THE GAME, STATS,
        // OR HELP UI AT ANY ONE TIME
        //initWorkspace();
        //initGameScreen();
        //initStatsPane();
        //initHelpPane();

        // WE'LL START OUT WITH THE GAME SCREEN
        changeWorkspace(SokobanUIState.PLAY_GAME_STATE);

    }

    /**
     * This function initializes all the controls that go in the north toolbar.
     */
    private void initNorthToolbar() {
        // MAKE THE NORTH TOOLBAR, WHICH WILL HAVE FOUR BUTTONS
        northToolbar = new HBox();
        northToolbar.setStyle("-fx-background-color:lightgray");
        northToolbar.setAlignment(Pos.CENTER);
        northToolbar.setPadding(marginlessInsets);
        northToolbar.setSpacing(10.0);

        // MAKE AND INIT THE GAME BUTTON
        gameButton = initToolbarButton(northToolbar,
                SokobanPropertyType.GAME_IMG_NAME);
        gameButton.setText("Game");
        gameButton.setFocusTraversable(false);
        gameButton.setVisible(false);
        //setTooltip(gameButton, SokobanPropertyType.GAME_TOOLTIP);
        gameButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler
                        .respondToSwitchScreenRequest(SokobanUIState.PLAY_GAME_STATE);
            }
        });
        
        // MAKE AND INIT THE STATS BUTTON
        statsButton = initToolbarButton(northToolbar,
                SokobanPropertyType.STATS_IMG_NAME);
        //setTooltip(statsButton, SokobanPropertyType.STATS_TOOLTIP);
        statsButton.setText("Stats");
        statsButton.setFocusTraversable(false);
        statsButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler
                        .respondToSwitchScreenRequest(SokobanUIState.VIEW_STATS_STATE);
            }

        });
       
        /*// MAKE AND INIT THE HELP BUTTON
        helpButton = initToolbarButton(northToolbar,
                SokobanPropertyType.HELP_IMG_NAME);
        //setTooltip(helpButton, SokobanPropertyType.HELP_TOOLTIP);
        helpButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler
                        .respondToSwitchScreenRequest(SokobanUIState.VIEW_HELP_STATE);
            }

        });

        // MAKE AND INIT THE EXIT BUTTON
        exitButton = initToolbarButton(northToolbar,
                SokobanPropertyType.EXIT_IMG_NAME);
        //setTooltip(exitButton, SokobanPropertyType.EXIT_TOOLTIP);
        exitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToExitRequest(primaryStage);
            }

        });
*/
        // AND NOW PUT THE NORTH TOOLBAR IN THE FRAME
        //mainPane.getChildren().add(northToolbar);
       
    }

    /**
     * This method helps to initialize buttons for a simple toolbar.
     *
     * @param toolbar The toolbar for which to add the button.
     *
     * @param prop The property for the button we are building. This will
     * dictate which image to use for the button.
     *
     * @return A constructed button initialized and added to the toolbar.
     */
    private Button initToolbarButton(HBox toolbar, SokobanPropertyType prop) {
        // GET THE NAME OF THE IMAGE, WE DO THIS BECAUSE THE
        // IMAGES WILL BE NAMED DIFFERENT THINGS FOR DIFFERENT LANGUAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imageName = props.getProperty(prop);

        // LOAD THE IMAGE
        Image image = loadImage(imageName);
        ImageView imageIcon = new ImageView(image);

        // MAKE THE BUTTON
        Button button = new Button();
        button.setGraphic(imageIcon);
        button.setPadding(marginlessInsets);

        // PUT IT IN THE TOOLBAR
        toolbar.getChildren().add(button);

        // AND SEND BACK THE BUTTON
        return button;
    }

    /**
     * The workspace is a panel that will show different screens depending on
     * the user's requests.
     */
    private void initWorkspace() {
        // THE WORKSPACE WILL GO IN THE CENTER OF THE WINDOW, UNDER THE NORTH
        // TOOLBAR
        workspace = new Pane();
        mainPane.setCenter(workspace);
        //mainPane.getChildren().add(workspace);
        System.out.println("in the initWorkspace");
    }


    public Image loadImage(String imageName) {
        Image img = new Image(ImgPath + imageName);
        return img;
    }

    /**
     * This function selects the UI screen to display based on the uiScreen
     * argument. Note that we have 3 such screens: game, stats, and help.
     *
     * @param uiScreen The screen to be switched to.
     */
    public void changeWorkspace(SokobanUIState uiScreen) {
        switch (uiScreen) {
            case VIEW_HELP_STATE:
                mainPane.setCenter(helpPanel);
                break;
            case PLAY_GAME_STATE:
                mainPane.setCenter(gamePanel);
                break;
            case VIEW_STATS_STATE:
                initStatsPane();
              
                break;
            default:
        }

    }


}
