package imaing.expsys.client.domain;

public class FuzzyClass extends DTOObject {
	private Characteristic chr;
	private String value;
	private double[] membershipVal;

	public Characteristic getChr() {
		return chr;
	}

	public void setChr(Characteristic chr) {
		this.chr = chr;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public double[] getMembershipVal() {
		return membershipVal;
	}
	
	public double getMembershipVal(int idx) {
		return membershipVal[idx];
	}

	public void setMembershipVal(double[] membershipVal) {
		this.membershipVal = membershipVal;
	}
	
	public void setMembershipVal(int idx, double val) {
		this.membershipVal[idx] = val;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chr == null) ? 0 : chr.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FuzzyClass other = (FuzzyClass) obj;
		if (chr == null) {
			if (other.chr != null)
				return false;
		} else if (!chr.equals(other.chr))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
