package uk.ac.ss2249.dictionary;

import java.io.IOException;

/**
 * The search method used in DictionarySearch (strategy design pattern)
 * 
 * @author Sam Snyder
 * @version 1.0 Released 30/12/2014
 */
abstract class SearchMethod {
	protected String mDicFileName;
	protected boolean mLoaded;
	
	/**
	 * Creates a new search method with the dictionaries file path
	 * 
	 * @param dictionary file path
	 */
	protected SearchMethod(String dicFileName){
		mDicFileName = dicFileName;
		mLoaded = false;
	}
	
	/**
	 * Gets whether the dictionary is loaded
	 * 
	 * @return if dictionary is loaded
	 */
	protected boolean isLoaded(){
		return mLoaded;
	}
	
	/**
	 * Sets the dictionaries file path
	 * 
	 * @param dicFileName
	 */
	protected void setDicFileName(String dicFileName){
		mDicFileName = dicFileName;
	}
	
	abstract protected void load() throws IOException;
	abstract protected boolean search(String word, SearchInterface s) throws IOException;
}