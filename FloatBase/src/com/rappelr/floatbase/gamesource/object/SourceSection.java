package com.rappelr.floatbase.gamesource.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import lombok.val;

public class SourceSection extends SourceObject {

	protected final HashMap<String, SourceObject> contents;

	protected SourceSection(String key) {
		super(key);

		contents = new HashMap<String, SourceObject>();
	}
	
	protected static SourceSection create(String key) {
		return new SourceSection(key);
	}

	public boolean add(SourceObject object) {
		return contents.put(object.getKey(), object) != null;
	}

	public boolean contains(String key) {
		return contents.containsKey(key);
	}

	public boolean match(String key, String value) {
		String found = getString(key);
		if(found == null)
			return value == null;
		return found.contentEquals(value);
	}

	public boolean match(String key, int value) {
		return value == getInteger(key);
	}

	public boolean match(String key, double value) {
		return value == getDouble(key);
	}

	public boolean match(String key, SourceSection section) {
		SourceSection found = getSection(key);
		if(found == null)
			return section == null;
		return found.equals(section);
	}

	public boolean reachMatch(String key, String value) {
		String found = reachString(key);
		if(found == null)
			return value == null;
		return found.contentEquals(value);
	}

	public boolean reachMatch(String key, int value) {
		return value == reachInteger(key);
	}

	public boolean reachMatch(String key, double value) {
		return value == reachDouble(key);
	}

	public boolean reachMatch(String key, SourceSection section) {
		SourceSection found = reachSection(key);
		if(found == null)
			return section == null;
		return found.equals(section);
	}

	public SourceObject get(String key) {
		return contents.get(key);
	}
	
	public SourceObject reach(String path) {
		if(!path.contains("."))
			return get(path);
		
		val i = path.indexOf(".");
		val section = getSection(path.substring(0, i));
		
		if(section != null)
			return section.reach(path.substring(i + 1));
		
		return null;
	}

	public SourceSection getSection(String key) {
		val obj = get(key);
		return obj instanceof SourceSection ? (SourceSection) obj : null;
	}

	public SourceSection reachSection(String path) {
		val result = reach(path);
		if(result instanceof SourceSection)
			return (SourceSection) result;
		return null;
	}

	public SourceSection where(String key, String value) {
		for(SourceSection child : getChildren())
			if(child.match(key, value))
				return child;
		return null;
	}

	public SourceSection where(String key, int value) {
		for(SourceSection child : getChildren())
			if(child.match(key, value))
				return child;
		return null;
	}

	public SourceSection where(String key, double value) {
		for(SourceSection child : getChildren())
			if(child.match(key, value))
				return child;
		return null;
	}

	public SourceSection where(String key, SourceSection section) {
		for(SourceSection child : getChildren())
			if(child.match(key, section))
				return child;
		return null;
	}

	public SourceSection reachWhere(String key, String value) {
		for(SourceSection child : getChildren())
			if(child.reachMatch(key, value))
				return child;
		return null;
	}

	public SourceSection reachWhere(String key, int value) {
		for(SourceSection child : getChildren())
			if(child.reachMatch(key, value))
				return child;
		return null;
	}

	public SourceSection reachWhere(String key, double value) {
		for(SourceSection child : getChildren())
			if(child.reachMatch(key, value))
				return child;
		return null;
	}

	public SourceSection reachWhere(String key, SourceSection section) {
		for(SourceSection child : getChildren())
			if(child.reachMatch(key, section))
				return child;
		return null;
	}
	
	public String getString(String key) {
		val obj = get(key);
		return obj instanceof SourceString ? ((SourceString) obj).getValue() : null;
	}

	public String reachString(String path) {
		val result = reach(path);
		if(result instanceof SourceString)
			return ((SourceString) result).getValue();
		return null;
	}

	public double getDouble(String key) {
		val obj = get(key);
		return obj instanceof SourceDouble ? ((SourceDouble) obj).getValue() : -1d;
	}

	public double reachDouble(String path) {
		val result = reach(path);
		if(result instanceof SourceDouble)
			return ((SourceDouble) result).getValue();
		return -1d;
	}

	public int getInteger(String key) {
		val obj = get(key);
		return obj instanceof SourceInteger ? ((SourceInteger) obj).getValue() : -1;
	}

	public int reachInteger(String path) {
		val result = reach(path);
		if(result instanceof SourceInteger)
			return ((SourceInteger) result).getValue();
		return -1;
	}

	public List<SourceSection> getChildren() {
		val children = new ArrayList<SourceSection>();

		contents.values().stream()
				.filter(o -> o instanceof SourceSection)
				.forEach(o -> children.add((SourceSection) o));

		return children;
	}

	public Set<String> getKeys() {
		return contents.keySet();
	}

	@Override
	public void print(String prefix) {
		System.out.println(prefix + getKey() + " {");
		for(SourceObject obj : contents.values())
			obj.print(prefix + "  ");
		System.out.println(prefix + "}");
	}
	
	public void print(String prefix, boolean deep) {
		if(deep)
			print(prefix);
		
		else {
			System.out.println(prefix + getKey() + " {");
			for(SourceObject obj : contents.values()) {
				if(obj instanceof SourceSection) {
					val sec = (SourceSection) obj;
					System.out.println(prefix + "  " + sec.getKey() + ": Section (" + sec.getKeys().size() + ")");
				} else
					obj.print(prefix + "  ");
			}
			System.out.println(prefix + "}");
		}
	}
	
	public void print(boolean deep) {
		print("", deep);
	}

}
