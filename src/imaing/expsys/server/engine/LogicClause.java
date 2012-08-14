package imaing.expsys.server.engine;

public abstract class LogicClause implements Rule {
	protected final Rule leftLiteral;
	protected final Rule rightLiteral;
	
	public LogicClause(Rule leftLiteral, Rule rightLiteral) {
		if (leftLiteral == null || rightLiteral == null) {
			throw new IllegalArgumentException("Literals musn't be null!");
		}
		
		this.leftLiteral = leftLiteral;
		this.rightLiteral = rightLiteral;
	}
	
	@Override
	public abstract double eval(Product p);
}
