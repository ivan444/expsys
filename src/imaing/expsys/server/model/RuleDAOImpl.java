package imaing.expsys.server.model;

import imaing.expsys.client.domain.Rule;
import imaing.expsys.client.domain.Shop;

import java.util.LinkedList;
import java.util.List;

public class RuleDAOImpl extends GenericDAOImpl<RuleEnt, Rule> implements RuleDAO {
	
	public RuleDAOImpl(Class<RuleEnt> type) {
		super(type);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rule> listRulesForShop(Shop shop) {
		List<RuleEnt> ents = (List<RuleEnt>) entityManager.createNamedQuery("RuleEnt.listRulesForShop")
														  .setParameter("shop", new ShopEnt(shop)).getResultList();
		List<Rule> dtos = new LinkedList<Rule>();
		if (ents != null) {
			for (RuleEnt e : ents) {
				dtos.add(e.getCleaned());
			}
		}
		return dtos;
	}
	
}
