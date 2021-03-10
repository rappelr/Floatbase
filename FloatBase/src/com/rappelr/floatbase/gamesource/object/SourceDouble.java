package com.rappelr.floatbase.gamesource.object;

import lombok.Getter;

public class SourceDouble extends SourceObject {

	@Getter
	private final double value;
	
	SourceDouble(String key, double value) {
		super(key);

		this.value = value;
	}

	@Override
	public void print(String prefix) {
		System.out.println(prefix + getKey() + ": " + value + "d");
	}

}
