package imaing.expsys.client.domain;

import java.util.List;
import java.util.Map;

public class Literal extends LogClause {
	public static final String KEY_SEPARATOR = "\t";
	
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
	
	public static String makeKey(FuzzyClass fc) {
		return makeKey(fc.getChr().getName(), fc.getValue());
	}
	
	public static String makeKey(String chrName, String val) {
		return chrName + KEY_SEPARATOR + val;
	}

	@Override
	protected double eval(Product p, Map<String, FuzzyClass> fclsByChar) {
		String chrName = chr.getName();
		String val = null;
		List<ProdChr> chrs = p.getCharacteristics();
		for (ProdChr pc : chrs) {
			if (chrName.equals(pc.getChr().getName())) {
				val = pc.getValue();
				break;
			}
		}
		
		String key = makeKey(chrName, val);
		FuzzyClass fc = fclsByChar.get(key);
		if (fc == null) return 0.0;
		
		double litVal = fc.getMembershipVal(fuzzyClsIdx.intValue());
		return litVal;
	}
}
