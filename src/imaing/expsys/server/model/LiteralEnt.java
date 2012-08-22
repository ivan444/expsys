package imaing.expsys.server.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import imaing.expsys.client.domain.Literal;

@Entity
@DiscriminatorValue("LITERAL")
public class LiteralEnt extends LogClauseEnt<Literal> {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="chr_id", nullable=false, updatable=false)
	private CharacteristicEnt chr;
	
	@Column(name="fuzzy_cls_idx")
	private Integer fuzzyClsIdx;
	
	public LiteralEnt() {
	}
	
	public LiteralEnt(Literal g) {
		super(g);
	}
	
	@Transient
	@Override
	public Literal getCleaned() {
		Literal g = new Literal();
		g.setId(getId());
		g.setNsLeft(getNsLeft());
		g.setNsRight(getNsRight());
		g.setRule(getRule().getCleaned());
		g.setFuzzyClsIdx(getFuzzyClsIdx());
		g.setChr(getChr().getCleaned());
		return g;
	}

	@Transient
	@Override
	public void fill(Literal g) {
		if (g.getFuzzyClsIdx() == null)
			throw new IllegalArgumentException("Fuzzy class index mustn't be null!");
		if (g.getFuzzyClsIdx().intValue() < 0)
			throw new IllegalArgumentException("Fuzzy class index must be positive!");
		if (g.getFuzzyClsIdx().intValue() >= g.getChr().getfClsNum())
			throw new IllegalArgumentException("Fuzzy class index must be less then chr's number of fuzzy classes!");
		
		setId(g.getId());
		setNsLeft(g.getNsLeft());
		setNsRight(g.getNsRight());
		setRule(new RuleEnt(g.getRule()));
		setFuzzyClsIdx(g.getFuzzyClsIdx());
		setChr(new CharacteristicEnt(g.getChr()));
	}

	public CharacteristicEnt getChr() {
		return chr;
	}

	public void setChr(CharacteristicEnt chr) {
		this.chr = chr;
	}

	public Integer getFuzzyClsIdx() {
		return fuzzyClsIdx;
	}

	public void setFuzzyClsIdx(Integer fuzzyClsIdx) {
		this.fuzzyClsIdx = fuzzyClsIdx;
	}

}
