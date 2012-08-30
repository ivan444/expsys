package imaing.expsys.client.domain;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class LogClause extends DTOObject implements Iterable<LogClause> {
	protected Rule rule;
	
	protected Integer nsLeft;
	
	protected Integer nsRight;

	public Rule getRule() {
		return rule;
	}
	
	public int determineNSetVals(int parentVal) {
		setNsLeft(Integer.valueOf(parentVal+1));
		int leftVal = goLeft(getNsLeft().intValue());
		int rightVal = goRight(leftVal);
		setNsRight(Integer.valueOf(rightVal+1));
		
		return getNsRight().intValue();
	}

	protected abstract int goLeft(int parentVal);
	protected abstract int goRight(int val);

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
	
	public interface HasLeft {
		LogClause getLeftClause();
		void setLeftClause(LogClause clause);
	}
	
	public interface HasRight {
		LogClause getRightClause();
		void setRightClause(LogClause clause);
	}

	@Override
	public Iterator<LogClause> iterator() {
		final LogClause me = this;
		return new Iterator<LogClause>() {
			private boolean wentLeft = false;
			private boolean wentRight = false;
			private boolean didMe = false;
			private Iterator<LogClause> lowerIter = null;
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException("Unimplemented!");
			}
			
			@Override
			public LogClause next() {
				if (!didMe) {
					didMe = true;
					return me;
				}
				
				if (lowerIter != null && lowerIter.hasNext()) {
					return lowerIter.next();
				} else {
					boolean isSet = setNextIter();
					if (isSet) return lowerIter.next();
					else throw new NoSuchElementException("There are no more logic clauses!");
				}
			}
			
			private boolean setNextIter() {
				if (!didMe) return true; // new iter isn't set, but current iter is good to go!
				
				if (!wentLeft && me instanceof HasLeft) {
					wentLeft = true;
					lowerIter = ((HasLeft) me).getLeftClause().iterator();
					return true;
					
				} else if (!wentRight && me instanceof HasRight) {
					wentRight = true;
					lowerIter = ((HasRight) me).getRightClause().iterator();
					return true;
				}
				
				return false;
			}
			
			@Override
			public boolean hasNext() {
				boolean iterHas = !didMe || (lowerIter != null && lowerIter.hasNext());
				if (!iterHas) {
					boolean isSet = setNextIter();
					if (!isSet) return false;
					else return lowerIter.hasNext();
				} else {
					return true;
				}
			}
		};
	}
}
