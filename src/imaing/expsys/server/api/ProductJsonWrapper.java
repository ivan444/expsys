package imaing.expsys.server.api;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.ProdChr;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductJsonWrapper {
	private String description;
	private String integId;;
	private Map<String, String> characteristics;
	
	public ProductJsonWrapper() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIntegId() {
		return integId;
	}

	public void setIntegId(String integId) {
		this.integId = integId;
	}

	public Map<String, String> getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(Map<String, String> characteristics) {
		this.characteristics = characteristics;
	}
	
	public Product genProduct(Shop shop, List<Characteristic> chrs) {
		Product p = new Product();
		p.setShop(shop);
		p.setDescription(getDescription());
		p.setIntegrationId(getIntegId());

		List<ProdChr> parsedProdChrs = new ArrayList<ProdChr>(characteristics.size());
		for (Characteristic c : chrs) {
			if (characteristics.containsKey(c.getName())) {
				String value = characteristics.get(c.getName());
				
				ProdChr pc = new ProdChr();
				pc.setChr(c);
				pc.setProd(p);
				pc.setValue(value);
				
				parsedProdChrs.add(pc);
			}
		}
		
		p.setCharacteristics(parsedProdChrs);
		
		return p;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((characteristics == null) ? 0 : characteristics.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((integId == null) ? 0 : integId.hashCode());
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
		ProductJsonWrapper other = (ProductJsonWrapper) obj;
		if (characteristics == null) {
			if (other.characteristics != null)
				return false;
		} else if (!characteristics.equals(other.characteristics))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (integId == null) {
			if (other.integId != null)
				return false;
		} else if (!integId.equals(other.integId))
			return false;
		return true;
	}
}
