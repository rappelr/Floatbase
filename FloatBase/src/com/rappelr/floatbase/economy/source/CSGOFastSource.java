package com.rappelr.floatbase.economy.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.rappelr.floatbase.FloatBaseConfigation;
import com.rappelr.floatbase.skin.Attribute;
import com.rappelr.floatbase.skin.Condition;
import com.rappelr.floatbase.skin.Weapon;

import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor
public class CSGOFastSource extends EconomySource {
	
	private static final double PRICE_ADDATIVE = .02d;

	private static final String PAGE_URL = "https://api.csgofast.com/price/all";

	@Override
	public boolean load() {
		if(!checkHook())
			return false;
		
		System.out.println("loading csgofast prices...");
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(new URL(PAGE_URL).openStream(), StandardCharsets.UTF_8));
		} catch (IOException e) {
			System.out.println("failed to open url " + PAGE_URL);
			e.printStackTrace();
			return false;
		}

		int successes = 0;
		
		try {
			val line = in.readLine();

			in.close();

			val split = line.substring(2, line.length() - 1).split(",\"");
			for(String string : split)
				try {
					if(process(string))
						successes++;
				} catch (Exception e) {
					e.printStackTrace();
				}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("failed after loading " + successes + " prices");
			return false;
		}

		System.out.println("loaded " + successes + " prices");
		return true;
	}

	private boolean process(String string) throws Exception {
		if(!string.contains("|") || string.startsWith("★"))
			return false;

		val price = Double.parseDouble(string.substring(string.lastIndexOf(":") + 1));
		String item = string.substring(0, string.lastIndexOf(":") - 1);
		boolean special = false;

		if(item.startsWith("StatTrak") || item.startsWith("Souvenir")) {
			item = item.substring(item.indexOf(" ") + 1);
			special = true;
		}

		item = item.replace(" | ", "#");

		val split = item.split("#");
		val paint = split[1];
		val weapon = Weapon.byFormal(split[0]);

		if(weapon == null)
			return false;

		val skinName = paint.substring(0, paint.lastIndexOf("(") - 1);
		val skin = getHook().getFloatBase().getSkin(skinName, weapon);
		
		if(skin == null) {
			if(getHook().getFloatBase().getConfiguration().getDebugLevel() >= FloatBaseConfigation.DEBUG_ALL)
				System.out.println("skin not found: " + weapon.getFormal() + " | " + skinName);
			return false;
		}
		
		val condition = Condition.byFormal(paint.substring(paint.lastIndexOf("(") + 1, paint.length() - 1));

		if(condition == null)
			return false;

		getHook().put(getHook().getFloatBase().getEconomy().getInstance(skin, condition, special ? skin.getSpecialAttribute() : Attribute.NORMAL), price + PRICE_ADDATIVE);
		return true;
	}
}
