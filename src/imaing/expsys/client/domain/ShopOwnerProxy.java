package imaing.expsys.client.domain;

import imaing.expsys.server.model.ShopOwner;
import imaing.expsys.server.model.ShopOwnerDAOImpl;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = ShopOwner.class, locator = ShopOwnerDAOImpl.class)
public interface ShopOwnerProxy extends EntityProxy {
	String getPassword();

	void setPassword(String password);

	String getEmail();

	void setEmail(String email);

	String getShopName();

	void setShopName(String shopName);

	List<ProductProxy> getProducts();

	void setProducts(List<ProductProxy> products);

	Long getId();
}
