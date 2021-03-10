package com.rappelr.floatbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.rappelr.floatbase.collection.SkinCollection;
import com.rappelr.floatbase.economy.Economy;
import com.rappelr.floatbase.skin.Skin;
import com.rappelr.floatbase.skin.SkinInstance;
import com.rappelr.floatbase.skin.Weapon;
import com.rappelr.floatbase.utils.StringUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

public class FloatBase {
	
	private static final String VERSION = "1.0.0";
	
	@Getter
	private final List<SkinCollection> collections;
	
	private final HashMap<Skin, List<SkinInstance>> skins;
	
	private final FloatBaseLoader loader;

	@Getter
	private final Economy economy;
	
	@Getter @Setter 
	private FloatBaseConfigation configuration;
	
	public FloatBase(FloatBaseConfigation configation) {
		System.out.println("initializing floatbase version " + VERSION);
		this.configuration = configation;

		collections = new ArrayList<SkinCollection>();
		skins = new HashMap<Skin, List<SkinInstance>>();

		loader = new FloatBaseLoader(this);
		economy = new Economy(this);
	}
	
	public FloatBase() {
		this(FloatBaseConfigation.DEFAULT);
	}

	public boolean load() {
		return load(false);
	}

	private synchronized boolean load(boolean raw) {
		System.out.println("loading floatbase ...");
		val time = System.currentTimeMillis();
		
		if(!loader.load())
			return false;

		if(!economy.load()) {
			System.out.println("economy failed to load");
			return false;
		}

		System.out.println("floatbase loaded successfully!");
		System.out.println("load time: " + StringUtil.formatSeconds(System.currentTimeMillis() - time));
		return true;
	}
	
	public Set<Skin> getSkins() {
		return skins.keySet();
	}
	
	public List<SkinInstance> getSkinInstances() {
		val result = new ArrayList<SkinInstance>();
		skins.values().forEach(result::addAll);
		return result;
	}

	public SkinInstance getSkinInstance(String id) {
		for(SkinInstance instance : getSkinInstances())
			if(instance.getId().contentEquals(id))
				return instance;
		return null;
	}

	public Skin getSkin(String name, Weapon weapon) {
		for(Skin skin : getSkins())
			if(skin.getName().equalsIgnoreCase(name) && skin.getWeapon() == weapon)
				return skin;
		return null;
	}

	public Skin getSkin(String id) {
		for(Skin skin : getSkins())
			if(skin.getId().contentEquals(id))
				return skin;
		return null;
	}

	public List<SkinInstance> getSkinInstances(Skin skin) {
		return skins.get(skin);
	}

	public SkinCollection getCollection(String id) {
		for(SkinCollection collection : collections)
			if(collection.getId().contentEquals(id))
				return collection;
		return null;
	}
	
	void clear() {
		collections.clear();
		skins.clear();
	}
	
	void add(SkinCollection collection) {
		collections.add(collection);
		collection.calculate();
		collection.getSkins().forEach(this::add);
	}
	
	void add(Skin skin) {
		skins.put(skin, skin.calculate());
	}
}
