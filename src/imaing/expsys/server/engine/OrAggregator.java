package imaing.expsys.server.engine;

public class OrAggregator implements Aggregator {

	@Override
	public double aggregate(double ... vals) {
		double res = 0.0;
		
		for (int i = 0; i < vals.length; i++) {
			res = res+vals[i] - res*vals[i];
		}
		
		return res;
	}

}
