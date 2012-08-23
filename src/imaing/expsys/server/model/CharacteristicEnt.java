package imaing.expsys.server.model;

import imaing.expsys.client.domain.Characteristic;

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
@Table(name="characteristic",uniqueConstraints=@UniqueConstraint(
		columnNames={"shop_id", "name"}
))
@NamedQueries({
    @NamedQuery(name="CharacteristicEnt.listCharacteristicsForShop",query="select c from CharacteristicEnt as c where c.shop=:shop"),
    @NamedQuery(name="CharacteristicEnt.getCharacteristicForShopAndName",query="select c from CharacteristicEnt as c where c.shop=:shop and c.name=:name")
})
public class CharacteristicEnt extends BaseEntity<Characteristic> {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="shop_id", nullable=false, updatable=false)
	private ShopEnt shop;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="fclsnum")
	private int fClsNum;
	
	public CharacteristicEnt() {
	}
	
	public CharacteristicEnt(Characteristic c) {
		super(c);
	}
	
	@Transient
	@Override
	public Characteristic getCleaned() {
		Characteristic chr = new Characteristic();
		chr.setId(getId());
		chr.setfClsNum(getfClsNum());
		chr.setName(getName());
		chr.setShop(getShop().getCleaned());
		return chr;
	}

	@Transient
	@Override
	public void fill(Characteristic g) {
		setId(g.getId());
		setfClsNum(g.getfClsNum());
		setName(g.getName());
		setShop(new ShopEnt(g.getShop()));
	}

	public ShopEnt getShop() {
		return shop;
	}

	public void setShop(ShopEnt shop) {
		this.shop = shop;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getfClsNum() {
		return fClsNum;
	}

	public void setfClsNum(int fClsNum) {
		this.fClsNum = fClsNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CharacteristicEnt other = (CharacteristicEnt) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (shop == null) {
			if (other.shop != null)
				return false;
		} else if (!shop.equals(other.shop))
			return false;
		return true;
	}

	
}
