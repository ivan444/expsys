package imaing.expsys.server.dao;

import imaing.expsys.client.domain.ShopOwner;

public interface ShopOwnerRepository extends GenericRepository<ShopOwnerDao, ShopOwner> {
	ShopOwner getShopOwnerForEmail(String email);
}
