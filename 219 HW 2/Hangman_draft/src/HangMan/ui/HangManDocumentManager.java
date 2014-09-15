package HangMan.ui;

import java.io.IOException;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import application.Main.HangManPropertyType;
import HangMan.game.HangManGameData;
import HangMan.game.HangManGameStateManager;
import properties_manager.PropertiesManager;
import javafx.stage.Stage;

public class HangManDocumentManager {
	private HangManUI ui;

	public HangManDocumentManager(HangManUI initUI) {
		ui = initUI;
	}

	// This record the matched string since each game
	public String oldGuessMatch = new String();
	// THESE ARE THE DOCUMENTS WE'LL BE UPDATING HERE
	private HTMLDocument gameDoc;
	private HTMLDocument statsDoc;

	// WE'LL USE THESE TO BUILD OUR HTML
	private final String START_TAG = "<";
	private final String END_TAG = ">";
	private final String SLASH = "/";
	private final String SPACE = " ";
	private final String EMPTY_TEXT = "";
	private final String NL = "\n";
	private final String QUOTE = "\"";
	private final String OPEN_PAREN = "(";
	private final String CLOSE_PAREN = ")";
	private final String COLON = ":";
	private final String EQUAL = "=";
	private final String COMMA = ",";
	private final String RGB = "rgb";

	// THESE ARE IDs IN THE GAME DISPLAY HTML FILE SO THAT WE
	// CAN GRAB THE NECESSARY ELEMENTS AND UPDATE THEM
	private final String GUESSES_SUBHEADER_ID = "guesses_subheader";
	private final String GUESSES_LIST_ID = "guesses_list";
	private final String WIN_DISPLAY_ID = "win_display";
	private final String LOSE_DISPLAY_ID = "lose_display";

	// THESE ARE IDs IN THE STATS HTML FILE SO THAT WE CAN
	// GRAB THE NECESSARY ELEMENTS AND UPDATE THEM
	private final String GAMES_PLAYED_ID = "games_played";
	private final String WINS_ID = "wins";
	private final String LOSSES_ID = "losses";
	private final String FEWEST_GUESSES_ID = "fewest_guesses";
	private final String FASTEST_WIN_ID = "fastest_win";
	private final String GAME_RESULTS_HEADER_ID = "game_results_header";
	private final String GAME_RESULTS_LIST_ID = "game_results_list";

	/**
	 * Accessor method for initializing the game doc, which displays while the
	 * game is being played and displays the guesses. Note that this must be
	 * done before this object can be used.
	 * 
	 * @param initGameDoc
	 *            The game document to be displayed while the game is being
	 *            played.
	 */
	public void setGameDoc(HTMLDocument initGameDoc) {
		gameDoc = initGameDoc;
	}
	
	/**
     * Accessor method for initializing the stats doc, which displays past
     * game results and statistics. Note that this must be done before this
     * object can be used.
     * 
     * @param initStatsDoc The stats document to be displayed on the
     * stats screen.
     */
	
	public void setStatsDoc(HTMLDocument initStatsDoc)  
    {
        statsDoc = initStatsDoc;  
    }

