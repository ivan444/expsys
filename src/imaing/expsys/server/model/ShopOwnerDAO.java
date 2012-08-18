package imaing.expsys.server.model;

import imaing.expsys.server.model.GenericDAO;

public interface ShopOwnerDAO extends GenericDAO<ShopOwner> {
	ShopOwner getShopOwnerForEmail(String email);
}
