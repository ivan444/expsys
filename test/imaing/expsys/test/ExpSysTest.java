package imaing.expsys.test;

import imaing.expsys.server.engine.Aggregator;
import imaing.expsys.server.engine.AndClause;
import imaing.expsys.server.engine.AvgAggregator;
import imaing.expsys.server.engine.ImplicationClause;
import imaing.expsys.server.engine.Laptop;
import imaing.expsys.server.engine.Literal;
import imaing.expsys.server.engine.Product;
import imaing.expsys.server.engine.Rule;
import imaing.expsys.server.engine.ImplicationClause.Relevance;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ExpSysTest {
	private List<Rule> rules;
	private List<Product> laptops;
	private Aggregator aggreg;
	
	public void makeRules() {
		rules = new LinkedList<Rule>();
		
		rules.add(new ImplicationClause(
				new AndClause(
					new AndClause(
						new Literal("proc", 0), new Literal("mon", 0)),
					new Literal("hdd", 0)),
				Relevance.REL_HIGH));
		
		rules.add(new ImplicationClause(
				new AndClause(
					new AndClause(
						new Literal("proc", 0), new Literal("mon", 0)),
					new Literal("hdd", 0)),
				Relevance.REL_MID));
		
		rules.add(new ImplicationClause(
				new AndClause(new Literal("mon", 2), new Literal("proc", 2)),
				Relevance.REL_MID));
		
		rules.add(new ImplicationClause(
				new Literal("mon", 2), Relevance.REL_LOW));
		
		rules.add(new ImplicationClause(
				new Literal("mon", 2), Relevance.REL_HIGH));
	}
	
	public void makeProducts() {
		laptops = new LinkedList<Product>();
		
		laptops.add(new Laptop("1", "i7", "17in", "1000GB"));
		laptops.add(new Laptop("2", "i7", "10in", "1000GB"));
		laptops.add(new Laptop("3", "P4", "10in", "1000GB"));
		laptops.add(new Laptop("4", "CoreDuo", "13.3in", "250GB"));
		laptops.add(new Laptop("5", "i7", "10in", "1000GB"));
		laptops.add(new Laptop("6", "CoreDuo", "15in", "1000GB"));
		laptops.add(new Laptop("7", "i5", "15in", "500GB"));
		laptops.add(new Laptop("8", "i3", "13.3in", "100GB"));
		laptops.add(new Laptop("9", "i7", "17in", "80GB"));
		laptops.add(new Laptop("10", "i7", "17in", "750GB"));
		laptops.add(new Laptop("11", "i7", "13.3in", "1000GB"));
	}
	
	@Before
	public void init() {
		makeRules();
		makeProducts();
		
		aggreg = new AvgAggregator();
	}
	
	@Test
	public void evalRuleMultimediaHighTest() {
		List<Product> srt = evalRules(rules.get(0));
		Assert.assertEquals(srt.get(0).describe(), "1");
	}
	
	@Test
	public void evalRuleHighMultiLowMonLowTest() {
		List<Product> srt = evalRules(rules.get(0), rules.get(3));
		Assert.assertEquals(srt.get(0).describe(), "1");
	}
	
	@Test
	public void evalRuleHighMultiLowMonHighTest() {
		List<Product> srt = evalRules(rules.get(0), rules.get(4));
		Assert.assertEquals(srt.get(0).describe(), "2");
	}
	
	public List<Product> evalRules(Rule ... rules) {
		List<ProductScore> prodScores = new LinkedList<ProductScore>();
		for (Product p : laptops) {
			double score = evaluate(p, aggreg, rules);
			prodScores.add(new ProductScore(p, score));
		}
		
		Collections.sort(prodScores);
		List<Product> sortProd = new LinkedList<Product>();
		System.out.println();
		for (ProductScore ps : prodScores) {
			sortProd.add(ps.p);
			System.out.println(ps);
		}
		
		return sortProd;
	}

	private static double evaluate(Product p, Aggregator aggreg, Rule ... rules) {
		double[] scores = new double[rules.length];
		for (int i = 0; i < scores.length; i++) {
			scores[i] = rules[i].eval(p);
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
			else if (diff < 0) return 1;
			else return -1;
		}

		@Override
		public String toString() {
			return score + "\t" + p;
		}
	}

}