package imaing.expsys.client.presenter;


import imaing.expsys.client.domain.ShopOwnerProxy;
import imaing.expsys.shared.service.ExpsysRequestFactory;
import imaing.expsys.shared.service.ShopOwnerRequest;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class WelcomePagePresenter implements Presenter {

	private final EventBus eventBus;
	private final Display display;
	private final ExpsysRequestFactory requestFactory;
	
	public interface Display {
		HasClickHandlers getAddButton();
		HasClickHandlers getListButton();
		TextBox getTitleField();
		TextBox getEmailField();
		void listShopOwners(List<ShopOwnerProxy> owners);
		
		Widget asWidget();
	}
	
	public WelcomePagePresenter(ExpsysRequestFactory requestFactory, EventBus eventBus, Display view){
		this.eventBus = eventBus;
		this.display = view;
		this.requestFactory = requestFactory;
		
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
				addOwner();
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
	
	private void addOwner() {
		ShopOwnerRequest request = requestFactory.shopRequest();
		ShopOwnerProxy newShopOwner = request.create(ShopOwnerProxy.class);
		newShopOwner.setEmail(display.getEmailField().getText());
		newShopOwner.setShopName(display.getTitleField().getText());
		
		Request<ShopOwnerProxy> createReq = request.save(newShopOwner);//.using(newShopOwner);
		
//		Product p1 = new Product();
//		p1.setDescription("d1");
//		p1.setShop(ownr);
//		
//		Product p2 = new Product();
//		p1.setDescription("d2");
//		p1.setShop(ownr);
//		
//		Product p3 = new Product();
//		p1.setDescription("d3");
//		p1.setShop(ownr);
//		
//		List<Product> prods = new ArrayList<Product>();
//		prods.add(p1);
//		prods.add(p2);
//		prods.add(p3);
//		ownr.setProducts(prods);
		
		createReq.fire(new Receiver<ShopOwnerProxy>() {
			@Override
			public void onSuccess(ShopOwnerProxy arg0) {
				Window.alert("Saved!");
			}

			@Override
			public void onFailure(ServerFailure error) {
				GWT.log("Failed to add owner");
				System.out.println("##########################");
				System.out.println(error.getStackTraceString());
				System.out.println("##########################");
				Window.alert("Failed to add owner");
			}
		});
	}
	
	private void listOwners() {
		requestFactory.shopRequest().list().fire(new Receiver<List<ShopOwnerProxy>>() {
			public void onSuccess(List<ShopOwnerProxy> owners) {
				display.listShopOwners(owners);
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				GWT.log("Failed to list owners");
				System.out.println("##########################");
				System.out.println(error.getStackTraceString());
				System.out.println("##########################");
				Window.alert("Failed to list owners");
			}
		});
	}
	
}
