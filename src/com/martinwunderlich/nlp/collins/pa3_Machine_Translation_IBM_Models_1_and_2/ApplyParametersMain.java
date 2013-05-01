package com.martinwunderlich.nlp.collins.pa3_Machine_Translation_IBM_Models_1_and_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.martinwunderlich.nlp.collins.common.AbstractSentence;
import com.martinwunderlich.nlp.collins.common.Sentence;

public class ApplyParametersMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String englishCorpus = args[0];
		String spanishCorpus = args[1];
		String tParametersFile = args[2];
		String alignmentOutFile = args[3];
		
		ArrayList<SentencePair> pairs = new ArrayList<SentencePair>();
		
		HashMap<String, Double> tParameters = readParametersFromFile(tParametersFile);
		
		try {
			BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(englishCorpus), "UTF8"));
			BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(spanishCorpus), "UTF8"));
			
	        String lineEN = "";
	        String lineES = "";

			System.out.println("Reading in corpus files...");
			
	        while((lineEN = br1.readLine()) != null) {
	        	lineES = br2.readLine();
	            
	        	// Generate sentences pairs and add to list
	            AbstractSentence englishSentence = new Sentence(lineEN);
	            AbstractSentence spanishSentence = new Sentence(lineES);
	            
	            SentencePair pair = new SentencePair(englishSentence, spanishSentence);
	            pairs.add(pair);        		            
	        }
	        
	        br1.close();
	        br2.close();
	        
	        System.out.println("Reading in corpus files...DONE");			
	        System.out.println("Total number of sentences: " + pairs.size());
		}
		catch(Exception ex) {
			System.out.println("ERROR while reading file: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
		
		writeAlignmentFile(alignmentOutFile, pairs, tParameters);
	}

	private static HashMap<String, Double> readParametersFromFile(String tParametersFile) {
		HashMap<String, Double> tParams = new HashMap<String, Double>();
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(tParametersFile), "UTF8"));
			
	        String line = "";

	        System.out.println("Reading in parameters file...");
			
	        while((line = br.readLine()) != null) {
	        	String key = line.split(" = ")[0];
	        	Double value = Double.parseDouble(line.split(" = ")[1]);
	        	
	        	tParams.put(key, value);
	        }
	        
	        br.close();
	        
	        System.out.println("Reading in parameters file...DONE");			
	        System.out.println("Total number of parameters: " + tParams.size());	        
		}
		catch(Exception ex) {
			System.out.println("ERROR while reading file: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
		
		return tParams;
	}

	/**
	 * @param alignmentOutputFile
	 * @param alignedSentencePairs
	 * @param outputParameters TODO
	 */
	private static void writeAlignmentFile(String alignmentOutputFile, ArrayList<SentencePair> alignedSentencePairs, HashMap<String, Double> outputParameters) {
		try {
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(alignmentOutputFile), "UTF8"));			

			System.out.println("Writing out alignment file...");			

			int senCount = 1;
			
			for(SentencePair pair : alignedSentencePairs) {
				AbstractSentence senEN = pair.getSentenceOne();
				AbstractSentence senES = pair.getSentenceTwo();
				
				List<String> allWordsES = senES.getWords();
				List<String> allWordsEN = senEN.getWords();
				
				String key = "";
				Double maxParam = 0.0;
				int maxWordEN = 0;
				int indexEN = 0;
				int indexES = 1;
				
				
				for(String wordES : allWordsES) {
					
					maxParam = outputParameters.get("NULL+" + wordES); 
					maxWordEN = 0;
					indexEN = 1;
					
					for(String wordEN : allWordsEN) {
						key = wordEN + "+" + wordES; 
						if(outputParameters.get(key) > maxParam) {
							maxParam = outputParameters.get(key);
							maxWordEN = indexEN; 
						}
						indexEN++;
					}
					
					wr.write(senCount + " " + maxWordEN + " " + indexES);
					wr.newLine();
					wr.flush();
					
					indexES++;
				}				
				
				senCount++;
			}
			wr.close();
		}
		catch(Exception ex) {
			System.out.println("Error while writing dev alignment out file: " + ex.toString());
		}
        
		System.out.println("Writing out alignment file...DONE");
	}
}


