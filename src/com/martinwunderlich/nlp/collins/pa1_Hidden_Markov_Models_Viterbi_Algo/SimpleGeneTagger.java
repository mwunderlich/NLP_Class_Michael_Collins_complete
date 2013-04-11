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
import java.util.HashMap;
import java.util.List;


public class SimpleGeneTagger extends GeneTagger {

	private HashMap<String, Double> emissionParameters = null; 
	
	public SimpleGeneTagger(CountFileProcessor countFileprocessor, List<String> tagTypes, HashMap<String, Double> emissionParameters) {
		this.myCFP = countFileprocessor; 
		this.tagTypes = tagTypes; 
		this.emissionParameters = emissionParameters;
		this.vocab = myCFP.getVocabulary();
	}

	public void tag(String inFile, String outFile) {
		int lineCounter = 0;
        try {
			BufferedReader br = new BufferedReader(new FileReader(inFile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
	        String line = "";
	        String[] lineParts; 
	        
	        while((line = br.readLine()) != null) {
	             lineParts = line.split(" ");
	             String word = "";
	             String tag = "";
	             
	             if( lineParts[0].equals("") )
	             {
	            	 bw.newLine();
	             	 continue;
	             }
	             
	             word = lineParts[0];
	             
	             tag = getArgMaxEmissionParameterForWord(word);
	              
	             String newLine = word + " " + tag;
	              
	             bw.write(newLine);
	             bw.newLine();
	             lineCounter++;
	        }
	        
	        bw.close();
	        br.close();
		}
		catch(Exception ex) {
			System.out.println("ERROR at line " + lineCounter + " while tagging file: " + ex.getMessage());
		}
		
		System.out.println("Tagging file...DONE.");
	}

	String getArgMaxEmissionParameterForWord(String word) {
		String tag = "";
		Double freq = 0.0;
		Double emissionParameter = 0.0;
		
		for( String tagCandidate : tagTypes ) 
		{
			emissionParameter = this.emissionParameters.get(word + "+" + tagCandidate);
			if( emissionParameter == null ) // word-tag combination not seen in training
			{
				freq = this.emissionParameters.get("_RARE_" + "+" + tagCandidate);	
				tag = tagCandidate;
			}	
			else if( emissionParameter > freq )
			{
				freq = emissionParameter; 
				tag = tagCandidate;
			}
		}
		
		return tag;
	}

}
