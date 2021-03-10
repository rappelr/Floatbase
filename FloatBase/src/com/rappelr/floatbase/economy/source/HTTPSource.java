package com.rappelr.floatbase.economy.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class HTTPSource extends EconomySource {
	
	@Getter
	private final String url;

	@Override
	public boolean load() {
		if(!checkHook())
			return false;
		
		System.out.println("loading http prices...");
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(new URL(getUrl()).openStream(), StandardCharsets.UTF_8));
		} catch (IOException e) {
			System.out.println("failed to open url " + getUrl());
			e.printStackTrace();
			return false;
		}
		
		try {
			val line = in.readLine();

			in.close();

			return process(line);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("failed to process content");
			return false;
		}
	}
	
	protected abstract boolean process(String content);

}
