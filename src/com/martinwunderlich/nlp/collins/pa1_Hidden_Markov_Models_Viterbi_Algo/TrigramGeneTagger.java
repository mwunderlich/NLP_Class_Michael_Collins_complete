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
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.TagAction;

import com.martinwunderlich.nlp.collins.common.*;

public class TrigramGeneTagger extends GeneTagger{

	List<Sentence> sentences = null; 
	List<ArrayList<String>> tagSequences = null;  
	HashMap<String, Double> emissionParameters = null;
	
	public TrigramGeneTagger(CountFileProcessor countFileprocessor, List<String> tagTypes, HashMap<String, Double> emissionParameters) {
		this.myCFP = countFileprocessor;
		this.tagTypes = tagTypes;
		this.sentences = new ArrayList<Sentence>();
		this.tagSequences = new ArrayList<ArrayList<String>>();
		this.emissionParameters = emissionParameters;
	}

	public void tag(String inFile, String outFile) {
		
		readInAllSentences(inFile);
		
		// Implementation of Viterbi algorithm
			//		Input: a sentence x1 : : : xn, parameters q(sju; v) and e(xjs).
			//		De�nitions: De�ne K to be the set of possible tags. De�ne K-1 = K0 = *, and Kk = K for k = 1: : : n.
			//		Initialization: Set PI(0;*;*) = 1.
			//		Algorithm:
			//		 	For k = 1...n,
			//				� For uRE Kk-1, v E Kk,
			//					PI(k, u, v) = max[w2Kk2] PI((k-1; w; u) X  q(v|w, u) X e(xk|v))
			//		 Return maxu2Kn1;v2Kn ((n; u; v)  q(STOPju; v))     
		
		for(Sentence s : this.sentences) {
			// Initialisation
			int n = s.length();
			DynamicProgrammingTableForDoubleValue PI = new DynamicProgrammingTableForDoubleValue();
			DynamicProgrammingTableForStringValue BP = new DynamicProgrammingTableForStringValue();
			PI.set(0, "*", "*", 1.0);
			for(String uTag : tagTypes)
				for(String vTag : tagTypes)
					PI.set(0, uTag, vTag, 0.0);
			
			
			
			
			// Do the math
			fillDynamicProgrammingTables(s, n, PI, BP);
			
			ArrayList<String> mostLikelyTagSequence = new ArrayList<String>();
			
			mostLikelyTagSequence = retrieveMostLikelyTagSequenceBasedOnBackPointers(n, PI, BP);
			
			tagSequences.add(mostLikelyTagSequence);

			// System.out.println(s.toString());
			// System.out.println(mostLikelyTagSequence.toString());
		}
		System.out.println("Tagging file...DONE.");
		
		writeResults(sentences, tagSequences, outFile);
		
		System.out.println("Writing results...DONE.");
		
	}

	/**
	 * @param s
	 * @param n
	 * @param PI
	 * @param BP
	 */
	private void fillDynamicProgrammingTables(Sentence s, int n, DynamicProgrammingTableForDoubleValue PI, DynamicProgrammingTableForStringValue BP) {
		for(int k = 1; k <= n; k++) {
			String[] u = getPossibleTagsForPosition(k-1);
			String[] v = getPossibleTagsForPosition(k);
			String[] w = getPossibleTagsForPosition(k-2);
			
			for(String vTag : v)
				for(String uTag : u) {
					Double max = 0.0;
					String maxWtag = "";
					
					for(String wTag : w) {
					Double q = getTransitionParameterQForTagSequence(wTag, uTag, vTag);
					String wordForVtag = s.getWord(k);
					Double e = getEmissionParameter(wordForVtag, vTag); 
			
					Double value = PI.get(k-1, wTag, uTag) * q * e;
					if( value >= max ) {
						max = value;
						maxWtag = wTag; 
						BP.set(k, uTag, vTag, maxWtag );													
						PI.set(k, uTag, vTag, max);	
					}
				}
			}
		}
	}

