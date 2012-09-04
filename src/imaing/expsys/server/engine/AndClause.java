package imaing.expsys.server.engine;

public class AndClause extends LogicClause {
	
	public AndClause(IRule leftLiteral, IRule rightLiteral) {
		super(leftLiteral, rightLiteral);
	}
	
	public double eval(IProduct p) {
//		return Math.min(leftLiteral.eval(p), rightLiteral.eval(p));
		double left = leftLiteral.eval(p);
		double right = rightLiteral.eval(p);
		double res = left*right;
		return res;
	}
}
