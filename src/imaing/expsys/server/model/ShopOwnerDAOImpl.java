package imaing.expsys.server.model;

import imaing.expsys.server.model.GenericDAOImpl;
import imaing.expsys.shared.exceptions.InvalidDataException;

import javax.persistence.NoResultException;

import org.springframework.transaction.annotation.Transactional;

public class ShopOwnerDAOImpl extends GenericDAOImpl<ShopOwner> implements ShopOwnerDAO {
	
	public ShopOwnerDAOImpl(Class<ShopOwner> type) {
		super(type);
	}
	
	@Override
	@Transactional(readOnly=false)
	public void save(ShopOwner gwtObject) throws InvalidDataException {
		if (gwtObject.getId() == null) {
			// New record
			if (gwtObject.getEmail() == null) {
				throw new InvalidDataException("E-mail not set for ShopOwner: "
						+ gwtObject.getEmail() + " " + gwtObject.getShopName());
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
		ShopOwner result = null;
		try {
			result = (ShopOwner) entityManager.createNamedQuery("ShopOwner.getShopOwnerForEmail")
															.setParameter("email", email).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
		return result;
		
	}
}
