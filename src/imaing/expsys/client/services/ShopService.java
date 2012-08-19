package imaing.expsys.client.services;


import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("shop.rpc")
public interface ShopService extends RemoteService {

	List<Shop> list();
	
	Shop save(Shop shop) throws InvalidDataException;
	
	void addProduct(Product p) throws InvalidDataException;
}
