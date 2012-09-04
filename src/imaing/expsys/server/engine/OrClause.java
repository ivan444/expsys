package imaing.expsys.server.engine;

public class OrClause extends LogicClause {
	
	public OrClause(IRule leftLiteral, IRule rightLiteral) {
		super(leftLiteral, rightLiteral);
	}

	public double eval(IProduct p) {
		//return Math.max(leftLiteral.eval(p), rightLiteral.eval(p));
		double left = leftLiteral.eval(p);
		double right = rightLiteral.eval(p);
		double res = left+right - left*right;
		return res;
	}
}
