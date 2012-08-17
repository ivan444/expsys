package imaing.expsys.client.domain;


/**
 * Characteristic of an product. Every ShopOwner has it's
 * product characteristic (therefore, characteristic name
 * isn't globally unique, but it is unique for one ShopOwner).
 * 
 * <s>ShopOwner isn't present in DTO object because it is not
 * necessary (it should be known if we are using his
 * characteristic).</s>
 */
public class Characteristic extends DTOObject {
	private ShopOwner shop;
	private String name;
	private int fClsNum;
	
	public ShopOwner getShop() {
		return shop;
	}

	public void setShop(ShopOwner shop) {
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
		Characteristic other = (Characteristic) obj;
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
