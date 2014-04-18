package com.nibado.test.kryo;

public class UUID {
	
	private long mostSigBits, leastSigBits;
	public UUID(long mostSigBits, long leastSigBits) {
		this.mostSigBits = mostSigBits;
		this.leastSigBits = leastSigBits;
	}
	public long getMostSigBits() {
		return mostSigBits;
	}
	public void setMostSigBits(long mostSigBits) {
		this.mostSigBits = mostSigBits;
	}
	public long getLeastSigBits() {
		return leastSigBits;
	}
	public void setLeastSigBits(long leastSigBits) {
		this.leastSigBits = leastSigBits;
	}
	
	
}
