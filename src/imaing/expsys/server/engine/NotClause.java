package imaing.expsys.server.engine;

public class NotClause implements IRule {
	private final IRule literal;
	
	public NotClause(IRule literal) {
		if (literal == null) {
			throw new IllegalArgumentException("Literal musn't be null!");
		}
		this.literal = literal;
	}

	@Override
	public double eval(IProduct p) {
		return 1-literal.eval(p);
	}

}
