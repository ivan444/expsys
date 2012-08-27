package imaing.expsys.client.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import imaing.expsys.client.domain.LogClause.HasLeft;
import imaing.expsys.client.domain.LogClause.HasRight;
import imaing.expsys.shared.Relevance;

public class Rule extends DTOObject {
	private Shop shop;
	private String desc;
	private Relevance rel;
	private LogClause logClause;
	private Integer nsRoot;
	
	public void determineNSetVals() {
		int rootNsRight = logClause.determineNSetVals(0);
		setNsRoot(Integer.valueOf(rootNsRight+1));
	}
	
	public Rule() {
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Relevance getRel() {
		return rel;
	}

	public void setRel(Relevance rel) {
		this.rel = rel;
	}

	public LogClause getLogClause() {
		return logClause;
	}

	public void setLogClause(LogClause logClause) {
		this.logClause = logClause;
		determineNSetVals();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((rel == null) ? 0 : rel.hashCode());
		result = prime * result + ((shop == null) ? 0 : shop.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (rel != other.rel)
			return false;
		if (shop == null) {
			if (other.shop != null)
				return false;
		} else if (!shop.equals(other.shop))
			return false;
		return true;
	}

	public Integer getNsRoot() {
		return nsRoot;
	}

	public void setNsRoot(Integer nsRoot) {
		this.nsRoot = nsRoot;
	}

	public void buildClausesTree(List<LogClause> logClauses) {
		if (logClauses.size() == 0) {
//			throw new IllegalArgumentException("Can't build empty tree!");
			return;
		}
		
		Collections.sort(logClauses, new Comparator<LogClause>() {
			@Override
			public int compare(LogClause o1, LogClause o2) {
				return o1.getNsLeft().intValue()-o2.getNsLeft().intValue();
			}
		});
		
		
		LogClause parent = logClauses.get(0);
		recursiveTreeBuild(logClauses, 0);
		setLogClause(parent);
	}
	
	private int recursiveTreeBuild(List<LogClause> logClauses, int curIdx) {
		int lcSize = logClauses.size();
		if (curIdx >= lcSize) return curIdx;
		LogClause lc = logClauses.get(curIdx);
		
		if (lc.getNsRight().intValue()-lc.getNsLeft().intValue() == 1) {
			// Literal
			return curIdx;
		}
		
		if (curIdx+1 < lcSize) {
			// not last one
			LogClause lcNext = logClauses.get(curIdx+1);
			if (lcNext.getNsLeft().intValue() > lc.getNsRight().intValue()) {
				// end of branch
				return curIdx;
			}
			
			
			HasLeft lcHl = (HasLeft) lc;
			lcHl.setLeftClause(logClauses.get(curIdx+1));
			curIdx = recursiveTreeBuild(logClauses, curIdx+1);
			
			
			if (lcNext.getNsLeft().intValue() > lc.getNsRight().intValue()) {
				HasRight lcHr = (HasRight) lc;
				lcHr.setRightClause(logClauses.get(curIdx+1));
				curIdx = recursiveTreeBuild(logClauses, curIdx+1);
			}
		}
		
		return curIdx;
	}
	
	@Override
	public String toString() {
		return logClause + " => " + rel;
	}

}
