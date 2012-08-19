package imaing.expsys.client.presenter;


import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopServiceAsync;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

public class WelcomePagePresenter implements Presenter {

	private final EventBus eventBus;
	private final Display display;
	private final ShopServiceAsync shopSrv;
	
	public interface Display {
		HasClickHandlers getAddButton();
		HasClickHandlers getListButton();
		TextBox getTitleField();
		TextBox getEmailField();
		void listShopOwners(List<Shop> owners);
		
		Widget asWidget();
	}
	
	public WelcomePagePresenter(ShopServiceAsync rpcService, EventBus eventBus, Display view){
		this.eventBus = eventBus;
		this.display = view;
		this.shopSrv = rpcService;
		
	}
	
	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
	}

	private void bind() {
		display.getAddButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addShop();
			}
		});
		
		display.getListButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				listOwners();
			}
		});
		
		display.getTitleField().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.getTitleField().setText("");
			}
		});
		
		display.getEmailField().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.getEmailField().setText("");
			}
		});
		
	}
	
	private void addShop() {
		
		Shop shp = new Shop();
		shp.setEmail(display.getEmailField().getText());
		shp.setShopName(display.getTitleField().getText());
		
		shopSrv.save(shp, new AsyncCallback<Shop>() {
			@Override
			public void onSuccess(Shop result) {
				addProducts(result);
				Window.alert("Saved!");
			}
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to add owner", caught);
				Window.alert("Failed to add owner");
			}
		});
	}
	
	protected void addProducts(Shop shp) {
		Product p1 = new Product();
		p1.setShop(shp);
		p1.setDescription("d1");
		p1.setIntegrationId("i1");
		
		Product p2 = new Product();
		p2.setShop(shp);
		p2.setDescription("d2");
		p2.setIntegrationId("i2");
		
		Product p3 = new Product();
		p3.setShop(shp);
		p3.setDescription("d3");
		p3.setIntegrationId("i3");
		
		AsyncCallback<Void> addPCallb = new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				GWT.log("Product added");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to add product", caught);
				Window.alert("Failed to add product");
			}
		};
		
		shopSrv.addProduct(p1, addPCallb);
		shopSrv.addProduct(p2, addPCallb);
		shopSrv.addProduct(p3, addPCallb);
	}

	private void listOwners() {
		shopSrv.list(new AsyncCallback<List<Shop>>() {
			@Override
			public void onSuccess(List<Shop> owners) {
				display.listShopOwners(owners);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to list owners", caught);
				Window.alert("Failed to list owners");
			}
		});
	}
	
}
