package imaing.expsys.server.engine;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ExpSys {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<IProduct> products = new LinkedList<IProduct>();
		List<IRule> rules = new LinkedList<IRule>();
		Aggregator aggreg = new AvgAggregator();
		
		List<ProductScore> prodScores = new LinkedList<ProductScore>();
		for (IProduct p : products) {
			double score = evaluate(p, aggreg, rules);
			prodScores.add(new ProductScore(p, score));
		}
		
		Collections.sort(prodScores);
		for (ProductScore ps : prodScores) {
			System.out.println(ps);
		}
	}

	private static double evaluate(IProduct p, Aggregator aggreg, List<IRule> rules) {
		double[] scores = new double[rules.size()];
		for (int i = 0; i < scores.length; i++) {
			scores[i] = rules.get(i).eval(p);
		}
		
		return aggreg.aggregate(scores);
	}

	private static class ProductScore implements Comparable<ProductScore> {
		public final IProduct p;
		public final double score;
		
		public ProductScore(IProduct p, double score) {
			super();
			this.p = p;
			this.score = score;
		}
		
		@Override
		public int compareTo(ProductScore o) {
			double diff = score-o.score;
			if (Math.abs(diff) < 1e-6) return 0;
			else return (int) diff;
		}

		@Override
		public String toString() {
			return score + "\t" + p;
		}
		
		
	}
}
