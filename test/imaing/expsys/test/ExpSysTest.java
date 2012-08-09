package imaing.expsys.test;

import imaing.expsys.AndClauseMax;
import imaing.expsys.ImplicationClause;
import imaing.expsys.Literal;
import imaing.expsys.Product;
import imaing.expsys.RelevanceLiteral;
import imaing.expsys.Rule;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ExpSysTest {
	private List<Rule> rules;
	private List<Product> laptops;
	
	public void makeRules() {
		rules.add(new ImplicationClause(
				new AndClauseMax(new AndClauseMax(leftLiteral, rightLiteral),
						new Literal(fcls, characteristic)),
				new RelevanceLiteral()));
	}
	
	public void makeProducts() {
		
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
