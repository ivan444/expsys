package imaing.expsys.server.model;

import imaing.expsys.client.domain.ProdChr;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;

public class ProductDAOImpl extends GenericDAOImpl<ProductEnt, Product> implements ProductDAO {
	
	@Autowired ProdChrDAO prodChrDao;
	
	public ProductDAOImpl(Class<ProductEnt> type) {
		super(type);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> listProductsForShop(Shop shop) {
		List<ProductEnt> ents = (List<ProductEnt>) entityManager.createNamedQuery("ProductEnt.listProductsForShop")
																.setParameter("shop", new ShopEnt(shop)).getResultList();
		List<Product> dtos = new LinkedList<Product>();
		if (ents != null) {
			for (ProductEnt e : ents) {
				dtos.add(e.getCleaned());
			}
		}
		return dtos;
	}

	@Override
	public Product getProductForShopAndIntegrationId(Shop shop, String integrationId) {
		ProductEnt result = null;
		try {
			result = (ProductEnt) entityManager.createNamedQuery("ProductEnt.getProductForShopAndIntegrationId")
											   .setParameter("shop", new ShopEnt(shop)).setParameter("integrationId", integrationId)
											   .getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
		if (result == null) return null;
		else return result.getCleaned();
	}

	@Override
	public List<Product> listProductsWCharsForShop(Shop shop) {
		List<Product> prods = listProductsForShop(shop);
		for (Product p : prods) {
			List<ProdChr> pcs = prodChrDao.listProdChrForProduct(p);
			p.setCharacteristics(pcs);
		}
		return prods;
	}

	@Override
	public Product getProductWCharsForShopAndIntegrationId(Shop shop,
			String integrationId) {
		Product p = getProductForShopAndIntegrationId(shop, integrationId);
		List<ProdChr> pcs = prodChrDao.listProdChrForProduct(p);
		p.setCharacteristics(pcs);
		return p;
	}
}
