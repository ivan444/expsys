package imaing.expsys.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum Relevance implements IsSerializable {
	REL_HIGH(0.9),
	REL_MID(0.6),
	REL_LOW(0.3);
	
	private final double memberValue;
	
	private Relevance(double memberValue) {
		this.memberValue = memberValue;
	}
	
	public double mval() { return memberValue; }
}