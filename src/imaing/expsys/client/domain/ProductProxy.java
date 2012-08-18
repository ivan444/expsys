package imaing.expsys.client.domain;

import imaing.expsys.server.model.GenericDAOImpl;
import imaing.expsys.server.model.Product;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = Product.class, locator = GenericDAOImpl.class)
public interface ProductProxy extends EntityProxy {
	ShopOwnerProxy getShop();

	void setShop(ShopOwnerProxy shop);

	String getDescription();

	void setDescription(String description);

	int getfClsNum();

	void setfClsNum(int fClsNum);

	Long getId();
}
