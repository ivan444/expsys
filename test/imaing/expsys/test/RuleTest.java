package imaing.expsys.test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import imaing.expsys.client.domain.AndClause;
import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.Literal;
import imaing.expsys.client.domain.LogClause;
import imaing.expsys.client.domain.NotClause;
import imaing.expsys.client.domain.OrClause;
import imaing.expsys.client.domain.Rule;
import imaing.expsys.shared.exceptions.InvalidDataException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
@Transactional
public class RuleTest {
//	@Autowired private RuleDAO ruleDao;
	
	@Test
	public void shouldCorrectlyAnnotateLogClausesNestedSets() throws InvalidDataException {
		Characteristic chr1 = new Characteristic();
		Characteristic chr2 = new Characteristic();
		
		Rule rule = new Rule();
		
		LogClause logClause = new AndClause(
						new OrClause(
								new Literal(chr1, 3),
								new NotClause(new Literal(chr2, 1))),
						new NotClause(
								new Literal(chr2, 3)));
		rule.setLogClause(logClause);
		
		String msg = "Nested set values aren't correct";
		Assert.assertEquals(msg, Integer.valueOf(15), rule.getNsRoot());
		
		try {
			AndClause orgAnd = (AndClause) logClause;
			Assert.assertEquals(msg, Integer.valueOf(1), orgAnd.getNsLeft());
			Assert.assertEquals(msg, Integer.valueOf(14), orgAnd.getNsRight());
				OrClause orgOr = (OrClause) orgAnd.getLeftClause();
				Assert.assertEquals(msg, Integer.valueOf(2), orgOr.getNsLeft());
				Assert.assertEquals(msg, Integer.valueOf(9), orgOr.getNsRight());
					Assert.assertEquals(msg, Integer.valueOf(3), orgOr.getLeftClause().getNsLeft());
					Assert.assertEquals(msg, Integer.valueOf(4), orgOr.getLeftClause().getNsRight());
					
					NotClause orgOrNot = (NotClause) orgOr.getRightClause();
					Assert.assertEquals(msg, Integer.valueOf(5), orgOrNot.getNsLeft());
					Assert.assertEquals(msg, Integer.valueOf(8), orgOrNot.getNsRight());
						Assert.assertEquals(msg, Integer.valueOf(6), orgOrNot.getLeftClause().getNsLeft());
						Assert.assertEquals(msg, Integer.valueOf(7), orgOrNot.getLeftClause().getNsRight());
			
				NotClause orgNot = (NotClause) orgAnd.getRightClause();
					Assert.assertEquals(msg, Integer.valueOf(10), orgNot.getNsLeft());
					Assert.assertEquals(msg, Integer.valueOf(13), orgNot.getNsRight());
						Assert.assertEquals(msg, Integer.valueOf(11), orgNot.getLeftClause().getNsLeft());
						Assert.assertEquals(msg, Integer.valueOf(12), orgNot.getLeftClause().getNsRight());
					
		} catch(ClassCastException e) {
			Assert.fail("Clause types are incorrect");
		}
	}
	
	@Test
	public void shouldIterateThroughLogClausesInOrderParentLeftRight() {
		Characteristic chr1 = new Characteristic();
		Characteristic chr2 = new Characteristic();
		
		Rule rule = new Rule();
		
		LogClause logClause = new AndClause(
			new OrClause(
					new Literal(chr1, 3),
					new NotClause(new Literal(chr2, 1))),
			new NotClause(
					new Literal(chr2, 3)));
		rule.setLogClause(logClause);
		
		Iterator<LogClause> logIter = logClause.iterator();
		
		try {
			String msg = "Clause tree is invalid!";
			LogClause lc = logIter.next();
			Assert.assertTrue(msg, lc instanceof AndClause);
				lc = logIter.next();
				Assert.assertTrue(msg, lc instanceof OrClause);
					lc = logIter.next();
					Assert.assertTrue(msg, lc instanceof Literal);
					
					lc = logIter.next();
					Assert.assertTrue(msg, lc instanceof NotClause);
						lc = logIter.next();
						Assert.assertTrue(msg, lc instanceof Literal);
						
				lc = logIter.next();
				Assert.assertTrue(msg, lc instanceof NotClause);
					lc = logIter.next();
					Assert.assertTrue(msg, lc instanceof Literal);
			
		} catch (NoSuchElementException e) {
			Assert.fail("Failed to iterate through whole clause tree.");
		}
		
		Assert.assertFalse("There shouldn't be any more nodes to iterate.", logIter.hasNext());
	}
}