	/**
	 * This method lets us add a guess to the game page display without having
	 * to rebuild the entire page. We just add it to the HTML list of guesses
	 * made so far this game.
	 * 
	 * @param guess
	 *            Guess letter .
	 * @param newGuessMatch
	 *            The so-far-guessed word,with correct letters and underlines .
	 */
	public void addGuessToGamePage(String guess, String newGuessMatch) {
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		HangManGameData gameInProgress = ui.getGSM().getGameInProgress();

		try {
			// START BY LOADING THE LANGUAGE-DEPENDENT SUBHEADER
			String guessesSubheaderText = props
					.getProperty(HangManPropertyType.GAME_SUBHEADER_TEXT);
			Element h2 = gameDoc.getElement(GUESSES_SUBHEADER_ID);
			gameDoc.setInnerHTML(h2, guessesSubheaderText);

			// AND NOW FILL IN THE LIST. WE'RE GOING TO ADD
			// LIST ITEMS TO THE ORDERED LIST
			Element ol = gameDoc.getElement(GUESSES_LIST_ID);

			oldGuessMatch = newGuessMatch;
			String htmlText = buildGuessHTML(guess, newGuessMatch);
			gameDoc.insertBeforeEnd(ol, htmlText);
			// AND NOW ADD THE GAME OVER TEXT IF NEEDED
			Element winH2 = gameDoc.getElement(WIN_DISPLAY_ID);
			Element loseH2 = gameDoc.getElement(LOSE_DISPLAY_ID);

			System.out.println(gameInProgress.getWrongTimes());
			if (gameInProgress.isWordFound() && gameInProgress.getWrongTimes() < 6) {    //check added in case kept clicking 
				// PLAYER HAS WON
				String winText = props
						.getProperty(HangManPropertyType.WIN_DISPLAY_TEXT);
				gameDoc.setInnerHTML(winH2, winText);
			}

			if (gameInProgress.getWrongTimes() >= 6 && !gameInProgress.isWordFound()) {  //check
				// PLAYER HAS LOST
				System.out.println("GET LOST");

				String lossText = props
						.getProperty(HangManPropertyType.LOSE_DISPLAY_TEXT);
				gameDoc.setInnerHTML(loseH2, lossText);
			} else {
				// GAME IS STILL IN PROGRESS
				gameDoc.setInnerHTML(winH2, EMPTY_TEXT);
			}
		}
		// THE ERROR HANDLER WILL DEAL WITH ERRORS ASSOCIATED WITH BUILDING
		// THE HTML FOR THE PAGE, WHICH WOULD LIKELY BE DUE TO BAD DATA FROM
		// AN XML SETUP FILE
		catch (BadLocationException | IOException e) {
			HangManErrorHandler errorHandler = ui.getErrorHandler();
			errorHandler
					.processError(HangManPropertyType.INVALID_DOC_ERROR_TEXT);
		}
	}
	
	/**
     * This private helper method builds the HTML associated with a guess
     * as a list item, adding the proper colors as currently set by the
     * player.
     * 
     * @param guess Guess letter .
     * @param newGuessMatch The so-far-guessed word,with correct letters and underlines .
     * @return htmlText
     */
	
	private String buildGuessHTML(String guess, String guessMatch)
    {
        // FIRST THE OPENING LIST ITEM TAG WITH THE GUESS
        // AS ITS ID. THIS IS OK SINCE WE DON'T ALLOW
        // DUPLICATE GUESSES
      String htmlText = START_TAG + HTML.Tag.LI + SPACE + HTML.Attribute.ID + EQUAL + QUOTE + QUOTE + END_TAG;
              // NOW WE NEED TO FORMAT THE COLOR FOR EACH CHARACTER IN THE GUESS
        for (int i = 0; i < guessMatch.length(); i++)
        {
                // GET THE COLOR FOR EACH CHARACTER
            char c = guessMatch.charAt(i);
           
            
            // AND BUILD HTML TEXT TO COLOR CODE EACH CHARACTER SEPARATELY
            htmlText += SPACE + c + START_TAG + SLASH + HTML.Tag.SPAN + END_TAG;    
        }
     
        // NOW WE NEED TO FORMAT THE COLOR FOR EACH CHARACTER IN THE GUESS
        for (int i = 0; i < guess.length(); i++)
        {
              
        }
        // NOW ADD INFORMATION ABOUT THE NUMBER OF LETTERS IN THE
        // GUESS THAT ARE IN THE SECRET WORD
        htmlText +=START_TAG + SLASH + HTML.Tag.LI + END_TAG + NL;    
        return htmlText;
    }
	
