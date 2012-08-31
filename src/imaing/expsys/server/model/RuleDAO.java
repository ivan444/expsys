package imaing.expsys.server.model;

import imaing.expsys.client.domain.LogClause;
import imaing.expsys.client.domain.Rule;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.List;

public interface RuleDAO extends GenericDAO<RuleEnt, Rule> {
	List<Rule> listRulesForShop(Shop shop);
	List<LogClause> listLogClausesForRule(Rule rule);
	LogClause getRootLogClauseForRule(Rule rule);
	void delete(Rule dto) throws InvalidDataException;
}
