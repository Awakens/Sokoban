package HangMan.ui;

import application.Main;
import application.Main.HangManPropertyType;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;

import HangMan.file.HangManFileLoader;
import HangMan.game.HangManGameData;
import HangMan.game.HangManGameStateManager;
import application.Main.HangManPropertyType;
import properties_manager.PropertiesManager;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javax.swing.JScrollPane;

public class HangManUI extends Pane {
	/**
	 * The HangManUIState represents the four screen states that are possible
	 * for the HangMan game application. Depending on which state is in current
	 * use, different controls will be visible.
	 */
	public enum HangManUIState {
		SPLASH_SCREEN_STATE, PLAY_GAME_STATE, VIEW_STATS_STATE, VIEW_HELP_STATE, 
		HANG1_STATE, HANG2_STATE, HANG3_STATE, HANG4_STATE, HANG5_STATE, HANG6_STATE,
	}

	// mainStage
	private Stage primaryStage;

	// mainPane
	private BorderPane mainPane;
	private BorderPane hmPane;
	private CheatKeyHandler cheatKeyHandler;
        SwingNode statsSwingNode = new SwingNode();   //check  //swingnodes for swithcing frames
        SwingNode gameSwingNode = new SwingNode();
        SwingNode helpSwingNode = new SwingNode();

	// SplashScreen
	private ImageView splashScreenImageView;
	private Pane splashScreenPane;
	private Label splashScreenImageLabel;
	private HBox languageSelectionPane;
	private ArrayList<Button> languageButtons;

	// NorthToolBar
	private HBox northToolbar;
	private Button gameButton;
	private Button statsButton;
	private Button helpButton;
	private Button exitButton;

	// GamePane
	private Label HangManLabel;
	private Button newGameButton;
	private HBox letterButtonsPane;
	private HashMap<Character,Button> letterButtons;
	private BorderPane gamePanel = new BorderPane();
        private HBox hbox = new HBox();
        private Label ClickNew = new Label("Click New Game");    //check
        private boolean NewGameClicked;
       
	
	//StatsPane
	private ScrollPane statsScrollPane;
	private JEditorPane statsPane;
	
	//HelpPane
	private BorderPane helpPanel;
	private ScrollPane helpScrollPane;
	private JEditorPane helpPane;
	private Button homeButton;
	private Pane workspace;

	// Padding
	private Insets marginlessInsets;

	// Image path
	private String ImgPath = "file:img/";

	// mainPane weight && height
	private int paneWidth;
	private int paneHeigth;

	// THIS CLASS WILL HANDLE ALL ACTION EVENTS FOR THIS PROGRAM
	private HangManEventHandler eventHandler;
	private HangManErrorHandler errorHandler;
	private HangManDocumentManager docManager;

	HangManGameStateManager gsm;
	
	public HangManUI() {
		// WE'LL USE THIS EVENT HANDLER FOR LOTS OF CONTROLS
		eventHandler = new HangManEventHandler(this);

		gsm = new HangManGameStateManager(this);
		
		cheatKeyHandler = new CheatKeyHandler(this);
		
		eventHandler = new HangManEventHandler(this);
		
		errorHandler = new HangManErrorHandler(primaryStage);
		
		docManager = new HangManDocumentManager(this);

		initMainPane();
		initSplashScreen();

	}

	public void SetStage(Stage stage) {
		primaryStage = stage;
	}

	public BorderPane GetMainPane() {
		return this.mainPane;
	}

	public HangManGameStateManager getGSM() {
		return gsm;
	}
	
	public HangManDocumentManager getDocManager(){
		return docManager;
	}
	
	public HangManErrorHandler getErrorHandler(){
		return errorHandler;
	}
	
	public JEditorPane getHelpPane() 
    { 
        return helpPane; 
    }

	public void initMainPane() {
		marginlessInsets = new Insets(5, 5, 5, 5);
		mainPane = new BorderPane();
		
		
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		paneWidth = Integer.parseInt(props
				.getProperty(HangManPropertyType.WINDOW_WIDTH));
		paneHeigth = Integer.parseInt(props
				.getProperty(HangManPropertyType.WINDOW_HEIGHT));
		mainPane.resize(paneWidth, paneHeigth);
		mainPane.setPadding(marginlessInsets);
	}

