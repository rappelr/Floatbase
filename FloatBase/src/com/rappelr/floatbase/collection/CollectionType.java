package com.rappelr.floatbase.collection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.rappelr.floatbase.skin.Attribute;

import lombok.AccessLevel;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CollectionType {
	
	MAP(Attribute.SOUVENIR),
	CASE(Attribute.STATTRAK);
	
	@Getter
	private final Attribute attribute;

}
