package imaing.expsys.client.services;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Rule;
import imaing.expsys.client.domain.Shop;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ShopServiceAsync {
	// TODO: move to AdminService!!
	void listShops(AsyncCallback<List<Shop>> callback);
	void saveShop(Shop shop, AsyncCallback<Shop> callback);
	void deleteShop(long shopId, AsyncCallback<Void> callback);
	
	void listCharacteristics(Shop shop, AsyncCallback<List<Characteristic>> callback);
	void saveCharacteristic(Characteristic chr, AsyncCallback<Characteristic> callback);
	void deleteCharacteristic(long chrId, AsyncCallback<Void> callback);
	
	void listProducts(Shop shop, AsyncCallback<List<Product>> callback);
	void deleteProduct(long prodId, AsyncCallback<Void> callback);
	void addProduct(Product p, AsyncCallback<Product> callback);
	void addAllProducts(Collection<Product> ps, AsyncCallback<List<Product>> callback);
	
	void listFuzzyClassesForShop(Shop shop, AsyncCallback<List<FuzzyClass>> callback);
	void updateFuzzyClasses(List<FuzzyClass> fcls, AsyncCallback<Void> callback);
	
	void listRulesForShop(Shop shop, AsyncCallback<List<Rule>> callback);
	void saveRule(Rule rule, AsyncCallback<Rule> callback);
	void deleteRule(Rule rule, AsyncCallback<Void> callback);
}
