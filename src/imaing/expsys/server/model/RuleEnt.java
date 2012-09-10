package imaing.expsys.server.model;


import imaing.expsys.client.domain.Rule;
import imaing.expsys.shared.Relevance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * http://www.slideshare.net/billkarwin/sql-antipatterns-strike-back
 *
 */
@Entity
@Table(name="fuzzy_rule",uniqueConstraints=@UniqueConstraint(
		columnNames={"rel", "shop_id", "description"}
))
@NamedQueries({
    @NamedQuery(name="RuleEnt.listRulesForShop",query="select e from RuleEnt as e where e.shop=:shop"),
    @NamedQuery(name="RuleEnt.listLogClausesForRule",query="select e from LogClauseEnt as e where e.rule=:rule"),
    @NamedQuery(name="RuleEnt.getRootLogClauseForRule",query="select e from LogClauseEnt as e where e.rule=:rule and nsLeft=1"),
    @NamedQuery(name="RuleEnt.getLiteralEntsForCharacteristic",query="select e from LiteralEnt as e where e.chr=:chr")
})
public class RuleEnt extends BaseEntity<Rule> {
	private static final long serialVersionUID = 1L;
	
	@Column(name="rel", nullable=false)
	@Enumerated
	private Relevance rel;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name="shop_id", nullable=false, updatable=false)
	private ShopEnt shop;
	
	@Column(name="description", nullable=false)
	private String desc;
	
	@Column(name="ns_root")
	private Integer nsRoot;
	
	public RuleEnt() {}
	
	public RuleEnt(Rule g) {
		super(g);
	}
	
	@Transient
	@Override
	public void fill(Rule g) {
		setId(g.getId());
		setDesc(g.getDesc());
		setNsRoot(g.getNsRoot());
		setRel(g.getRel());
		setShop(new ShopEnt(g.getShop()));
	}
	
	@Transient
	@Override
	public Rule getCleaned() {
		Rule g = new Rule();
		
		g.setId(getId());
		g.setDesc(getDesc());
		g.setNsRoot(getNsRoot());
		g.setShop(getShop().getCleaned());
		
		// clean relevance
		switch (getRel()) {
		case REL_HIGH:
			g.setRel(Relevance.REL_HIGH);
			break;
		case REL_MID:
			g.setRel(Relevance.REL_MID);
			break;
		case REL_LOW:
			g.setRel(Relevance.REL_LOW);
			break;
		default:
			throw new RuntimeException("Invalid relevance setting!! Relevace set to: " + getRel());
		}
		
		return g;
	}

	public Relevance getRel() {
		return rel;
	}

	public void setRel(Relevance rel) {
		this.rel = rel;
	}

	public ShopEnt getShop() {
		return shop;
	}

	public void setShop(ShopEnt shop) {
		this.shop = shop;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getNsRoot() {
		return nsRoot;
	}

	public void setNsRoot(Integer nsRoot) {
		this.nsRoot = nsRoot;
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
		RuleEnt other = (RuleEnt) obj;
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
	
}
