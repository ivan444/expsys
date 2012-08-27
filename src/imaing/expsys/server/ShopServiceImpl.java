package imaing.expsys.server;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.LogClause;
import imaing.expsys.client.domain.ProdChr;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Rule;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopService;
import imaing.expsys.server.model.CharacteristicDAO;
import imaing.expsys.server.model.FuzzyClassDAO;
import imaing.expsys.server.model.ProdChrDAO;
import imaing.expsys.server.model.ProductDAO;
import imaing.expsys.server.model.RuleDAO;
import imaing.expsys.server.model.RuleEnt;
import imaing.expsys.server.model.ShopDAO;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

public class ShopServiceImpl implements ShopService, SessionAware {
	
	@Autowired private ShopDAO shpDao;
	@Autowired private ProductDAO prodDao;
	@Autowired private ProdChrDAO prodChrDao;
	@Autowired private CharacteristicDAO chrDao;
	@Autowired private FuzzyClassDAO fclsDao;
	@Autowired private RuleDAO ruleDao;
	
	public ShopServiceImpl() {
	}
	
	@Override
	public void setSession(HttpSession session) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Shop> listShops() {
		return shpDao.list();
	}

	@Override
	public Shop saveShop(Shop shopOwner) throws InvalidDataException {
		Shop saved = shpDao.save(shopOwner);
		return saved;
	}

	@Override
	public Product addProduct(Product p) throws InvalidDataException {
		Product savedProd = prodDao.save(p);
		
		List<ProdChr> savedProdChars = new ArrayList<ProdChr>();
		for (ProdChr pc : p.getCharacteristics()) {
			pc.setProd(savedProd);
			savedProdChars.add(prodChrDao.save(pc));
		}
		
		savedProd.setCharacteristics(savedProdChars);
		
		createFuzzyClasses(savedProd.getShop(), savedProdChars);
		
		return savedProd;
	}

	/**
	 * Create new (unique) fuzzy classes from new ProdChar objects.
	 * 
	 * @param savedProdChars New ProdChar objects that maybe don't have appropriate fuzzy classes.
	 * @throws InvalidDataException If error during FuzzyClass object save occurs.
	 */
	private void createFuzzyClasses(Shop shop, List<ProdChr> savedProdChars) throws InvalidDataException {
		final String MOSTLIKELY_UNIQ = "<$>";
		List<FuzzyClass> existingFcls = fclsDao.listFClsForShop(shop);
//		Map<Characteristic, List<FuzzyClass>> existingFclsByChr = new HashMap<Characteristic, List<FuzzyClass>>();
		Set<String> existingFclsKeys = new HashSet<String>();
		
		for (FuzzyClass fc : existingFcls) {
			existingFclsKeys.add(fc.getChr().getName()+MOSTLIKELY_UNIQ+fc.getValue()); // DANGEROUS: suppose that "<$>" won't collide...
		}
		
		List<FuzzyClass> newFcls = new LinkedList<FuzzyClass>();
		for (ProdChr pc : savedProdChars) {
			String chr = pc.getChr().getName();
			String val = pc.getValue();
			String key = chr+MOSTLIKELY_UNIQ+val;
			if (!existingFclsKeys.contains(key)) {
				FuzzyClass fc = new FuzzyClass();
				fc.setChr(pc.getChr());
				fc.setMembershipVal(new double[pc.getChr().getfClsNum()]);
				fc.setValue(val);
				
				newFcls.add(fc);
				existingFclsKeys.add(key);
			}
		}
		
		for (FuzzyClass fc : newFcls) {
			fclsDao.save(fc);
		}
	}

	@Override
	public void deleteShop(long shopId) throws InvalidDataException {
		shpDao.delete(Long.valueOf(shopId));
	}

	@Override
	public List<Characteristic> listCharacteristics(Shop shop) {
		return chrDao.listCharacteristicsForShop(shop);
	}

	@Override
	public Characteristic saveCharacteristic(Characteristic chr)
			throws InvalidDataException {
		return chrDao.save(chr);
	}

	@Override
	public void deleteCharacteristic(long chrId) throws InvalidDataException {
		chrDao.delete(Long.valueOf(chrId));
	}

	@Override
	public List<Product> listProducts(Shop shop) {
		return prodDao.listProductsWCharsForShop(shop);
	}

	@Override
	public void deleteProduct(long prodId) throws InvalidDataException {
		prodDao.delete(Long.valueOf(prodId));
	}

	@Override
	public List<FuzzyClass> listFuzzyClassesForShop(Shop shop) {
//		return fclsDao.listFClsForShop(shop);
		return fclsDao.list();
	}

	@Override
	public void updateFuzzyClasses(List<FuzzyClass> fcls) throws InvalidDataException {
		for (FuzzyClass fc : fcls) {
			if (fc.getId() == null) throw new InvalidDataException("Some FuzzyClasses doesn't have ID set!");
		}
		
		for (FuzzyClass fc : fcls) {
			fclsDao.save(fc);
		}
		
	}

	@Override
	public List<Rule> listRulesForShop(Shop shop) {
		return ruleDao.listRulesForShop(shop);
	}

	@Override
	public Rule saveRule(Rule rule) throws InvalidDataException {
		return ruleDao.save(rule);
	}

	@Override
	public void deleteRule(Rule rule) throws InvalidDataException {
		ruleDao.delete(new RuleEnt(rule));
	}

}
