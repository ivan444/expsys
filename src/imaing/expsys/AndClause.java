package imaing.expsys;

public class AndClause extends LogicClause {
	
	public AndClause(Rule leftLiteral, Rule rightLiteral) {
		super(leftLiteral, rightLiteral);
	}
	
	public double eval(Product p) {
//		return Math.min(leftLiteral.eval(p), rightLiteral.eval(p));
		double left = leftLiteral.eval(p);
		double right = rightLiteral.eval(p);
		double res = left*right;
		return res;
	}
}
