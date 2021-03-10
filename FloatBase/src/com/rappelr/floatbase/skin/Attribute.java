package com.rappelr.floatbase.skin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Attribute {
	
	NORMAL("", ""),
	STATTRAK("StatTrak\u2122", "ST"),
	SOUVENIR("Souvenir", "SV");
	
	@Getter
	private final String formal, simple;

	public boolean isSpecial() {
		return this != NORMAL;
	}

}
