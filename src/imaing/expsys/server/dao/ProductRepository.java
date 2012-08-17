package imaing.expsys.server.dao;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.ShopOwner;

import java.util.List;

public interface ProductRepository extends GenericRepository<CharacteristicDao, Characteristic> {
	List<Characteristic> listCharacteristicsForShop(ShopOwner owner);
	Characteristic getCharacteristicForShopAndName(ShopOwner owner, String name);
}
