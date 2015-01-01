package uk.ac.cam.ss2249.boggle;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Grid;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Tile;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.TileCollection;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.TileData;
import uk.ac.cam.ss2249.boggle.ai.AI;
import uk.ac.ss2249.dictionary.DictionarySearch;
import uk.ac.ss2249.dictionary.SearchMethodOption;

/**
 * Engine is the game engine used to run the game logic
 * 
 * @author Sam Snyder
 * @version 1.0 Released 30/12/2014
 */
public class Engine {
	private UI mUi;
	private Grid grid;
	private DictionarySearch dic;
	private AI mAi;
	
	private List<Point> cTrail;
	private List<FoundWord> foundWords;

	/**
	 * Creates an instance of the Game and takes the UI class
	 */
	public Engine(UI ui) {
		mUi = ui;
		init();
	}

	/**
	 * Initialises the game by loading the dictionary
	 * 
	 */
	public void init(){
		try {
			dic = new DictionarySearch("/Users/sam/Downloads/words.txt", SearchMethodOption.BINARY_SEARCH);
			dic.load();
			reset();
		} catch (IOException e) {
			mUi.onError("Could not load dictionary.");
		}
		
	}
	
	/**
	 * Resets the GUI for a new game
	 * 
	 */
	public void reset(){
		TileCollection collection = new TileCollection();
		grid = new Grid(Constants.ROWS, Constants.COLS, collection);
		mAi = new AI(grid);
		if(cTrail == null)
			cTrail = new LinkedList<Point>();
		else{
			for(Point p : cTrail){
				mUi.uncheckTileAtPoint(p);
			}
			cTrail.clear();
		}
		if(foundWords == null)
			foundWords = new ArrayList<FoundWord>();
		else
			foundWords.clear();
		
	}
	
	/**
	 * Called from the UI when the UI has been loaded
	 * 
	 */
	public void guiReady(){
		mUi.updateWords();
	}
	
	/**
	 * Selects the best possible word from the AI
	 * 
	 */
	public void selectBestWord(){
		Point[] path = mAi.bestWord();
		if(path == null){
			mUi.showAlert("No word could be found!");
			return;
		}
		cTrail.clear();
		for(Point p : path){
			cTrail.add(p);
			checkTileAtPoint(p);
		}
	}
	
	/**
	 * Click a tile
	 * 
	 * @param the clicked tile
	 */
	public void tileClicked(Tile t){
		Point p = grid.positionOf(t);
		if(t.checkActive()){
			int index = cTrail.indexOf(p);
			if(index >= 0){
				while(cTrail.size() > index){
					uncheckTileAtPoint(cTrail.get(index));
					cTrail.remove(index);
				}
			}
		}else{
			if(cTrail.size() == 0 || grid.isAdjacent(p, cTrail.get(cTrail.size() - 1))){
				cTrail.add(p);
				checkTileAtPoint(p);
			}else{
				mUi.showAlert("You must select an adjacent cell!");
			}
			
		}
	}
	
	/**
	 * Uncheck a tile at a point on the board
	 * 
	 * @param the point
	 */
	private void uncheckTileAtPoint(Point p){
		grid.tileAt(p).active(false);
		mUi.uncheckTileAtPoint(p);
	}
	
	/**
	 * Check a tile at a point on the board
	 * 
	 * @param the point
	 */
	private void checkTileAtPoint(Point p){
		grid.tileAt(p).active(true);
		mUi.checkTileAtPoint(p);
	}
	
	/**
	 * Gets the engine's grid
	 * 
	 * @return the grid
	 */
	public Grid getGrid(){
		return grid;
	}
	
	/**
	 * See if the currently checked word is valid,
	 * if so, calls foundWord
	 * 
	 */
	public void tryForWord(){
		StringBuilder sb = new StringBuilder();
		for(Point p : cTrail){
			sb.append(grid.tileAt(p).letter());
		}
		String word = sb.toString();
		try {
			if(dic.checkForWord(word.toLowerCase())){
				foundWord(word);
			}else{
				mUi.showAlert(word + " is not a valid word.");
			}
		} catch (IOException e) {
			mUi.onError("Could not load dictionary for search.");
		}
	}
	
	/**
	 * Called when a valid word has been found
	 * Checks the word and adds to the score
	 * 
	 * @param the word
	 */
	public void foundWord(String word){
		int wordScore = 0;
		for(int i=0; i<word.length(); i++){
			wordScore += TileData.VALUE[charToIndex(word.charAt(i))];
		}
		wordScore *= word.length();
		foundWords.add(0, new FoundWord(word, wordScore));
		mUi.updateWords();
		for(Point p : cTrail){
			mUi.usedTileAtPoint(p);
		}
		cTrail.clear();
		StringBuilder sb = new StringBuilder();
		sb.append("You found '");
		sb.append(word);
		sb.append("'! This gives a score of ");
		sb.append(wordScore);
		mUi.showAlert(sb.toString());
	}
	
	/**
	 * Gets the current score
	 * 
	 * @return score
	 */
	public int getScore(){
		int s = 0;
		for(FoundWord w : foundWords)
			s += w.getScore();
		return s;
	}
	
	/**
	 * Gets the most recently found word
	 * 
	 * @return the word
	 */
	public FoundWord getRecentWord(){
		return foundWords.size() > 0 ? foundWords.get(0) : null;
	}
	
	/**
	 * Converts a letter to its index in the alphabet
	 * 
	 * @param the character
	 * @return the index
	 */
	static public int charToIndex(char l){
		return l - 65;
	}
	
	/**
	 * Converts an index in the alphabet to a letter
	 * 
	 * @param the index
	 * @return the character
	 */
	static public char indexToChar(int i){
		return (char) (65 + i);
	}
}