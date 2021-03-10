package com.rappelr.floatbase.skin;

import java.util.ArrayList;
import java.util.List;

import com.rappelr.floatbase.collection.SkinCollection;
import com.rappelr.floatbase.gamesource.object.SourceSection;
import com.rappelr.floatbase.utils.Identifiable;
import com.rappelr.floatbase.utils.StringUtil;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;

@RequiredArgsConstructor
public class Skin extends Identifiable {

	@Getter
	private final String name;

	@Getter
	private final Weapon weapon;

	@Getter
	private final Rarity rarity;

	@Getter
	private final double wearMax, wearMin;
	
	@Getter @Setter
	private SkinCollection collection;

	public static Skin of(SourceSection section) {
		if(section == null)
			return null;

		val name = section.getString("name");
		val weapon = Weapon.valueOf(section.getString("weapon"));
		val rarity = Rarity.valueOf(section.getString("rarity"));
		val wearMax = section.getDouble("wearMax");
		val wearMin = section.getDouble("wearMin");

		if(name != null && weapon != null && rarity != null && wearMax != -1 && wearMin != -1d)
			return new Skin(name, weapon, rarity, wearMax, wearMin);
		return null;
	}
	
	public List<SkinInstance> calculate() {
		val instances = new ArrayList<SkinInstance>();
		for(Condition condition : getAvailableConditions()) {
			instances.add(new SkinInstance(this, condition, Attribute.NORMAL));
			instances.add(new SkinInstance(this, condition, this.getSpecialAttribute()));
		}
		return instances;
	}

	public String getFormal() {
		return weapon.getFormal() + " | " + name;
	}

	public List<Condition> getAvailableConditions() {
		return Condition.allAvailable(this);
	}

	public Attribute getSpecialAttribute() {
		return collection.getType().getAttribute();
	}

	public List<String> serialize() {
		val lines = new ArrayList<String>();

		lines.add("\t\t" + StringUtil.lignQuotes(getId()));
		lines.add("\t\t{");
		lines.add("\t\t\t\"name\"  " + StringUtil.lignQuotes(name));
		lines.add("\t\t\t\"weapon\"  " + StringUtil.lignQuotes(weapon.name()));
		lines.add("\t\t\t\"rarity\"  " + StringUtil.lignQuotes(rarity.name()));
		lines.add("\t\t\t\"wearMax\"  " + StringUtil.lignQuotes("" + wearMax));
		lines.add("\t\t\t\"wearMin\"  " + StringUtil.lignQuotes("" + wearMin));
		lines.add("\t\t}");

		return lines;
	}
	
	public String generateMarketHash() {
		return StringUtil.encode(getFormal());
	}

	@Override
	protected String generateId() {
		return StringUtil.flatten(weapon.name().replace("weapon_", "")) + "_" + StringUtil.flatten(name);
	}
}
