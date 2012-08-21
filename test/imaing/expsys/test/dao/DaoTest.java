package imaing.expsys.test.dao;

import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.server.model.ProductDAO;
import imaing.expsys.server.model.ShopDAO;
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
	
	private Shop newShop() {
		Shop shp = new Shop();
		shp.setEmail(TestUtils.generateRandEmail());
		shp.setPassword(TestUtils.generateRandStr(10));
		shp.setShopName(TestUtils.generateRandStr("shop_", 4));
		return shp;
	}
	
	private Product newProduct() {
		Product p = new Product();
		p.setDescription(TestUtils.generateRandStr(10));
		p.setIntegrationId(TestUtils.generateRandStr(5));
		p.setShop(null);
		return p;
	}
	
	@Test
	public void shouldCreateNewShop() throws InvalidDataException {
		Shop ns = newShop();
		
		shopDao.save(ns);
		List<Shop> shops = shopDao.list();
		
		Assert.assertEquals("There should be only one shop", 1, shops.size());
		Assert.assertEquals("The shop should be our shop", ns, shops.get(0));
	}
	
	@Test
	public void shouldCreateTenNewShops() throws InvalidDataException {
		for (int i = 0; i < 10; i++) {
			shopDao.save(newShop());
		}
		List<Shop> shops = shopDao.list();
		
		Assert.assertEquals("There should be ten shops", 10, shops.size());
	}
	
	@Test(expected=InvalidDataException.class)
	public void shouldNotCreateTwoShopsWithSameEmail() throws InvalidDataException {
		Shop ns1 = newShop();
		Shop ns2 = newShop();
		
		ns2.setEmail(ns1.getEmail());
		
		shopDao.save(ns1);
		shopDao.save(ns2);
		
		Assert.assertFalse(true);
	}
	
	@Test
	public void shouldCreateNewShopWithTenProducts() throws InvalidDataException {
		Shop ns = newShop();
		
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

}
