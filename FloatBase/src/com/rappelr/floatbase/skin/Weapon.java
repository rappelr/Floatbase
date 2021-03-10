package com.rappelr.floatbase.skin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Weapon {
	
	weapon_cz75a(WeaponType.SECONDARY, "CZ75-Auto"),
	weapon_deagle(WeaponType.SECONDARY, "Desert Eagle"),
	weapon_elite(WeaponType.SECONDARY, "Dual Berettas"),
	weapon_fiveseven(WeaponType.SECONDARY, "Five-SeveN"),
	weapon_glock(WeaponType.SECONDARY, "Glock-18"),
	weapon_hkp2000(WeaponType.SECONDARY, "P2000"),
	weapon_p250(WeaponType.SECONDARY, "P250"),
	weapon_revolver(WeaponType.SECONDARY, "R8 Revolver"),
	weapon_tec9(WeaponType.SECONDARY, "Tec-9"),
	weapon_usp_silencer(WeaponType.SECONDARY, "USP-S"),
	weapon_mag7(WeaponType.SHOTGUN, "MAG-7"),
	weapon_nova(WeaponType.SHOTGUN, "Nova"),
	weapon_sawedoff(WeaponType.SHOTGUN, "Sawed-Off"),
	weapon_xm1014(WeaponType.SHOTGUN, "XM1014"),
	weapon_m249(WeaponType.MACHINEGUN, "M249"),
	weapon_negev(WeaponType.MACHINEGUN, "Negev"),
	weapon_mac10(WeaponType.SMG, "MAC-10"),
	weapon_mp5sd(WeaponType.SMG, "MP5-SD"),
	weapon_mp7(WeaponType.SMG, "MP7"),
	weapon_mp9(WeaponType.SMG, "MP9"),
	weapon_p90(WeaponType.SMG, "P90"),
	weapon_bizon(WeaponType.SMG, "PP-Bizon"),
	weapon_ump45(WeaponType.SMG, "UMP-45"),
	weapon_ak47(WeaponType.RIFLE, "AK-47"),
	weapon_aug(WeaponType.RIFLE, "AUG"),
	weapon_famas(WeaponType.RIFLE, "FAMAS"),
	weapon_galilar(WeaponType.RIFLE, "Galil AR"),
	weapon_m4a1_silencer(WeaponType.RIFLE, "M4A1-S"),
	weapon_m4a1(WeaponType.RIFLE, "M4A4"),
	weapon_sg556(WeaponType.RIFLE, "SG 553"),
	weapon_awp(WeaponType.SNIPER_RIFLE, "AWP"),
	weapon_g3sg1(WeaponType.SNIPER_RIFLE, "G3SG1"),
	weapon_scar20(WeaponType.SNIPER_RIFLE, "SCAR-20"),
	weapon_ssg08(WeaponType.SNIPER_RIFLE, "SSG 08");
	
	@Getter
	private final WeaponType type;
	
	@Getter
	private final String formal;
	
	public static Weapon byFormal(String formal) {
		for(Weapon weapon : values())
			if(weapon.formal.equalsIgnoreCase(formal))
				return weapon;
		return null;
	}

}
