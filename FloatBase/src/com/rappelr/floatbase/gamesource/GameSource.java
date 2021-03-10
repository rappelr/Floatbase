package com.rappelr.floatbase.gamesource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.rappelr.floatbase.FloatBaseConfigation;
import com.rappelr.floatbase.gamesource.object.SourceObject;
import com.rappelr.floatbase.gamesource.object.SourceSection;
import com.rappelr.floatbase.utils.StringUtil;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.val;

public class GameSource extends SourceSection {

	@Getter(AccessLevel.PROTECTED)
	private final Charset charSet;
	
	private String path;

	protected File file;

	@Setter
	private int debugLevel = 0;
	
	{
		path = StringUtil.randomFile();
		charSet = StandardCharsets.UTF_8;
	}

	public GameSource(File file) {
		super("root");
		setFile(file);
	}

	public GameSource() {
		super("root");
		file = null;
		path = null;
	}
	
	public boolean load() {
		if(!hasFile()) {
			info("no path set for gamesource " + this);
			return false;
		}
		
		try {
			safeLoad();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void safeLoad() throws IOException {
		info("preparing source " + file.getName());

		int failures = 0, duplicateSections = 0, duplicateValues = 0, currentLine = 0;

		if(!file.exists()) {
			info("source file does not exist");
			return;
		}

		val reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), getCharSet()));
		try {
			val sectionStack = new ArrayList<SourceSection>();
			sectionStack.add(this);
			String line = "", prev = null;

			while((line = reader.readLine()) != null) {
				currentLine++;

				val last = sectionStack.get(sectionStack.size() - 1);

				//catch comments
				val i = line.indexOf("//");
				if(i != -1) {
					if(i == 0)
						continue;
					
					val lower = line.substring(0, i).replace("\t", " ");

					if(!lower.isBlank())
						line = lower;
					else
						continue;
				}

				if(line.endsWith("{")) {
					if(last.contains(prev)) {
						debug("merged duplicate section (" + currentLine + "): " + prev);
						sectionStack.add(last.getSection(prev));
						duplicateSections++;
					} else
						sectionStack.add(SourceSection.create(prev));
					prev = null;
				} 
				
				else if (line.endsWith("}")) {
					if(sectionStack.size() == 1)
						continue;

					sectionStack.remove(last);
					sectionStack.get(sectionStack.size() - 1).add(last);
					prev = null;
				}
				
				else {
					val split = StringUtil.safeSplit(line.replace("\"\"", "\" \""));

					if(split.length != 1) {
						SourceObject obj = SourceObject.of(split);

						if(obj == null)
							obj = SourceObject.of(StringUtil.safeSplit(line.substring(0, line.indexOf("//"))));

						if(obj != null) {
							if(last.add(obj)) {
								debug("replaced duplicate value (" + currentLine + "): " + line);
								duplicateValues++;
							}
						} else {
							debug("deserialization failure (" + currentLine + "): " + line);
							failures++;
						}
					} else
						prev = split[0];
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			info("read " + currentLine + " lines");
			info("closing reader");
			reader.close();
			info("finished reading " + file.getName());
			info("failed: " + failures + ", merged: " + duplicateSections + ", replaced: " + duplicateValues);
		}
	}
	
	public void setPath(@NonNull String path) {
		this.path = path;
		file = new File(path);
	}
	
	public void setFile(@NonNull File file) {
		this.file = file;
		path = file.getAbsolutePath();
	}
	
	public boolean hasFile() {
		return file != null;
	}
	
	public boolean exists() {
		return hasFile() ? file.exists() : false;
	}
	
	public String getName() {
		if(hasFile())
			return file.getName();
		return path;
	}

	private void debug(final String message) {
		if(debugLevel >= FloatBaseConfigation.DEBUG_ALL)
			System.out.println(message);
	}

	private void info(final String message) {
		if(debugLevel >= FloatBaseConfigation.DEBUG_LIGHT)
			System.out.println(message);
	}

}
