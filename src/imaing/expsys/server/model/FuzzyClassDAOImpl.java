package imaing.expsys.server.model;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.Shop;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

public class FuzzyClassDAOImpl extends GenericDAOImpl<FuzzyClassEnt, FuzzyClass> implements FuzzyClassDAO {
	
	public FuzzyClassDAOImpl(Class<FuzzyClassEnt> type) {
		super(type);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FuzzyClass> listFClsForCharacteristic(Characteristic chr) {
		List<FuzzyClassEnt> ents = (List<FuzzyClassEnt>) em.createNamedQuery("FuzzyClassEnt.listFClsForCharacteristic")
																		 .setParameter("chr", new CharacteristicEnt(chr)).getResultList();
		List<FuzzyClass> flcClean = new LinkedList<FuzzyClass>();
		if (ents != null){
			for (FuzzyClassEnt fc : ents) {
				flcClean.add(fc.getCleaned());
			}
		}
		return flcClean;
	}

	@Override
	public FuzzyClass getFClsForCharacteristicAndValue(Characteristic chr, String value) {
		FuzzyClassEnt result = null;
		try {
			result = (FuzzyClassEnt) em.createNamedQuery("FuzzyClassEnt.getFClsForCharacteristicAndValue")
										 .setParameter("chr", new CharacteristicEnt(chr)).setParameter("value", value).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
		if (result == null) return null;
		else return result.getCleaned();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FuzzyClass> listFClsForShop(Shop shop) {
		List<FuzzyClassEnt> ents = (List<FuzzyClassEnt>) em.createNamedQuery("FuzzyClassEnt.listFClsForShop")
				 										   .setParameter("shop", new ShopEnt(shop)).getResultList();
		List<FuzzyClass> flcClean = new LinkedList<FuzzyClass>();
		if (ents != null) {
			for (FuzzyClassEnt fc : ents) {
				flcClean.add(fc.getCleaned());
			}
		}
		return flcClean;
	}
}
