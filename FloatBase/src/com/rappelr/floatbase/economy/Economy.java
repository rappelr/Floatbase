package com.rappelr.floatbase.economy;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.rappelr.floatbase.FloatBase;
import com.rappelr.floatbase.FloatBaseConfigation;
import com.rappelr.floatbase.economy.source.EconomySource;
import com.rappelr.floatbase.skin.Attribute;
import com.rappelr.floatbase.skin.Condition;
import com.rappelr.floatbase.skin.Skin;
import com.rappelr.floatbase.skin.SkinInstance;

import lombok.NonNull;
import lombok.val;

public class Economy implements EconomyHook {

	private final HashMap<SkinInstance, EconomyPricePoint> points;

	private final FloatBase base;

	private EconomySource source;

	public Economy(FloatBase base) {
		System.out.println("initializing economy");
		this.base = base;

		points = new HashMap<SkinInstance, EconomyPricePoint>();

	}

	public boolean load(EconomySource source) {
		setSource(source);
		return load();
	}

	public boolean load() {
		System.out.println("filling economy...");
		points.clear();

		base.getSkinInstances().forEach(i -> points.put(i, new EconomyPricePoint()));

		System.out.println("economy now contains " + points.size() + " price points");

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

	public boolean hasPrice(SkinInstance instance) {
		return getPoint(instance).hasPrice();
	}

	public double getPrice(SkinInstance instance) {
		return getPoint(instance).getPrice();
	}

	public double getVolume(SkinInstance instance) {
		return getPoint(instance).getVolume();
	}

	public double getLowestPrice(SkinInstance instance) {
		return getPoint(instance).getLowestPrice();
	}

	public double getMedianPrice(SkinInstance instance) {
		return getPoint(instance).getMedianPrice();
	}

	public boolean hasPrice(Skin skin, Condition condition, Attribute attribute) {
		return getPoint(getInstance(skin, condition, attribute)).hasPrice();
	}

	public double getPrice(Skin skin, Condition condition, Attribute attribute) {
		return getPrice(getInstance(skin, condition, attribute));
	}

	public double getVolume(Skin skin, Condition condition, Attribute attribute) {
		return getPoint(getInstance(skin, condition, attribute)).getVolume();
	}

	public double getLowestPrice(Skin skin, Condition condition, Attribute attribute) {
		return getPoint(getInstance(skin, condition, attribute)).getLowestPrice();
	}

	public double getMedianPrice(Skin skin, Condition condition, Attribute attribute) {
		return getPoint(getInstance(skin, condition, attribute)).getMedianPrice();
	}

	public EconomyPricePoint getPoint(SkinInstance instance) {
		return points.get(instance);
	}

	public SkinInstance getInstance(Skin skin, Condition condition, Attribute attribute) {
		for(SkinInstance i : points.keySet())
			if(i.match(skin, condition, attribute))
				return i;
		return null;
	}

	@Override
	public void put(SkinInstance skin, EconomyPricePoint point) {
		points.replace(skin, point);
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

		points.values().stream()
		.filter(d -> d != null)
		.peek(d -> found.incrementAndGet())
		.filter(d -> d.hasPrice())
		.forEach(d -> loaded.incrementAndGet());

		val setPercent = (int) ((100d / points.size()) * loaded.get());

		System.out.println("economy analysis results:");
		System.out.println("total entries: " + points.size());
		System.out.println("set entries: " + loaded.get() + " ~" + setPercent + "%");
		System.out.println("null entries: " + (points.size() - found.get()));
	}

}
