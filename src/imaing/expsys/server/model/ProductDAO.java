package imaing.expsys.server.model;

import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;

import java.util.List;

public interface ProductDAO extends GenericDAO<ProductEnt, Product> {
	List<Product> listProductsForShop(Shop shop);
	List<Product> listProductsWCharsForShop(Shop shop);
	Product getProductForShopAndIntegrationId(Shop shop, String integrationId);
	Product getProductWCharsForShopAndIntegrationId(Shop shop, String integrationId);
}
