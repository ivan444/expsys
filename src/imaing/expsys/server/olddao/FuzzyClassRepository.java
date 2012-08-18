package imaing.expsys.server.olddao;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.server.model.GenericDAO;

import java.util.List;

public interface FuzzyClassRepository extends GenericRepository<FuzzyClassDao, FuzzyClass> {
	List<FuzzyClass> listFClsForCharacteristic(Characteristic chr);
	FuzzyClass getFClsForCharacteristicAndValue(Characteristic chr, String value);
}
