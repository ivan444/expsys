package imaing.expsys;

public class RelevanceLiteral implements Rule {
	private double[][] mVal = new double[][]{
			{1.0, 0.6, 0.0},
			{0.0, 0.5, 1.0},
			{0.0, 0.0, 0.0},
	};
	
	public RelevanceLiteral() {
	}

	@Override
	public double eval(Product p) {
		return -1;
	}
}
