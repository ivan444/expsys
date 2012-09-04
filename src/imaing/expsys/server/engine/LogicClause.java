package imaing.expsys.server.engine;

public abstract class LogicClause implements IRule {
	protected final IRule leftLiteral;
	protected final IRule rightLiteral;
	
	public LogicClause(IRule leftLiteral, IRule rightLiteral) {
		if (leftLiteral == null || rightLiteral == null) {
			throw new IllegalArgumentException("Literals musn't be null!");
		}
		
		this.leftLiteral = leftLiteral;
		this.rightLiteral = rightLiteral;
	}
	
	@Override
	public abstract double eval(IProduct p);
}
