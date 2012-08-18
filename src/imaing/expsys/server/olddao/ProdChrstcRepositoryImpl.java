package imaing.expsys.server.olddao;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.server.model.GenericDAOImpl;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

public class ProdChrstcRepositoryImpl extends GenericRepositoryImpl<FuzzyClassDao, FuzzyClass> implements FuzzyClassRepository {
	
	public ProdChrstcRepositoryImpl(Class<FuzzyClassDao> type) {
		super(type);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FuzzyClass> listFClsForCharacteristic(Characteristic chr) {
		List<FuzzyClassDao> flcDaos = (List<FuzzyClassDao>) entityManager.createNamedQuery("FuzzyClassDao.listFClsForCharacteristic")
																		 .setParameter("chr", new CharacteristicDao(chr)).getResultList();
		List<FuzzyClass> flcClean = new LinkedList<FuzzyClass>();
		if (flcDaos != null){
			for (FuzzyClassDao fc : flcDaos) {
				flcClean.add(fc.getCleaned());
			}
		}
		return flcClean;
	}

	@Override
	public FuzzyClass getFClsForCharacteristicAndValue(Characteristic chr, String value) {
		FuzzyClassDao result = null;
		try {
			result = (FuzzyClassDao) entityManager.createNamedQuery("FuzzyClassDao.getFClsForCharacteristicAndValue")
													  .setParameter("chr", new CharacteristicDao(chr)).setParameter("value", value).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
		if (result == null) return null;
		else return result.getCleaned();
	}
}
