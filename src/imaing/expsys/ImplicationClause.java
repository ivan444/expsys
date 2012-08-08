package imaing.expsys;

public class ImplicationClause extends LogicClause {

	public ImplicationClause(Rule leftLiteral, Rule rightLiteral) {
		super(leftLiteral, rightLiteral);
	}

	@Override
	public double eval(Product p) {
		OrClauseMin or = new OrClauseMin(new NotClause(leftLiteral), rightLiteral);
		return or.eval(p);
	}

}
