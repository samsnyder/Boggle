package uk.ac.cam.ss2249.boggle;

import java.awt.Point;

/**
 * The UI interface that the engine uses
 * 
 * @author Sam Snyder
 * @version 1.0 Released 30/12/2014
 */
public interface UI {
	
	/**
	 * Visually uncheck the tile at a point
	 * 
	 * @param the point
	 */
	public void uncheckTileAtPoint(Point p);
	
	/**
	 * Visually check the tile at a point
	 * 
	 * @param the point
	 */
	public void checkTileAtPoint(Point p);
	
	/**
	 * Visually set a tile as used at a point
	 * 
	 * @param the point
	 */
	public void usedTileAtPoint(Point p);
	
	/**
	 * Show an error
	 * 
	 * @param error message
	 */
	public void onError(String message);
	
	/**
	 * Show an alert
	 * 
	 * @param message
	 */
	public void showAlert(String message);
	
	/**
	 * Update the found words
	 */
	public void updateWords();
}
