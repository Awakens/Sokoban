package HangMan.ui;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JEditorPane;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import application.Main.HangManPropertyType;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;
import HangMan.file.HangManFileLoader;
import HangMan.game.HangManGameStateManager;

public class HangManEventHandler {
	private HangManUI ui;
	
	/**
     * Constructor that simply saves the ui for later.
     * 
     * @param initUI 
     */
	public HangManEventHandler(HangManUI initUI){
		ui = initUI;
	}
	
	/**
     * This method responds to when the user wishes to switch between 
     * the Game, Stats, and Help screens.
     * 
     * @param uiState The ui state, or screen, that the user wishes
     * to switch to.
     */
	public void respondToSwitchScreenRequest(HangManUI.HangManUIState uiState){
		
		//ui.changeWor
	}
	
	/**
     * This method responds to when the user presses the
     * new game method. 
     */
    public void respondToNewGameRequest()
    {
        HangManGameStateManager gsm = ui.getGSM();
        gsm.startNewGame();
    }
    
    /**
     * This method responds to when the user requests to go the help
     * screen's home page. It responds by loading that page.
     */
    public void respondToGoHomeRequest()
    {
        JEditorPane helpPage = ui.getHelpPane();
        ui.loadPage(helpPage, HangManPropertyType.HELP_FILE_NAME);
    }
	
	/**
     * This method handles when the user selects a language for the application.
     * At that time we'll need to load language-specific controls.
     * 
     * @param language The language selected by the user.
     */
	public void respondToSelectLanguageRequest(String language){
		
		HangManGameStateManager gsm = ui.getGSM();
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		
		// GET THE SELECTED LANGUAGE & IT'S XML FILE
        ArrayList<String> languages = props.getPropertyOptionsList(HangManPropertyType.LANGUAGE_OPTIONS);
        ArrayList<String> languageData = props.getPropertyOptionsList(HangManPropertyType.LANGUAGE_DATA_FILE_NAMES);
        int langIndex = languages.indexOf(language);
        String langDataFile = languageData.get(langIndex);
        String langSchema = props.getProperty(HangManPropertyType.PROPERTIES_SCHEMA_FILE_NAME);
        try
        {
            // LOAD THE LANGUAGE SPECIFIC PROPERTIES
            props.loadProperties(langDataFile, langSchema);
                       
            // LOAD THE WORD LIST
            String wordListFile = props.getProperty(HangManPropertyType.WORD_LIST_FILE_NAME);
            String wordList = HangManFileLoader.loadTextFile(wordListFile);
            gsm.loadWordList(wordList);
            
            // INITIALIZE THE USER INTERFACE WITH THE SELECTED LANGUAGE
            ui.initHangManUI();
            
            // WE'LL START THE GAME TOO
            //gsm.startNewGame();
        }
        catch(InvalidXMLFileFormatException ixmlffe)
        {
            ui.getErrorHandler().processError(HangManPropertyType.INVALID_XML_FILE_ERROR_TEXT);
            System.exit(0);
        }        
        catch(IOException ioe)
        {
            ui.getErrorHandler().processError(HangManPropertyType.INVALID_DICTIONARY_ERROR_TEXT);
            System.exit(0);
        }
	}
	
	/**
     * This method responds to when the user presses on letter 
     * 
     * @param source the letter button user click on
     * @param guess the letter user guessed
     */
    public void respondToGuessWordRequest(Object source,String guess)
    {
        Button letterButton = (Button)source;
        HangManGameStateManager gsm = ui.getGSM();
        //CHECK IF THE LETTER IS IN SECRETWORD
        //SET COLOR FIRST
       
        if(ui.getGSM().getGameInProgress().isRightGuess(guess)){
            //letterButton.setBackground(Color.GREEN);
        	letterButton.setStyle("-fx-base:green");
            //letterButton.setForeground(Color.BLUE);
        	letterButton.setStyle("-fx-text-fill:blue");
                letterButton.setStyle("-fx-background-color: green;");    //check
        	
            //letterButton.setEnabled(false);
        	letterButton.setDisable(true);

        }
        //WRONG GUESS
        else if(!ui.getGSM().getGameInProgress().isRightGuess(guess)){
            //letterButton.setBackground(Color.RED);
            //letterButton.setForeground(Color.WHITE);
            //letterButton.setEnabled(false);
        	letterButton.setStyle("-fx-base:red");
        	letterButton.setStyle("-fx-text-fill:black");
                letterButton.setStyle("-fx-background-color: red;");     //check
        	letterButton.setDisable(true);
        }

            // THEN PROCESS THE GUESS

                String guessState=ui.getGSM().getGameInProgress().getNewGuess();
                System.out.println(guessState);
                gsm.processGuess(guessState,guess);

    }
	
	/**
     * This method responds to when the user requests to exit the application.
     * 
     * @param window The window that the user has requested to close.
     */
	public void respondToExitRequest(Stage primaryStage)
    {
        // ENGLIS IS THE DEFAULT
        String options[] = new String[]{"Yes", "No"};
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        options[0] = props.getProperty(HangManPropertyType.DEFAULT_YES_TEXT);
        options[1] = props.getProperty(HangManPropertyType.DEFAULT_NO_TEXT);
        String verifyExit = props.getProperty(HangManPropertyType.DEFAULT_EXIT_TEXT);
        
        // NOW WE'LL CHECK TO SEE IF LANGUAGE SPECIFIC VALUES HAVE BEEN SET
        if (props.getProperty(HangManPropertyType.YES_TEXT) != null)
        {
            options[0] = props.getProperty(HangManPropertyType.YES_TEXT);
            options[1] = props.getProperty(HangManPropertyType.NO_TEXT);
            verifyExit = props.getProperty(HangManPropertyType.EXIT_REQUEST_TEXT);
        }
        
        // FIRST MAKE SURE THE USER REALLY WANTS TO EXIT
        /*int selection = JOptionPane.showOptionDialog(   window, 
                                                        verifyExit, 
                                                        verifyExit, 
                                                        JOptionPane.YES_NO_OPTION, 
                                                        JOptionPane.ERROR_MESSAGE,
                                                        null,
                                                        options,
                                                        null);
        // WHAT'S THE USER'S DECISION?
        if (selection == JOptionPane.YES_NO_OPTION)
        {
            // YES, LET'S EXIT
            System.exit(0);
        }*/
        
        // FIRST MAKE SURE THE USER REALLY WANTS TO EXIT
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        BorderPane exitPane = new BorderPane();
        HBox optionPane = new HBox();
        Button yesButton = new Button(options[0]);
        Button noButton = new Button(options[1]);
        optionPane.setSpacing(10.0);
        optionPane.getChildren().addAll(yesButton, noButton);
        Label exitLabel = new Label(verifyExit);
        exitPane.setCenter(exitLabel);
        exitPane.setBottom(optionPane);
        Scene scene = new Scene(exitPane, 50, 100);
        dialogStage.setScene(scene);
        dialogStage.show();
        // WHAT'S THE USER'S DECISION?
        yesButton.setOnAction(e -> {
            // YES, LET'S EXIT
            System.exit(0);
        });

    }    
	
	
}
