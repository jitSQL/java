package jitsql.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

import jitsql.parser.trie.Trie;

public class Parser {
	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		char[] cmd = reader.readLine().toCharArray();
		
		Trie data = new Trie();
		
		Hashtable<String, Integer> h = new Hashtable<String, Integer>();
		
		ArrayList<Tuple> matches = new ArrayList<Tuple>();
		
		h.put("SELECT", 1);
		h.put("FROM", 2);
		h.put("WHERE", 3);
		h.put("JEFE", Integer.MAX_VALUE);
		h.put("STRING", 5);
		
		data.insert("SELECT");
		data.insert("FROM");
		data.insert("WHERE");
		data.insert("JEFE");
		
		int start = 0;
		int end = 0;
		while (start < cmd.length) {
			
			if (cmd[start] == '\"') {
				end = start + 1;
				while (true) {
					if (cmd[end] == '\"') break;
					else end++;
				}
				matches.add(new Tuple(start, end, 5));
			} else {
				boolean match = false;
				String build = "";
				while (!match) {				
					for (int i = start; i < end; i++) {
						if (end > cmd.length) break;
						build += cmd[i];
					}
					
					if (data.search(build)) {
						match = true;
						
						int type = h.get(build);
						Tuple t = new Tuple(start, end, type);
						
						matches.add(t);
						
						start = end;
						break;
					}
					
					if (build.length() > 6) {
						
					}
					end++;
				}
			}
		}
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
}
