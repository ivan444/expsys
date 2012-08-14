package imaing.expsys.server.engine;

public class AvgAggregator implements Aggregator {

	@Override
	public double aggregate(double... vals) {
		if (vals.length == 0) return 0.0;
		
		double sum = 0;
		for (int i = 0; i < vals.length; i++) {
			sum += vals[i];
		}
		
		return sum/vals.length;
	}

}
