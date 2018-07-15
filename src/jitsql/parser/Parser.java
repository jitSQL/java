package jitsql.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import jitsql.parser.trie.Trie;

public class Parser {
	public static void main(String[] args) throws IOException {
		
		// Take input
		//BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		// Convert to a char array for efficiency
		//char[] cmd = reader.readLine().toCharArray();
		
		// We use a Trie to pattern match reserved words
		Trie data = new Trie();
		
		// Hash table serves as a category reference
		Hashtable<String, Integer> h = new Hashtable<String, Integer>();
		
		// we store each match as a Tuple of start, end indices and the category
		ArrayList<Tuple> matches = new ArrayList<Tuple>();
		
		// Add our vocabulary to category conversion to the hash map
		h.put("SELECT", 1);
		h.put("FROM", 2);
		h.put("WHERE", 3);
		h.put("JEFE", Integer.MAX_VALUE);
		h.put("STRING", 5);
		// Add our vocab to Trie matcher
		data.insert("SELECT");
		data.insert("FROM");
		data.insert("WHERE");
		data.insert("JEFE");
		
		int start = 0;
		int end = 0;
		long startTime = System.currentTimeMillis();
		
		ArrayList<String> queries = new ArrayList<String>();
		for(int i = 0;i < 10000000; i++) {
			queries.add("SELECT \"id\" FROM");
		}
		for(String query: queries) {
			char[] cmd = query.toCharArray();
		// Continue looping through the entire input until we parse all of it
			while (start < cmd.length && end < cmd.length) {
				//System.out.println(cmd.length);
				//System.out.println("STARTNUM: " + start);
				//System.out.println("ENDNUM: " + end);
				
				//System.out.println(cmd[start]);
				// Edge case checker for strings - non reserved words
				if (cmd[start] == '"') {
					while (true) {
						//System.out.println("START " + cmd[start]);
						//System.out.println("END " + cmd[end]);
						if (cmd[end] == '"') {
							end++;
							break;
						}
						else end++;
					}
					// don't include quotations
					matches.add(new Tuple(start + 1, end - 1, 5));
					//System.out.println(matches.toString());
				} else {
					// look for reserved words
					
					boolean match = false;
					
					while (!match) {	
						String build = "";
						// Inefficient - build our search string
						for (int i = start; i < end; i++) {
							if (end > cmd.length) {
								match = true;
								break;
							}
							build += cmd[i];
						}
						//System.out.println("BUILD: " + build);
						if (build.equals(" ")) {
							break;
						}
						// search for the string in the trie
						if (data.search(build)) {
							match = true;
							// if we've found the string, add a new Tuple and stop looping
							int type = h.get(build);
							Tuple t = new Tuple(start, end, type);
							
							matches.add(t);
							break;
						} else {
							end++;
						}
					}
				}
				// Increment to the next char
				start = end;
				end = start + 1;
			}
		}
		System.out.println(matches.toString());
		System.out.println("Took " + (System.currentTimeMillis() - startTime) + " ms");
	}
}

class Tuple {
	final int start;
	final int end;
	final int type;
	public Tuple(int start, int end, int type) {
		this.start = start;
		this.end = end;
		this.type = type;
	}
	
	public String toString() {
		return "|" + start + ", " + this.end + ", S" + this.type + "|";
	}
}
