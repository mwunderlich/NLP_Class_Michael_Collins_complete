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

package com.martinwunderlich.nlp.collins.pa3_Machine_Translation_IBM_Models_1_and_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.martinwunderlich.nlp.collins.common.AbstractSentence;
//import com.martinwunderlich.nlp.collins.common.DynamicProgrammingTableWithIntegerKeysForDoubleValue;
import com.martinwunderlich.nlp.collins.common.Sentence;
import com.martinwunderlich.nlp.collins.common.Vocabulary;

public class Main {
	
	private static ArrayList<SentencePair> alignedSentencePairs = new ArrayList<SentencePair>();
	private static Vocabulary englishVocab = new Vocabulary();
	private static Vocabulary spanishVocab = new Vocabulary();
	private static HashMap<String, Double> parameters = new HashMap<String, Double>();
	private static HashMap<String, Integer> numberOfSpanishWordsForEnglishWord = new HashMap<String, Integer>();
	
	private static HashMap<String, Double> expectedCountsEnglishSpanish= new HashMap<String, Double>();
	private static HashMap<String, Double> expectedCountsEnglish = new HashMap<String, Double>();
	
	private static int iterCount = 5;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String englishTrainingCorpus = args[0];
		String spanishTrainingCorpus = args[1]; 
		String englishDevCorpus = args[2];
		String spanishDevCorpus  = args[3];
		String englishTestCorpus = args[4];
		String spanishTestCorpus = args[5]; 
		String parameterOutputFile = args[6];
		String alignmentOutputFileDev = args[7];
		String alignmentOutputFileTest = args[8];
		
		
		// ==========================		
		// ======= PA3 part 1 =======
		// ==========================
//		
//		// Read in training corpus
//		readInTrainingCorpus(englishTrainingCorpus, spanishTrainingCorpus);
//	    
//		// Initialize uniform distribution
//		initializeUniformDistribution();
//		
//		// Initialize counts and run five iterations of EM algorithm
//		Set<String> wordsEN = englishVocab.getAllWords();
//		Set<String> wordsES = spanishVocab.getAllWords();
//		runInitialIterations(wordsEN, wordsES);
		
//		
//		// Save parameters to file
//		saveParametersToFile(parameterOutputFile);
//		
//		// Run model on dev sentences (target F1-Score of 0.420)
//		ArrayList<SentencePair> alignedSentencePairsDev = new ArrayList<SentencePair>();
//		alignedSentencePairsDev = runModel(englishDevCorpus, spanishDevCorpus);
//		writeAlignmentFile(alignmentOutputFileDev, alignedSentencePairsDev, parameters);			
//
//        
//		// Run model on test sentences
//		ArrayList<SentencePair> alignedSentencePairsTest = new ArrayList<SentencePair>();	
//		alignedSentencePairsTest = runModel(englishTestCorpus, spanishTestCorpus);
//		writeAlignmentFile(alignmentOutputFileTest, alignedSentencePairsTest, parameters);
		
		
		
		// ==========================
		// ======= PA3 part 2 =======
		// ==========================		
		int L = 0; // max length English sentences
		int M = 0; // max length Spanish sentences
		HashMap<String, Double> tParameters = new HashMap<String, Double>();
		HashMap<String, Double> cParametersLong = new HashMap<String, Double>();
		HashMap<String, Double> cParametersShort = new HashMap<String, Double>();
		HashMap<String, Double> qParameters = new HashMap<String, Double>();
				
