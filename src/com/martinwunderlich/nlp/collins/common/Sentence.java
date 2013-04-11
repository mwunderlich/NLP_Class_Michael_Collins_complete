package com.martinwunderlich.nlp.collins.common;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
	List<String> words = new ArrayList<String>();
	List<String> tags = new ArrayList<String>();
	int insertionPoint = 2;
	
	public Sentence() {
		this.words.add("*");
		this.words.add("*");
		this.words.add("STOP");
		
		this.tags.add("*");
		this.tags.add("*");
		this.tags.add("STOP");
	}
	
	public void addWord(String word) {
		this.words.add(insertionPoint, word);
		insertionPoint++;
	}
	
	public String getWord(int index) {
		// index is assumed to be 1-based
		return this.words.get(index + 1); // need to adjust for * * sequence at start of sentence
	}
	
	public String getWordMinusOne(int index) {
		return this.words.get(index);
	}
	
	public String getWordMinusTwo(int index) {
		return this.words.get(index - 1);
	}
	
	public int getWordCount() {
		return this.words.size();
	}
	
	public void tagWordAtPositionWithTag(int position, String tag) {
		this.tags.add(position + 1, tag);
	}
	
	public String getTag(int index) {
		return this.tags.get(index + 1);
	}
	
	public String getTagMinusOne(int index) {
		return this.tags.get(index );
	}
	
	public String getTagMinusTwo(int index) {
		return this.tags.get(index - 1);
	}
	
	public String toString() {
		String sentence = "";
		
		for( String word : this.words )
			sentence = sentence + " " + word;
		return sentence;
	}

	public void adjustLastFullStopToBeStopSymbol() {
		deleteWordAtPosition(this.words.size() - 2);
	}

	public void deleteWordAtPosition(int i) {
		this.words.remove(i);
	}

	public void setWordAtPosition(int i, String w) {
		this.words.set(i, w);
	}

	public int length() {
		return this.getWordCount() - 3; // without start symbols and stop symbol
	}
}
