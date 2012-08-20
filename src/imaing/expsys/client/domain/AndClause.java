package imaing.expsys.client.domain;

public class AndClause extends LogClause {
	private LogClause leftClause;
	
	private LogClause rightClause;
	
	public AndClause() {
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

}
