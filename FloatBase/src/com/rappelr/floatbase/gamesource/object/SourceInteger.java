package com.rappelr.floatbase.gamesource.object;

import lombok.Getter;

public class SourceInteger extends SourceObject {

	@Getter
	private final int value;
	
	SourceInteger(String key, int value) {
		super(key);

		this.value = value;
	}

	@Override
	public void print(String prefix) {
		System.out.println(prefix + getKey() + ": " + value);
	}

}
