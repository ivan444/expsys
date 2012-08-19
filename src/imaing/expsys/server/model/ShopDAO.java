package imaing.expsys.server.model;

import imaing.expsys.client.domain.Shop;

public interface ShopDAO extends GenericDAO<ShopEnt, Shop> {
	Shop getShopForEmail(String email);
}
