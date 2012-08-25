package imaing.expsys.server.model;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.Shop;

import java.util.List;

public interface FuzzyClassDAO extends GenericDAO<FuzzyClassEnt, FuzzyClass> {
	List<FuzzyClass> listFClsForCharacteristic(Characteristic chr);
	List<FuzzyClass> listFClsForShop(Shop shop);
	FuzzyClass getFClsForCharacteristicAndValue(Characteristic chr, String value);
}
