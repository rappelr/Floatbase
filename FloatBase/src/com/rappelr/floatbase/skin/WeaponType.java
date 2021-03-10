package com.rappelr.floatbase.skin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum WeaponType {
	
	SECONDARY("Pistol"),
	SMG("SMG"),
	RIFLE("Rifle"),
	SNIPER_RIFLE("Sniper Rifle"),
	SHOTGUN("Shotgun"),
	MACHINEGUN("Machinegun");
	
	@Getter
	private final String formal;

}
