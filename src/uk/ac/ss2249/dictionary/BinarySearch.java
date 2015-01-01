package uk.ac.ss2249.dictionary;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * A search method that uses Binary Search (strategy design pattern)
 * 
 * @author Sam Snyder
 * @version 1.0 Released 30/12/2014
 */
class BinarySearch extends SearchMethod{

	private int[] indexes;
	
	/**
	 * Constructor to call SearchMethod constructor
	 * 
	 * @param dictionary file path
	 */
	BinarySearch(String dicFileName) {
		super(dicFileName);
	}
	
	/**
	 * Resets the dictionary's file path, and sets the loaded flag to false
	 * so dictionary will lazily reload
	 * 
	 * @param new file path
	 */
	@Override
	protected void setDicFileName(String dicFileName){
		super.setDicFileName(dicFileName);
		mLoaded = false;
	}
	
	/**
	 * Loads the dictionary from the fileby loading the character
	 * index of the start of every word into indexes
	 * 
	 * @throws IOException from reading file
	 */
	@Override
	protected void load() throws IOException {
		List<Integer> indexList = new ArrayList<Integer>();
		indexList.add(0);
		FileReader fr = new FileReader(mDicFileName);
		int i = 0;
		while(true){
			int c = fr.read();
			if(c == '\n')
				indexList.add(i+1);
			else if(c < 0)
				break;
			i++;
		}
		fr.close();
		indexes = new int[indexList.size()];
		for(int j=0; j<indexList.size(); j++)
			indexes[j] = indexList.get(j).intValue();
		mLoaded = true;
	}

	/**
	 * Searches for a word using a SearchInterface
	 * 
	 * @params word to search
	 * @params SearchInterface to use while searching
	 * @return whether the word was found
	 */
	@Override
	protected boolean search(String word, SearchInterface s) throws IOException{
		if(!isLoaded())
			load();
		RandomAccessFile f = new RandomAccessFile(mDicFileName, "r");
		boolean found = searchSubset(word.getBytes(), 0, indexes.length-1, f, s);
		f.close();
		return found;
	}
	
	/**
	 * Searches a subset of the dictionary, recursively called.
	 * The size of the subset is halved every time, resulting in
	 * an O(log n) complexity
	 * 
	 * @param word to search for
	 * @param left index of subset
	 * @param right index of subset
	 * @param file to use in search
	 * @param SearchInterface to user
	 * @return whether word was found in current check or subset check
	 * @throws IOException in reading the dictionary file
	 */
	private boolean searchSubset(byte[] word, int left, int right, RandomAccessFile f, SearchInterface in) throws IOException{
		if(left > right)
			return false;
		int searchIndex = (left + right) / 2;
		long from = indexes[searchIndex];
		long to = searchIndex >= indexes.length - 1 ? f.length() : indexes[searchIndex+1] - 1;
		f.seek(indexes[searchIndex]);
		byte[] c = new byte[(int) (to - from)];
		f.read(c);
		int compare = in.checkValue(word, c);
		if(compare == 0)
			return true;
		if(compare > 0)
			return searchSubset(word, searchIndex+1, right, f, in);
		else
			return searchSubset(word, left, searchIndex-1, f, in);
	}
}
