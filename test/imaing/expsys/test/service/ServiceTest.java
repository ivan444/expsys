package imaing.expsys.test.service;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.ProdChr;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopService;
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
public class ServiceTest {
	@Autowired private ShopService shopSrv;
	
	@Test
	public void shouldSaveProductsAndBuildFuzzyClasses() throws InvalidDataException {
		Shop shop = TestUtils.newShop();
		shop = shopSrv.saveShop(shop);
		
		Characteristic proc = new Characteristic();
		proc.setShop(shop);
		proc.setName("proc");
		proc.setfClsNum(3);
		
		Characteristic hdd = new Characteristic();
		hdd.setShop(shop);
		hdd.setName("hdd");
		hdd.setfClsNum(3);
		
		Characteristic mem = new Characteristic();
		mem.setShop(shop);
		mem.setName("mem");
		mem.setfClsNum(3);
		
		proc = shopSrv.saveCharacteristic(proc);
		hdd = shopSrv.saveCharacteristic(hdd);
		mem = shopSrv.saveCharacteristic(mem);
		
		Product p1 = new Product();
		p1.setDescription("laptop1");
		p1.setIntegrationId("integ1");
		p1.setShop(shop);
		
		ProdChr pc1Mem = new ProdChr();
		pc1Mem.setChr(mem);
		pc1Mem.setProd(p1);
		pc1Mem.setValue("4GB");
		
		ProdChr pc1Hdd = new ProdChr();
		pc1Hdd.setChr(hdd);
		pc1Hdd.setProd(p1);
		pc1Hdd.setValue("500GB");
		
		ProdChr pc1Proc = new ProdChr();
		pc1Proc.setChr(proc);
		pc1Proc.setProd(p1);
		pc1Proc.setValue("i7");
		
		p1.getCharacteristics().add(pc1Mem);
		p1.getCharacteristics().add(pc1Hdd);
		p1.getCharacteristics().add(pc1Proc);
		
		Product p2 = new Product();
		p2.setDescription("laptop2");
		p2.setIntegrationId("integ2");
		p2.setShop(shop);
		
		ProdChr pc2Mem = new ProdChr();
		pc2Mem.setChr(mem);
		pc2Mem.setProd(p2);
		pc2Mem.setValue("4GB");
		
		ProdChr pc2Hdd = new ProdChr();
		pc2Hdd.setChr(hdd);
		pc2Hdd.setProd(p2);
		pc2Hdd.setValue("1000GB");
		
		ProdChr pc2Proc = new ProdChr();
		pc2Proc.setChr(proc);
		pc2Proc.setProd(p2);
		pc2Proc.setValue("i5");
		
		p2.getCharacteristics().add(pc2Mem);
		p2.getCharacteristics().add(pc2Hdd);
		p2.getCharacteristics().add(pc2Proc);
		
		shopSrv.addProduct(p1);
		shopSrv.addProduct(p2);

		List<Product> savedProd = shopSrv.listProducts(shop);
		Assert.assertEquals("There should be two products", 2, savedProd.size());
		
		List<FuzzyClass> fclasses = shopSrv.listFuzzyClassesForShop(shop);
		Assert.assertEquals("There should be five fuzzy classes", 5, fclasses.size());
	}

}
