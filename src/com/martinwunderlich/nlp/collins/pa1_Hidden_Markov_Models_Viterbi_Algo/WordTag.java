package com.martinwunderlich.nlp.collins.pa1_Hidden_Markov_Models_Viterbi_Algo;

public class WordTag {
	
	private int count; 
	private String word; 
	private String tag;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public WordTag(int count, String word, String tag) {
		this.count = count; 
		this.word = word;
		this.tag = tag;
	}

}
