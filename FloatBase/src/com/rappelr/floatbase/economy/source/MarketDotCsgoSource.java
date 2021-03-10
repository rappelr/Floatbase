package com.rappelr.floatbase.economy.source;

import com.rappelr.floatbase.FloatBaseConfigation;
import com.rappelr.floatbase.economy.EconomyPricePoint;
import com.rappelr.floatbase.skin.Attribute;
import com.rappelr.floatbase.skin.Condition;
import com.rappelr.floatbase.skin.Weapon;
import com.rappelr.floatbase.utils.StringUtil;

import lombok.NonNull;
import lombok.val;

public class MarketDotCsgoSource extends HTTPSource {

	public static final String USD = "USD", EURO = "EUR", RUB = "RUB";

	private static final String PAGE_URL = "https://market.csgo.com/api/v2/prices/%.json",
			STATTRAK = "StatTrak\\u2122 ",
			SOUVENIR = "Souvenir ";
	
	public MarketDotCsgoSource(@NonNull String currency) {
		super(PAGE_URL.replace("%", currency));
	}
	
	@Override
	protected boolean process(String content) {
		int successes = 0;
		
		val split = content.substring(content.indexOf("[{") + 2, content.lastIndexOf("}]")).split("\\},\\{");

		for(String line : split)
			try {
				
				if(processLine(line))
					successes++;
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		System.out.println("MarketDotCsgoSource processed " + successes + " prices successfully");
		return true;
	}

	private boolean processLine(String line) throws Exception {
		if(!line.contains("|") || line.startsWith("★"))
			return false;

		val split = line.split(",");
		
		if(split.length != 3)
			return false;
		
		String item = StringUtil.unLign(split[0].split(":")[1]);
		val volume = Integer.parseInt(StringUtil.unLign(split[1].split(":")[1]));
		val price = Float.parseFloat(StringUtil.unLign(split[2].split(":")[1]));
		
		Attribute attribute = Attribute.NORMAL;
		
		if(item.startsWith(STATTRAK)) {
			item = item.substring(STATTRAK.length());
			attribute = Attribute.STATTRAK;
		} else if(item.startsWith(SOUVENIR)) {
			item = item.substring(SOUVENIR.length());
			attribute = Attribute.SOUVENIR;
		}
		
		item = item.replace(" | ", "#");

		val itemSplit = item.split("#");
		val paint = itemSplit[1];
		val weapon = Weapon.byFormal(itemSplit[0]);

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

		getHook().put(getHook().getFloatBase().getEconomy().getInstance(skin, condition, attribute), new EconomyPricePoint(price, price, volume));
		return true;
	}
}
