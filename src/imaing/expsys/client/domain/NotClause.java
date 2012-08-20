package imaing.expsys.client.domain;

public class NotClause extends LogClause {
	private LogClause leftClause;
	
	public NotClause() {
	}

	public LogClause getLeftClause() {
		return leftClause;
	}

	public void setLeftClause(LogClause leftClause) {
		this.leftClause = leftClause;
	}

}
