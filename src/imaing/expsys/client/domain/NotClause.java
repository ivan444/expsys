package imaing.expsys.client.domain;

import java.util.Map;

public class NotClause extends LogClause implements LogClause.HasLeft {
	private LogClause leftClause;
	
	public NotClause() {
	}
	
	@Override
	protected int goLeft(int parentVal) {
		return leftClause.determineNSetVals(parentVal);
	}

	@Override
	protected int goRight(int val) {
		return val;
	}
	
	public NotClause(LogClause leftClause) {
		this.leftClause = leftClause;
	}

	public LogClause getLeftClause() {
		return leftClause;
	}

	public void setLeftClause(LogClause leftClause) {
		this.leftClause = leftClause;
	}
	
	@Override
	public String toString() {
		return "NOT( " + leftClause + " )";
	}

	@Override
	protected double eval(Product p, Map<String, FuzzyClass> fclsByChar) {
		return 1.0-leftClause.eval(p, fclsByChar);
	}

}
