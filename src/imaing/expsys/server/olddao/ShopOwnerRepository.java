package imaing.expsys.server.olddao;

import imaing.expsys.client.domain.ShopOwner;
import imaing.expsys.server.model.GenericDAO;

public interface ShopOwnerRepository extends GenericRepository<ShopOwnerDao, ShopOwner> {
	ShopOwner getShopOwnerForEmail(String email);
}
