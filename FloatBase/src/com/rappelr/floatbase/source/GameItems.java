package com.rappelr.floatbase.source;

import java.util.List;

import com.rappelr.floatbase.source.object.SourceSection;

public class GameItems extends GameSource {
	
	private SourceSection items, itemSets, prefabs, paintKits, clientLootList;
	
	@Override
	public boolean load() {
		if(!super.load())
			return false;
		
		try {
			items = reachSection("items_game.items");
			itemSets = reachSection("items_game.item_sets");
			prefabs = reachSection("items_game.prefabs");
			paintKits = reachSection("items_game.paint_kits");
			clientLootList = reachSection("items_game.client_loot_lists");
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<SourceSection> getCollections() {
		return itemSets.getChildren();
	}
	
	public SourceSection getPrefab(String weaponId) {
		return prefabs.getSection(weaponId + "_prefab");
	}
	
	public SourceSection getPaintKit(String skinId) {
		return paintKits.where("name", skinId);
		
	}
	
	public SourceSection getCrate(String collectionId) throws Exception {
		for(SourceSection child : items.getChildren())
			if(child.reachMatch("tags.ItemSet.tag_value", collectionId)
					&& !child.getString("name").contains("weapon")
					&& !child.getString("name").contains("promo"))
				return child;
		return null;
	}
	
	public SourceSection getRarities(String crateId) {
		return clientLootList.getSection(crateId);
	}

}
