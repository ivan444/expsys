package imaing.expsys.server.model;

import imaing.expsys.client.domain.Product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="product",uniqueConstraints=@UniqueConstraint(
		columnNames={"shop_id", "integration_id"}
))
@NamedQueries({
    @NamedQuery(name="ProductEnt.listProductsForShop",query="select e from ProductEnt as e where e.shop=:shop"),
    @NamedQuery(name="ProductEnt.getProductForShopAndIntegrationId",query="select e from ProductEnt as e where e.shop=:shop and e.integrationId=:integrationId")
})
public class ProductEnt extends BaseEntity<Product> {
	private static final long serialVersionUID = 1L;

	@ManyToOne
    @JoinColumn(name="shop_id", nullable=false)
	private ShopEnt shop;
	
	@Column(name="description")
	private String description;
	
	@Column(name="integration_id", nullable=false)
	private String integrationId;
	
	public ProductEnt() {
	}
	
	public ProductEnt(Product g) {
		super(g);
	}
	
	@Override
	@Transient
	public Product getCleaned() {
		Product g = new Product();
		g.setId(getId());
		g.setDescription(getDescription());
		g.setIntegrationId(getIntegrationId());
		g.setShop(getShop().getCleaned());
		return g;
	}

	@Override
	@Transient
	public void fill(Product g) {
		setId(g.getId());
		setDescription(g.getDescription());
		setIntegrationId(g.getIntegrationId());
		setShop(new ShopEnt(g.getShop()));
	}
	
	public ShopEnt getShop() {
		return shop;
	}

	public void setShop(ShopEnt shop) {
		this.shop = shop;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIntegrationId() {
		return integrationId;
	}

	public void setIntegrationId(String integrationId) {
		this.integrationId = integrationId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((integrationId == null) ? 0 : integrationId.hashCode());
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
		ProductEnt other = (ProductEnt) obj;
		if (integrationId == null) {
			if (other.integrationId != null)
				return false;
		} else if (!integrationId.equals(other.integrationId))
			return false;
		if (shop == null) {
			if (other.shop != null)
				return false;
		} else if (!shop.equals(other.shop))
			return false;
		return true;
	}

}
