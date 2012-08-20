package imaing.expsys.server.model;

import imaing.expsys.client.domain.DTOObject;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="log_clause",uniqueConstraints=@UniqueConstraint(
		columnNames={"rule_id", "ns_right", "ns_left"}
))
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE", discriminatorType=DiscriminatorType.STRING,length=10)
@DiscriminatorValue("LOG")
public abstract class LogClauseEnt<L extends DTOObject> extends BaseEntity<L> {
    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name="rule_id", nullable=false, updatable=false)
	protected RuleEnt rule;
	
	@Column(name="ns_left", nullable=false)
	protected Integer nsLeft;
	
	@Column(name="ns_right", nullable=false)
	protected Integer nsRight;
	
	public LogClauseEnt() {
	}
	
	public LogClauseEnt(L g) {
		super(g);
	}

	public RuleEnt getRule() {
		return rule;
	}

	public void setRule(RuleEnt rule) {
		this.rule = rule;
	}

	public Integer getNsLeft() {
		return nsLeft;
	}

	public void setNsLeft(Integer nsLeft) {
		this.nsLeft = nsLeft;
	}

	public Integer getNsRight() {
		return nsRight;
	}

	public void setNsRight(Integer nsRight) {
		this.nsRight = nsRight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nsLeft == null) ? 0 : nsLeft.hashCode());
		result = prime * result + ((nsRight == null) ? 0 : nsRight.hashCode());
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogClauseEnt other = (LogClauseEnt) obj;
		if (nsLeft == null) {
			if (other.nsLeft != null)
				return false;
		} else if (!nsLeft.equals(other.nsLeft))
			return false;
		if (nsRight == null) {
			if (other.nsRight != null)
				return false;
		} else if (!nsRight.equals(other.nsRight))
			return false;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		return true;
	}
}
