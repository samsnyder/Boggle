package uk.ac.ss2249.dictionary;

/**
 * The interface used for searching.
 * 
 * @author Sam Snyder
 * @version 1.0 Released 30/12/2014
 */
public interface SearchInterface{
	/**
	 * Compares two words contained in byte arrays for efficiency
	 * 
	 * @param word searching for
	 * @param current value to check against
	 * @return 0 if the search should end with true (not neccesarilly that they are equal)
	 */
	public int checkValue(byte[] word, byte[] v);
}