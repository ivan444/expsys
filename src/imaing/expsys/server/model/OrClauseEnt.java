package imaing.expsys.server.model;

import imaing.expsys.client.domain.LogClause;
import imaing.expsys.client.domain.OrClause;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="or_clause")
@DiscriminatorValue("OR")
public class OrClauseEnt extends LogClauseEnt<OrClause> {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="left_id", nullable=false, updatable=false)
	private LogClauseEnt<?> leftClause;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="right_id", nullable=false, updatable=false)
	private LogClauseEnt<?> rightClause;
	
	public OrClauseEnt() {
	}
	
	public OrClauseEnt(OrClause g) {
		super(g);
	}
	
	@Transient
	@Override
	public OrClause getCleaned() {
		OrClause g = new OrClause();
		g.setId(getId());
		g.setNsLeft(getNsLeft());
		g.setNsRight(getNsRight());
		g.setRule(getRule().getCleaned());
		g.setLeftClause((LogClause) getLeftClause().getCleaned());
		g.setRightClause((LogClause) getRightClause().getCleaned());
		
		return g;
	}

	@Transient
	@Override
	public void fill(OrClause g) {
		setId(g.getId());
		setNsLeft(g.getNsLeft());
		setNsRight(g.getNsRight());
		setRule(new RuleEnt(g.getRule()));
		setLeftClause(LogClauseEnt.instanceEntity(g.getLeftClause()));
		setRightClause(LogClauseEnt.instanceEntity(g.getRightClause()));
	}

	public LogClauseEnt<?> getLeftClause() {
		return leftClause;
	}

	public void setLeftClause(LogClauseEnt<?> leftClause) {
		this.leftClause = leftClause;
	}

	public LogClauseEnt<?> getRightClause() {
		return rightClause;
	}

	public void setRightClause(LogClauseEnt<?> rightClause) {
		this.rightClause = rightClause;
	}

}
