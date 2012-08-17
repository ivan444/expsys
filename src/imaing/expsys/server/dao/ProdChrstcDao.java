package imaing.expsys.server.dao;

import imaing.expsys.client.domain.ProdChrstc;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.ShopOwner;

import java.util.ArrayList;
import java.util.List;

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
@Table(name="prodchrstc",uniqueConstraints=@UniqueConstraint(
		columnNames={"chr", "value"}
))
@NamedQueries({
    @NamedQuery(name="FuzzyClassDao.listFClsForCharacteristic",query="select c from FuzzyClassDao as c where c.chr=:chr"),
    @NamedQuery(name="FuzzyClassDao.getFClsForCharacteristicAndValue",query="select c from FuzzyClassDao as c where c.chr=:chr and c.value=:value")
})
public class ProdChrstcDao implements DAOobject<ProdChrstc> {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private CharacteristicDao chrstc;
	
	@Column(name="prodid")
	private Long prodId;
	
	@Column(name="value")
	private String value;
	
	public ProdChrstcDao() {
	}
	
	public ProdChrstcDao(ProdChrstc fc) {
		fill(fc);
	}
	
	@Transient
	@Override
	public ProdChrstc getCleaned() {
		return getCleaned(null);
	}

	@Transient
	@Override
	public ProdChrstc getCleaned(Object caller) {
		if (!(caller instanceof Product)) {
			throw new IllegalStateException(
					"ProdChrstc can be cleaned only by the Product! (caller MUST be Product)");
		}
		
		ProdChrstc g = new ProdChrstc();
		g.setId(getId());
		g.setValue(getValue());
		g.setChrstc(getChrstc().getCleaned());
		g.setProd((Product) caller);
		return g;
	}

	@Transient
	@Override
	public void fill(ProdChrstc g) {
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public CharacteristicDao getChrstc() {
		return chrstc;
	}

	public void setChrstc(CharacteristicDao chrstc) {
		this.chrstc = chrstc;
	}

	public ProductDao getProd() {
		return prod;
	}

	public void setProd(ProductDao prod) {
		this.prod = prod;
	}

}
