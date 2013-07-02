package imaing.expsys.client.services;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyChrCls;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Rule;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.shared.Tuple;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("shop.rpc")
public interface ShopService extends RemoteService {
	/* Shops */
	List<Shop> listShops();
	Shop saveShop(Shop shop) throws InvalidDataException;
	void deleteShop(long shopId) throws InvalidDataException;
	
	/* Characteristics */
	List<Characteristic> listCharacteristics(Shop shop);
	Characteristic saveCharacteristic(Characteristic chr) throws InvalidDataException;
	void deleteCharacteristic(long chrId) throws InvalidDataException;
	
	/* Products */
	List<Product> listProducts(Shop shop);
	void deleteProduct(long prodId) throws InvalidDataException;
	Product addProduct(Product p) throws InvalidDataException;
	List<Product> addAllProducts(Collection<Product> ps) throws InvalidDataException;
	
	/* Fuzzy classes and their definitions */
	List<FuzzyClass> listFuzzyClassesForShop(Shop shop);
	void updateFuzzyClasses(List<FuzzyClass> fcls) throws InvalidDataException;
	
	Map<Characteristic, Tuple<List<FuzzyChrCls>, List<FuzzyClass>>> listFuzzySetsAndValsForShop(Shop shop);
	
	List<FuzzyChrCls> listFuzzyClassesDefinitions(Shop shop);
	void updateFuzzyClassesDefinitions(List<FuzzyChrCls> fcls) throws InvalidDataException;
	
	/* Rules */
	List<Rule> listRulesForShop(Shop shop);
	Rule saveRule(Rule rule) throws InvalidDataException;
	void deleteRule(Rule rule) throws InvalidDataException;
}
