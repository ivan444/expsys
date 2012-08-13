package imaing.expsys;

public class ImplicationClause implements Rule {

	public enum Relevance {
		REL_HIGH(1.0),
		REL_MID(0.7),
		REL_LOW(0.5);
		
		private final double memberValue;
		
		private Relevance(double memberValue) {
			this.memberValue = memberValue;
		}
		
		public double mval() { return memberValue; }
	}
	
	private final Rule leftLiteral;
	private final Relevance rel;
	
	public ImplicationClause(Rule leftLiteral, Relevance rel) {
		if (leftLiteral == null) {
			throw new IllegalArgumentException("Literal musn't be null!");
		}
		
		this.leftLiteral = leftLiteral;
		this.rel = rel;
	}

	@Override
	public double eval(Product p) {
//		OrClause or = new OrClause(new NotClause(leftLiteral), new Rule() {
//				@Override
//				public double eval(Product p) {
//					return rel.mval();
//				}
//			});
//		
//		return or.eval(p);
		AndClause and = new AndClause(leftLiteral, new Rule() {
			@Override
			public double eval(Product p) {
				return rel.mval();
			}
		});
		
		return and.eval(p);
	}

}
