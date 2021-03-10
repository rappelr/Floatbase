package com.rappelr.floatbase.skin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Rarity {
	
	common(0, "Consumer Grade"),
	uncommon(1, "Industrial Grade"),
	rare(2, "Mil-Spec"),
	mythical(3, "Restricted"),
	legendary(4, "Classified"),
	ancient(5, "Covert");
	
	private final int value;
	
	@Getter
	private final String formal;
	
	public boolean over(Rarity rarity) {
		if(rarity == null)
			return true;
		return rarity.value < value;
	}
	
	public boolean under(Rarity rarity) {
		if(rarity == null)
			return true;
		return rarity.value > value;
	}
	
	private static Rarity byValue(int value) {
		for(Rarity rarity : values())
			if(rarity.value == value)
				return rarity;
		return null;
	}
	
	public Rarity shiftDown() {
		if(this == common)
			return null;
		return byValue(value -1);
	}
	
	public Rarity shiftUp() {
		if(this == ancient)
			return null;
		return byValue(value +1);
	}

}
