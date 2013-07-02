package imaing.expsys.server.model;

import imaing.expsys.client.domain.FuzzyChrCls;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="fuzzychrcls",uniqueConstraints=@UniqueConstraint(
		columnNames={"chr_id", "xleftup", "xleftdown", "xrightup", "xrightdown"}
))
@NamedQueries({
    @NamedQuery(name="FuzzyChrClsEnt.listForCharacteristic",query="select e from FuzzyChrClsEnt as e where e.chr=:chr"),
    @NamedQuery(name="FuzzyChrClsEnt.listForShop",query="select e from FuzzyChrClsEnt as e where e.chr.shop=:shop")
})
public class FuzzyChrClsEnt extends BaseEntity<FuzzyChrCls> {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
	@JoinColumn(name="chr_id", nullable=false, updatable=false)
	private CharacteristicEnt chr;
	
	@Column(name="xleftup")
	private double xLeftUp;
	
	@Column(name="xleftdown")
	private double xLeftDown;
	
	@Column(name="xrightup")
	private double xRightUp;
	
	@Column(name="xrightdown")
	private double xRightDown;
	
	public FuzzyChrClsEnt() {
	}
	
	public FuzzyChrClsEnt(FuzzyChrCls c) {
		super(c);
	}
	
	@Transient
	@Override
	public FuzzyChrCls getCleaned() {
		FuzzyChrCls g = new FuzzyChrCls();
		g.setId(getId());
		g.setChr(getChr().getCleaned());
		g.setxLeftDown(getxLeftDown());
		g.setxLeftUp(getxLeftUp());
		g.setxRightDown(getxRightDown());
		g.setxRightUp(getxRightUp());
		return g;
	}

	@Transient
	@Override
	public void fill(FuzzyChrCls g) {
		setId(g.getId());
		setChr(new CharacteristicEnt(g.getChr()));
		setxLeftDown(g.getxLeftDown());
		setxLeftUp(g.getxLeftUp());
		setxRightDown(g.getxRightDown());
		setxRightUp(g.getxRightUp());
	}

	public CharacteristicEnt getChr() {
		return chr;
	}

	public void setChr(CharacteristicEnt chr) {
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
		FuzzyChrClsEnt other = (FuzzyChrClsEnt) obj;
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
