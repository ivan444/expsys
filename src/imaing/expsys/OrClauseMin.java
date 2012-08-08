package imaing.expsys;

public class OrClauseMin extends LogicClause {
	
	public OrClauseMin(Rule leftLiteral, Rule rightLiteral) {
		super(leftLiteral, rightLiteral);
	}

	public double eval(Product p) {
		return Math.max(leftLiteral.eval(p), rightLiteral.eval(p));
	}
}
