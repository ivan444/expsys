package imaing.expsys.server.dao;

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

import imaing.expsys.client.domain.Characteristic;

@Entity
@Table(name="characteristic",uniqueConstraints=@UniqueConstraint(
		columnNames={"shop", "name"}
))
@NamedQueries({
    @NamedQuery(name="CharacteristicDao.listCharacteristicsForShop",query="select c from CharacteristicsDao as c where c.shop=:shop"),
    @NamedQuery(name="CharacteristicDao.getCharacteristicForShopAndName",query="select c from CharacteristicsDao as c where c.shop=:shop and c.name=:name")
})
public class CharacteristicDao implements DAOobject<Characteristic> {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private ShopOwnerDao shop;
	
	@Column(name="name")
	private String name;
	
	@Column(name="fclsnum")
	private int fClsNum;
	
	public CharacteristicDao() {
	}
	
	public CharacteristicDao(Characteristic c) {
		fill(c);
	}
	
	@Transient
	@Override
	public Characteristic getCleaned() {
		return getCleaned(null);
	}

	@Transient
	@Override
	public Characteristic getCleaned(Object caller) {
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
		setShop(new ShopOwnerDao(g.getShop()));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ShopOwnerDao getShop() {
		return shop;
	}

	public void setShop(ShopOwnerDao shop) {
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
		CharacteristicDao other = (CharacteristicDao) obj;
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
