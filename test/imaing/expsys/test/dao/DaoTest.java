package imaing.expsys.test.dao;

import imaing.expsys.client.domain.AndClause;
import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.Literal;
import imaing.expsys.client.domain.LogClause;
import imaing.expsys.client.domain.NotClause;
import imaing.expsys.client.domain.OrClause;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Rule;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.server.model.CharacteristicDAO;
import imaing.expsys.server.model.ProductDAO;
import imaing.expsys.server.model.RuleDAO;
import imaing.expsys.server.model.ShopDAO;
import imaing.expsys.shared.Relevance;
import imaing.expsys.shared.exceptions.InvalidDataException;
import imaing.expsys.test.util.TestUtils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/*
 * NOTE: Primjer integracijskog testa. Ovaj test simulira integraciju servisnog
 * i DAO sloja. Servis prima preko dependency injectiona (konfigurirano u
 * servicesConfig.xml).
 */

// Ova anotacija kaze da zelimo da ovaj test ima pristup applicationContextu
// Ukoliko se radi o unit testu koji testira funkcionalnost neke funkcije
// koja moze raditi sama za sebe (nema reference na druge objekte) ili
// se sami brinemo za dohvat referenci na druge objekte, ova anotacija nije potrebna.
@RunWith(SpringJUnit4ClassRunner.class)

// Ovom anotacijom kazemo gdje se nalazi konfiguracijski XML koji zelimo da se ucita
// te se pomocu njega stvori applicationContext
// applicationContext se cache-ira na razini JVM-a pa je u redu na ovakav nacin
// includea-ti applicationContext u sve testove
// TODO: create test env! (in memory db)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})

// Ovom linijom kazemo da se radi o transakcijskom testu. Jako korisno kod testiranja
// integracije s bazom podataka. Spring ce automatski povuci sve promjene koje je test
// napravio te ce baza ostati u istom stanju.
// Ova anotacija se moze koristiti i na razini test funkcije, u tom slucaju je samo 
// ta funkcija transakcijska (ovako su sve unutar klase transakcijske)
@Transactional
public class DaoTest {
	@Autowired private ShopDAO shopDao;
	@Autowired private ProductDAO prodDao;
	@Autowired private CharacteristicDAO chrDao;
	@Autowired private RuleDAO ruleDao;
	
	private Product newProduct() {
		Product p = new Product();
		p.setDescription(TestUtils.generateRandStr(10));
		p.setIntegrationId(TestUtils.generateRandStr(5));
		p.setShop(null);
		return p;
	}
	
	@Test
	public void shouldCreateNewShop() throws InvalidDataException {
		Shop ns = TestUtils.newShop();
		
		shopDao.save(ns);
		List<Shop> shops = shopDao.list();
		
		Assert.assertEquals("There should be only one shop", 1, shops.size());
		Assert.assertEquals("The shop should be our shop", ns, shops.get(0));
	}
	
	@Test
	public void shouldCreateTenNewShops() throws InvalidDataException {
		for (int i = 0; i < 10; i++) {
			shopDao.save(TestUtils.newShop());
		}
		List<Shop> shops = shopDao.list();
		
		Assert.assertEquals("There should be ten shops", 10, shops.size());
	}
	
	@Test(expected=InvalidDataException.class)
	public void shouldNotCreateTwoShopsWithSameEmail() throws InvalidDataException {
		Shop ns1 = TestUtils.newShop();
		Shop ns2 = TestUtils.newShop();
		
		ns2.setEmail(ns1.getEmail());
		
		shopDao.save(ns1);
		shopDao.save(ns2);
		
		Assert.assertFalse(true);
	}
	
	@Test
	public void shouldCreateNewShopWithTenProducts() throws InvalidDataException {
		Shop ns = TestUtils.newShop();
		
		shopDao.save(ns);
		Shop savedNs = shopDao.getShopForEmail(ns.getEmail());
		Assert.assertNotNull("Shop's id musn't be null!!", savedNs.getId());
		
		for (int i = 0; i < 10; i++) {
			Product p = newProduct();
			p.setShop(savedNs);
			prodDao.save(p);
			System.out.println("Saved: " + i);
		}
		List<Product> prods = prodDao.listProductsForShop(savedNs);
		
		Assert.assertEquals("There should be ten products", 10, prods.size());
	}
	
