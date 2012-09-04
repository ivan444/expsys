package imaing.expsys.server.engine;

public class Literal implements IRule {
	private final String characteristic;
	private final int fcls;
	
	public Literal(String characteristic, int fcls) {
		this.characteristic = characteristic;
		this.fcls = fcls;
	}

	@Override
	public double eval(IProduct p) {
		return p.charaMemberVal(characteristic, fcls);
	}
}
