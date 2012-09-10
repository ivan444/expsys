package imaing.expsys.server.model;

import imaing.expsys.client.domain.ProdChr;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name="prod_chr",uniqueConstraints=@UniqueConstraint(
		columnNames={"chr_id", "prod_id"}
))
@NamedQueries({
    @NamedQuery(name="ProdChrEnt.listProdChrForProduct",query="select e from ProdChrEnt as e where e.product=:product"),
    @NamedQuery(name="ProdChrEnt.getProdChrForProductAndCharacteristic",query="select e from ProdChrEnt as e where e.product=:product and e.chr=:chr"),
    @NamedQuery(name="ProdChrEnt.listProdChrForCharacteristic",query="select e from ProdChrEnt as e where e.chr=:chr")
})
public class ProdChrEnt extends BaseEntity<ProdChr> {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="chr_id", nullable=false, updatable=false)
	private CharacteristicEnt chr;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="prod_id", nullable=false, updatable=false)
	private ProductEnt product;
	
	@Column(name="value")
	private String value;
	
	public ProdChrEnt() {
	}
	
	public ProdChrEnt(ProdChr fc) {
		super(fc);
	}
	
	@Transient
	@Override
	public ProdChr getCleaned() {
		ProdChr g = new ProdChr();
		g.setId(getId());
		g.setValue(getValue());
		g.setChr(getChr().getCleaned());
		g.setProd(getProduct().getCleaned());
		return g;
	}

	@Transient
	@Override
	public void fill(ProdChr g) {
		setId(g.getId());
		setValue(g.getValue());
		setChr(new CharacteristicEnt(g.getChr()));
		setProduct(new ProductEnt(g.getProd()));
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ProductEnt getProduct() {
		return product;
	}

	public void setProduct(ProductEnt product) {
		this.product = product;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chr == null) ? 0 : chr.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
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
		ProdChrEnt other = (ProdChrEnt) obj;
		if (chr == null) {
			if (other.chr != null)
				return false;
		} else if (!chr.equals(other.chr))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

	public CharacteristicEnt getChr() {
		return chr;
	}

	public void setChr(CharacteristicEnt chr) {
		this.chr = chr;
	}

}
