package imaing.expsys;

public class Literal implements Rule {
	private final Fuzzyfier fuzzyfier;
	private final FuzzyClass fcls;
	private final Characteristic prodChar;
	
	public Literal(Fuzzyfier fuzzyfier, FuzzyClass fcls, Characteristic prodChar) {
		this.fuzzyfier = fuzzyfier;
		this.fcls = fcls;
		this.prodChar = prodChar;
	}

	@Override
	public double eval(Product p) {
		return fuzzyfier.fuzz(prodChar.filter(p), fcls);
	}
}
