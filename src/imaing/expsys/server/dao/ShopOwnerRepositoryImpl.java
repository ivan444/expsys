package imaing.expsys.server.dao;

import imaing.expsys.client.domain.ShopOwner;
import imaing.expsys.shared.exceptions.InvalidDataException;

import javax.persistence.NoResultException;

import org.springframework.transaction.annotation.Transactional;

public class ShopOwnerRepositoryImpl extends GenericRepositoryImpl<ShopOwnerDao, ShopOwner> implements ShopOwnerRepository {
	
	public ShopOwnerRepositoryImpl(Class<ShopOwnerDao> type) {
		super(type);
	}
	
	@Override
	@Transactional(readOnly=false)
	public void save(ShopOwner gwtObject) throws InvalidDataException {
		if (gwtObject.getId() == null) {
			// New record
			if (gwtObject.getEmail() == null) {
				throw new InvalidDataException("E-mail not set for ShopOwner: "
						+ gwtObject.getEmail() + " " + gwtObject.getTitle());
			} else {
				ShopOwner other = getShopOwnerForEmail(gwtObject.getEmail());
				if (other != null) {
					throw new InvalidDataException("ShopOwner with e-mail "
							+ gwtObject.getEmail() + " already exists!");
				}
			}
		}
		super.save(gwtObject);
	}

	@Override
	public ShopOwner getShopOwnerForEmail(String email) {
		ShopOwnerDao result = null;
		try {
			result = (ShopOwnerDao) entityManager.createNamedQuery("ShopOwnerDao.getShopOwnerForEmail")
															.setParameter("email", email).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
		if (result == null){
			return null;
		} else {
			return result.getCleaned();
		}
	}
}
