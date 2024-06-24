package com.aflac.core.util;

public enum MasterAppProductSequence {
	
	AC(1),
	CI(2),
	HI(3),
	D(4),
	DI(5),
	TL(6),
	WL(7);
	
	private int value;
	
	MasterAppProductSequence(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}

}
