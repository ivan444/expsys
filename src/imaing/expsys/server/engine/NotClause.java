package imaing.expsys.server.engine;

public class NotClause implements Rule {
	private final Rule literal;
	
	public NotClause(Rule literal) {
		if (literal == null) {
			throw new IllegalArgumentException("Literal musn't be null!");
		}
		this.literal = literal;
	}

	@Override
	public double eval(Product p) {
		return 1-literal.eval(p);
	}

}
