package com.rappelr.floatbase;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.rappelr.floatbase.collection.CollectionType;
import com.rappelr.floatbase.collection.SkinCollection;
import com.rappelr.floatbase.gamesource.GameItems;
import com.rappelr.floatbase.gamesource.Language;
import com.rappelr.floatbase.gamesource.ProcessedSource;
import com.rappelr.floatbase.gamesource.object.SourceSection;
import com.rappelr.floatbase.skin.Rarity;
import com.rappelr.floatbase.skin.Skin;
import com.rappelr.floatbase.skin.Weapon;
import com.rappelr.floatbase.utils.StringUtil;

import lombok.val;

class FloatBaseLoader {

	private final FloatBase base;

	private final ProcessedSource processed;
	
	private final GameItems items;

	private final Language language;

	FloatBaseLoader(FloatBase base) {
		this.base = base;

		processed = new ProcessedSource();
		items = new GameItems();
		language = new Language();
	}

	boolean load() {
		System.out.println("loading data...");
		boolean loaded = false;
		val force = base.getConfiguration().isForceReprocess();

		if(!force) {
			processed.setDebugLevel(base.getConfiguration().getDebugLevel());
			processed.setPath(base.getConfiguration().getProcessedSourceFilePath());
			
			if(!processed.exists()) {
				System.out.println(processed.getName() + " not found");
			} else
				loaded = loadProcessed();
		}

		if(!loaded) {
			if(force)
				System.out.println("generating from game files");
			else
				System.out.println("processed file failed to load, generating from game files");
			if(loadSource()) {
				System.out.println("Load success, generating new processed file");
				loaded = true;
				try {
					generate();
					System.out.println("successfully generated processed file ");
				} catch (IOException e) {
					System.out.println("failed generating processed file");
					e.printStackTrace();
				}
			} else
				System.out.println("Load failed, try checking your source file configuration");
		}
		return loaded;
	}

