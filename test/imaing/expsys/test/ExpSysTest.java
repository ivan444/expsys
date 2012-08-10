package imaing.expsys.test;

import imaing.expsys.AndClauseMax;
import imaing.expsys.ImplicationClause;
import imaing.expsys.ImplicationClause.Relevance;
import imaing.expsys.Laptop;
import imaing.expsys.Literal;
import imaing.expsys.Product;
import imaing.expsys.Rule;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ExpSysTest {
	private List<Rule> rules;
	private List<Product> laptops;
	
	public void makeRules() {
		rules = new LinkedList<Rule>();
		
		rules.add(new ImplicationClause(
				new AndClauseMax(
					new AndClauseMax(
						new Literal("proc", 0), new Literal("mon", 0)),
					new Literal("hdd", 0)),
				Relevance.REL_HIGH));
		
		rules.add(new ImplicationClause(
				new AndClauseMax(
					new AndClauseMax(
						new Literal("proc", 0), new Literal("mon", 0)),
					new Literal("hdd", 0)),
				Relevance.REL_MID));
		
		rules.add(new ImplicationClause(
				new AndClauseMax(new Literal("mon", 2), new Literal("proc", 2)),
				Relevance.REL_MID));
		
		rules.add(new ImplicationClause(
				new Literal("mon", 2), Relevance.REL_LOW));
		
		rules.add(new ImplicationClause(
				new Literal("mon", 2), Relevance.REL_HIGH));
	}
	
	public void makeProducts() {
		laptops = new LinkedList<Product>();
		
		laptops.add(new Laptop("1", "i7", "17in", "1000GB"));
	}
	
	@Before
	public void init() {
		makeRules();
		makeProducts();
	}
	
	@Test
	public void evalRule1Test() {
		
		
	}

}
