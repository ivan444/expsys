package imaing.expsys.server.dao;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.ShopOwner;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

public class ProductRepositoryImpl extends GenericRepositoryImpl<CharacteristicDao, Characteristic> implements CharacteristicRepository {
	
	public ProductRepositoryImpl(Class<CharacteristicDao> type) {
		super(type);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Characteristic> listCharacteristicsForShop(ShopOwner owner) {
		List<CharacteristicDao> charDaos = (List<CharacteristicDao>) entityManager.createNamedQuery("CharacteristicDao.getCharacteristicForShopAndName")
																				  .setParameter("owner", new ShopOwnerDao(owner)).getResultList();
		List<Characteristic> chrsClean = new LinkedList<Characteristic>();
		if (charDaos != null){
			for (CharacteristicDao cd : charDaos) {
				chrsClean.add(cd.getCleaned());
			}
		}
		return chrsClean;
	}

	@Override
	public Characteristic getCharacteristicForShopAndName(ShopOwner owner, String name) {
		CharacteristicDao result = null;
		try {
			result = (CharacteristicDao) entityManager.createNamedQuery("CharacteristicDao.getCharacteristicForShopAndName")
													  .setParameter("owner", new ShopOwnerDao(owner)).setParameter("name", name).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
		if (result == null) return null;
		else return result.getCleaned();
	}
}
