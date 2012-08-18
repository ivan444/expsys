package imaing.expsys.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="product",uniqueConstraints=@UniqueConstraint(
		columnNames={"shop_id", "description"}
))
public class Product extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
    @JoinColumn(name="shop_id", nullable=false)
	private ShopOwner shop;
	
	@Column(name="description")
	private String description;
	
	@Column(name="fclsnum")
	private int fClsNum;
	
	public Product() {
	}
	
	public ShopOwner getShop() {
		return shop;
	}

	public void setShop(ShopOwner shop) {
		this.shop = shop;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		result = prime * result + ((description == null) ? 0 : description.hashCode());
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
		Product other = (Product) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (shop == null) {
			if (other.shop != null)
				return false;
		} else if (!shop.equals(other.shop))
			return false;
		return true;
	}

}
