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

import java.util.ArrayList;
import java.util.List;

public class TaggedSentence extends Sentence {
	List<String> tags = new ArrayList<String>();
	public TaggedSentence() {
		super();
		
		this.tags.add("*");
		this.tags.add("*");
		this.tags.add("STOP");
	}
	
	public void tagWordAtPositionWithTag(int position, String tag) {
		this.tags.add(position + 1, tag);
	}
	
	public String getTag(int index) {
		return this.tags.get(index + 1);
	}
	
	public String getTagMinusOne(int index) {
		return this.tags.get(index );
	}
	
	public String getTagMinusTwo(int index) {
		return this.tags.get(index - 1);
	}
}
