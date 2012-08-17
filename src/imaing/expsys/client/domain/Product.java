package imaing.expsys.client.domain;

import java.util.List;

public class Product extends DTOObject {
	private ShopOwner shop;
	private String description;
	private String integrationId;
	private List<ProdChrstc> characteristics;

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

	public String getIntegrationId() {
		return integrationId;
	}

	public void setIntegrationId(String integrationId) {
		this.integrationId = integrationId;
	}

	public List<ProdChrstc> getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(List<ProdChrstc> characteristics) {
		this.characteristics = characteristics;
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
		Product other = (Product) obj;
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
