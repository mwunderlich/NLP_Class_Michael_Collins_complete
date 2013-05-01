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

import com.martinwunderlich.nlp.collins.common.AbstractSentence;

public class SentencePair {
	private AbstractSentence sentenceOne = null; 
	private AbstractSentence sentencenTwo = null; 
	
	public SentencePair(AbstractSentence s1, AbstractSentence s2){
		this.sentenceOne = s1; 
		this.sentencenTwo = s2;
	}

	void setSentenceOne(AbstractSentence sentenceOne) {
		this.sentenceOne = sentenceOne;
	}

	AbstractSentence getSentenceOne() {
		return sentenceOne;
	}

	void setSentencenTwo(AbstractSentence sentencenTwo) {
		this.sentencenTwo = sentencenTwo;
	}

	AbstractSentence getSentenceTwo() {
		return sentencenTwo;
	}
}
