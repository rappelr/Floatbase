package com.rappelr.floatbase.source;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.rappelr.floatbase.source.object.SourceSection;

public class Language extends GameSource {
	
	private static final Charset CHARSET = StandardCharsets.UTF_16LE;
	
	private SourceSection tokens;
	
	@Override
	public boolean load() {
		if(!super.load())
			return false;
		
		try {
			tokens = reachSection("lang.Tokens");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected Charset getCharSet() {
		return CHARSET;
	}
	
	@Override
	public String getString(String token) {
		return tokens.getString(token.substring(1));
	}
}
