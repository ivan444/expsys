package imaing.expsys.server.model;

import imaing.expsys.client.domain.LogClause;
import imaing.expsys.client.domain.Rule;
import imaing.expsys.client.domain.Shop;

import java.util.List;

public interface RuleDAO extends GenericDAO<RuleEnt, Rule> {
	List<Rule> listRulesForShop(Shop shop);
	List<LogClause> listLogClausesForRule(Rule rule);
}
