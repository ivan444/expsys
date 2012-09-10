package imaing.expsys.server.model;

import imaing.expsys.client.domain.ProdChr;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.transaction.annotation.Transactional;

public class ProductDAOImpl extends GenericDAOImpl<ProductEnt, Product> implements ProductDAO {
	
	private ProdChrDAO prodChrDao;
	
	public ProductDAOImpl(Class<ProductEnt> type) {
		super(type);
	}
	
	public void setProdChrDao(ProdChrDAO prodChrDao) {
		this.prodChrDao = prodChrDao;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> listProductsForShop(Shop shop) {
		List<ProductEnt> ents = (List<ProductEnt>) em.createNamedQuery("ProductEnt.listProductsForShop")
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
			result = (ProductEnt) em.createNamedQuery("ProductEnt.getProductForShopAndIntegrationId")
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

	@Override
	@Transactional(readOnly=false)
	public Product save(Product g) throws InvalidDataException {
		if (g == null) throw new InvalidDataException("Trying to save null object!");
		if (g.getShop() == null || g.getShop().getId() == null) throw new InvalidDataException("Product's shop is null object or not persisted!");
		
		ProductEnt pe = new ProductEnt();
		pe.fill(g);
		pe = em.merge(pe);
		return pe.getCleaned();
	}
	
	@Override
	protected void extraDeleteOperations(ProductEnt ent)
			throws InvalidDataException {
		Product p = ent.getCleaned();
		prodChrDao.deleteAllForProduct(p);
	}

}
