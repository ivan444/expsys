package imaing.expsys.client.domain;

public class OrClause extends LogClause implements LogClause.HasLeft, LogClause.HasRight {
	private LogClause leftClause;
	
	private LogClause rightClause;
	
	public OrClause() {
	}
	
	public OrClause(LogClause leftClause, LogClause rightClause) {
		this.leftClause = leftClause;
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
	public String toString() {
		return "OR( " + leftClause + ", "+ rightClause + " )";
	}

}
