package imaing.expsys.server.olddao;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.ShopOwner;
import imaing.expsys.server.model.GenericDAO;

import java.util.List;

public interface CharacteristicRepository extends GenericRepository<CharacteristicDao, Characteristic> {
	List<Characteristic> listCharacteristicsForShop(ShopOwner owner);
	Characteristic getCharacteristicForShopAndName(ShopOwner owner, String name);
}
