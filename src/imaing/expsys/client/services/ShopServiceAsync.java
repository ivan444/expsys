package imaing.expsys.client.services;


import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ShopServiceAsync {

	void listShops(AsyncCallback<List<Shop>> callback);
	void listCharacteristics(Shop shop, AsyncCallback<List<Characteristic>> callback);
	
	// TODO: move to AdminService!!
	void saveShop(Shop shop, AsyncCallback<Shop> callback);
	void deleteShop(long shopId, AsyncCallback<Void> callback);
	
	void saveCharacteristic(Characteristic chr, AsyncCallback<Characteristic> callback);
	void deleteCharacteristic(long chrId, AsyncCallback<Void> callback);
	
	void addProduct(Product p, AsyncCallback<Product> callback);
}
