package com.rappelr.floatbase.gamesource;

import java.io.File;
import java.util.List;

import com.rappelr.floatbase.FloatBase;
import com.rappelr.floatbase.gamesource.object.SourceSection;

public class ProcessedSource extends GameSource {
	
	@Override
	public boolean load() {
		return super.load();
	}
	
	public List<SourceSection> getCollections() {
		return getSection("collections").getChildren();
	}

	public File getFile(FloatBase base) {
		if(!hasFile())
			setFile(new File(base.getConfiguration().getProcessedSourceFilePath()));
		return file;
	}

}
