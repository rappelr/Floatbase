package com.rappelr.floatbase.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {

	public static double skew(double a, double min, double max) {
		if(min == max)
			return 0d;
		return min + (a * (max - min));
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
