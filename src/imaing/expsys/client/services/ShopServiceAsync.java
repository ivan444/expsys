package imaing.expsys.client.services;


import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ShopServiceAsync {

	void list(AsyncCallback<List<Shop>> callback);
	
	// TODO: move to AdminService!!
	void save(Shop shop, AsyncCallback<Shop> callback);
	void deleteShop(long shopId, AsyncCallback<Void> callback);
	
	void addProduct(Product p, AsyncCallback<Product> callback);
}
