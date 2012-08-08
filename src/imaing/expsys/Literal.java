package imaing.expsys;

public class Literal implements Rule {
	private final double memberValue;

	public Literal(double memberValue) {
		this.memberValue = memberValue;
	}

	@Override
	public double eval(Product p) {
		return memberValue;
	}
}
