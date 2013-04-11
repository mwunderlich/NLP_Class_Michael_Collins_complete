/*  Solutions to programming assignments from Michael Collins' (Columbia) NLP class 
	(http://class.coursera.org/nlangp-001). 
    
	Copyright (C) 2013  Martin Wunderlich

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */

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
