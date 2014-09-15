package HangMan.game;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * HangManGameData stores the data necessary for a single HangMan game. Note
 * that this class works in concert with the HangManGameStateManager, so all
 * instance variables have default (package-level) access.
 */
public class HangManGameData {
	// THE WORD THE PLAYER IS TRYING TO GUESS
	String secretWord;
	String newGuess = new String();
	int wrongTimes = 0;
	// WHEN FOUND, THE GAME IS OVER AND THE PLAYER WINS, BUT IF
	// THE PLAYER GIVES UP THIS WELL REMAIN FALSE
	boolean wordFound;

	// HISTORY OF ALL GUESSES THIS GAME
	ArrayList<String> guesses;

	// START AND END TIME WILL BE USED TO CALCULATE THE
	// TIME IT TAKES TO PLAY THIS GAME
	GregorianCalendar startTime;
	GregorianCalendar endTime;

	// THESE ARE USED FOR FORMATTING THE TIME OF GAME
	final long MILLIS_IN_A_SECOND = 1000;
	final long MILLIS_IN_A_MINUTE = 1000 * 60;
	final long MILLIS_IN_AN_HOUR = 1000 * 60 * 60;

	/*
	 * Construct this object when a game begins.
	 */
	public HangManGameData(String initSecretWord) {
		secretWord = initSecretWord;
		wordFound = false;
		guesses = new ArrayList();
		startTime = new GregorianCalendar();
		endTime = null;
	}

	// ACCESSOR METHODS

	/**
	 * Accessor method for this game's secret word.
	 * 
	 * @return The secret word for this game.
	 */
	public String getSecretWord() {
		return secretWord;
	}

	public int getWrongTimes() {
		return wrongTimes;
	}

	/**
	 * Accessor method for testing to see if the secred word has been found this
	 * game or not.
	 * 
	 * @return true if the secret word has been found this game, false
	 *         otherwise.
	 */
	public boolean isWordFound() {
		return wordFound;
	}

	/**
	 * Accessor method for getting the number of guesses the player made this
	 * game.
	 * 
	 * @return The number of guesses the player made this game.
	 */
	public int getNumGuesses() {
		return guesses.size();
	}

	/**
	 * Accessor method for testing to see if the user has made at least one
	 * guess.
	 * 
	 * @return true if the user has made at least one guess, false otherwise.
	 */
	public boolean hasGuessBeenMade() {
		return !guesses.isEmpty();
	}

	/**
	 * Accessor method for going through all the guesses this game.
	 * 
	 * @return An Iterator for going through each guess made this game.
	 */
	public Iterator<String> guessesIterator() {
		return guesses.iterator();
	}

	/**
	 * Gets the total time (in milliseconds) that this game took.
	 * 
	 * @return The time of the game in milliseconds.
	 */
	public long getTimeOfGame() {
		// IF THE GAME ISN'T OVER YET, THERE IS NO POINT IN CONTINUING
		if (endTime == null) {
			return -1;
		}

		// THE TIME OF THE GAME IS END-START
		long startTimeInMillis = startTime.getTimeInMillis();
		long endTimeInMillis = endTime.getTimeInMillis();

		// CALC THE DIFF AND RETURN IT
		long diff = endTimeInMillis - startTimeInMillis;
		return diff;
	}

	/*
	 * INITIALIZE THE GUESS WORD TO "_____" IF WE JUST BEGIN TO GUESS THE WORD
	 * 
	 * @return "______" or current guess state
	 */
	public String getNewGuess() {
		if (newGuess.isEmpty()) {
			for (int i = 0; i < secretWord.length(); i++) {
				newGuess += "_";
			}
		}
		return newGuess;
	}

	/**
	 * This method tests the guess argument to see if it is in the secret word.
	 * If it is, update the so-far-guessed string(with underlines) plus this
	 * method also update wrong guess times and check if secret word is found.
	 * 
	 * @param guess
	 *            Word to test to see if it's in this game's secret word.
	 * @param oldGuessMatch
	 *            the old so-far-guessed string
	 * @return guessMatch The new so-far-guessed string
	 */
	public String guess(String oldGuessMatch, String guess) {
		String guessMatch = new String();
		if (!isWordFound()) {
			// ADD THE MISSES GUESS TO ARRAYLIST
			guesses.add(guess);
			if (oldGuessMatch.isEmpty()) {
				for (int i = 0; i < secretWord.length(); i++) {
					oldGuessMatch += "_";
				}
			}
			for (int i = 0; i < secretWord.length(); i++) {

				if (secretWord.charAt(i) == guess.charAt(0)) {
					guessMatch += guess;
				} else if (oldGuessMatch.charAt(i) != '_') {
					guessMatch += oldGuessMatch.charAt(i);
				} else
					guessMatch += "_";
			}
			newGuess = guessMatch;
		}

		if (guessMatch.equals(secretWord)) {
			wordFound = true;
			endTime = new GregorianCalendar();
		}
		if (guessMatch.equals(oldGuessMatch)) {
			wrongTimes++;
		}
		return guessMatch;
	}

	/**
	 * This method tests the guess argument to see if this letter is in the
	 * secret word. If it is, add the guess letter to guessing result.
	 * 
	 * @param guess
	 *            Word to test to see if it's this game's secret word.
	 * @return whether the guess letter hit or not
	 */
	public boolean isRightGuess(String guess) {
		boolean rightGuess = false;
		for (int i = 0; i < secretWord.length(); i++) {

			if (secretWord.charAt(i) == guess.charAt(0)) {
				rightGuess = true;
				break;
			}

		}
		return rightGuess;
	}

	/**
	 * Called when a player quits a game before guessing the secret word, not
	 * that it simply calculates the end time. Also note that it does not change
	 * the wordFound variable, since that is used to keep track of wins and
	 * losses.
	 */
	public void giveUp() {
		endTime = new GregorianCalendar();
	}

	/**
	 * Builds and returns a texual summary of this game.
	 * 
	 * @return A textual summary of this game, including the secred word, the
	 *         time of the game, and a listing of all the guesses.
	 */
	@Override
	public String toString() {
		// CALCULATE GAME TIEM USING HOURS : MINUTES : SECONDS
		long timeInMillis = this.getTimeOfGame();
		long hours = timeInMillis / MILLIS_IN_AN_HOUR;
		timeInMillis -= hours * MILLIS_IN_AN_HOUR;
		long minutes = timeInMillis / MILLIS_IN_A_MINUTE;
		timeInMillis -= minutes * MILLIS_IN_A_MINUTE;
		long seconds = timeInMillis / MILLIS_IN_A_SECOND;

		// AND NOW BUILD THE STRING SUMMARY. START WITH THE SECRET WORD
		String text = secretWord;

		// THEN ADD THE TIME OF GAME SUMMARIZED IN PARENTHESES
		String minutesText = "" + minutes;
		if (minutes < 10)
			minutesText = "0" + minutesText;
		String secondsText = "" + seconds;
		if (seconds < 10)
			secondsText = "0" + secondsText;
		text += " (" + hours + ":" + minutesText + ":" + secondsText + ") - ";

		// THEN ADD THE GUESSES
		Iterator<String> guessIt = guesses.iterator();
		int counter = 0;
		while (guessIt.hasNext()) {
			String guess = guessIt.next();

			// WE'LL ADD A COMMA BEFORE EACH GUESS IN THE LIST,
			// EXCEPT THE FIRST ONE OF COURSE
			if (counter == 0)
				text += guess;
			else
				text += ", " + guess;
			counter++;
		}
		return text;
	}
}
