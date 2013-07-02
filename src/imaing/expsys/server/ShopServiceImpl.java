package imaing.expsys.server;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyChrCls;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.ProdChr;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Rule;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopService;
import imaing.expsys.server.model.CharacteristicDAO;
import imaing.expsys.server.model.FuzzyChrClsDAO;
import imaing.expsys.server.model.FuzzyClassDAO;
import imaing.expsys.server.model.ProdChrDAO;
import imaing.expsys.server.model.ProductDAO;
import imaing.expsys.server.model.RuleDAO;
import imaing.expsys.server.model.ShopDAO;
import imaing.expsys.shared.Tuple;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

public class ShopServiceImpl implements ShopService, SessionAware {
	
	@Autowired private ShopDAO shpDao;
	@Autowired private ProductDAO prodDao;
	@Autowired private ProdChrDAO prodChrDao;
	@Autowired private CharacteristicDAO chrDao;
	@Autowired private FuzzyClassDAO fclsDao;
	@Autowired private FuzzyChrClsDAO fChrClsDao;
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
		final String MOSTLIKELY_UNIQ = "\t";
		List<FuzzyClass> existingFcls = fclsDao.listFClsForShop(shop);
		Set<String> existingFclsKeys = new HashSet<String>();
		
		for (FuzzyClass fc : existingFcls) {
			existingFclsKeys.add(fc.getChr().getName()+MOSTLIKELY_UNIQ+fc.getValue()); // DANGEROUS: assumes that "\t" won't collide...
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
		Characteristic c = chrDao.save(chr);
		int fSetsNum = c.getfClsNum();
		
		final int shift = 50;
		for (int i = 0; i < fSetsNum; i++) {
			FuzzyChrCls fSet = new FuzzyChrCls();
			fSet.setChr(c);
			fSet.setxLeftDown(i*shift);
			fSet.setxLeftUp(i*shift+20);
			fSet.setxRightUp(i*shift+50);
			fSet.setxRightDown(i*shift+70);
			fChrClsDao.save(fSet);
		}
		
		return c;
	}

	@Override
	public void deleteCharacteristic(long chrId) throws InvalidDataException {
		Long characteristicId = Long.valueOf(chrId);
		List<FuzzyChrCls> fSets = fChrClsDao.listForCharacteristic(chrDao.getById(characteristicId));
		fChrClsDao.deleteAll(fSets);
		chrDao.delete(characteristicId);
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
		return fclsDao.listFClsForShop(shop);
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
		ruleDao.delete(rule.getId());
	}

	@Override
	public List<Product> addAllProducts(Collection<Product> ps)
			throws InvalidDataException {
		
		List<Product> savedProds = new ArrayList<Product>(ps.size());

		for (Product p : ps) {
			List<ProdChr> unsavedPcrs = p.getCharacteristics();
			Product pSaved = prodDao.save(p);
			
			for (ProdChr pc : unsavedPcrs) {
				pc.setProd(pSaved);
			}
			List<ProdChr> savedPcrs = prodChrDao.saveAll(unsavedPcrs);
			pSaved.setCharacteristics(savedPcrs);

			createFuzzyClasses(pSaved.getShop(), savedPcrs);
			
			savedProds.add(pSaved);
		}
		
		return savedProds;
	}

	@Override
	public List<FuzzyChrCls> listFuzzyClassesDefinitions(Shop shop) {
		return fChrClsDao.listForShop(shop);
	}

	@Override
	public void updateFuzzyClassesDefinitions(List<FuzzyChrCls> fcls)
			throws InvalidDataException {
		for (FuzzyChrCls fc : fcls) {
			if (fc.getId() == null) throw new InvalidDataException("Some FuzzyChrClses doesn't have ID set!");
		}
		
		for (FuzzyChrCls fc : fcls) {
			fChrClsDao.save(fc);
		}
		
	}

	@Deprecated
	@Override
	public Map<Characteristic, Tuple<List<FuzzyChrCls>, List<FuzzyClass>>> listFuzzySetsAndValsForShop(
			Shop shop) {
		List<FuzzyChrCls> fuzzySets = fChrClsDao.listForShop(shop);
		List<FuzzyClass> fuzzyVals = fclsDao.listFClsForShop(shop);
		
		Set<Characteristic> allChrs = new HashSet<Characteristic>();
		
		Map<Characteristic, List<FuzzyChrCls>> mapChrFuzSets = new HashMap<Characteristic, List<FuzzyChrCls>>();
		for (FuzzyChrCls fuzzyChrCls : fuzzySets) {
			Characteristic chr = fuzzyChrCls.getChr();
			allChrs.add(chr);
			List<FuzzyChrCls> fuzSetLst = mapChrFuzSets.get(chr);
			if (fuzSetLst == null) {
				fuzSetLst = new LinkedList<FuzzyChrCls>();
				mapChrFuzSets.put(chr, fuzSetLst);
			}
			fuzSetLst.add(fuzzyChrCls);
		}
		
		Map<Characteristic, List<FuzzyClass>> mapChrFuzVals = new HashMap<Characteristic, List<FuzzyClass>>();
		for (FuzzyClass fuzzyVal : fuzzyVals) {
			Characteristic chr = fuzzyVal.getChr();
			allChrs.add(chr);
			List<FuzzyClass> fuzValLst = mapChrFuzVals.get(chr);
			if (fuzValLst == null) {
				fuzValLst = new LinkedList<FuzzyClass>();
				mapChrFuzVals.put(chr, fuzValLst);
			}
			fuzValLst.add(fuzzyVal);
		}
		
		Map<Characteristic, Tuple<List<FuzzyChrCls>, List<FuzzyClass>>> fuzChrDesc = new HashMap<Characteristic, Tuple<List<FuzzyChrCls>,List<FuzzyClass>>>();
		for (Characteristic chr : allChrs) {
			Tuple<List<FuzzyChrCls>, List<FuzzyClass>> fuzDesc =
					new Tuple<List<FuzzyChrCls>, List<FuzzyClass>>(mapChrFuzSets.get(chr), mapChrFuzVals.get(chr));
			fuzChrDesc.put(chr, fuzDesc);
		}
		
		return fuzChrDesc;
	}

}
