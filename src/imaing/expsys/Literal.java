package imaing.expsys;

public class Literal implements Rule {
	private final String characteristic;
	private final int fcls;
	
	public Literal(String characteristic, int fcls) {
		this.characteristic = characteristic;
		this.fcls = fcls;
	}

	@Override
	public double eval(Product p) {
		return p.charaMemberVal(characteristic, fcls);
	}
}
