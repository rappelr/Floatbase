package com.rappelr.floatbase.economy.source;

import com.rappelr.floatbase.economy.EconomyHook;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class EconomySource {
	
	@Getter(AccessLevel.PROTECTED) @Setter
	private EconomyHook hook;
	
	public abstract boolean load();
	
	protected boolean checkHook() {
		if(hook == null) {
			System.out.println("no economy loader hook set up");
			return false;
		}
		return true;
	}

}
