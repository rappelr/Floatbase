package com.rappelr.floatbase.economy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class EconomyPricePoint {

	private static final double NULL_VALUE = -1d;

	@Getter(AccessLevel.PACKAGE) @Setter(AccessLevel.PACKAGE)
	private double lowestPrice, medianPrice;
	
	@Getter(AccessLevel.PACKAGE) @Setter(AccessLevel.PACKAGE)
	private int volume;
	
	public EconomyPricePoint(double price) {
		this(price, price, 1);
	}
	
	public EconomyPricePoint() {
		this(NULL_VALUE);
	}
	
	boolean hasPrice() {
		return lowestPrice != NULL_VALUE || medianPrice != NULL_VALUE;
	}
	
	double getPrice() {
		return medianPrice == NULL_VALUE ? lowestPrice : medianPrice;
	}
	
	void setPrice(double price) {
		lowestPrice = medianPrice = price;
	}

}
