package com.rappelr.floatbase.collection;

import java.util.ArrayList;
import java.util.List;

import com.rappelr.floatbase.gamesource.object.SourceSection;
import com.rappelr.floatbase.skin.Rarity;
import com.rappelr.floatbase.skin.Skin;
import com.rappelr.floatbase.utils.Identifiable;
import com.rappelr.floatbase.utils.StringUtil;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class SkinCollection extends Identifiable {
	
	@Getter
	private final String name;
	
	@Getter
	private final CollectionType type;

	@Getter
	private final List<Skin> skins;
	
	@Getter
	private Rarity from, to;
	
	public List<Skin> byRarity(Rarity rarity) {
		val results = new ArrayList<Skin>();
		skins.forEach(skin -> {
			if(skin.getRarity() == rarity)
				results.add(skin);
		});
		return results;
	}
	
	public List<String> serialize() {
		val lines = new ArrayList<String>();
		
		lines.add("\t" + StringUtil.lignQuotes(getId()));
		lines.add("\t{");
		lines.add("\t\t\"name\"  " + StringUtil.lignQuotes(name));
		lines.add("\t\t\"type\"  " + StringUtil.lignQuotes(type.name()));
		for(Skin skin : skins)
			for(String line : skin.serialize())
				lines.add(line);
		lines.add("\t}");
		
		return lines;
	}
	
	public static SkinCollection of(SourceSection section) {
		if(section == null)
			return null;

		val name = section.getString("name");
		val type = CollectionType.valueOf(section.getString("type"));
		
		if(name == null || type == null)
			return null;
		
		val skins = new ArrayList<Skin>();
		for(SourceSection sub : section.getChildren()) {
			val skin = Skin.of(sub);
			if(skin == null)
				System.out.println(section.getKey() + " failed to deserialize");
			else
				skins.add(skin);
		}
		
		return new SkinCollection(name, type, skins);
	}

	public void calculate() {
		from = to = null;
		for(Skin skin : skins) {
			skin.setCollection(this);
			if(skin.getRarity().over(to))
				to = skin.getRarity();
			if(skin.getRarity().under(from))
				from = skin.getRarity();
		}
	}

	@Override
	protected String generateId() {
		return StringUtil.flatten(name);
	}
	
}
