package com.rappelr.floatbase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class FloatBaseConfigation {

	public static final int DEBUG_NONE = 0, DEBUG_LIGHT = 1, DEBUG_ALL = 2;

	protected static final FloatBaseConfigation DEFAULT; 

	static {
		DEFAULT = new FloatBaseConfigation("floatbase_processed.txt",
				"C:\\Program Files (x86)\\Steam\\steamapps\\common\\Counter-Strike Global Offensive\\csgo\\scripts\\items\\items_game.txt", 
				"C:\\Program Files (x86)\\Steam\\steamapps\\common\\Counter-Strike Global Offensive\\csgo\\resource\\csgo_english.txt", 
				DEBUG_LIGHT, 
				false);
	}

	@Getter @Setter
	private String processedSourceFilePath, itemsSourceFilePath, languageSourceFilePath;

	@Getter @Setter
	private int debugLevel;

	@Getter @Setter
	private boolean forceReprocess;

}