	@Test
	public void shouldCreateRuleWithAllTypesOfClauesAndRetrieveIt() throws InvalidDataException {
		Shop shop = TestUtils.newShop();
		shop = shopDao.save(shop);

		Characteristic chr1 = new Characteristic();
		chr1.setfClsNum(10);
		chr1.setShop(shop);
		chr1.setName(TestUtils.generateRandStr(10));
		
		Characteristic chr2 = new Characteristic();
		chr2.setfClsNum(10);
		chr2.setShop(shop);
		chr2.setName(TestUtils.generateRandStr(10));
		
		chr1 = chrDao.save(chr1);
		chr2 = chrDao.save(chr2);
		
		Rule rule = new Rule();
		rule.setDesc(TestUtils.generateRandStr(10));
		rule.setShop(shop);
		rule.setRel(Relevance.REL_HIGH);
		
		LogClause logClause = new AndClause(
						new OrClause(
								new Literal(chr1, 3),
								new NotClause(new Literal(chr2, 1))),
						new NotClause(
								new Literal(chr2, 3)));
		rule.setLogClause(logClause);
		
		Rule savedRule = ruleDao.save(rule);
		
		List<LogClause> logClauses = ruleDao.listLogClausesForRule(savedRule);
		savedRule.buildClausesTree(logClauses);
		
		Assert.assertNotNull("savedRule should have non-null log clause", savedRule.getLogClause());
		
		boolean areSame = true;
		try {
			AndClause orgAnd = (AndClause) logClause;
			AndClause savedAnd = (AndClause) savedRule.getLogClause();
				OrClause orgOr = (OrClause) orgAnd.getLeftClause();
				OrClause savedOr = (OrClause) savedAnd.getLeftClause();
					int orgOrLitIdx = ((Literal)orgOr.getLeftClause()).getFuzzyClsIdx().intValue();
					int savedOrLitIdx = ((Literal)savedOr.getLeftClause()).getFuzzyClsIdx().intValue();
					areSame = areSame && (orgOrLitIdx == savedOrLitIdx);
					areSame = areSame &&
							(((Literal)orgOr.getLeftClause()).getChr().equals(
									((Literal)savedOr.getLeftClause()).getChr()));
					
					NotClause orgOrNot = (NotClause) orgOr.getRightClause();
					NotClause savedOrNot = (NotClause) savedOr.getRightClause();
						int orgOrNotLitIdx = ((Literal)orgOrNot.getLeftClause()).getFuzzyClsIdx().intValue();
						int savedOrNotLitIdx = ((Literal)savedOrNot.getLeftClause()).getFuzzyClsIdx().intValue();
						areSame = areSame && (orgOrNotLitIdx == savedOrNotLitIdx);
						areSame = areSame &&
								(((Literal)orgOrNot.getLeftClause()).getChr().equals(
										((Literal)savedOrNot.getLeftClause()).getChr()));
			
				NotClause orgNot = (NotClause) orgAnd.getRightClause();
				NotClause savedNot = (NotClause) savedAnd.getRightClause();
					int orgNotLitIdx = ((Literal)orgNot.getLeftClause()).getFuzzyClsIdx().intValue();
					int savedNotLitIdx = ((Literal)savedNot.getLeftClause()).getFuzzyClsIdx().intValue();
					areSame = areSame && (orgNotLitIdx == savedNotLitIdx);
					areSame = areSame &&
							(((Literal)orgNot.getLeftClause()).getChr().equals(
									((Literal)savedNot.getLeftClause()).getChr()));
					
		} catch(ClassCastException e) {
			areSame = false;
		}
		
		Assert.assertTrue("Original and retrieved logical clauses are the same", areSame);
	}
	
	@Test
	public void shouldDeleteRuleWithLogClauses() throws InvalidDataException {
		Shop shop = TestUtils.newShop();
		shop = shopDao.save(shop);

		Characteristic chr1 = new Characteristic();
		chr1.setfClsNum(10);
		chr1.setShop(shop);
		chr1.setName(TestUtils.generateRandStr(10));
		
		Characteristic chr2 = new Characteristic();
		chr2.setfClsNum(10);
		chr2.setShop(shop);
		chr2.setName(TestUtils.generateRandStr(10));
		
		chr1 = chrDao.save(chr1);
		chr2 = chrDao.save(chr2);
		
		Rule rule = new Rule();
		rule.setDesc(TestUtils.generateRandStr(10));
		rule.setShop(shop);
		rule.setRel(Relevance.REL_HIGH);
		
		LogClause logClause = new AndClause(
						new OrClause(
								new Literal(chr1, 3),
								new NotClause(new Literal(chr2, 1))),
						new NotClause(
								new Literal(chr2, 3)));
		rule.setLogClause(logClause);
		
		Rule savedRule = ruleDao.save(rule);
		
		List<LogClause> logClauses = ruleDao.listLogClausesForRule(savedRule);
		savedRule.buildClausesTree(logClauses);
		
		Assert.assertNotNull("savedRule should have non-null log clause", savedRule.getLogClause());
		
		ruleDao.delete(savedRule.getId());
		
		Rule retreivedRule = ruleDao.getById(savedRule.getId());
		Assert.assertNull("Rule shouldn't exist", retreivedRule);
	}
}
