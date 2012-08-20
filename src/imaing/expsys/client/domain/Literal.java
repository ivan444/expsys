package imaing.expsys.client.domain;

public class Literal extends LogClause {
	private Characteristic chr;
	
	private Integer fuzzyClsIdx;
	
	public Literal() {
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
}
