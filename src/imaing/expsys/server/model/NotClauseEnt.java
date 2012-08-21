package imaing.expsys.server.model;

import imaing.expsys.client.domain.LogClause;
import imaing.expsys.client.domain.NotClause;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("NOT")
public class NotClauseEnt extends LogClauseEnt<NotClause> {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="left_id", updatable=false)
	private LogClauseEnt<?> leftClause;
	
	public NotClauseEnt() {
	}
	
	public NotClauseEnt(NotClause g) {
		super(g);
	}
	
	@Transient
	@Override
	public NotClause getCleaned() {
		NotClause g = new NotClause();
		g.setId(getId());
		g.setNsLeft(getNsLeft());
		g.setNsRight(getNsRight());
		g.setRule(getRule().getCleaned());
		g.setLeftClause((LogClause) getLeftClause().getCleaned());
		
		return g;
	}

	@Transient
	@Override
	public void fill(NotClause g) {
		setId(g.getId());
		setNsLeft(g.getNsLeft());
		setNsRight(g.getNsRight());
		setRule(new RuleEnt(g.getRule()));
//		setLeftClause(leftClause)
		throw new IllegalStateException("Don't know how to cast leftClause, type is ambiguous"); //FIXME: make the exception disappear
	}

	public LogClauseEnt<?> getLeftClause() {
		return leftClause;
	}

	public void setLeftClause(LogClauseEnt<?> leftClause) {
		this.leftClause = leftClause;
	}

}
