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

package com.martinwunderlich.nlp.collins.pa1_Hidden_Markov_Models_Viterbi_Algo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.martinwunderlich.nlp.collins.common.Vocabulary;

public class CountFileProcessor {
	private List<WordTag> allWordTags = new ArrayList<WordTag>();
	private HashMap<String, Integer> wordTagCounts = new HashMap<String, Integer>();
	private List<GramCount> allGramCounts = new ArrayList<GramCount>();
	private HashMap<String, Integer> gramCounts = new HashMap<String, Integer>();
	private Vocabulary vocabulary = new Vocabulary();
	private List<String> tagTypes = new ArrayList<String>();
	
	private CountFileProcessor() {
		// intentionally left blank
	}

	public CountFileProcessor(List<String> tagTypes2) {
		this.tagTypes = tagTypes2;
	}


	public void readCountResults(String filePath) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
	        String line = "";
	        String[] lineParts; 
	        
	        while((line = br.readLine()) != null) {
	             lineParts = line.split(" ");
	             int count = Integer.parseInt(lineParts[0]);
            	 
	             if( lineParts[1].equals("WORDTAG") ) {
	            	 extractWordtagData(lineParts, count);
	             }
	             else {
	            	 extractNGramData(lineParts, count);
	             }
	        }
	        
	        br.close();
		}
		catch(Exception ex) {
			System.out.println("ERROR while reading file: " + ex.getMessage());
		}
		
		showResultsFromFileProcessing();
	}

	/**
	 * 
	 */
	private void showResultsFromFileProcessing() {
		System.out.println("Vocab size: " + vocabulary.getSize());
		System.out.println("Number of gramCounts: " + allGramCounts.size());
		System.out.println("Number of wordTagCounts: " + allWordTags.size());
	}
	
	public void readCountResultsAndAdjustForRareWords(String filePath) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			//BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/martinwunderlich/Documents/Fortbildung/NLP_Michael_Collins_2013/Prog_Assignment_1/h1-p/gene_adjusted.counts"));
	        String line = "";
	        String[] lineParts; 
	        
	        while((line = br.readLine()) != null) {
	             lineParts = line.split(" ");
	             int count = Integer.parseInt(lineParts[0]);
            	 
//	             if(count >= 5)
//	            	 bw.write(line);
//	             else
//	            	 bw.write(lineParts[0] + " " + lineParts[1] + " " + lineParts[2] + " _RARE_");
//	             bw.newLine();
//	             
	             if( lineParts[1].equals("WORDTAG") ) {
	            	 extractWordtagDataAdjusted(lineParts, count);
	             }
	             else {
	            	 extractNGramData(lineParts, count);
	             }
	        }
	        
	        //bw.close();
	        br.close();
		}
		catch(Exception ex) {
			System.out.println("ERROR while reading file: " + ex.getMessage());
		}
		
		showResultsFromFileProcessing();
	}

	
	/**
	 * @param lineParts
	 * @param count
	 */
	private void extractNGramData(String[] lineParts, int count) {
		String gramType = lineParts[1];
		 String firstGram = lineParts[2];
		 String secondGram = "";
		 String thirdGram = "";
		 
		 if( lineParts.length > 3)
			 secondGram = lineParts[3];
		 if( lineParts.length > 4)
			 thirdGram = lineParts[4];
		 
		 GramCount gramCount = new GramCount(gramType, count, firstGram, secondGram, thirdGram);	     
		 allGramCounts.add(gramCount);
		 gramCounts.put(firstGram + "+" + secondGram + "+" + thirdGram, count);
	}


	/**
	 * @param lineParts
	 * @param count
	 */
	private void extractWordtagData(String[] lineParts, int count) {
		String word = lineParts[3];
		 String tag = lineParts[2];
		 WordTag wordTag = new WordTag(count, word, tag);
		 allWordTags.add(wordTag);
		 wordTagCounts.put(word + "+" + tag, count);
		 vocabulary.addWordWithWordCount(word, count);
	}


	/**
	 * @param lineParts
	 * @param count
	 */
	private void extractWordtagDataAdjusted(String[] lineParts, int count) {
		String word = "";
		if( count < 5 )
			word = "_RARE_";
		else 
			word = lineParts[3];
		 String tag = lineParts[2];
		 WordTag wordTag = new WordTag(count, word, tag);
		 if( ! allWordTags.contains(wordTag) )
				 allWordTags.add(wordTag);
		 
		 String wordTagLabel = word + "+" + tag;
		 if( wordTagCounts.containsKey(wordTagLabel) )
		 {
			 int oldCount = (int)wordTagCounts.get(wordTagLabel);
			 wordTagCounts.put( wordTagLabel, oldCount++ );
		 }
		 else
		 {
			 wordTagCounts.put( wordTagLabel, 1 );			 
		 }		
		 vocabulary.addWord(word);
	}
	
	public Vocabulary getVocabulary() {
		return this.vocabulary;
	}

	public List<WordTag> getAllWordTags() {
		return this.allWordTags;
	}

	public HashMap<String, Integer> getWordTagCounts() {
		return this.wordTagCounts;
	}

	public List<GramCount> getAllGramCounts() {
		return this.allGramCounts;
	}

	public HashMap<String, Integer> getGramCounts() {
		return this.gramCounts;
	}
}
