package imaing.expsys.server.model;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.Shop;

import java.util.List;

public interface CharacteristicDAO extends GenericDAO<CharacteristicEnt, Characteristic> {
	List<Characteristic> listCharacteristicsForShop(Shop shop);
	Characteristic getCharacteristicForShopAndName(Shop shop, String name);
}
