package imaing.expsys.server.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/*
 *  Obavezna literatura s primjerima: http://docs.jboss.org/hibernate/stable/annotations/reference/en/html/
 */

@Entity
@Table(name="shopowner",uniqueConstraints=@UniqueConstraint(
		columnNames={"email"}
))
@NamedQueries({
    @NamedQuery(name="ShopOwner.getShopOwnerForEmail",query="select o from ShopOwner as o where o.email=:email")
})
public class ShopOwner extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(name="password")
	private String password;
	
	@Column(name="email")
	private String email;
	
	@Column(name="shopName")
	private String shopName;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="shop")
	private List<Product> products = new ArrayList<Product>();
	
	public ShopOwner() {}
	
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
		ShopOwner other = (ShopOwner) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
