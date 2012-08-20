package imaing.expsys.server.model;

import imaing.expsys.client.domain.Shop;
import imaing.expsys.shared.exceptions.InvalidDataException;

import javax.persistence.NoResultException;

import org.springframework.transaction.annotation.Transactional;

public class ShopDAOImpl extends GenericDAOImpl<ShopEnt, Shop> implements ShopDAO {
	
	public ShopDAOImpl(Class<ShopEnt> type) {
		super(type);
	}
	
	@Override
	@Transactional(readOnly=false)
	public void save(Shop gwtObject) throws InvalidDataException {
		if (gwtObject.getId() == null) {
			// New record
			if (gwtObject.getEmail() == null) {
				throw new InvalidDataException("E-mail not set for Shop: "
						+ gwtObject.getEmail() + " " + gwtObject.getShopName());
			} else {
				Shop other = getShopForEmail(gwtObject.getEmail());
				if (other != null) {
					throw new InvalidDataException("Shop with e-mail "
							+ gwtObject.getEmail() + " already exists!");
				}
			}
		}
		super.save(gwtObject);
	}

	@Override
	public Shop getShopForEmail(String email) {
		ShopEnt result = null;
		try {
			result = (ShopEnt) entityManager.createNamedQuery("ShopEnt.getShopForEmail")
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