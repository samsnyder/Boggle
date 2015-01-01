package uk.ac.cam.ss2249.boggle.ai;

import java.awt.Point;

import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.TileData;

/**
 * Represents a path in the tree the AI builds
 * 
 * Immutable object
 * 
 * @author Sam Snyder
 * @version 1.0 Released 30/12/2014
 */
class AIRoute{
	private Point[] mPath;
	private String mWord;
	private int mScore;
	
	/**
	 * Creates empty AIRoute with no word and zero score
	 */
	private AIRoute(){
		mPath = new Point[0];
		mWord = "";
		mScore = 0;
	}
	
	/**
	 * Creates AIRoute from point array and word and calculates the score
	 * 
	 * @param path array
	 * @param word
	 */
	private AIRoute(Point[] path, String word){
		mPath = path;
		mWord = word;
		
		mScore = 0;
		for(char c : word.toCharArray())
			mScore += TileData.VALUE[c - 97];
		mScore *= word.length();
	}

	/**
	 * Gets the path array
	 * 
	 * @return path array
	 */
	protected Point[] getPath() {
		return mPath;
	}
	
	/**
	 * Gets the last point in the path
	 * 
	 * @return last point
	 */
	protected Point lastPoint(){
		return mPath[mPath.length - 1];
	}

	/**
	 * Gets the word
	 * 
	 * @return word
	 */
	protected String getWord() {
		return mWord;
	}
	
	/**
	 * Gets the score
	 * 
	 * @return score
	 */
	protected int getScore(){
		return mScore;
	}
	
	/**
	 * Creates a new AIRoute from a character at a point
	 * appended to current route
	 * 
	 * @param new character
	 * @param characters point
	 * @return new route
	 */
	protected AIRoute appendLetter(char c, Point p){
		Point[] newPath = new Point[mPath.length+1];
		for(int j=0; j<mPath.length; j++)
			newPath[j] = (Point) mPath[j];
		newPath[mPath.length] = p;
		return new AIRoute(newPath, mWord + c);
	}
	
	/**
	 * Creates a new route with a character at a point
	 * 
	 * @param character
	 * @param characters point
	 * @return the 1 length route
	 */
	protected static AIRoute routeWithChar(char c, Point p){
		return new AIRoute(new Point[]{p}, String.valueOf(c));
	}
	
	/**
	 * Creates an empty route with score 0
	 * 
	 * @return the empty route
	 */
	protected static AIRoute emptyAiRoute(){
		return new AIRoute();
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(mWord.toUpperCase());
		sb.append(" (");
		sb.append(mScore);
		sb.append(") (");
		for(int i=0; i<mPath.length; i++)
			sb.append(mPath[i] + (i==mPath.length-1 ? "" : ", "));
		return sb.toString();
	}
}
