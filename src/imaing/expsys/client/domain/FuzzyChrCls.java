package imaing.expsys.client.domain;

/**
 * Fuzzy class descriptor for characteristic.
 */
public class FuzzyChrCls extends DTOObject {
	private Characteristic chr;
	private double xLeftUp;
	private double xLeftDown;
	private double xRightUp;
	private double xRightDown;

	public Characteristic getChr() {
		return chr;
	}

	public void setChr(Characteristic chr) {
		this.chr = chr;
	}

	public double getxLeftUp() {
		return xLeftUp;
	}

	public void setxLeftUp(double xLeftUp) {
		this.xLeftUp = xLeftUp;
	}

	public double getxLeftDown() {
		return xLeftDown;
	}

	public void setxLeftDown(double xLeftDown) {
		this.xLeftDown = xLeftDown;
	}

	public double getxRightUp() {
		return xRightUp;
	}

	public void setxRightUp(double xRightUp) {
		this.xRightUp = xRightUp;
	}

	public double getxRightDown() {
		return xRightDown;
	}

	public void setxRightDown(double xRightDown) {
		this.xRightDown = xRightDown;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chr == null) ? 0 : chr.hashCode());
		long temp;
		temp = Double.doubleToLongBits(xLeftDown);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(xLeftUp);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(xRightDown);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(xRightUp);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		FuzzyChrCls other = (FuzzyChrCls) obj;
		if (chr == null) {
			if (other.chr != null)
				return false;
		} else if (!chr.equals(other.chr))
			return false;
		if (Double.doubleToLongBits(xLeftDown) != Double
				.doubleToLongBits(other.xLeftDown))
			return false;
		if (Double.doubleToLongBits(xLeftUp) != Double
				.doubleToLongBits(other.xLeftUp))
			return false;
		if (Double.doubleToLongBits(xRightDown) != Double
				.doubleToLongBits(other.xRightDown))
			return false;
		if (Double.doubleToLongBits(xRightUp) != Double
				.doubleToLongBits(other.xRightUp))
			return false;
		return true;
	}

}
