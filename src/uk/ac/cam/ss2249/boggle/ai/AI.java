package uk.ac.cam.ss2249.boggle.ai;

import java.awt.Point;
import java.io.IOException;

import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Grid;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Tile;
import uk.ac.cam.ss2249.boggle.Constants;
import uk.ac.ss2249.dictionary.DictionarySearch;
import uk.ac.ss2249.dictionary.SearchMethodOption;

/**
 * An AI to find the best word available on a board
 * 
 * @author Sam Snyder
 * @version 1.0 Released 30/12/2014
 */
public class AI {
	
	Grid mGrid;
	DictionarySearch mDic;
	AIRoute best = null;
	
	/**
	 * Creates an AI object from a boggle grid
	 * 
	 * @param the grid
	 */
	public AI(Grid g){
		mGrid = g;
		mDic = new DictionarySearch("/Users/sam/Downloads/words.txt", SearchMethodOption.BINARY_SEARCH);
		try {
			mDic.load();
		} catch (IOException e) {
			System.out.println("Could not load the dictionary.");
		}
	}
	
	/**
	 * Finds the best possible word on the grid
	 * 
	 * @return an array of the letter's points, in word order
	 */
	public Point[] bestWord(){
		boolean[] options = new boolean[Constants.COLS * Constants.ROWS];
		for(int x=0; x<Constants.COLS; x++)
			for(int y=0; y<Constants.ROWS; y++)
				options[y*Constants.ROWS+x] = mGrid.tileAt(x, y).checkActive();

		AIRoute bestRoute = AIRoute.emptyAiRoute();
		for(int x=0; x<Constants.COLS; x++){
			for(int y=0; y<Constants.ROWS; y++){
				if(!options[y*Constants.ROWS+x])
					bestRoute = findBestRoute(new Point(x, y), options.clone(), bestRoute);
			}
		}
		return bestRoute.getScore() > 0 ? bestRoute.getPath() : null;
	}
	
	/**
	 * Starts a search from a point
	 * 
	 * @param point to start from
	 * @param available options for grid tiles
	 * @param the current best route to compare to and replace if needed
	 * @return the best route, returned from findWords
	 */
	public AIRoute findBestRoute(Point p, boolean[] options, AIRoute cBestRoute){
		Tile tile = mGrid.tileAt(p);
		char c = toLower(tile.letter());
		options[pointToIndex(p)] = true;
		try {
			return findWords(AIRoute.routeWithChar(c, p), options, cBestRoute);
		} catch (IOException e) {
			System.out.println("Could not search dictionary for " + p);
		}
		return cBestRoute;
	}
	
	/**
	 * Recursive method to build a tree of options for routes
	 * Finds the best possible route from an existing route onwards
	 * 
	 * Changes cBestRoute if it finds a better route, if not return the
	 * best route it started with
	 * 
	 * @param the route to start from
	 * @param available grid tiles
	 * @param the current best route
	 * @return the best route after this search
	 * @throws IOException from dictionary search
	 */
	private AIRoute findWords(AIRoute origin, boolean[] options, AIRoute cBestRoute) throws IOException{
		for(int i=0; i<9; i++){
			if(i == 4) continue;
			Point lastPoint = origin.lastPoint();
			Point point = new Point(lastPoint.x + i%3 - 1, lastPoint.y + i/3 - 1);
			if(!xYOk(point) || options[pointToIndex(point)]) continue;
			char c = toLower(mGrid.tileAt(point).letter());
			if(!mDic.checkForWordPrefix(origin.getWord() + c)) continue;
			boolean[] newB = options.clone();
			newB[pointToIndex(point)] = true;
			AIRoute route = origin.appendLetter(c, point);
			if(route.getScore() > cBestRoute.getScore() && mDic.checkForWord(route.getWord()))
				cBestRoute = route;
			AIRoute bestChild = findWords(route, newB, cBestRoute);
			if(bestChild.getScore() > cBestRoute.getScore())
				cBestRoute = bestChild;
		}
		return cBestRoute;
	}
	
	/**
	 * Checks if point is in the grid
	 * 
	 * @param point to check
	 * @return whether point is on grid
	 */
	private static boolean xYOk(Point p){
		return p.x >= 0 && p.x < Constants.COLS && p.y >= 0 && p.y < Constants.ROWS;
	}
	
	/**
	 * Point to tile index, which is rows first then columns
	 * i.e:
	 * 
	 * 1 2 3
	 * 4 5 6
	 * 7 8 9
	 * 
	 * @param point
	 * @return index
	 */
	private static int pointToIndex(Point p){
		return Constants.ROWS*p.y + p.x;
	}
	
	/**
	 * Converts a character to lower case
	 * 
	 * @param upper case character
	 * @return lower case character
	 */
	private static char toLower(char c){
		return (char) (c + 32);
	}
	
}
