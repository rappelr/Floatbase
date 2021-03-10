package com.rappelr.floatbase.economy.source;

import com.rappelr.floatbase.FloatBase;
import com.rappelr.floatbase.skin.SkinInstance;

public interface EconomySourceHook {
	
	public FloatBase getFloatBase();

	public void put(SkinInstance skin, double price);
	
}
