package imaing.expsys.server.model;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.ProdChr;
import imaing.expsys.client.domain.Product;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

public class ProdChrDAOImpl extends GenericDAOImpl<ProdChrEnt, ProdChr> implements ProdChrDAO {
	
	public ProdChrDAOImpl(Class<ProdChrEnt> type) {
		super(type);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdChr> listProdChrForProduct(Product product) {
		List<ProdChrEnt> ents = (List<ProdChrEnt>) em.createNamedQuery("ProdChrEnt.listProdChrForProduct")
																	  .setParameter("product", new ProductEnt(product)).getResultList();
		List<ProdChr> dtos = new LinkedList<ProdChr>();
		if (ents != null){
			for (ProdChrEnt fc : ents) {
				dtos.add(fc.getCleaned());
			}
		}
		return dtos;
	}

	@Override
	public ProdChr getProdChrForProductAndCharacteristic(Product product, Characteristic chr) {
		ProdChrEnt result = null;
		try {
			result = (ProdChrEnt) em.createNamedQuery("ProdChrEnt.getProdChrForProductAndCharacteristic")
											   .setParameter("product", new ProductEnt(product)).setParameter("chr", new CharacteristicEnt(chr)).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
		if (result == null) return null;
		else return result.getCleaned();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteAllForProduct(Product product) throws InvalidDataException {
		List<ProdChrEnt> ents = (List<ProdChrEnt>) em.createNamedQuery("ProdChrEnt.listProdChrForProduct")
				  .setParameter("product", new ProductEnt(product)).getResultList();
		for (ProdChrEnt pce : ents) {
			delete(pce);
		}
	}

}
