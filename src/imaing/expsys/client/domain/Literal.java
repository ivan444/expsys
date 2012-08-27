package imaing.expsys.client.domain;

public class Literal extends LogClause {
	private Characteristic chr;
	
	private Integer fuzzyClsIdx;
	
	public Literal() {
	}
	
	public Literal(Characteristic chr, Integer fuzzyClsIdx) {
		this.chr = chr;
		this.fuzzyClsIdx = fuzzyClsIdx;
	}

	public Characteristic getChr() {
		return chr;
	}

	public void setChr(Characteristic chr) {
		this.chr = chr;
	}

	public Integer getFuzzyClsIdx() {
		return fuzzyClsIdx;
	}

	public void setFuzzyClsIdx(Integer fuzzyClsIdx) {
		this.fuzzyClsIdx = fuzzyClsIdx;
	}

	@Override
	protected int goLeft(int parentVal) {
		return parentVal;
	}

	@Override
	protected int goRight(int val) {
		return val;
	}
	
	@Override
	public String toString() {
		return chr.getName() + " is " + fuzzyClsIdx.intValue();
	}
}
