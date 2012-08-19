package imaing.expsys.server.model;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.Shop;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

public class CharacteristicDAOImpl extends GenericDAOImpl<CharacteristicEnt, Characteristic> implements CharacteristicDAO {
	
	public CharacteristicDAOImpl(Class<CharacteristicEnt> type) {
		super(type);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Characteristic> listCharacteristicsForShop(Shop owner) {
		List<CharacteristicEnt> ents = (List<CharacteristicEnt>) entityManager.createNamedQuery("CharacteristicEnt.getCharacteristicForShopAndName")
																				  .setParameter("owner", new ShopEnt(owner)).getResultList();
		List<Characteristic> chrsClean = new LinkedList<Characteristic>();
		if (ents != null){
			for (CharacteristicEnt cd : ents) {
				chrsClean.add(cd.getCleaned());
			}
		}
		return chrsClean;
	}

	@Override
	public Characteristic getCharacteristicForShopAndName(Shop owner, String name) {
		CharacteristicEnt result = null;
		try {
			result = (CharacteristicEnt) entityManager.createNamedQuery("CharacteristicEnt.getCharacteristicForShopAndName")
													  .setParameter("owner", new ShopEnt(owner)).setParameter("name", name).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
		if (result == null) return null;
		else return result.getCleaned();
	}
}
