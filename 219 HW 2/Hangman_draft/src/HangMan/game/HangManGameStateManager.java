package HangMan.game;

import java.util.ArrayList;
import java.util.Iterator;

import HangMan.ui.HangManUI;

public class HangManGameStateManager {
	// THE GAME WILL ALWAYS BE IN
	// ONE OF THESE THREE STATES
	public enum HangManGameState {
		GAME_NOT_STARTED, GAME_IN_PROGRESS, GAME_OVER
	}

	// STORES THE CURRENT STATE OF THIS GAME
	private HangManGameState currentGameState;

	// WHEN THE STATE OF THE GAME CHANGES IT WILL NEED TO BE
	// REFLECTED IN THE USER INTERFACE, SO THIS CLASS NEEDS
	// A REFERENCE TO THE UI
	private HangManUI ui;

	// THIS IS THE DICTIONARY OF LEGAL GUESS WORDS
	private ArrayList<String> wordList;

	// THESE WORDS DON'T HAVE ANY REPEATING LETTERS AND
	// SO ARE CANDIDATES FOR SECRET WORDS
	private ArrayList<String> nonRepeatingWordList;

	// THIS IS THE GAME CURRENTLY BEING PLAYED
	private HangManGameData gameInProgress;

	// HOLDS ALL OF THE COMPLETED GAMES. NOTE THAT THE GAME
	// IN PROGRESS IS NOT ADDED UNTIL IT IS COMPLETED
	private ArrayList<HangManGameData> gamesHistory;

	private final String NEWLINE_DELIMITER = "\n";

	public HangManGameStateManager(HangManUI initUI) {
		ui = initUI;

		// WE HAVE NOT STARTED A GAME YET
		currentGameState = HangManGameState.GAME_NOT_STARTED;

		// NO GAMES HAVE BEEN PLAYED YET, BUT INITIALIZE
		// THE DATA STRCUTURE FOR PLACING COMPLETED GAMES
		gamesHistory = new ArrayList();

		// THE FIRST GAME HAS NOT BEEN STARTED YET
		gameInProgress = null;
	}

	// ACCESSOR METHODS

	/**
	 * Accessor method for getting the game currently being played.
	 * 
	 * @return The game currently being played.
	 */
	public HangManGameData getGameInProgress() {
		return gameInProgress;
	}

	/**
	 * Accessor method for getting the number of games that have been played.
	 * 
	 * @return The total number of games that have been played during this game
	 *         session.
	 */
	public int getGamesPlayed() {
		return gamesHistory.size();
	}

	/**
	 * Accessor method for getting all the games that have been completed.
	 * 
	 * @return An Iterator that allows one to go through all the games that have
	 *         been played so far.
	 */
	public Iterator<HangManGameData> getGamesHistoryIterator() {
		return gamesHistory.iterator();
	}

	/**
	 * Accessor method for testing to see if any games have been started yet.
	 * 
	 * @return true if at least one game has already been started during this
	 *         session, false otherwise.
	 */
	public boolean isGameNotStarted() {
		return currentGameState == HangManGameState.GAME_NOT_STARTED;
	}

	/**
	 * Accessor method for testing to see if the current game is over.
	 * 
	 * @return true if the game in progress has completed, false otherwise.
	 */
	public boolean isGameOver() {
		return currentGameState == HangManGameState.GAME_OVER;
	}

	/**
	 * Accessor method for testing to see if the current game is in progress.
	 * 
	 * @return true if a game is in progress, false otherwise.
	 */
	public boolean isGameInProgress() {
		return currentGameState == HangManGameState.GAME_IN_PROGRESS;
	}
	
	/**
     * Counts and returns the number of wins during this game session.
     * 
     * @return The number of games in that have been completed that
     * the player won.
     */
    public int getWins()
    {
        // ITERATE THROUGH ALL THE COMPLETED GAMES
        Iterator<HangManGameData> it = gamesHistory.iterator();
        int wins = 0;
        while(it.hasNext())
        {
            // GET THE NEXT GAME IN THE SEQUENCE
            HangManGameData game = it.next();
            
            // IF IT ENDED IN A WIN, INC THE COUNTER
            if (game.isWordFound())
                wins++;
        }
        return wins;
    }
    
