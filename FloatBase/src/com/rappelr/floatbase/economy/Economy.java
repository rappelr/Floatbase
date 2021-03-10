package com.rappelr.floatbase.economy;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.rappelr.floatbase.FloatBase;
import com.rappelr.floatbase.FloatBaseConfigation;
import com.rappelr.floatbase.economy.source.EconomySource;
import com.rappelr.floatbase.economy.source.EconomySourceHook;
import com.rappelr.floatbase.skin.Attribute;
import com.rappelr.floatbase.skin.Condition;
import com.rappelr.floatbase.skin.Skin;
import com.rappelr.floatbase.skin.SkinInstance;

import lombok.NonNull;
import lombok.val;

public class Economy implements EconomySourceHook {

	public static final double NULL_VALUE = -1d;

	private final HashMap<SkinInstance, Double> prices;

	private final FloatBase base;

	private EconomySource source;

	public Economy(FloatBase base) {
		System.out.println("initializing economy");
		this.base = base;

		prices = new HashMap<SkinInstance, Double>();

	}

	public boolean load(EconomySource source) {
		setSource(source);
		return load();
	}

	public boolean load() {
		System.out.println("filling economy...");
		prices.clear();

		base.getSkinInstances().forEach(i -> prices.put(i, NULL_VALUE));

		System.out.println("economy now contains " + prices.size() + " price points");

		if(source == null) {
			System.out.println("no economy source set");
			return false;
		}

		if(source.load()) {
			if(base.getConfiguration().getDebugLevel() >= FloatBaseConfigation.DEBUG_LIGHT)
				analyze();
			return true;
		}

		return false;
	}

	public boolean has(SkinInstance instance) {
		return get(instance) != NULL_VALUE;
	}

	public double get(SkinInstance instance) {
		return prices.get(instance);
	}

	public boolean has(Skin skin, Condition condition, Attribute attribute) {
		return get(skin, condition, attribute) != NULL_VALUE;
	}

	public double get(Skin skin, Condition condition, Attribute attribute) {
		return get(getInstance(skin, condition, attribute));
	}

	public SkinInstance getInstance(Skin skin, Condition condition, Attribute attribute) {
		for(SkinInstance i : prices.keySet())
			if(i.match(skin, condition, attribute))
				return i;
		return null;
	}

	@Override
	public void put(SkinInstance instance, double price) {
		prices.replace(instance, price);
	}

	@Override
	public FloatBase getFloatBase() {
		return base;
	}

	public void setSource(@NonNull EconomySource source) {
		this.source = source;
		this.source.setHook(this);
	}

	public void analyze() {
		AtomicInteger loaded = new AtomicInteger(), found = new AtomicInteger();

		prices.values().stream()
		.filter(d -> d != null)
		.peek(d -> found.incrementAndGet())
		.filter(d -> d != -1d)
		.forEach(d -> loaded.incrementAndGet());

		val setPercent = (int) ((100d / prices.size()) * loaded.get());

		System.out.println("economy analysis results:");
		System.out.println("total entries: " + prices.size());
		System.out.println("set entries: " + loaded.get() + " ~" + setPercent + "%");
		System.out.println("null entries: " + (prices.size() - found.get()));
	}

}
