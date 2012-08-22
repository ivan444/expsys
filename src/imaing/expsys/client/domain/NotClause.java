package imaing.expsys.client.domain;

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
		setNsRight(Integer.valueOf(val+1));
		return getNsRight().intValue();
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

}
