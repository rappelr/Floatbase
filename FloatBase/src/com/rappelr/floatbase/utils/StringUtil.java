package com.rappelr.floatbase.utils;

import java.text.SimpleDateFormat;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat; 
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import lombok.val;

public class StringUtil {

	private static final String PLACEHOLDER = "%``", ESCAPED = "\\\"";

	public static String[] safeSplit(String base) {
		if(base.contains(ESCAPED)) {

			val split = splitLine(base.replace(ESCAPED, PLACEHOLDER));
			for(int i = 0; i < split.length; i++)
				split[i] = split[i].replace(PLACEHOLDER, ESCAPED);
			return split;

		} else
			return splitLine(base);
	}	

	public static String encode(String string) {
		try {
			return URLEncoder.encode(string, java.nio.charset.StandardCharsets.UTF_8.toString()).replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String[] splitLine(String base) {
		val split = base.split("\"");

		switch (split.length) {
		case 1:
			return toArray(split[0]);
		case 2:
			return toArray(split[1]);
		case 3:
			return toArray(split[0], split[2]);
		case 4:
			return toArray(split[1], split[3]);
		case 5:
			return toArray(split[1], split[3]);
		default:
			return new String[0];
		}
	}
	
	public static String flatten(String string) {
		return string.replace(" ", "_").toLowerCase();
	}

	public static String[] chop(String string) {
		return string.split("(?!^)");
	}

	public static String lignQuotes(String base) {
		return "\"" + base + "\"";
	}

	public static String lignParentheses(String base) {
		return "(" + base + ")";
	}

	public static String unLign(String base) {
		return base.substring(1, base.length() - 1);
	}

	public static String[] toArray(final String... strings) {
		return strings;
	}

	public static String formatSeconds(long time) {
		return (new SimpleDateFormat("ss.SSS")).format(new Date(time));
	}

	public static String formatDate() {
		return DateFormat.getInstance().format(new Date()); 
	}

	public static String randomFile() {
		return "default_" + ThreadLocalRandom.current().nextInt(1000) + ".txt";
	}

}
