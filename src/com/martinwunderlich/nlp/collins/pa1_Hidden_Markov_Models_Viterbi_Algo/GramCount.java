package com.martinwunderlich.nlp.collins.pa1_Hidden_Markov_Models_Viterbi_Algo;

public class GramCount {

	private String gramType = "";
	private int count = 0;
	private String firstGram = "";
	private String secondGram = "";
	private String thirdGram = "";
	
	public String getGramType() {
		return gramType;
	}

	public void setGramType(String gramType) {
		this.gramType = gramType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getFirstGram() {
		return firstGram;
	}

	public void setFirstGram(String firstGram) {
		this.firstGram = firstGram;
	}

	public String getSecondGram() {
		return secondGram;
	}

	public void setSecondGram(String secondGram) {
		this.secondGram = secondGram;
	}

	public String getThirdGram() {
		return thirdGram;
	}

	public void setThirdGram(String thirdGram) {
		this.thirdGram = thirdGram;
	}

	public GramCount(String gramType, int count, String firstGram,
			String secondGram, String thirdGram) {
		this.gramType = gramType;
		this.count = count;
		this.firstGram = firstGram;
		this.secondGram = secondGram;
		this.thirdGram = thirdGram;		
	}

}
