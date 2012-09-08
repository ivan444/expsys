package imaing.expsys.server.engine;

import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.Literal;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExpSys {

	public static List<String> sortProducts(List<Product> products, List<Rule> rules, List<FuzzyClass> fclses) {
		Map<String, FuzzyClass> fclsByValChr = new HashMap<String, FuzzyClass>();
		for (FuzzyClass fc : fclses) {
			fclsByValChr.put(Literal.makeKey(fc), fc);
		}
		
		Aggregator aggreg = new OrAggregator();
		
		List<ProductScore> prodScores = new LinkedList<ProductScore>();
		for (Product p : products) {
			double score = evaluate(p, aggreg, rules, fclsByValChr);
			prodScores.add(new ProductScore(p, score));
		}
		
		Collections.sort(prodScores);
		List<String> sortedIntegIds = new ArrayList<String>(prodScores.size());
		for (ProductScore ps : prodScores) {
			sortedIntegIds.add(ps.p.getIntegrationId());
		}
		
		return sortedIntegIds;
	}

	private static double evaluate(Product p, Aggregator aggreg, List<Rule> rules, Map<String, FuzzyClass> fclsByValChr) {
		double[] scores = new double[rules.size()];
		for (int i = 0; i < scores.length; i++) {
			scores[i] = rules.get(i).eval(p, fclsByValChr);
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
			else if (diff < 0.0) return -1;
			else return 1;
		}

		@Override
		public String toString() {
			return score + "\t" + p;
		}
		
		
	}
}
