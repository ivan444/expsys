package imaing.expsys.client.domain;

/**
 * Value of one products characteristic.
 */
public class ProdChrstc extends DTOObject {
	private Product prod;
	private Characteristic chrstc;
	private String value;

	public Product getProd() {
		return prod;
	}

	public void setProd(Product prod) {
		this.prod = prod;
	}

	public Characteristic getChrstc() {
		return chrstc;
	}

	public void setChrstc(Characteristic chrstc) {
		this.chrstc = chrstc;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chrstc == null) ? 0 : chrstc.hashCode());
		result = prime * result + ((prod == null) ? 0 : prod.hashCode());
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
		ProdChrstc other = (ProdChrstc) obj;
		if (chrstc == null) {
			if (other.chrstc != null)
				return false;
		} else if (!chrstc.equals(other.chrstc))
			return false;
		if (prod == null) {
			if (other.prod != null)
				return false;
		} else if (!prod.equals(other.prod))
			return false;
		return true;
	}

}
