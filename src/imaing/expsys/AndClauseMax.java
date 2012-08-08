package imaing.expsys;

public class AndClauseMax extends LogicClause {
	
	public AndClauseMax(Rule leftLiteral, Rule rightLiteral) {
		super(leftLiteral, rightLiteral);
	}
	
	public double eval(Product p) {
		return Math.min(leftLiteral.eval(p), rightLiteral.eval(p));
	}
}
