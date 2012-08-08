package imaing.expsys;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ExpSys {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Product> products = new LinkedList<Product>();
		List<Rule> rules = new LinkedList<Rule>();
		Aggregator aggreg = new AvgAggregator();
		
		List<ProductScore> prodScores = new LinkedList<ProductScore>();
		for (Product p : products) {
			double score = evaluate(p, aggreg, rules);
			prodScores.add(new ProductScore(p, score));
		}
		
		Collections.sort(prodScores);
		for (ProductScore ps : prodScores) {
			System.out.println(ps);
		}
	}

	private static double evaluate(Product p, Aggregator aggreg, List<Rule> rules) {
		double[] scores = new double[rules.size()];
		for (int i = 0; i < scores.length; i++) {
			scores[i] = rules.get(i).eval(p);
		}
		
		return aggreg.aggregate(scores);
	}

	private static class ProductScore implements Comparable<ProductScore> {
		public final Product p;
		public final double score;
		
		public ProductScore(Product p, double score) {
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
