package imaing.expsys.client.domain;


public abstract class LogClause extends DTOObject {
	protected Rule rule;
	
	protected Integer nsLeft;
	
	protected Integer nsRight;

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public Integer getNsLeft() {
		return nsLeft;
	}

	public void setNsLeft(Integer nsLeft) {
		this.nsLeft = nsLeft;
	}

	public Integer getNsRight() {
		return nsRight;
	}

	public void setNsRight(Integer nsRight) {
		this.nsRight = nsRight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nsLeft == null) ? 0 : nsLeft.hashCode());
		result = prime * result + ((nsRight == null) ? 0 : nsRight.hashCode());
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
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
		LogClause other = (LogClause) obj;
		if (nsLeft == null) {
			if (other.nsLeft != null)
				return false;
		} else if (!nsLeft.equals(other.nsLeft))
			return false;
		if (nsRight == null) {
			if (other.nsRight != null)
				return false;
		} else if (!nsRight.equals(other.nsRight))
			return false;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		return true;
	}
}
