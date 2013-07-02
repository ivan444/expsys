package imaing.expsys.server.model;

import imaing.expsys.client.domain.FuzzyClass;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="fuzzy_class",uniqueConstraints=@UniqueConstraint(
		columnNames={"chr_id", "value"}
))
@NamedQueries({
    @NamedQuery(name="FuzzyClassEnt.listFClsForCharacteristic",query="select c from FuzzyClassEnt as c where c.chr=:chr"),
    @NamedQuery(name="FuzzyClassEnt.listFClsForShop",query="select c from FuzzyClassEnt as c where c.chr.shop=:shop"),
    @NamedQuery(name="FuzzyClassEnt.getFClsForCharacteristicAndValue",query="select c from FuzzyClassEnt as c where c.chr=:chr and c.value=:value")
})
public class FuzzyClassEnt extends BaseEntity<FuzzyClass> {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="chr_id", nullable=false, updatable=false)
	private CharacteristicEnt chr;
	
	@Column(name="value", nullable=false)
	private String value;
	
	@Column(name="membershipval")
	@ElementCollection(fetch=FetchType.EAGER)
	private List<Double> membershipVal = new ArrayList<Double>();
	
	@Column(name="x_pos")
	private Integer xPos;
	
	public FuzzyClassEnt() {
	}
	
	public FuzzyClassEnt(FuzzyClass fc) {
		super(fc);
	}
	
	@Transient
	@Override
	public FuzzyClass getCleaned() {
		FuzzyClass fc = new FuzzyClass();
		fc.setId(getId());
		fc.setChr(getChr().getCleaned());
		fc.setValue(getValue());
		fc.setxPos(getxPos());
		
		List<Double> mvalEnt = getMembershipVal();
		double[] mvalClean = new double[mvalEnt.size()];
		for (int i = 0; i < mvalClean.length; i++) {
			mvalClean[i] = mvalEnt.get(i).doubleValue();
		}
		fc.setMembershipVal(mvalClean);
		return fc;
	}

	@Transient
	@Override
	public void fill(FuzzyClass g) {
		setId(g.getId());
		setChr(new CharacteristicEnt(g.getChr()));
		setValue(g.getValue());
		
		double[] mval = g.getMembershipVal();
		for (int i = 0; i < mval.length; i++) {
			getMembershipVal().add(Double.valueOf(mval[i]));
		}
	}

	public CharacteristicEnt getChr() {
		return chr;
	}

	public void setChr(CharacteristicEnt chr) {
		this.chr = chr;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Double> getMembershipVal() {
		return membershipVal;
	}

	public void setMembershipVal(List<Double> membershipVal) {
		this.membershipVal = membershipVal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chr == null) ? 0 : chr.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		FuzzyClassEnt other = (FuzzyClassEnt) obj;
		if (chr == null) {
			if (other.chr != null)
				return false;
		} else if (!chr.equals(other.chr))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public Integer getxPos() {
		if (xPos != null) return xPos;
		else return Integer.valueOf(0);
	}

	public void setxPos(Integer xPos) {
		this.xPos = xPos;
	}

}
