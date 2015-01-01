package uk.ac.cam.cl.dtg.sac92.oop.word_game.grid;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

import uk.ac.cam.ss2249.boggle.Constants;

/**
 * TileCollection Class
 *
 * This class represents a collection of tiles
 * 
 * @author Stephen Cummins
 * @version 1.0 Released 11/10/2005
 */
public class TileCollection {
	private List<Tile> collection;
	private static int multiplier;

	/**
	 * Default Constructor This will create a new random collection of tiles and
	 * shuffle them.
	 */
	public TileCollection() {
		multiplier = (int) Math.ceil(((double) Constants.ROWS*Constants.COLS) / 36.0);
		Tile[] tileArray = makeTileArray();
		shuffleTiles(tileArray);
		this.collection = new ArrayList<Tile>(Arrays.asList(tileArray));
	}

	/**
	 * Exposes the collection of tiles.
	 * 
	 * @return a List of tiles.
	 */
	public List<Tile> getCollection() {
		return collection;
	}

	/**
	 * Remove a tile
	 * 
	 * @return the tile removed.
	 */
	public Tile removeOne() {
		if (this.size() == 0) {
			throw new NoSuchElementException("TileCollection:removeOne(): collection is empty");
		} else {
			return (Tile) collection.remove(0);
		}
	}

	/**
	 * Size
	 * 
	 * @return the size of the collection
	 */
	public int size() {
		return collection.size();
	}

	private int totalTiles() {
		int total = 0;
		for (int tileNumber = 0; tileNumber < TileData.FREQUENCY.length; tileNumber++) {
			total = total + TileData.FREQUENCY[tileNumber]*multiplier;
		}
		return total;
	}

	private Tile[] makeTileArray() {
		Tile[] tiles = new Tile[totalTiles()];
		int instanceIndex = 0;

		for (int tileNumber = 0; tileNumber < TileData.LETTER.length; tileNumber++) {

			char tileLetter = TileData.LETTER[tileNumber];
			int tileValue = TileData.VALUE[tileNumber];

			for (int instances = 0; instances < TileData.FREQUENCY[tileNumber]*multiplier; instances++) {

				tiles[instanceIndex] = new Tile(tileLetter, tileValue);
				instanceIndex++;
			}
		}
		return tiles;
	}

	private void shuffleTiles(Tile[] tileArray) {
		Random random = new Random();

		int numberOfRuns = tileArray.length;

		while (numberOfRuns > 0) {
			numberOfRuns--;

			for (int index = 0; index < tileArray.length - 1; index++) {
				int destination = random.nextInt(tileArray.length);

				Tile tile = tileArray[destination];
				tileArray[destination] = tileArray[index];
				tileArray[index] = tile;
			}
		}
	}
}
