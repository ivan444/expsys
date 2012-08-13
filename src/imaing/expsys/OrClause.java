package imaing.expsys;

public class OrClause extends LogicClause {
	
	public OrClause(Rule leftLiteral, Rule rightLiteral) {
		super(leftLiteral, rightLiteral);
	}

	public double eval(Product p) {
		//return Math.max(leftLiteral.eval(p), rightLiteral.eval(p));
		double left = leftLiteral.eval(p);
		double right = rightLiteral.eval(p);
		double res = left+right - left*right;
		return res;
	}
}
