package uk.ac.ss2249.dictionary;

import java.io.IOException;

/**
 * Searches a dictionary from a sorted text file with words on
 * individual lines
 * 
 * @author Sam Snyder
 * @version 1.0 Released 30/12/2014
 */
public class DictionarySearch {
	
	SearchMethod mMethod;
	
	/**
	 * Creates a new dictionary with file path and the search method
	 * 
	 * @param file path
	 * @param search method
	 */
	public DictionarySearch(String fn, SearchMethodOption method){
		switch(method){
			case BINARY_SEARCH:
				mMethod = new BinarySearch(fn);
		}
	}
	
	/**
	 * Loads the dictionary by calling the search method function load
	 * 
	 * @throws IOException from loading file
	 */
	public void load() throws IOException{
		mMethod.load();
	}
	
	/**
	 * Checks if whole, exact word is in the dictionary
	 * 
	 * @param word to search for
	 * @return result of search
	 * @throws IOException from reading the dictionary
	 */
	public boolean checkForWord(String word) throws IOException{
		return mMethod.search(word, new SearchInterface(){

			@Override
			public int checkValue(byte[] word, byte[] v) {
				int i=0;
				while(true){
					if(i >= word.length && i >= v.length)
						return 0;
					else if(i >= word.length)
						return -1;
					else if(i >= v.length)
						return 1;
					if(word[i] != v[i])
						return word[i] - v[i];
					i++;
				}
			}
			
		});
	}
	
	/**
	 * Checks if one or more words in the dictionary has a certain prefix
	 * 
	 * @param the prefix to search for
	 * @return result of the search
	 * @throws IOException in reading the dictionary
	 */
	public boolean checkForWordPrefix(String prefix) throws IOException{
		return mMethod.search(prefix, new SearchInterface(){
			@Override
			public int checkValue(byte[] word, byte[] v) {
				for(int i=0; i<word.length; i++){
					if(i >= v.length - 1)
						return 1;
					if(word[i] != v[i])
						return word[i] - v[i];
				}
				return 0;
			}
		});
	}
}
