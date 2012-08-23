package imaing.expsys.client.presenter;


import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopServiceAsync;

import java.text.ParseException;
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
 * Presenter for administration of the shop's content.
 */
public class ShopPresenter implements Presenter {

	private final EventBus eventBus;
	private final Display display;
	private final ShopServiceAsync shopSrv;
	private final Shop shop;
	
	private List<Characteristic> characteristics;
	
	public interface Display {
		HasClickHandlers getAddCharacteristic();
		HasClickHandlers getDeleteCharacteristic();
		String getCharacteristicName();
		int getCharacteristicFClsNum() throws ParseException;
		long getSelectedCharacteristicId();
		void listCharacteristics(List<Characteristic> chrs);
		
//		void reportError(String errorMsg);
		Widget asWidget();
	}
	
	public ShopPresenter(ShopServiceAsync rpcService, EventBus eventBus, Display view, Shop shop){
		this.eventBus = eventBus;
		this.display = view;
		this.shopSrv = rpcService;
		this.shop = shop;
		
		this.characteristics = new ArrayList<Characteristic>();
	}
	
	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
		init();
	}
	
	private void init() {
		listCharacteristics();
	}
	
	private void bind() {
		display.getAddCharacteristic().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addCharacteristic();
			}
		});
		
		display.getDeleteCharacteristic().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteCharacteristic();
			}
		});
		
	}
	
	/**
	 * Delete shop from database.
	 * ID of shop to be deleted is provided by
	 * display.getSelectedShopId().
	 * 
	 */
	private void deleteCharacteristic() {
		long selId;
		try {
			selId = display.getSelectedCharacteristicId();
		} catch (IllegalStateException e) {
			return;
		}
		
		final long selCharacteristicId = selId;
		
		shopSrv.deleteCharacteristic(selCharacteristicId, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				int toDeleteIdx = -1;
				for (int i = 0; i < characteristics.size(); i++) {
					if (characteristics.get(i).getId().longValue() == selCharacteristicId) {
						toDeleteIdx = i;
						break;
					}
				}
				
				if (toDeleteIdx == -1) {
					Window.alert("Failed to remove characteristic! Couldn't find id!");
				} else {
					characteristics.remove(toDeleteIdx);
					display.listCharacteristics(characteristics);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to delete characteristic", caught);
				Window.alert("Failed to delete characteristic");
			}
		});
	}

	/**
	 * Add new shop to database.
	 */
	private void addCharacteristic() {
		Characteristic chr = new Characteristic();
		try {
			chr.setfClsNum(display.getCharacteristicFClsNum());
		} catch (ParseException e) {
			String errMsg = "Failed to parse number of fuzzy classes.";
			GWT.log(errMsg, e);
			Window.alert(errMsg);
			return;
		}
		chr.setShop(shop);
		chr.setName(display.getCharacteristicName());
		
		shopSrv.saveCharacteristic(chr, new AsyncCallback<Characteristic>() {
			@Override
			public void onSuccess(Characteristic result) {
				characteristics.add(result);
				display.listCharacteristics(characteristics);
			}
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to add shop", caught);
				Window.alert("Failed to add shop");
			}
		});
	}
	
	private void listCharacteristics() {
//		Window.alert(shop == null ? "null" : shop.getId().toString());
		shopSrv.listCharacteristics(shop, new AsyncCallback<List<Characteristic>>() {
			@Override
			public void onSuccess(List<Characteristic> result) {
				characteristics.addAll(result);
				display.listCharacteristics(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				String errMsg = "Failed to list characteristics";
				GWT.log(errMsg, caught);
				Window.alert(errMsg);
			}
		});
		
	}
	
	
	
	
}