	/**
	 * @param n
	 * @param PI
	 * @param BP
	 * @return
	 */
	private ArrayList<String> retrieveMostLikelyTagSequenceBasedOnBackPointers(
			int n, DynamicProgrammingTableForDoubleValue PI,
			DynamicProgrammingTableForStringValue BP) {
		ArrayList<String> mostLikelyTagSequence = new ArrayList<String>();
		while(mostLikelyTagSequence.size() <= n) mostLikelyTagSequence.add(" ");
		String[] u = getPossibleTagsForPosition(n-1);
		String[] v = getPossibleTagsForPosition(n);
		Double max2 = 0.0;
		for(String uTag : u)
			for(String vTag : v) {
				Double q = getTransitionParameterQForTagSequence(uTag, vTag, "STOP");
				Double value = PI.get(n, uTag, vTag) * q;
				if( value >= max2) {
					max2 = value;
					mostLikelyTagSequence.set(n-1, uTag);
					mostLikelyTagSequence.set(n, vTag);
				}
			}
		
		// Build most likely tag sequence
		for(int k = n-2; k >= 1; k--) {
			String yk_1 = mostLikelyTagSequence.get(k+1);
			String yk_2 = mostLikelyTagSequence.get(k+2);
			String mostLikelyTag = BP.get(k+2, yk_1, yk_2);
			mostLikelyTagSequence.set(k, mostLikelyTag);				
		}
		return mostLikelyTagSequence;
	}

	private void writeResults(List<Sentence> senList,List<ArrayList<String>> tagList, String outFile) {
		int length = 0;
		int senLength = 0;
		int tagLength = 0;
		String line = "";
		
		length = senList.size();
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			for(int i = 0; i < length; i++) {
				Sentence s = senList.get(i);
				ArrayList<String> tags = tagList.get(i);
				senLength = s.length();
				tagLength = tags.size();
				for(int j = 0; j < senLength; j++) {
					line = s.getWord(j+1) + " " + tags.get(j + 1); 
					bw.write(line);
					bw.newLine();
				}
				//bw.write(". O");
				bw.newLine();
			}
			bw.close();	
		}
		catch(Exception ex) {
			System.out.println("ERROR while trying to write file: " + ex.getMessage());
		}
	}

	private Double getEmissionParameter(String word, String v) {
		String wordTagLabel = word + "+" + v;
		Double parameter = 0.0;
		if( this.emissionParameters.containsKey(wordTagLabel) ) {
			parameter = this.emissionParameters.get(wordTagLabel);			
		}
		else {
			wordTagLabel = "_RARE_" + "+" + v;
			parameter = this.emissionParameters.get(wordTagLabel);
		}
		
		return parameter;
	}

	private String[] getPossibleTagsForPosition(int i) {
		String[] tags = {"*"};
		if( i == -1 || i == 0)
			return tags;
		else {
			Object[] tagObjects = this.tagTypes.toArray();
			tags = Arrays.copyOf(tagObjects, tagObjects.length, String[].class);
		}
		
		return tags;
	}

	/**
	 * @param inFile
	 */
	private void readInAllSentences(String inFile) {
		int lineCounter = 0;
        try {
			BufferedReader br = new BufferedReader(new FileReader(inFile));
	        Sentence s = new Sentence();
			String line = "";
	        String[] lineParts; 
	        
	        while((line = br.readLine()) != null) {
	             lineParts = line.split(" ");
	             String word = "";
	             
	             word = lineParts[0];
	             
	             if( word.equals("") )
	             {
	            	 // s.adjustLastFullStopToBeStopSymbol();
	            	 // s.addWord("STOP");
	            	 sentences.add(s);
	            	 s = new Sentence();
	             	 continue;
	             }
	             else
	            	 s.addWord(word);
	             lineCounter++;
	        }
	        
	        System.out.println("Reading in file...DONE");
	        System.out.println("Sentences found: "+ this.sentences.size());
	        
//	        int i = 0;
//	        for( Sentence sen : this.sentences )
//	        	System.out.println(i++ + " " + sen.toString() );
	        br.close();
		}
		catch(Exception ex) {
			System.out.println("ERROR at line " + lineCounter + " while tagging file: " + ex.getMessage());
		}
	}

	
	public double getTransitionParameterQForTagSequence(String tag1, String tag2, String tag3) {
		int triCount = getTrigramCountForTagSequence(tag1, tag2, tag3);
		int biCount =  getBigramCountForTagSequence(tag1, tag2);
		
		double transitionParameterQ = new Double(triCount) / new Double(biCount);
		
		return transitionParameterQ;
	}
	
	public int getTrigramCountForTagSequence(String tag1, String tag2, String tag3) {
		int count = 0;

		String gramLabel = tag1 + "+" + tag2 + "+" + tag3;
		if( this.myCFP.getGramCounts().containsKey(gramLabel) )
			count = this.myCFP.getGramCounts().get(gramLabel);
		
		return count;
	}
	
	public int getBigramCountForTagSequence(String tag1, String tag2) {
		int count = 0;

		String gramLabel = tag1 + "+" + tag2 + "+";
		count = this.myCFP.getGramCounts().get(gramLabel);
		
		return count;
	}
}
