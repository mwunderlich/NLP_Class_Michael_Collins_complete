package com.martinwunderlich.nlp.collins.common;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Vocabulary {

	private HashMap<String, Integer> vocabulary = new HashMap<String, Integer>();

	public void addWord(String word) {
		if( this.vocabulary.containsKey(word) )
		{
			int oldCount = (int)this.vocabulary.get(word);
			int newCount = oldCount + 1;
			this.vocabulary.put( word, newCount );
		}
		else
		{
			this.vocabulary.put( word, 1 );			 
		}	
	}

	public int getSize() {
		return this.vocabulary.size();
	}

	public Set<String> getAllWords() {
		return this.vocabulary.keySet();
	}

	public int getCountForWord(String word) {
		int count = 0;
		if( this.vocabulary.containsKey(word) )
			count = (int)this.vocabulary.get(word);
		
		return count;
	}

	public void print() {
		for( String word : this.vocabulary.keySet() )
			System.out.println(word + " " + this.vocabulary.get(word));
	}

	public void addWordWithWordCount(String word, int count) {
		if( this.vocabulary.containsKey(word) )
		{
			int oldCount = (int)this.vocabulary.get(word);
			int newCount = oldCount + count;
			this.vocabulary.put( word, newCount );
		}
		else
		{
			this.vocabulary.put( word, count );			 
		}		
	}
}
