package imaing.expsys.server.api;

import java.util.List;

public class Ranking {
	private List<String> integIds;
	private List<Long> ruleIds;
	
	public Ranking() {
	}

	public List<String> getIntegIds() {
		return integIds;
	}

	public void setIntegIds(List<String> integIds) {
		this.integIds = integIds;
	}

	public List<Long> getRuleIds() {
		return ruleIds;
	}

	public void setRuleIds(List<Long> ruleIds) {
		this.ruleIds = ruleIds;
	}
}
