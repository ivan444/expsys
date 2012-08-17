package imaing.expsys.server.dao;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyClass;

import java.util.List;

public interface FuzzyClassRepository extends GenericRepository<FuzzyClassDao, FuzzyClass> {
	List<FuzzyClass> listFClsForCharacteristic(Characteristic chr);
	FuzzyClass getFClsForCharacteristicAndValue(Characteristic chr, String value);
}
