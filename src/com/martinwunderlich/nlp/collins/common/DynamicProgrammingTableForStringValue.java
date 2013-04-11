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