    /**
     * Counts and returns the number of losses during this game session.
     * 
     * @return The number of games in that have been completed that
     * the player lost.
     */
    public int getLosses()
    {
        // ITERATE THROUGH ALL THE COMPLETED GAMES
        Iterator<HangManGameData> it = gamesHistory.iterator();
        int losses = 0;
        while(it.hasNext())
        {
            // GET THE NEXT GAME IN THE SEQUENCE
            HangManGameData game = it.next();

            // IF IT ENDED IN A LOSS, INC THE COUNTER
            if (!game.isWordFound())
                losses++;
        }
        return losses;
    }
    
    /**
     * Finds the completed game that the player won that
     * required the fewest guesses before correctly guessing
     * the secret word.
     * 
     * @return The completed game that the player won requiring
     * the fewest guesses.
     */
    public HangManGameData getFewestGuessesWin()
    {
        // IF NO GAMES HAVE BEEN PLAYED, THERE IS
        // NOTHING TO RETURN
        if (gamesHistory.isEmpty())
            return null;
        
        // NOTE THAT ALL THE GAMES PLAYED MAY BE LOSSES
        HangManGameData fewest = null;
        
        // GO THROUGH ALL THE GAMES THAT HAVE BEEN PLAYED
        Iterator<HangManGameData> it = gamesHistory.iterator();        
        while(it.hasNext())
        {
            // GET THE NEXT GAME IN THE SEQUENCE
            HangManGameData game = it.next();
            
            // WE ONLY CONSIDER GAMES THAT WERE WON
            if (game.isWordFound())
            {
                // IF IT'S THE FIRST WIN FOUND, START OUT
                // WITH IT AS THE FEWEST UNTIL WE FIND ONE BETTER
                if (fewest == null)
                    fewest = game;
                // OTHERWISE IF IT TOOK THE FEWEST GUESSES THEN
                // MAKE IT THE FEWEST           
                else if (game.getNumGuesses() < fewest.getNumGuesses())
                    fewest = game;
            }
        }
        // RETURN THE GAME THAT TOOK THE FEWEST GUESSES
        return fewest;        
    }
    
    /**
     * Finds the completed game that the player won that
     * required the least amount of time.
     * 
     * @return The completed game that the player won requiring
     * the least amount of time.
     */
    public HangManGameData getFastestWin()
    {
        // IF NO GAMES HAVE BEEN PLAYED, THERE IS
        // NOTHING TO RETURN
        if (gamesHistory.isEmpty())
            return null;

        // NOTE THAT ALL THE GAMES PLAYED MAY BE LOSSES
        HangManGameData fastest = null;

        // GO THROUGH ALL THE GAMES THAT HAVE BEEN PLAYED
        Iterator<HangManGameData> it = gamesHistory.iterator();
        while(it.hasNext())
        {
            // GET THE NEXT GAME IN THE SEQUENCE
            HangManGameData game = it.next();

            // WE ONLY CONSIDER GAMES THAT WERE WON
            if (game.isWordFound())
            {
                // IF IT'S THE FIRST WIN FOUND, START OUT
                // WITH IT AS THE FASTEST UNTIL WE FIND ONE BETTER
                if (fastest == null)
                    fastest = game;
                // OTHERWISE IF IT IS FASTER THEN
                // MAKE IT THE FASTEST           
                else if (game.getTimeOfGame() < fastest.getTimeOfGame())
                    fastest = game;
            }
        }
        // RETURN THE FASTEST GAME
        return fastest;        
    }

	/**
	 * Tests to see if the testWord has any repeating letters in the word.
	 * 
	 * @param testWord
	 *            Word to test for repeating letters.
	 * 
	 * @return true if the word has repeating letters, false otherwise. For
	 *         example, 'hello' would return true because it has repeating 'l'
	 *         letters. 'great' would return false because it has no repeating
	 *         letters.
	 */
	public boolean hasRepeatingLetters(String testWord) {
		for (int i = 0; i < testWord.length(); i++) {
			char testChar = testWord.charAt(i);
			for (int j = i + 1; j < testWord.length(); j++) {
				char testChar2 = testWord.charAt(j);
				if (testChar == testChar2)
					return true;
			}
		}
		return false;
	}

	/**
	 * Initializes the dictionary to be used to play the game.
	 * 
	 * @param initWordList
	 *            This String contains all the words to be loaded into the
	 *            dictionary, separated by newline characters.
	 */

