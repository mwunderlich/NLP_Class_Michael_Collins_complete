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

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class DynamicProgrammingTableForStringValue {

	HashMap<String, String> entries = new HashMap<String, String>();
	
	public void set(int k, String u, String v, String value) {
		String entryLabel = k + "+" + u + "+" + v;
		this.entries.put(entryLabel, value);
	}
	
	public String get(int k, String u, String v) {
		String entryLabel = k + "+" + u + "+" + v;
		
		return this.entries.get(entryLabel);
	}
	
	public String toString() {
		String result = "";
		SortedSet<String> keys = new TreeSet<String>(entries.keySet());
		for (String key : keys) { 
		   String value = this.entries.get(key) + "\n";
		   result += key + "\t\t\t\t\t" + value;
		}
		return result;
	}
}
