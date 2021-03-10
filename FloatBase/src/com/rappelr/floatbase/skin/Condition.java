package com.rappelr.floatbase.skin;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Condition {
	
	FACTORY_NEW(0d,.07d, "Factory New", "FN"),
	MINIMAL_WEAR(.07d,.15d, "Minimal Wear", "MW"),
	FIELD_TESTED(.15d,.38d, "Field-Tested", "FT"),
	WELL_WORN(.38d,.45d, "Well-Worn", "WW"),
	BATTLE_SCARRED(.45d,1d, "Battle-Scarred", "BS");
	
	@Getter
	private final double min, max;
	
	@Getter
	private final String formal, simple;
	
	public static Condition byFormal(String formal) {
		for(Condition condition : values())
			if(condition.formal.equalsIgnoreCase(formal))
				return condition;
		return null;
	}
	
	public static List<Condition> allAvailable(Skin skin) {
		val available = new ArrayList<Condition>();
		for(Condition condition : values())
			if(condition.isAvailable(skin))
				available.add(condition);
		return available;
	}
	
	public static Condition at(double wear) {
		if(wear >= 0d && wear <= 1d)
			for(Condition condition : values())
				if(condition.contains(wear))
					return condition;
		return null;
	}
	
	public boolean isAvailable(Skin skin) {
		if(skin.getWearMin() == 0d && skin.getWearMax() == 1d)
			return true;
		return skin.getWearMin() < min && skin.getWearMax() > max
				|| contains(skin.getWearMax()) || contains(skin.getWearMin());
	}
	
	public boolean contains(double d) {
		switch (this) {
		case FACTORY_NEW:
			return d <= max;
		case BATTLE_SCARRED:
			return d > min;
		default:
			return d > min && d <= max;
		}
	}

}
