package imaing.expsys.client.presenter;


import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopServiceAsync;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Presenter for administration tasks.
 */
public class AdminPagePresenter implements Presenter {

	private final EventBus eventBus;
	private final Display display;
	private final ShopServiceAsync shopSrv;
	private List<Shop> shops;
	
	public interface Display {
		HasClickHandlers getAddShop();
		HasClickHandlers getDeleteShop();
		String getShopName();
		String getShopEmail();
		String getShopPassword();
		long getSelectedShopId();
		void listShops(List<Shop> shops);
		
		Widget asWidget();
	}
	
	public AdminPagePresenter(ShopServiceAsync rpcService, EventBus eventBus, Display view){
		this.eventBus = eventBus;
		this.display = view;
		this.shopSrv = rpcService;
		
		this.shops = new ArrayList<Shop>();
	}
	
	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
		init();
	}
	
	private void init() {
		listShops();
	}

	private void bind() {
		display.getAddShop().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addShop();
			}
		});
		
		display.getDeleteShop().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteShop();
			}
		});
		
	}
	
	/**
	 * Delete shop from database.
	 * ID of shop to be deleted is provided by
	 * display.getSelectedShopId().
	 * 
	 */
	private void deleteShop() {
		final long selShopId = display.getSelectedShopId();
		if (selShopId == -1) return;
		
		shopSrv.deleteShop(selShopId, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				int toDeleteIdx = -1;
				for (int i = 0; i < shops.size(); i++) {
					if (shops.get(i).getId().longValue() == selShopId) {
						toDeleteIdx = i;
						break;
					}
				}
				
				if (toDeleteIdx == -1) {
					Window.alert("Failed to remove shop! Couldn't find id!");
				} else {
					shops.remove(toDeleteIdx);
					display.listShops(shops);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to delete shop", caught);
				Window.alert("Failed to delete shop");
			}
		});
	}

	/**
	 * Add new shop to database.
	 */
	private void addShop() {
		Shop shop = new Shop();
		shop.setEmail(display.getShopEmail());
		shop.setShopName(display.getShopName());
		shop.setPassword(display.getShopPassword());
		
		shopSrv.save(shop, new AsyncCallback<Shop>() {
			@Override
			public void onSuccess(Shop result) {
				shops.add(result);
				display.listShops(shops);
			}
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to add shop", caught);
				Window.alert("Failed to add shop");
			}
		});
	}

	/**
	 * Load full list of shops and show them on display.
	 */
	private void listShops() {
		shopSrv.list(new AsyncCallback<List<Shop>>() {
			@Override
			public void onSuccess(List<Shop> allShops) {
				shops.addAll(allShops);
				display.listShops(allShops);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to list shops", caught);
				Window.alert("Failed to list shops");
			}
		});
	}
	
}
