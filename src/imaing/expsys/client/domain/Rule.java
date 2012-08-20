package imaing.expsys.client.domain;

import imaing.expsys.shared.Relevance;

public class Rule extends DTOObject {
	private Shop shop;
	private String desc;
	private Relevance rel;
	private LogClause logClause;
	private Integer nsRoot;
	
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

}
