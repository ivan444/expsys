package imaing.expsys.server.model;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.transaction.annotation.Transactional;

public class ShopDAOImpl extends GenericDAOImpl<ShopEnt, Shop> implements ShopDAO {
	
	private ProductDAO prodDao;
	private CharacteristicDAO chrDao;
	
	public ShopDAOImpl(Class<ShopEnt> type) {
		super(type);
	}
	
	@Override
	@Transactional(readOnly=false)
	public Shop save(Shop gwtObject) throws InvalidDataException {
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
		
		return super.save(gwtObject);
	}

	@Override
	public Shop getShopForEmail(String email) {
		ShopEnt result = null;
		try {
			result = (ShopEnt) em.createNamedQuery("ShopEnt.getShopForEmail")
											.setParameter("shopEmail", email).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
		if (result == null){
			return null;
		} else {
			return result.getCleaned();
		}
	}

	@Override
	protected void extraDeleteOperations(ShopEnt ent)
			throws InvalidDataException {
		Shop shop = ent.getCleaned();
		
		List<Product> prods = prodDao.listProductsForShop(shop);
		prodDao.deleteAll(prods);
		
		List<Characteristic> chrs = chrDao.listCharacteristicsForShop(shop);
		chrDao.deleteAll(chrs);
	}

	public void setChrDao(CharacteristicDAO chrDao) {
		this.chrDao = chrDao;
	}

	public void setProdDao(ProductDAO prodDao) {
		this.prodDao = prodDao;
	}
}
