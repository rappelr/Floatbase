package com.rappelr.floatbase.economy;

import com.rappelr.floatbase.FloatBase;
import com.rappelr.floatbase.skin.SkinInstance;

public interface EconomyHook {
	
	public FloatBase getFloatBase();

	public void put(SkinInstance skin, EconomyPricePoint point);
	
}
