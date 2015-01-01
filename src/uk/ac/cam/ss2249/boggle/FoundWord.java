package uk.ac.cam.ss2249.boggle;

/**
 * FoundWord represents a word that was found in the game,
 * and stores the word along with it's score
 * 
 * @author Sam Snyder
 * @version 1.0 Released 30/12/2014
 */
public class FoundWord {
	private String word;
	private int score;
	
	/**
	 * Creates a found word with a string and it's score
	 * 
	 * @param the word
	 * @param it's score
	 */
	public FoundWord(String w, int s){
		word = w;
		score = s;
	}

	/**
	 * Gets the word
	 * 
	 * @return
	 */
	public String getWord() {
		return word;
	}

	/**
	 * Gets the word's score
	 * 
	 * @return
	 */
	public int getScore() {
		return score;
	}
}
