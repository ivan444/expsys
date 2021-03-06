package imaing.expsys.client.domain;

import java.util.Map;

public class AndClause extends LogClause implements LogClause.HasLeft, LogClause.HasRight {
	private LogClause leftClause;
	
	private LogClause rightClause;
	
	public AndClause() {
	}
	
	public AndClause(LogClause leftClause, LogClause rightClause) {
		this.leftClause = leftClause;
		this.rightClause = rightClause;
	}

	public LogClause getLeftClause() {
		return leftClause;
	}

	public void setLeftClause(LogClause leftClause) {
		this.leftClause = leftClause;
	}

	public LogClause getRightClause() {
		return rightClause;
	}

	public void setRightClause(LogClause rightClause) {
		this.rightClause = rightClause;
	}

	@Override
	protected int goLeft(int parentVal) {
		return leftClause.determineNSetVals(parentVal);
	}

	@Override
	protected int goRight(int val) {
		return rightClause.determineNSetVals(val);
	}

	@Override
	public String toString() {
		return "AND( " + leftClause + ", "+ rightClause + " )";
	}

	@Override
	protected double eval(Product p, Map<String, FuzzyClass> fclsByChar) {
		double left = leftClause.eval(p, fclsByChar);
		double right = 0.0;
		if (left != 0.0) {
			right = rightClause.eval(p, fclsByChar);
		}
		
		return left*right;
	}

}
