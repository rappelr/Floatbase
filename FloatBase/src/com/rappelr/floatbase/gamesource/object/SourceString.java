package com.rappelr.floatbase.gamesource.object;

import lombok.Getter;

public class SourceString extends SourceObject {

	@Getter
	private final String value;
	
	SourceString(String key, String value) {
		super(key);

		this.value = value;
	}

	@Override
	public void print(String prefix) {
		System.out.println(prefix + getKey() + ": \"" + value + "\"");
	}

}