	/**
     * When a new game starts the game page should not have a subheader
     * or display guesses or a win state, so all of that has to be cleared
     * out of the DOM at that time. This method does the work of clearing
     * out these nodes.
     */
    public void clearGamePage()
    {
        try
        {
            // WE'LL PUT THIS <br /> TAG IN PLACE OF THE CONTENT WE'RE REMOVING
            String lineBreak = START_TAG + HTML.Tag.BR + SPACE + SLASH + END_TAG;

            // CLEAR THE SUBHEADER
            Element h2 = gameDoc.getElement(GUESSES_SUBHEADER_ID);
            gameDoc.setInnerHTML(h2, lineBreak);

            // CLEAR THE GUESS LIST
            Element ol = gameDoc.getElement(GUESSES_LIST_ID);
            gameDoc.setInnerHTML(ol, lineBreak);

            // CLEAR THE WIN DISPLAY
            Element winH2 = gameDoc.getElement(WIN_DISPLAY_ID);
            gameDoc.setInnerHTML(winH2, lineBreak);
            
            //CLEAR THE LOSS DISPLAY
            Element loseH2 = gameDoc.getElement(LOSE_DISPLAY_ID);
            gameDoc.setInnerHTML(loseH2, lineBreak);
        } 
        // THE ERROR HANDLER WILL DEAL WITH ERRORS ASSOCIATED WITH BUILDING
        // THE HTML FOR THE PAGE, WHICH WOULD LIKELY BE DUE TO BAD DATA FROM
        // AN XML SETUP FILE
        catch (BadLocationException | IOException ex)
        {
            HangManErrorHandler errorHandler = ui.getErrorHandler();
            errorHandler.processError(HangManPropertyType.INVALID_DOC_ERROR_TEXT);
        }        
    }
    
   
    /**
     * This method adds the data from the completedGame argument
     * to the stats page, as well as loading all the newly computed
     * stats for all the games played.
     * 
     * @param completedGame Game whose summary will be added to
     * the stats page.
     */
        
    public void addGameResultToStatsPage(HangManGameData completedGame)
    {
        // GET THE GAME STATS
        HangManGameStateManager gsm = ui.getGSM();
        int gamesPlayed = gsm.getGamesPlayed();
        int wins = gsm.getWins();
        int losses = gsm.getLosses();
        HangManGameData fewestWin = gsm.getFewestGuessesWin();
        HangManGameData fastestWin = gsm.getFastestWin();
        
        try
        {
            // USE THE STATS TO UPDATE THE TABLE AT THE TOP OF THE PAGE
            Element gamePlayedElement = statsDoc.getElement(GAMES_PLAYED_ID);
            statsDoc.setInnerHTML(gamePlayedElement, EMPTY_TEXT + gamesPlayed);

            Element winsElement = statsDoc.getElement(WINS_ID);
            statsDoc.setInnerHTML(winsElement, EMPTY_TEXT + wins);
            
            Element lossesElement = statsDoc.getElement(LOSSES_ID);
            statsDoc.setInnerHTML(lossesElement, EMPTY_TEXT + losses);
            
            Element fewestWinElement = statsDoc.getElement(FEWEST_GUESSES_ID);
            if (fewestWin != null)
                statsDoc.setInnerHTML(fewestWinElement, fewestWin.toString());
            
            Element fastestWinElement = statsDoc.getElement(FASTEST_WIN_ID);
            if (fastestWin != null)
                statsDoc.setInnerHTML(fastestWinElement, fastestWin.toString());

            // ADD THE SUBHEADER
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String gameResultsText = props.getProperty(HangManPropertyType.GAME_RESULTS_TEXT);
            Element h2 = statsDoc.getElement(GAME_RESULTS_HEADER_ID);
            statsDoc.setInnerHTML(h2, gameResultsText);

            // AND NOW ADD THE LATEST GAME TO THE LIST
            Element ol = statsDoc.getElement(GAME_RESULTS_LIST_ID);
            String gameSummary = completedGame.toString();
            String htmlText = START_TAG + HTML.Tag.LI + END_TAG + gameSummary + START_TAG + SLASH + HTML.Tag.LI + END_TAG + NL;
            statsDoc.insertBeforeEnd(ol, htmlText);
        }
        // WE'LL LET THE ERROR HANDLER TAKE CARE OF ANY ERRORS,
        // WHICH COULD HAPPEN IF XML SETUP FILES ARE IMPROPERLY
        // FORMATTED
        catch(BadLocationException | IOException e)
        {
            HangManErrorHandler errorHandler = ui.getErrorHandler();
            errorHandler.processError(HangManPropertyType.INVALID_DOC_ERROR_TEXT);
        }
    }
}