	public void loadWordList(String initWordList) {
		// SEPARATE THE LOADED STRING INTO WORDS
		String[] words = initWordList.split(NEWLINE_DELIMITER);

		// WE'LL ACTUALLY USE 2 LISTS, ONE WITH ALL THE WORDS, WHICH
		// ARE ALL LEGAL FOR GUESSES
		wordList = new ArrayList();

		// AND ONE WITH ALL THE WORDS WITH NO REPEATING LETTERS,
		// WHICH MAKES THEM CANDIDATES TO BE SECRET WORDS
		nonRepeatingWordList = new ArrayList();

		// GO THROUGH ALL THE WORDS
		for (int i = 0; i < words.length; i++) {
			// WE'LL USE ALL CAPS
			String word = words[i].toUpperCase();

			// ADD THE WORDS TO THEIR CORRECT LISTS
			wordList.add(word);
			if (!this.hasRepeatingLetters(word)) {
				nonRepeatingWordList.add(word);
			}
		}
	}

	/**
	 * This method starts a new game, initializing all the necessary data for
	 * that new game as well as recording the current game (if it exists) in the
	 * games history data structure. It also lets the user interface know about
	 * this change of state such that it may reflect this change.
	 */
	public void startNewGame() {
		// IS THERE A GAME ALREADY UNDERWAY?
		// YES, SO END THAT GAME AS A LOSS
		if (!isGameNotStarted() && (!gamesHistory.contains(gameInProgress)))
			gamesHistory.add(gameInProgress);

		// IF THERE IS A GAME IN PROGRESS AND THE PLAYER HASN'T WON, THAT MEANS
		// THE PLAYER IS QUITTING, SO WE NEED TO SAVE THE GAME TO OUR HISTORY
		// DATA STRUCTURE. NOTE THAT IF THE PLAYER WON THE GAME, IT WOULD HAVE
		// ALREADY BEEN SAVED SINCE THERE WOULD BE NO GUARANTEE THE PLAYER WOULD
		// CHOOSE TO PLAY AGAIN
		if (isGameInProgress() && !gameInProgress.isWordFound()) {
			// QUIT THE GAME, WHICH SETS THE END TIME
			gameInProgress.giveUp();

			// MAKE SURE THE STATS PAGE KNOWS ABOUT THE COMPLETED GAME
			ui.getDocManager().addGameResultToStatsPage(gameInProgress);
		}

		// AND NOW MAKE A NEW GAME
		makeNewGame();

		// AND MAKE SURE THE UI REFLECTS A NEW GAME
		ui.resetUI();
	}
    
    /**
     * This method chooses a secret word and uses it to create
     * a new game, effectively starting it.
     */
    public void makeNewGame()
    {
        // FIRST PICK THE SECRET WORD
        int randomNum = (int)(Math.random() * nonRepeatingWordList.size());
        String secretWord = nonRepeatingWordList.get(randomNum);
        
        // THEN MAKE THE GAME WITH IT
        gameInProgress = new HangManGameData(secretWord);
        
        // THE GAME IS OFFICIALLY UNDERWAY
        currentGameState = HangManGameState.GAME_IN_PROGRESS;
    }
    
    /**
     * This method processes the guess letter, checking to make sure it's
     * in the secret word and then updating the game accordingly.
     * 
     * @param guess The letter that the player is guessing 
     * @param guessState  The current guess state
     */
    public void processGuess(String guessState,String guess) 
    {

        // RECORD THE GUESS

        String newGuessMatch=gameInProgress.guess(guessState,guess);
        int wrongGuessTimes=gameInProgress.getWrongTimes();
        
        ui.updateHangMan(wrongGuessTimes);
        
        //IS WRONG GUESS EXCEED SIX?
        if(wrongGuessTimes>=6){
             // CHANGE THE GAME STATE
            currentGameState = HangManGameState.GAME_OVER;
            
            // ADD THE COMPLETED GAME TO THE HISTORY
            gamesHistory.add(gameInProgress);
            
            // AND MAKE SURE THE STATS PAGE IS CURRENT
            ui.getDocManager().addGameResultToStatsPage(gameInProgress);
        }
        // IS IT THE WORD?
        if (gameInProgress.isWordFound())
        {
            // CHANGE THE GAME STATE
            currentGameState = HangManGameState.GAME_OVER;
            
            // ADD THE COMPLETED GAME TO THE HISTORY
            gamesHistory.add(gameInProgress);
            
            // AND MAKE SURE THE STATS PAGE IS CURRENT
            ui.getDocManager().addGameResultToStatsPage(gameInProgress);
        }

        // THE UI NEEDS TO RENDER WITH THE LATEST STATE INFO
        ui.getDocManager().addGuessToGamePage(guess,newGuessMatch);
        
        
    }
}
