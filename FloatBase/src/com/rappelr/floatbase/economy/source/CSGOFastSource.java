package com.rappelr.floatbase.economy.source;

import com.rappelr.floatbase.FloatBaseConfigation;
import com.rappelr.floatbase.economy.EconomyPricePoint;
import com.rappelr.floatbase.skin.Attribute;
import com.rappelr.floatbase.skin.Condition;
import com.rappelr.floatbase.skin.Weapon;

import lombok.val;

public class CSGOFastSource extends HTTPSource {

	private static final double PRICE_ADDATIVE = .02d;

	private static final String PAGE_URL = "https://api.csgofast.com/price/all";
	
	public CSGOFastSource() {
		super(PAGE_URL);
	}
	
	@Override
	protected boolean process(String content) {
		int successes = 0;
		
		val split = content.substring(2, content.length() - 1).split(",\"");
		for(String line : split)
			try {
				if(processLine(line))
					successes++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		System.out.println("CSGOFastSource processed " + successes + " prices successfully");
		return true;
	}

	private boolean processLine(String line) throws Exception {
		if(!line.contains("|") || line.startsWith("★"))
			return false;

		val price = Double.parseDouble(line.substring(line.lastIndexOf(":") + 1));
		String item = line.substring(0, line.lastIndexOf(":") - 1);
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

		getHook().put(getHook().getFloatBase().getEconomy().getInstance(skin, condition, special ? skin.getSpecialAttribute() : Attribute.NORMAL), new EconomyPricePoint(price + PRICE_ADDATIVE));
		return true;
	}
}