	private boolean loadSource() {
		base.clear();

		System.out.println("loading from source files...");
		
		items.setDebugLevel(base.getConfiguration().getDebugLevel());
		items.setPath(base.getConfiguration().getItemsSourceFilePath());

		if(!items.exists()) {
			System.out.println("items file not found: " + items.getName());
			return false;
		}

		language.setDebugLevel(base.getConfiguration().getDebugLevel());
		language.setPath(base.getConfiguration().getLanguageSourceFilePath());

		if(!language.exists()) {
			System.out.println("language file not found: " + language.getName());
			return false;
		}
		
		items.load();
		language.load();

		val rawCollections = items.getCollections();

		if(rawCollections == null) {
			System.out.println("collections not found");
			return false;
		}

		int collections = 0, potential = 0;

		for(SourceSection collectionSection : rawCollections) {

			if(collectionSection.getKey().contains("characters")) {
				System.out.println("skipping agent collection " + collectionSection.getKey());
				continue;
			}

			potential++;
			
			SourceSection crateSection;
			
			try {
				crateSection = items.getCrate(collectionSection.getKey());
			} catch (Exception e) {
				crateSection = null;
			}
			
			String crateName = collectionSection.getKey();

			if(crateSection == null) {
				//System.out.println("crate section of " + collectionSection.getKey() + " is missing, using collectionId");
			} else {
				if(crateSection.contains("loot_list_name"))
					crateName = crateSection.getString("loot_list_name");
				else
					crateName = crateSection.getString("name");
			}

			if(crateName == null) {
				System.out.println("crate name of " + collectionSection.getKey() + " is missing");
				continue;
			}

			SourceSection rarities = items.getRarities(crateName);
			
			if(rarities == null)
				rarities = items.getRarities(collectionSection.getKey().replace("set", "crate"));

			if(rarities == null) {
				System.out.println("crate rarities of " + crateName + " are missing");
				continue;
			}
			
			val collectionType = rarities.getKey().contains("crate") ? CollectionType.CASE : CollectionType.MAP;

			val itemRarities = new HashMap<String, Rarity>();

			for(String key : rarities.getKeys()) {
				val raritySection = items.getRarities(key);

				if(raritySection == null)
					continue;

				val rarityString = key.substring(key.lastIndexOf("_") + 1);
				Rarity rarity;

				try {
					rarity = Rarity.valueOf(rarityString);
				} catch(IllegalArgumentException e) {
					System.out.println("rarity type " + rarityString + " does not exist (in " + key + " of " + crateName + ")");
					continue;
				}

				for(String skinTag : raritySection.getKeys())
					itemRarities.put(skinTag, rarity);
			}

			if(itemRarities.isEmpty()) {
				System.out.println("no item rarities found in " + rarities.getKey());
				continue;
			}

			val collectionToken = collectionSection.getString("name");

			if(collectionToken == null) {
				System.out.println("collection " + collectionSection.getKey() + " is missing name");
				continue;
			}

			String collectionName = language.getString(collectionToken);

			if(collectionName == null) {
				System.out.println("collection name at " + collectionToken + " not found, replacing with token");
				collectionName = collectionToken;
			}

			val skins = new ArrayList<Skin>();
			val skinSection = collectionSection.getSection("items");

			if(skinSection == null) {
				System.out.println("collection " + collectionSection.getKey() + " is missing items");
				continue;
			}

			for(String skinTag : skinSection.getKeys()) {
				int i = skinTag.indexOf("]");

				if(i < 0) {
					System.out.println("faulty item tag: " + skinTag);
					continue;
				}

				val weaponId = skinTag.substring(skinTag.indexOf("]") + 1);
				val skinId = skinTag.substring(1, skinTag.indexOf("]"));

				val paintkit = items.getPaintKit(skinId);

				val weapon = Weapon.valueOf(weaponId);

				if(weapon == null) {
					System.out.println("weapon " + weaponId + " not found");
					continue;
				}

				if(paintkit == null) {
					System.out.println("paintkit not found for skin " + skinId);
					continue;
				}

				val descriptionTag = paintkit.getString("description_tag");

				if(descriptionTag == null) {
					System.out.println("description_tag in paintkit " + paintkit.getKey() + " for skin " + skinId);
					continue;
				}

				double wearMin = paintkit.getDouble("wear_remap_min"),
						wearMax = paintkit.getDouble("wear_remap_max");

				if(wearMax == -1d)
					wearMax = 1d;
				if(wearMin == -1d)
					wearMin = 0d;

				val prefab = items.getPrefab(weaponId);

				if(prefab == null) {
					System.out.println("prefab not found for weapon " + weaponId);
					continue;
				}

				val typeId = prefab.getString("prefab");

				if(typeId == null) {
					System.out.println("prefab not found for weapon " + weaponId + " in prefab " + prefab.getKey());
					continue;
				}

				String skinName = language.getString(descriptionTag);

				if(skinName == null) {
					System.out.println(descriptionTag + " not found in language, replacing with token");
					skinName = descriptionTag;
					continue;
				}

				val rarity = itemRarities.get(skinTag);

				if(rarity == null) {
					System.out.println("rarityId not found for tag " + skinTag);
					continue;
				}

				skins.add(new Skin(skinName, weapon, rarity, wearMax, wearMin));
			}
			
			base.add(new SkinCollection(collectionName, collectionType, skins));
			
			collections++;
		}

		System.out.println("generated " + collections + "/" + potential + " collections");
		return collections > 0;

	}

	private boolean loadProcessed() {
		System.out.println("loading from processed file");
		if(!processed.load())
			return false;

		base.clear();
		val sections = processed.getCollections();

		if(sections == null) {
			System.out.println(processed.getName() + " not readable");
			return false;
		}

		int collections = 0;

		for(SourceSection section : sections) {
			val collection = SkinCollection.of(section);
			if(collection == null)
				System.out.println(section.getKey() + " failed to deserialize");
			else {
				base.add(collection);
				collections++;
			}
		}

		System.out.println(processed.getName() + " resulted in " + collections + " collections");

		return collections > 0;
	}

	private void generate() throws IOException {
		FileWriter writer = new FileWriter(processed.getFile(base));

		writer.write("//Generated on " + StringUtil.formatDate() + "\n\"collections\"\n{\n");

		for(SkinCollection collection : base.getCollections())
			for(String line : collection.serialize())
				writer.write(line + "\n");

		writer.write("}");
		writer.close();
	}

}