		// Read in parameters from file (from Part 1)
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(parameterOutputFile), "UTF8"));
			
	        String line = "";

	        System.out.println("Reading in parameters file...");
			
	        while((line = br.readLine()) != null) {
	        	String key = line.split(" = ")[0];
	        	Double value = Double.parseDouble(line.split(" = ")[1]);
	        	
	        	tParameters.put(key, value);
	        }
	        
	        br.close();
	        
	        System.out.println("Reading in parameters file...DONE");			
	        System.out.println("Total number of parameters: " + tParameters.size());	        
		}
		catch(Exception ex) {
			System.out.println("ERROR while reading file: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
				
		// Read in training corpus
		ArrayList<SentencePair> trainingPairs = new ArrayList<SentencePair>();
		List<Integer> allEnglishSentenceLengthsTraining = new ArrayList<Integer>();
		List<Integer> allSpanishSentenceLengthsTraining = new ArrayList<Integer>();
		List<String> allPairsOfSentenceLengthsTraining = new ArrayList<String>();
				
		try {
			BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(englishTrainingCorpus), "UTF8"));
			BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(spanishTrainingCorpus), "UTF8"));
			
	        String lineEN = "";
	        String lineES = "";

	        int lengthEN = 0;
	        int lengthES = 0;
	        
			System.out.println("Reading in corpus files...");
			
	        while((lineEN = br1.readLine()) != null) {
	        	lineES = br2.readLine();
	            
	        	// Generate sentences pairs and add to list
	            AbstractSentence englishSentence = new Sentence(lineEN);
	            AbstractSentence spanishSentence = new Sentence(lineES);
	            
	            SentencePair pair = new SentencePair(englishSentence, spanishSentence);
	            trainingPairs.add(pair);        	
	            
	            // Check length of EN sentence
	            lengthEN = englishSentence.length();
	            allEnglishSentenceLengthsTraining.add(lengthEN);
	            if( lengthEN > L )
	            	L = lengthEN;

	            // Check length of ES sentence
	            lengthES = spanishSentence.length();
	            allSpanishSentenceLengthsTraining.add(lengthES);
	            if( lengthES > M )
	            	M = lengthES;
	            
	            allPairsOfSentenceLengthsTraining.add(lengthEN + "+" + lengthES);
	        }
	        
	        br1.close();
	        br2.close();
	        
	        System.out.println("Reading in corpus files...DONE");			
	        System.out.println("Total number of sentences: " + trainingPairs.size());
	        System.out.println("Max length English: " + L);
	        System.out.println("Max length Spanish: " + M);	        
		}
		catch(Exception ex) {
			System.out.println("ERROR while reading file: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
		
	
		// Initialize q parameters
		System.out.println("Initializing q parameters...");
		for(int l = 0; l <= L; l++)
			for(int m = 0; m <= M; m++) {
				if( ! allPairsOfSentenceLengthsTraining.contains(l + "+" + m) )
					continue;
				for(int i = 1; i <= m; i++)
					for(int j = 0; j <= l; j++) {
						String key = j + "+" + i + "+" + l + "+" + m;
						Double value = 1.0 / new Double(l + 1);
						qParameters.put(key, value);
					}
			}
		System.out.println("Initializing q parameters...DONE");
		
		
		// Run EM algo in iterations 
		
		String wordEN = "";
		String wordES = "";
		HashMap<String, Double> countsEnglishSpanish = new HashMap<String, Double>();
		HashMap<String, Double> countsEnglish = new HashMap<String, Double>();
		String key = "";
		
//		printParameters(tParameters, cParametersLong, cParametersShort, qParameters, countsEnglishSpanish, countsEnglish);
		
		
		
		int numberOfSentencePairs = trainingPairs.size();
		
		for(int it = 1; it <= iterCount; it++) {
			System.out.println();
			System.out.println("Iteration " + it);
			System.out.println("t(guerra|war) " + tParameters.get("war+guerra"));
			
			// Set all counts c(...) = 0 for following iterations (it's built in for the first iteration, because at that stage we don't know the keys, yet)
			
			if( it > 1 )
				resetCountParameters(countsEnglishSpanish, countsEnglish, cParametersLong, cParametersShort);
			
			for(int k = 1; k <= numberOfSentencePairs; k++) {
//				System.out.println(it + " - " + k);
				SentencePair pair = trainingPairs.get(k - 1);
				AbstractSentence englishSentence = pair.getSentenceOne();
				AbstractSentence spanishSentence = pair.getSentenceTwo();
				
				int englishSentenceLength = englishSentence.length();
				int spanishSentenceLength = spanishSentence.length();
				
				for(int i = 1; i <= spanishSentenceLength; i++) {
					for(int j = 0; j <= englishSentenceLength; j++) {
						if(j == 0)
							wordEN = "NULL";
						else
							wordEN = englishSentence.getWord(j); // e jk
						
						wordES = spanishSentence.getWord(i); // f ik
						int l = englishSentenceLength;
						int m = spanishSentenceLength;
						
						Double delta = getDelta(k, i, j, l, m, wordEN, wordES, qParameters, tParameters, englishSentence.getWords());
						
						// Update c (count) parameters
						key = wordEN + "+" + wordES;
						
						Double oldCount =  countsEnglishSpanish.containsKey(key) ? countsEnglishSpanish.get(key) : 0.0;
						Double newCount = oldCount + delta;
						countsEnglishSpanish.put(key, newCount);
						
						oldCount = countsEnglish.containsKey(wordEN) ? countsEnglish.get(wordEN) : 0.0;
						newCount = oldCount + delta; 
						countsEnglish.put(wordEN, newCount);
						
						// Update c (prob) parameters 
						String key1 = j + "+" + i + "+" + l + "+" + m;
						String key2 = i + "+" + l + "+" + m;
					
						oldCount = cParametersLong.containsKey(key1) ? cParametersLong.get(key1) : 0.0;
						newCount = oldCount + delta; 
						cParametersLong.put(key1, newCount );
						
						oldCount = cParametersShort.containsKey(key2) ? cParametersShort.get(key2) : 0.0;
						newCount = oldCount + delta; 
						cParametersShort.put(key2, newCount);
					}						
				}						
			}
			
			System.out.println("Iteration...DONE");
			
			// Update t and q parameters
			System.out.println("Updating parameters...");
			for(String tKey : countsEnglishSpanish.keySet()) {
				String en = ( tKey.split("\\+") )[0];
				tParameters.put(tKey, countsEnglishSpanish.get(tKey) / countsEnglish.get(en));
			}
			
			for(String qKey : qParameters.keySet()) {
				String dKey = qKey.substring(qKey.indexOf("+") + 1);
				qParameters.put(qKey, cParametersLong.get(qKey) / cParametersShort.get(dKey));
			}

			System.out.println("Updating parameters...DONE");
		}			
		System.out.println("All iterations...DONE");		
//		printParameters(tParameters, cParametersLong, cParametersShort, qParameters, countsEnglishSpanish, countsEnglish);						
		
		
		// Save t parameters to file
		System.out.println("Saving t parameters to file...");
		try {
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\Administrator\\Downloads\\parameters2.out"), "UTF8"));			
			
			for(String tKey : tParameters.keySet()) {
				Double parameterValue = tParameters.get(tKey);
				
				wr.write(tKey + " = " + parameterValue);
				wr.newLine();
				wr.flush();
			}
			wr.close();
		}
		catch(Exception ex) {
			System.out.println("Error while trying to write parameter file: " + ex.toString());
		}

		System.out.println("Saving t parameters to file...DONE");
		
		
		// Read in dev corpus
		ArrayList<SentencePair> devPairs = new ArrayList<SentencePair>();
			
		try {
			BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(englishTestCorpus), "UTF8"));
			BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(spanishTestCorpus), "UTF8"));
			
	        String lineEN = "";
	        String lineES = "";

			System.out.println("Reading in dev corpus files...");
			
	        while((lineEN = br1.readLine()) != null) {
	        	lineES = br2.readLine();
	            
	        	// Generate sentences pairs and add to list
	            AbstractSentence englishSentence = new Sentence(lineEN);
	            AbstractSentence spanishSentence = new Sentence(lineES);
	            
	            SentencePair pair = new SentencePair(englishSentence, spanishSentence);
	            devPairs.add(pair);        		            
	        }
	        
	        br1.close();
	        br2.close();
	        
	        System.out.println("Reading in dev corpus files...DONE");			
	        System.out.println("Total number of sentences: " + devPairs.size());
		}
		catch(Exception ex) {
			System.out.println("ERROR while reading file: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
		
		writeAlignmentFile(alignmentOutputFileTest, devPairs, tParameters);
	}


	private static void resetCountParameters(
			HashMap<String, Double> countsEnglishSpanish,
			HashMap<String, Double> countsEnglish,
			HashMap<String, Double> cParametersLong,
			HashMap<String, Double> cParametersShort) {
		
		for(String key : countsEnglishSpanish.keySet())
			countsEnglishSpanish.put(key, 0.0);
		
		for(String key : countsEnglish.keySet())
			countsEnglish.put(key, 0.0);
		
		for(String key : cParametersLong.keySet())
			cParametersLong.put(key, 0.0);
		
		for(String key : cParametersShort.keySet())
			cParametersShort.put(key, 0.0);		
	}


	/**
	 * @param tParameters
	 * @param cParametersLong
	 * @param cParametersShort
	 * @param qParameters
	 */
	private static void printParameters(HashMap<String, Double> tParameters,
			HashMap<String, Double> cParametersLong,
			HashMap<String, Double> cParametersShort,
			HashMap<String, Double> qParameters, 
			HashMap<String, Double> countsEnglishSpanish,
			HashMap<String, Double> countsEnglish 
			) {
		System.out.println("c1");
		for(String key2 : cParametersLong.keySet())
			System.out.println(key2 + "\t" + cParametersLong.get(key2));
		System.out.println("c2");
		for(String key3 : cParametersShort.keySet())
			System.out.println(key3 + "\t" + cParametersShort.get(key3));
		System.out.println("c EnEs");
		for(String key4 : countsEnglishSpanish.keySet())
			System.out.println(key4 + "\t" + countsEnglishSpanish.get(key4));
		System.out.println("c En");
		for(String key4 : countsEnglish.keySet())
			System.out.println(key4 + "\t" + countsEnglish.get(key4));
		System.out.println("t");
		for(String key4 : tParameters.keySet())
			System.out.println(key4 + "\t" + tParameters.get(key4));
		System.out.println("q:");
		for(String key1 : qParameters.keySet())
			System.out.println(key1 + "\t" + qParameters.get(key1));
	}				
	

	private static Double getDelta(int k, int i, int j, int l, int m, String wordEN, String wordES, HashMap<String, Double> qParameters, HashMap<String, Double> tParameters, List<String> wordsEN) {
		Double delta = 0.0;
		
		//  Add NULL word
		wordsEN.add(0, "NULL");
		
		// Calculate enumerator
		String key = j + "+" + i + "+" + l + "+" + m;
		Double q = qParameters.get(key);
		Double t = tParameters.get(wordEN + "+" + wordES);
		Double enumerator = q*t;
		
		// Calculate denominator
		double sum = 0.0;
		
		for(int jj = 0; jj <= l; jj++) {
			key = jj + "+" + i + "+" + l + "+" + m;
			q = qParameters.get(key);
			t = tParameters.get(wordsEN.get(jj) + "+" + wordES);
			sum = sum + (q*t); 
		}
		
		delta = enumerator / sum;
		
		return delta;
	}


	/**
	 * @param wordsEN
	 * @param wordsES
	 */
	private static void runInitialIterations(Set<String> wordsEN,
			Set<String> wordsES) {
		
		for( int iteration = 1; iteration <= iterCount; iteration++) {
			System.out.println();
			System.out.println("Iteration " + iteration);
			initializeExpectedCounts(wordsEN, wordsES);
			System.out.println("Runnning algo...");
			runEMalgorithm();
		
			System.out.println("Runnning algo...DONE");
			
			// Create alignments for dev sentences
			System.out.println("Updating parameters...");

			updateParameters(wordsEN, wordsES);

			System.out.println("Updating parameters...DONE");
		}
		System.out.println("All iterations done!");
	}


	/**
	 * @param parameterOutputFile
	 */
	private static void saveParametersToFile(String parameterOutputFile) {
		System.out.println("Saving parameters to file...");
		try {
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(parameterOutputFile), "UTF8"));			
			
			for(String key : parameters.keySet()) {
				Double parameterValue = parameters.get(key);
				
				wr.write(key + " = " + parameterValue);
				wr.newLine();
				wr.flush();
			}
			wr.close();
		}
		catch(Exception ex) {
			System.out.println("Error while trying to write parameter file: " + ex.toString());
		}

		System.out.println("Saving parameters to file...DONE");
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


	/**
	 * @param englishCorpus
	 * @param spanishCorpus
	 * @param alignedSentencePairsDev
	 * @return 
	 */
	private static ArrayList<SentencePair> runModel(String englishCorpus, String spanishCorpus) {
		ArrayList<SentencePair> alignedSentencePairs = new ArrayList<SentencePair>();
		
		try {
			BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(englishCorpus), "UTF8"));
			BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(spanishCorpus), "UTF8"));
			
	        String lineEN = "";
	        String lineES = "";

			System.out.println("Reading in corpus files...");
			
	        while((lineEN = br1.readLine()) != null) {
	        	lineES = br2.readLine();
	            
	            AbstractSentence englishSentence = new Sentence(lineEN);
	            AbstractSentence spanishSentence = new Sentence(lineES);
	            
	            SentencePair pair = new SentencePair(englishSentence, spanishSentence);
	            alignedSentencePairs.add(pair);        	
	        }
	        
	        br1.close();
	        br2.close();
	        
	        System.out.println("Reading in corpus files...DONE");			
		}
		catch(Exception ex) {
			System.out.println("ERROR while reading file: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
		return alignedSentencePairs;
	}


	/**
	 * @param wordsEN
	 * @param wordsES
	 */
	private static void updateParameters(Set<String> wordsEN, Set<String> wordsES) {
		Double param = 0.0;
		
		for(String wordES : wordsES) {
			String key = "NULL+" + wordES;
			if( expectedCountsEnglishSpanish.containsKey(key) )
				param = new Double( new Double(expectedCountsEnglishSpanish.get(key)) / new Double(expectedCountsEnglish.get("NULL")) );
			else
				param = 0.0;
			parameters.put(key, param);
		}

		for(String key : parameters.keySet()) {
			String wordEN = (key.split("\\+"))[0];
			if( expectedCountsEnglishSpanish.containsKey(key) )
				param = new Double( new Double(expectedCountsEnglishSpanish.get(key)) / new Double(expectedCountsEnglish.get(wordEN)) );
			else
				param = 0.0;
			parameters.put(key, param);
		}
	}


	/**
	 * 
	 */
	private static void runEMalgorithm() {
		for( int k = 1; k <= alignedSentencePairs.size(); k++ ) {
			SentencePair pair = alignedSentencePairs.get(k - 1);
			AbstractSentence senEN = pair.getSentenceOne();
			AbstractSentence senES = pair.getSentenceTwo();
			int l = senEN.length();
			int m = senES.length();
			
			for( int i=1; i <= m; i++){ 			// Loop over all Spanish words in sentence. 
				String wordES = senES.getWord(i);
				Double deltaSum = getDeltaSum(senEN, wordES);
				
				for( int j=0; j <= l; j++) {		// Loop over all English words in sentence; index = 0 means the NULL word.
					String wordEN = (j == 0 ? "NULL" : senEN.getWord(j));
					String key = wordEN + "+" + wordES;
					
					Double delta = parameters.get(key) / deltaSum;
					
					Double expectedCountENES = expectedCountsEnglishSpanish.get(key) + delta;
					Double expectedCountEN = expectedCountsEnglish.get(wordEN) + delta;
					
					expectedCountsEnglishSpanish.put(key, expectedCountENES);
					expectedCountsEnglish.put(wordEN, expectedCountEN);
				}	
			}
		}
	}

//
//	private static void printExpectedCounts() {
//		for(String key : expectedCountsEnglishSpanish.keySet()) {
//			if( expectedCountsEnglishSpanish.get(key) > 0.0 ) {
//				String en = (key.split("\\+"))[0];
//				String es = (key.split("\\+"))[1];
//				System.out.println("c(" + en + ", " + es + ") = " + expectedCountsEnglishSpanish.get(key));
//			}
//		}
//		for(String key : expectedCountsEnglish.keySet()) {
//			if( expectedCountsEnglish.get(key) > 0.0 )
//				System.out.println("c(" + key  + ") = " + expectedCountsEnglish.get(key));
//		}
//	}


	/**
	 * @param wordsEN
	 * @param wordsES
	 */
	private static void initializeExpectedCounts(Set<String> wordsEN,
			Set<String> wordsES) {
		System.out.println("Initializing counts...");
		
		expectedCountsEnglish.put("NULL", 0.0);

		String key = "";
		
		for(String wordES : wordsES) {
			key = "NULL+" + wordES;
			expectedCountsEnglishSpanish.put(key, 0.0); 
		}
		
		for(String wordEN : wordsEN) {
			expectedCountsEnglish.put(wordEN, 0.0);
		}		
		for(String key2 : parameters.keySet())
			expectedCountsEnglishSpanish.put(key2, 0.0);
		System.out.println("Initializing counts...DONE");
	}


	private static Double getDeltaSum(AbstractSentence senEN, String wordES) {
		Double sum = 0.0;
		
		sum = parameters.get("NULL+" + wordES);
		
		for(String wordEN : senEN.getWords()) {
			String key = wordEN + "+" + wordES;
			sum += parameters.get(key);				
		}
		
		return sum;
	}

//
//	private static void printParameters() {
//		
//		for(String key : parameters.keySet())
//			if( parameters.get(key) > 0.0 ) {
//				String en = (key.split("\\+"))[0];
//				String es = (key.split("\\+"))[1];
//		
//				System.out.println("t(" + es + " | " + en + ") = " + parameters.get(key));
//			}
//		}


	/**
	 * 
	 */
	private static void initializeUniformDistribution() {
		System.out.println("Finalizing parameter init...");
		
		Set<String> wordsEN = englishVocab.getAllWords();
		numberOfSpanishWordsForEnglishWord.put("NULL", spanishVocab.getSize());
		
		for(String wordEN : wordsEN) {
		   Set<String> uniqueSpanishWords = new HashSet<String>();
		   for(SentencePair pair : alignedSentencePairs) {
		      if( pair.getSentenceOne().containsWord(wordEN) ){
		    	  uniqueSpanishWords.addAll( pair.getSentenceTwo().getUniqueWords() );
		      }
		   }
		   int numberOfUniqueSpanishWords = uniqueSpanishWords.size();
		   numberOfSpanishWordsForEnglishWord.put(wordEN, numberOfUniqueSpanishWords);
		}
		
		for(String key : parameters.keySet()) {
			String wordEN = key.substring(0, key.indexOf("+"));
			int count = numberOfSpanishWordsForEnglishWord.get(wordEN);
			parameters.put(key, new Double(1.0/count));
		}
		
		System.out.println("Finalizing parameter init...DONE");
	}


	/**
	 * @param englishTrainingCorpus
	 * @param spanishTrainingCorpus
	 */
	private static void readInTrainingCorpus(String englishTrainingCorpus,
			String spanishTrainingCorpus) {
		try {
			BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(englishTrainingCorpus), "UTF8"));
			BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(spanishTrainingCorpus), "UTF8"));
			
	        String lineEN = "";
	        String lineES = "";

			System.out.println("Reading in corpus files...");
			
	        while((lineEN = br1.readLine()) != null) {
	        	lineES = br2.readLine();
	            
	            AbstractSentence englishSentence = new Sentence(lineEN);
	            AbstractSentence spanishSentence = new Sentence(lineES);
	            
	            SentencePair pair = new SentencePair(englishSentence, spanishSentence);
	            alignedSentencePairs.add(pair);
	            
	            englishVocab.addSentence(englishSentence);
	            spanishVocab.addSentence(spanishSentence);
	            
	            initParameterKeys(englishSentence, spanishSentence);	        	
	        }
	        
	        br1.close();
	        br2.close();
	        System.out.println("Reading in corpus files...DONE");			
		}
		catch(Exception ex) {
			System.out.println("ERROR while reading file: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}

		
		System.out.println("Corpus files read successfully!");
		System.out.println("Found sentence pairs: " + alignedSentencePairs.size());
		System.out.println("English vocabulary size (number of types): " + englishVocab.getSize());		
		System.out.println("Total English word count (number of tokens):" + englishVocab.getTotalWordCount());
		System.out.println("Spanish vocabulary size (number of types): " + spanishVocab.getSize());		
		System.out.println("Total Spanish word count (number of tokens):" + spanishVocab.getTotalWordCount());
		System.out.println("Number of parameters: " + parameters.size());
	}


	/**
	 * @param englishSentence
	 * @param spanishSentence
	 */
	private static void initParameterKeys(AbstractSentence englishSentence, AbstractSentence spanishSentence) {
		List<String> wordsEN = englishSentence.getWords(); 
		List<String> wordsES = spanishSentence.getWords(); 
		
		for( String wordES : wordsES ) {
			String key = "NULL+" + wordES;
			if( ! parameters.containsKey(key) )
				parameters.put(key, 0.0);	            	
		}
		
		for( String wordEN : wordsEN ) 
			for( String wordES : wordsES ) {
				String key = wordEN + "+" + wordES;
				if( ! parameters.containsKey(key) )
		    		parameters.put(key, 0.0);	        
			}
	}	
}
