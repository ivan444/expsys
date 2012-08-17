package imaing.expsys.server.dao;

import imaing.expsys.client.domain.FuzzyClass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="fuzzyclass",uniqueConstraints=@UniqueConstraint(
		columnNames={"chr", "value"}
))
@NamedQueries({
    @NamedQuery(name="FuzzyClassDao.listFClsForCharacteristic",query="select c from FuzzyClassDao as c where c.chr=:chr"),
    @NamedQuery(name="FuzzyClassDao.getFClsForCharacteristicAndValue",query="select c from FuzzyClassDao as c where c.chr=:chr and c.value=:value")
})
public class FuzzyClassDao implements DAOobject<FuzzyClass> {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private CharacteristicDao chr;
	
	@Column(name="value")
	private String value;
	
	@Column(name="membershipval")
	private double[] membershipVal;
	
	public FuzzyClassDao() {
	}
	
	public FuzzyClassDao(FuzzyClass fc) {
		fill(fc);
	}
	
	@Transient
	@Override
	public FuzzyClass getCleaned() {
		return getCleaned(null);
	}

	@Transient
	@Override
	public FuzzyClass getCleaned(Object caller) {
		FuzzyClass fc = new FuzzyClass();
		fc.setId(getId());
		fc.setChr(getChr().getCleaned());
		fc.setMembershipVal(getMembershipVal());
		fc.setValue(getValue());
		return fc;
	}

	@Transient
	@Override
	public void fill(FuzzyClass g) {
		setId(g.getId());
		setChr(new CharacteristicDao(g.getChr()));
		setMembershipVal(g.getMembershipVal());
		setValue(g.getValue());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CharacteristicDao getChr() {
		return chr;
	}

	public void setChr(CharacteristicDao chr) {
		this.chr = chr;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public double[] getMembershipVal() {
		return membershipVal;
	}

	public void setMembershipVal(double[] membershipVal) {
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
		FuzzyClassDao other = (FuzzyClassDao) obj;
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

}
