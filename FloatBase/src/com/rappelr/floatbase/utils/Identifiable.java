package com.rappelr.floatbase.utils;

public abstract class Identifiable {
	
	private String id = null;
	
	public String getId() {
		if(id == null)
			id = generateId();
		return id;
	}
	
	protected abstract String generateId();

}
