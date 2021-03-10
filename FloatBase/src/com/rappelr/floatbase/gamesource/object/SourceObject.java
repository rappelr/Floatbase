package com.rappelr.floatbase.gamesource.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class SourceObject {
	
	@Getter
	private final String key;
	
	public abstract void print(String prefix);
	
	public void print() {
		print("");
	}
	
	protected static SourceObject of(String[] set) {
		if(set.length != 2)
			return null;
		
		try {
			int value = Integer.parseInt(set[1]);
			return new SourceInteger(set[0], value);
		} catch(NumberFormatException e) {}
		
		try {
			double value = Double.parseDouble(set[1]);
			return new SourceDouble(set[0], value);
		} catch(NumberFormatException e) {}
		
		return new SourceString(set[0], set[1]);
	}

}