	public void initSplashScreen() {

		// INIT THE SPLASH SCREEN CONTROLS
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		String splashScreenImagePath = props
				.getProperty(HangManPropertyType.SPLASH_SCREEN_IMAGE_NAME);
		props.addProperty(HangManPropertyType.INSETS, "5");
		String str = props.getProperty(HangManPropertyType.INSETS);
		//System.out.println("11111111"+str);
		splashScreenPane = new Pane();

		Image splashScreenImage = loadImage(splashScreenImagePath);
		splashScreenImageView = new ImageView(splashScreenImage);

		splashScreenImageLabel = new Label();
		splashScreenImageLabel.setGraphic(splashScreenImageView);
		splashScreenImageLabel.setLayoutX(-45);// move the label position to fix
												// the pane
		splashScreenPane.getChildren().add(splashScreenImageLabel);
		// add key listener

		// GET THE LIST OF LANGUAGE OPTIONS
		ArrayList<String> languages = props
				.getPropertyOptionsList(HangManPropertyType.LANGUAGE_OPTIONS);
		ArrayList<String> languageImages = props
				.getPropertyOptionsList(HangManPropertyType.LANGUAGE_IMAGE_NAMES);

		languageSelectionPane = new HBox();
		languageSelectionPane.setSpacing(10.0);
		languageSelectionPane.setAlignment(Pos.CENTER);
		languageSelectionPane.setStyle("-fx-background-color:blue");
		// languageSelectionPane.setPadding(marginlessInsets);
		// add key listener
		languageButtons = new ArrayList<Button>();
		for (int i = 0; i < languages.size(); i++) {

			// GET THE LIST OF LANGUAGE OPTIONS
			String lang = languages.get(i);
			String langImageName = languageImages.get(i);
			Image langImage = loadImage(langImageName);
			ImageView langImageView = new ImageView(langImage);

			// AND BUILD THE BUTTON
			Button langButton = new Button();
			langButton.setGraphic(langImageView);
			// add key listener
			// add action command
			// langButton.setPadding(marginlessInsets);

			// CONNECT THE BUTTON TO THE EVENT HANDLER
			langButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					// System.out.println(lang);
					eventHandler.respondToSelectLanguageRequest(lang);

				}

			});
			languageSelectionPane.getChildren().add(langButton);
		}

		mainPane.setCenter(splashScreenPane);
		mainPane.setBottom(languageSelectionPane);
		//mainPane.getChildren().addAll(splashScreenPane,languageSelectionPane);
	}

	/**
	 * This method initializes the language-specific game controls, which
	 * includes the three primary game screens.
	 */
	public void initHangManUI() {
		// FIRST REMOVE THE SPLASH SCREEN
		mainPane.getChildren().clear();

		// GET THE UPDATED TITLE
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		String title = props.getProperty(HangManPropertyType.GAME_TITLE_TEXT);
		primaryStage.setTitle(title);

		// THEN ADD ALL THE STUFF WE MIGHT NOW USE
		initNorthToolbar();

		// OUR WORKSPACE WILL STORE EITHER THE GAME, STATS,
		// OR HELP UI AT ANY ONE TIME
		initWorkspace();
		initGameScreen();
		initStatsPane();
		initHelpPane();

		// WE'LL START OUT WITH THE GAME SCREEN
		changeWorkspace(HangManUIState.PLAY_GAME_STATE);

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
		// northToolbar.addKeyListener(cheatKeyHandler);

		// MAKE AND INIT THE GAME BUTTON
		gameButton = initToolbarButton(northToolbar,
				HangManPropertyType.GAME_IMG_NAME);
		setTooltip(gameButton, HangManPropertyType.GAME_TOOLTIP);
		gameButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				eventHandler
						.respondToSwitchScreenRequest(HangManUIState.PLAY_GAME_STATE);
                                  mainPane.getChildren().clear();   //check   //clear out frame
                                  mainPane.setCenter(gameSwingNode);   //switching frames 
                                  mainPane.setTop(northToolbar);
		                  mainPane.setBottom(hbox); 
		                  mainPane.setCenter(workspace);
			}
		});

		// MAKE AND INIT THE STATS BUTTON
		statsButton = initToolbarButton(northToolbar,
				HangManPropertyType.STATS_IMG_NAME);
		setTooltip(statsButton, HangManPropertyType.STATS_TOOLTIP);

		statsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				eventHandler
						.respondToSwitchScreenRequest(HangManUIState.VIEW_STATS_STATE);
                                  mainPane.getChildren().clear();   //check   //clear out frame
                                  mainPane.setCenter(statsSwingNode);   //switching frames 
                                  mainPane.setTop(northToolbar);
			}

		});
		// MAKE AND INIT THE HELP BUTTON
		helpButton = initToolbarButton(northToolbar,
				HangManPropertyType.HELP_IMG_NAME);
		setTooltip(helpButton, HangManPropertyType.HELP_TOOLTIP);
		helpButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				eventHandler
						.respondToSwitchScreenRequest(HangManUIState.VIEW_HELP_STATE);
                                  mainPane.getChildren().clear();   //check   //clear out frame
                                  
                                  mainPane.setCenter(helpSwingNode);   //switching frames 
                                  mainPane.setTop(northToolbar);
                                  mainPane.setBottom(homeButton);
			}

		});

		// MAKE AND INIT THE EXIT BUTTON
		exitButton = initToolbarButton(northToolbar,
				HangManPropertyType.EXIT_IMG_NAME);
		setTooltip(exitButton, HangManPropertyType.EXIT_TOOLTIP);
		exitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				eventHandler.respondToExitRequest(primaryStage);
			}

		});
		
		// AND NOW PUT THE NORTH TOOLBAR IN THE FRAME
		mainPane.setTop(northToolbar);
		//mainPane.getChildren().add(northToolbar);
                mainPane.setBottom(hbox);                      //check
                hbox.getChildren().add(ClickNew);
                ClickNew.setStyle("-fx-background-color: green;");
                ClickNew.setStyle("-fx-font-size:40;");
	}

	/**
	 * This method helps to initialize buttons for a simple toolbar.
	 * 
	 * @param toolbar
	 *            The toolbar for which to add the button.
	 * 
	 * @param prop
	 *            The property for the button we are building. This will dictate
	 *            which image to use for the button.
	 * 
	 * @return A constructed button initialized and added to the toolbar.
	 */
	private Button initToolbarButton(HBox toolbar, HangManPropertyType prop) {
		// GET THE NAME OF THE IMAGE, WE DO THIS BECAUSE THE
		// IMAGES WILL BE NAMED DIFFERENT THINGS FOR DIFFERENT LANGUAGES
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		String imageName = props.getProperty(prop);

		// LOAD THE IMAGE
		Image image = loadImage(imageName);
		// ImageIcon imageIcon = new ImageIcon(image);
		ImageView imageIcon = new ImageView(image);

		// MAKE THE BUTTON
		Button button = new Button();
		button.setGraphic(imageIcon);
		// button.addKeyListener(cheatKeyHandler);
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

		// THE CardLayout MANAGER LETS US SWITCH EASILY
		// CardLayout cardLayout = new CardLayout();
		// workspace.setLayout(cardLayout);
	}

	JEditorPane gamePane;
        JScrollPane guessesScrollPane;
       
	
	/**
	 * This method initializes the game screen for running the game.
	 */
	private void initGameScreen() {
		PropertiesManager props = PropertiesManager.getPropertiesManager();

		// THE GUESS HISTORY GOES IN THE CENTER, WHICH WE'LL DISPLAY
		// USING HTML IN A JEditorPane
		gamePane = new JEditorPane();
		// gamePane.addKeyListener(cheatKeyHandler);
		gamePane.setEditable(false);
		gamePane.setContentType("text/html");
		gamePane.setSize(600, 600);

		// LET'S LOAD THE INITIAL HTML INTO THE STATS EDITOR PAGE
		this.loadPage(gamePane, HangManPropertyType.GAME_FILE_NAME);
		HTMLDocument gameDoc = (HTMLDocument) gamePane.getDocument();
		docManager.setGameDoc(gameDoc);
		
		//embed swing into javafx
		guessesScrollPane = new JScrollPane(gamePane);
                gameSwingNode.setContent(guessesScrollPane);
		//guessesScrollPane.autosize();
		//guessesScrollPane.resize(200, 200);

		// LOAD THE HangMan PICTURE AT ZERO STAGE
		String HangMan = props
				.getProperty(HangManPropertyType.HANGMAN0_IMG_NAME);
		Image HangManImg = loadImage(HangMan);
		ImageView image = new ImageView(HangManImg);
		HangManLabel = new Label();
		HangManLabel.setGraphic(image);
		hmPane = new BorderPane();
		hmPane.setCenter(HangManLabel);
		//hmPanel.validate();

		// THE SOUTH GAME PANEL WILL BE DIVIDED IN TWO
		BorderPane southGamePane = new BorderPane();
		//southGamePanel.addKeyListener(cheatKeyHandler);
		//southGamePanel.setLayout(new BorderLayout());

		// WE'LL PUT THE GUESSING CONTROLS IN THE NORTH OF THE SOUTH
		HBox guessingPane = new HBox();
		guessingPane.setStyle("-fx-background-color:lightgray");
		guessingPane.setAlignment(Pos.CENTER);
		
		//guessingPanel.addKeyListener(cheatKeyHandler);
		
		// WHEN USE CHEAT,SHOW SECRET WORD
		guessingPane.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent ke) {
				// TODO Auto-generated method stub
				cheatKeyHandler.keyPressed(ke);
			}
			
			
		});

		// THE NEW GAME BUTTON IS LAST CONTROL FOR THE NORTH OF THE SOUTH
		newGameButton = new Button("New Game");
		
		setTooltip(newGameButton, HangManPropertyType.NEW_GAME_TOOLTIP);
		
		newGameButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				eventHandler.respondToNewGameRequest();
                                ClickNew.setVisible(false);     //check 
                                NewGameClicked = true;
			}
			
		});
		
		guessingPane.getChildren().add(newGameButton);

		// WE'LL PUT THE LETTER BUTTONS IN THE SOUTH OF THE SOUTH
		letterButtonsPane = new HBox();
		letterButtonsPane.setStyle("-fx-background-color: lightgray");
		letterButtonsPane.setPadding(marginlessInsets);
		letterButtonsPane.setSpacing(3.0);
		ArrayList<String> letters = props
				.getPropertyOptionsList(HangManPropertyType.LETTER_OPTIONS);

		// WE'LL STORE THE ALPHABET LETTERS IN A NICE EASY TO ACCESS HASH TABLE
		letterButtons = new HashMap();
		String fontFamily = props
				.getProperty(HangManPropertyType.LETTERS_FONT_FAMILY);
		int fontSize = Integer.parseInt(props
				.getProperty(HangManPropertyType.LETTERS_FONT_SIZE));
		//Font lettersFont = new Font(fontFamily, Font.BOLD, fontSize);
		Font lettersFont = new Font(fontSize);
		Font.font(fontFamily, FontWeight.BOLD, fontSize);
		
		for (int i = 0; i < letters.size(); i++) {
			// MAKE A BUTTON FOR EACH CHARACTER
			Character c = letters.get(i).charAt(0);
			Button letterButton = new Button("" + c);
			letterButton.setFont(lettersFont);
			letterButtons.put(c, letterButton);
			letterButton.setPadding(marginlessInsets);
			letterButton.setBorder(null);
			letterButtonsPane.getChildren().add(letterButton);
			final String guess;
			guess = c.toString();
			// LET THE EVENT HANDLER DEAL WITH WHEN SOMEONE PRESSES THIS BUTTON
			// THE RIGHT LETTER IN SECRET WORD WILL SHOW AS GREEN, WRONG WILL
			// SHOW AS RED
			// AND THEY WOULD BE DISABLED IF CHOSEN.
			letterButton.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
                                    if(NewGameClicked == true)
                                    {eventHandler.respondToGuessWordRequest(event.getSource(),
							guess);
				} else{ ClickNew.setStyle("-fx-background-color: red;");
                                        ClickNew.setStyle("-fx-font-size:40;");
                                    }
                                }
				
			});
		}

		// RESET ALL THE LETTER BUTTONS
		resetLetterButtonColors();

		// NOW LAY EVERYTHING OUT IN THE GAME PANEL

		//gamePanel.setLayout(new BorderLayout());
		southGamePane.setTop(guessingPane);
		southGamePane.setBottom(letterButtonsPane);
		gamePanel.setCenter(gameSwingNode);
		gamePanel.setBottom(southGamePane);
		gamePanel.setRight(hmPane);
		// NOW MAKE THIS PANEL PART OF THE WORKSPACE, WHICH MEANS WE
		// CAN EASILY SWITCH TO IT AT ANY TIME

		//workspace.add(gamePanel, HangManUIState.PLAY_GAME_STATE.toString());
		workspace.getChildren().add(gamePanel);
		System.out.println("in the initgamePane");

	}
	
	 /**
     * This method initializes the stats pane controls for use.
     */
    private void initStatsPane()
    {
        // WE'LL DISPLAY ALL STATS IN A JEditorPane
        statsPane = new JEditorPane();
        statsPane.setEditable(false);
        statsPane.setContentType("text/html");
 
        // LOAD THE STARTING STATS PAGE, WHICH IS JUST AN OUTLINE
        // AND DOESN"T HAVE ANY OF THE STATS, SINCE THOSE WILL 
        // BE DYNAMICALLY ADDED
        loadPage(statsPane, HangManPropertyType.STATS_FILE_NAME);
        HTMLDocument statsDoc = (HTMLDocument)statsPane.getDocument();
            docManager.setStatsDoc(statsDoc);  
        statsSwingNode.setContent(statsPane);
        statsScrollPane = new ScrollPane();
        statsScrollPane.setContent(statsSwingNode);
        
        // NOW ADD IT TO THE WORKSPACE, MEANING WE CAN SWITCH TO IT
        //workspace.add(statsScrollPane, HangManUIState.VIEW_STATS_STATE.toString());
        //workspace.getChildren().add(statsScrollPane);
    }

    /**
     * This method initializes the help pane and all of its controls.
     */
    private void initHelpPane()
    {
        // WE'LL DISPLAY ALL HELP INFORMATION USING HTML
        helpPane = new JEditorPane();
        helpPane.setEditable(false);
        helpSwingNode.setContent(helpPane);
        helpScrollPane = new ScrollPane();
        helpScrollPane.setContent(helpSwingNode);
                        
        // NOW LOAD THE HELP HTML
        helpPane.setContentType("text/html");
        
        // MAKE THE HELP BUTTON
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String homeImgName = props.getProperty(HangManPropertyType.HOME_IMG_NAME);
        Image homeImg = loadImage(homeImgName);
        ImageView homeImgIcon = new ImageView(homeImg);
        homeButton = new Button();
        homeButton.setGraphic(homeImgIcon);
        setTooltip(homeButton, HangManPropertyType.HOME_TOOLTIP);
        homeButton.setPadding(marginlessInsets);
        
        // WE'LL PUT THE HOME BUTTON IN A TOOLBAR IN THE NORTH OF THIS SCREEN,
        // UNDER THE NORTH TOOLBAR THAT'S SHARED BETWEEN THE THREE SCREENS
        Pane helpToolbar = new Pane();
        helpPanel = new BorderPane();
        //helpPanel.setLayout(new BorderLayout());
   
        
        helpPanel.setCenter(helpScrollPane);
        helpToolbar.getChildren().add(homeButton);
        helpToolbar.setStyle("-fx-background-color:white");
        
        // LOAD THE HELP PAGE
        loadPage(helpPane, HangManPropertyType.HELP_FILE_NAME);
        
        // LET OUR HELP PAGE GO HOME VIA THE HOME BUTTON
        homeButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				eventHandler.respondToGoHomeRequest();
                                  mainPane.getChildren().clear();   //check   //clear out frame
                                 
                                  mainPane.setCenter(splashScreenPane);
		                  mainPane.setBottom(languageSelectionPane);
			}
        	
        });
        
        // LET OUR HELP SCREEN JOURNEY AROUND THE WEB VIA HYPERLINK
        HelpHyperlinkListener hhl = new HelpHyperlinkListener(this);
        //helpPane.addHyperlinkListener(hhl);         
        
        // ADD IT TO THE WORKSPACE
       // workspace.add(helpPanel, HangManUIState.VIEW_HELP_STATE.toString());
       // workspace.getChildren().add(helpPanel);
    } 

	public Image loadImage(String imageName) {
		Image img = new Image(ImgPath + imageName);
		// System.out.print(imageName);
		return img;
	}
	
	/**
     * This function resets the color buttons so that they all represent
     * unknowns. We do this using grey background and black lettering.
     */
    private void resetLetterButtonColors()
    {
        for (Button letterButton : letterButtons.values())
        {
            //letterButton.setBackground(Color.LIGHT_GRAY);
            //letterButton.setForeground(Color.BLACK);
            //letterButton.setEnabled(true);
        	letterButton.setStyle("-fx-background-color:lightgray");
        	letterButton.setStyle("-fx-base:black");
        	letterButton.setDisable(false);
        }        
    }

	/**
	 * A tooltip is mouse-over text for a control. This method sets the tooltip
	 * for the button argument using the language-specific properties
	 * represented by tooltip.
	 * 
	 * @param button
	 *            The button whose tooltip we wish to set.
	 * 
	 * @param tooltip
	 *            The text to set as the tooltip.
	 */
	private void setTooltip(Button button, HangManPropertyType tooltip) {
		// GET THE TEXT AND SET IT AS THE TOOLITP
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		String tooltipText = props.getProperty(tooltip);
		Tooltip toolTip = new Tooltip(tooltipText);
		// button.setToolTipText(tooltipText);
		button.setTooltip(toolTip);
	}

	/**
	 * This function selects the UI screen to display based on the uiScreen
	 * argument. Note that we have 3 such screens: game, stats, and help.
	 * 
	 * @param uiScreen
	 *            The screen to be switched to.
	 */
	public void changeWorkspace(HangManUIState uiScreen) {
		// SWITCH TO THE REQUESTED SCREEN
		// CardLayout workspaceCardLayout = (CardLayout)workspace.getLayout();
		// workspaceCardLayout.show(workspace, uiScreen.toString());
	}
	
	/**
     * This is a cheat that helps with testing. It displays
     * the secret word in the window's title.
     */
    public void displaySecretWord()
    {
        // ONLY DO SO IF THE GAME IS IN PROGRESS
        HangManGameData gameInProgress = gsm.getGameInProgress();
        if (gameInProgress != null)
            primaryStage.setTitle(gameInProgress.getSecretWord());
    }

	/**
	 * This method loads the HTML page that corresponds to the fileProperty
	 * argument and puts it into the jep argument for display.
	 * 
	 * @param jep
	 *            The pane that will display the loaded HTML.
	 * 
	 * @param fileProperty
	 *            The file property, whose name can then be retrieved from the
	 *            property manager.
	 */

	public void loadPage(JEditorPane jep, HangManPropertyType fileProperty) {
		// GET THE FILE NAME
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		String fileName = props.getProperty(fileProperty);
		try {
			// LOAD THE HTML INTO THE EDITOR PANE
			String fileHTML = HangManFileLoader.loadTextFile(fileName);
			jep.setText(fileHTML);
		} catch (IOException ioe) {
			errorHandler.processError(HangManPropertyType.INVALID_URL_ERROR_TEXT);
		}
	}
	
	/*This method is used to set HanMan pictures when wrong guess times increases
     * 
     * 
     */
     public void updateHangMan(int wrongTimes){
     PropertiesManager props = PropertiesManager.getPropertiesManager();
     String HangMan = props.getProperty(HangManPropertyType.HANGMAN0_IMG_NAME);
     switch(wrongTimes){
         case 1:HangMan = props.getProperty(HangManPropertyType.HANGMAN1_IMG_NAME);
             break;
         case 2:HangMan = props.getProperty(HangManPropertyType.HANGMAN2_IMG_NAME);
             break;
         case 3:HangMan = props.getProperty(HangManPropertyType.HANGMAN3_IMG_NAME);
             break; 
         case 4:HangMan = props.getProperty(HangManPropertyType.HANGMAN4_IMG_NAME);
             break;
         case 5:HangMan = props.getProperty(HangManPropertyType.HANGMAN5_IMG_NAME);
             break;
         case 6:HangMan = props.getProperty(HangManPropertyType.HANGMAN6_IMG_NAME);
             break;
     }
     if(wrongTimes > 6)                                                           //check
         HangMan = props.getProperty(HangManPropertyType.HANGMAN6_IMG_NAME);

        Image HangManImg=loadImage(HangMan);
                
        ImageView image = new ImageView(HangManImg);
        HangManLabel=new Label();
        HangManLabel.setGraphic(image);
        hmPane.getChildren().clear();
        hmPane.setCenter(HangManLabel);

}
    /**
     * This method loads the link Web Page into the Help Screen's
     * editor pane.
     * 
     * @param link The Web Page to load.
     */
    public void loadRemoteHelpPage(URL link)
    {
        try
        {
            // PUT THE WEB PAGE IN THE HELP PANE
            Document doc = helpPane.getDocument();
            doc.putProperty(Document.StreamDescriptionProperty, null);
            helpPane.setPage(link);            
        }
        catch(IOException ioe)
        {
            errorHandler.processError(HangManPropertyType.INVALID_URL_ERROR_TEXT);
        }
    }
    
    /**
     * This function clears the UI when a new game is started, resetting the
     * letter color buttons and clearing the guesses display.
     */
    public void resetUI()
    {
        docManager.clearGamePage();
        resetLetterButtonColors();
  
    }
}
