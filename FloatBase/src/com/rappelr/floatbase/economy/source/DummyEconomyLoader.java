package com.rappelr.floatbase.economy.source;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DummyEconomyLoader extends EconomySource {

	@Override
	public boolean load() {
		System.out.println("dummy economy loader, no prices will be processed");
		return true;
	}

}
