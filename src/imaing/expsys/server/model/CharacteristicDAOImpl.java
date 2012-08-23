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
	public List<Characteristic> listCharacteristicsForShop(Shop shop) {
		List<CharacteristicEnt> ents = (List<CharacteristicEnt>) em.createNamedQuery("CharacteristicEnt.listCharacteristicsForShop")
																   .setParameter("shop", new ShopEnt(shop)).getResultList();
		List<Characteristic> chrsClean = new LinkedList<Characteristic>();
		if (ents != null){
			for (CharacteristicEnt cd : ents) {
				chrsClean.add(cd.getCleaned());
			}
		}
		return chrsClean;
	}

	@Override
	public Characteristic getCharacteristicForShopAndName(Shop shop, String name) {
		CharacteristicEnt result = null;
		try {
			result = (CharacteristicEnt) em.createNamedQuery("CharacteristicEnt.getCharacteristicForShopAndName")
										   .setParameter("shop", new ShopEnt(shop)).setParameter("name", name).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
		if (result == null) return null;
		else return result.getCleaned();
	}
}
