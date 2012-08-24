package imaing.expsys.client.services;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("shop.rpc")
public interface ShopService extends RemoteService {
	// TODO: move to AdminService!!
	List<Shop> listShops();
	Shop saveShop(Shop shop) throws InvalidDataException;
	void deleteShop(long shopId) throws InvalidDataException;
	
	List<Characteristic> listCharacteristics(Shop shop);
	Characteristic saveCharacteristic(Characteristic chr) throws InvalidDataException;
	void deleteCharacteristic(long chrId) throws InvalidDataException;
	
	List<Product> listProducts(Shop shop);
	void deleteProduct(long prodId) throws InvalidDataException;
	Product addProduct(Product p) throws InvalidDataException;
}
