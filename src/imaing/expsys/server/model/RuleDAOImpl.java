package imaing.expsys.server.model;

import imaing.expsys.client.domain.LogClause;
import imaing.expsys.client.domain.Rule;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.transaction.annotation.Transactional;

public class RuleDAOImpl extends GenericDAOImpl<RuleEnt, Rule> implements RuleDAO {
	
	public RuleDAOImpl(Class<RuleEnt> type) {
		super(type);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rule> listRulesForShop(Shop shop) {
		List<RuleEnt> ents = (List<RuleEnt>) em.createNamedQuery("RuleEnt.listRulesForShop")
											   .setParameter("shop", new ShopEnt(shop)).getResultList();
		List<Rule> dtos = new LinkedList<Rule>();
		if (ents != null) {
			for (RuleEnt e : ents) {
				dtos.add(e.getCleaned());
			}
		}
		
		for (Rule r : dtos) {
			List<LogClause> clauses = listLogClausesForRule(r);
			r.buildClausesTree(clauses);
		}
		
		return dtos;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<LogClause> listLogClausesForRule(Rule rule) {
		List<LogClauseEnt> ents = (List<LogClauseEnt>) em
				.createNamedQuery("RuleEnt.listLogClausesForRule")
				.setParameter("rule", new RuleEnt(rule)).getResultList();
		List<LogClause> dtos = new LinkedList<LogClause>();
		if (ents != null) {
			for (LogClauseEnt e : ents) {
				dtos.add((LogClause) e.getCleaned());
			}
		}
		return dtos;
	}
	
	@Override
	@Transactional(readOnly=false)
	public void delete(Rule dto) throws InvalidDataException {
		if (dto == null) throw new InvalidDataException("Trying to delete null object!");
		else if (dto.getId() == null) throw new InvalidDataException("Trying to delete unsaved object!");
		
		RuleEnt ent = em.find(RuleEnt.class, dto.getId());
		
		LogClause rootLc = getRootLogClauseForRule(dto);
		
		if (rootLc != null) {
			LogClauseEnt<?> lce = em.find(LogClauseEnt.class, rootLc.getId());
			em.remove(lce);
		}
		
		em.remove(ent);
	}
	
	@Override
	@Transactional(readOnly=false)
	public Rule save(Rule gRule) throws InvalidDataException {
		if (gRule == null) throw new InvalidDataException("Trying to save null object!");
		RuleEnt ent = new RuleEnt();
		
		gRule.determineNSetVals();
		
		ent.fill(gRule);
		
		try {
			ent = em.merge(ent);
		} catch (Exception e) {
			throw new InvalidDataException(e);
		}
		
		Rule savedRule = ent.getCleaned();
		LogClause rootClause = gRule.getLogClause();
		for (LogClause lc : rootClause) {
			lc.setRule(savedRule);
		}

		// very ugly :(
		em.merge(LogClauseEnt.instanceEntity(rootClause));
		
		List<LogClause> clauses = listLogClausesForRule(savedRule);
		savedRule.buildClausesTree(clauses);
		return savedRule;
	}

	@Override
	public LogClause getRootLogClauseForRule(Rule rule) {
		LogClauseEnt<?> result = null;
		try {
			result = (LogClauseEnt<?>) em.createNamedQuery("RuleEnt.getRootLogClauseForRule")
												.setParameter("rule", new RuleEnt(rule))
												.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
		if (result == null) return null;
		else return (LogClause)result.getCleaned();
	}
	
}
