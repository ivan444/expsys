package imaing.expsys.server.olddao;


import imaing.expsys.client.domain.ShopOwner;
import imaing.expsys.server.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/*
 *  Obavezna literatura s primjerima: http://docs.jboss.org/hibernate/stable/annotations/reference/en/html/
 */

@Entity
@Table(name="shopowner",uniqueConstraints=@UniqueConstraint(
		columnNames={"email"}
))
@NamedQueries({
    @NamedQuery(name="ShopOwnerDao.getShopOwnerForEmail",query="select o from ShopOwnerDao as o where o.email=:email")
})
public class ShopOwnerDao implements DAOobject<ShopOwner> {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="password")
	private String password;
	
	@Column(name="email")
	private String email;
	
	@Column(name="shopName")
	private String shopName;
	
	public ShopOwnerDao() {}
	
	public ShopOwnerDao(ShopOwner shpOwner) {
		fill(shpOwner);
	}
	
	@Transient
	@Override
	public void fill(ShopOwner shpOwner) {
		setId(shpOwner.getId());
		setPassword(shpOwner.getPassword());
		setEmail(shpOwner.getEmail());
		setShopName(shpOwner.getShopName());
	}
	
	@Transient
	@Override
	public ShopOwner getCleaned(Object caller) {
		ShopOwner shpOwner = new ShopOwner();
		
		shpOwner.setId(getId());
		shpOwner.setPassword(getPassword());
		shpOwner.setEmail(getEmail());
		shpOwner.setShopName(getShopName());
		
		return shpOwner;
	}
	
	@Transient
	@Override
	public ShopOwner getCleaned() {
		return getCleaned(null);
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		ShopOwnerDao other = (ShopOwnerDao) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
}
