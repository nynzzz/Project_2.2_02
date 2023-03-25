package project.multi.agent;

import java.util.Arrays;
import java.util.List;

class Symbol {
	String name;
	List<List<Symbol>> productions;

	Symbol(String name, List<List<Symbol>> productions) {
		this.name = name;
		this.productions = productions;
	}

	public Symbol(String name) {
		this(name, new java.util.ArrayList<>());
	}

	@Override
	public String toString() {
		return name;
	}
	
	
	 
	public String toStringLong() {
		return name + Arrays.toString(productions.toArray());
	}
}