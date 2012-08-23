package imaing.expsys.server.model;


import imaing.expsys.client.domain.Shop;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="shop",uniqueConstraints=@UniqueConstraint(
		columnNames={"shopEmail"}
))
@NamedQueries({
    @NamedQuery(name="ShopEnt.getShopForEmail",query="select o from ShopEnt as o where o.email=:shopEmail")
})
public class ShopEnt extends BaseEntity<Shop> {
	private static final long serialVersionUID = 1L;

	@Column(name="password")
	private String password;
	
	@Column(name="shopEmail")
	private String email;
	
	@Column(name="shopName")
	private String shopName;
	
	public ShopEnt() {}
	
	public ShopEnt(Shop shpOwner) {
		super(shpOwner);
	}
	
	@Transient
	@Override
	public void fill(Shop shpOwner) {
		setId(shpOwner.getId());
		setPassword(shpOwner.getPassword());
		setEmail(shpOwner.getEmail());
		setShopName(shpOwner.getShopName());
	}
	
	@Transient
	@Override
	public Shop getCleaned() {
		Shop g = new Shop();
		
		g.setId(getId());
		g.setPassword(getPassword());
		g.setEmail(getEmail());
		g.setShopName(getShopName());
		
		return g;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		ShopEnt other = (ShopEnt) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
}
