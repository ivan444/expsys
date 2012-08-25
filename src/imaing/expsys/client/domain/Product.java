package imaing.expsys.client.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class Product extends DTOObject {
	private Shop shop;
	private String description;
	private String integrationId;
	private List<ProdChr> characteristics;
	
	public Product() {
		characteristics = new ArrayList<ProdChr>();
	}
	
	public static List<Product> parseProductsJSON(String prodsJson, Shop shop, List<Characteristic> characteristics) throws ParseException {
		JSONArray jsonProds = null;
		try {
			jsonProds = JSONParser.parseStrict(prodsJson).isObject().get("products").isArray();
			if (jsonProds == null) {
				throw new ParseException("Products JSON is not an JSON array as it should be!", 0);
			}
		} catch (NullPointerException e) {
			throw new ParseException("Malformed products JSON!", 0);
		}
		
		int prodsNum = jsonProds.size();
		List<Product> parsedProds = new ArrayList<Product>(prodsNum);
		for (int i = 0; i < prodsNum; i++) {
			JSONObject prod = jsonProds.get(i).isObject();
			if (prod == null) {
				throw new ParseException("Product JSON is not an JSON object as it should be!", 0);
			}
			
			try {
				String desc = prod.get("desc").isString().stringValue();
				String integId = prod.get("integid").isString().stringValue();
				
				Product p = new Product();
				p.setShop(shop);
				p.setDescription(desc);
				p.setIntegrationId(integId);
				
				JSONObject prodChrs = prod.get("chrs").isObject();
				List<ProdChr> parsedProdChrs = new ArrayList<ProdChr>(prodChrs.size());
				for (Characteristic c : characteristics) {
					if (prodChrs.containsKey(c.getName())) {
						String value = prodChrs.get(c.getName()).isString().stringValue();
						
						ProdChr pc = new ProdChr();
						pc.setChr(c);
						pc.setProd(p);
						pc.setValue(value);
						
						parsedProdChrs.add(pc);
					}
				}
				
				p.setCharacteristics(parsedProdChrs);
				
				parsedProds.add(p);
				
			} catch (NullPointerException e) {
				throw new ParseException("Malformed products JSON!", 0);
			}
		}
		
		return parsedProds;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
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

	public List<ProdChr> getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(List<ProdChr> characteristics) {
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
