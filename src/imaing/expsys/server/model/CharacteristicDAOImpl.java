package imaing.expsys.server.model;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.ProdChr;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

public class CharacteristicDAOImpl extends GenericDAOImpl<CharacteristicEnt, Characteristic> implements CharacteristicDAO {
	
	private ProdChrDAO prodChrDao;
	private FuzzyClassDAO fclsDao;
	private RuleDAO ruleDao;
	
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

	@Override
	protected void extraDeleteOperations(CharacteristicEnt ent) throws InvalidDataException {
		Characteristic chr = ent.getCleaned();
		
		List<FuzzyClass> fclss = fclsDao.listFClsForCharacteristic(chr);
		fclsDao.deleteAll(fclss);
		
		List<LiteralEnt> literals = ruleDao.getLiteralEntsForCharacteristic(chr);
		Set<RuleEnt> rulesWithChr = new HashSet<RuleEnt>();
		for (LiteralEnt literalEnt : literals) {
			rulesWithChr.add(literalEnt.getRule());
		}
		for (RuleEnt ruleEnt : rulesWithChr) {
			ruleDao.delete(ruleEnt);
		}
		
		List<ProdChr> pcs = prodChrDao.listProdChrForCharacteristic(chr);
		prodChrDao.deleteAll(pcs);
		
	}

	public void setProdChrDao(ProdChrDAO prodChrDao) {
		this.prodChrDao = prodChrDao;
	}

	public void setFclsDao(FuzzyClassDAO fclsDao) {
		this.fclsDao = fclsDao;
	}

	public void setRuleDao(RuleDAO ruleDao) {
		this.ruleDao = ruleDao;
	}

}
