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
