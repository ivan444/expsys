package imaing.expsys.client.view.widgets;

import imaing.expsys.client.domain.Shop;

import com.google.gwt.user.client.ui.Label;

/**
 * Label for displaying basic informations about the provided shop.
 */
public class ShopLblWidget extends Label {
	private Shop shop;

	public Long getShopId() {
		return shop.getId();
	}

	public ShopLblWidget(Shop shop) {
		super(shop.getShopName() + " " + shop.getEmail());
		this.shop = shop;
	}
	
}
