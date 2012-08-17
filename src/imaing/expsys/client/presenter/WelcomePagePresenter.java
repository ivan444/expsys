package imaing.expsys.client.presenter;


import imaing.expsys.client.domain.ShopOwner;
import imaing.expsys.client.services.ShopOwnerServiceAsync;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class WelcomePagePresenter implements Presenter {

	private final HandlerManager eventBus;
	private final Display display;
	private final ShopOwnerServiceAsync shopOwnerSrv;
	
	public interface Display {
		HasClickHandlers getAddButton();
		HasClickHandlers getListButton();
		TextBox getTitleField();
		TextBox getEmailField();
		void listShopOwners(List<ShopOwner> owners);
		
		Widget asWidget();
	}
	
	public WelcomePagePresenter(ShopOwnerServiceAsync rpcService, HandlerManager eventBus, Display view){
		this.eventBus = eventBus;
		this.display = view;
		this.shopOwnerSrv = rpcService;
		
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
		
		ShopOwner ownr = new ShopOwner();
		ownr.setEmail(display.getEmailField().getText());
		ownr.setShopName(display.getTitleField().getText());
		
		shopOwnerSrv.save(ownr, new AsyncCallback<ShopOwner>() {
			@Override
			public void onSuccess(ShopOwner result) {
				Window.alert("Saved!");
			}
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to add owner", caught);
				Window.alert("Failed to add owner");
			}
		});
	}
	
	private void listOwners() {
		shopOwnerSrv.list(new AsyncCallback<List<ShopOwner>>() {
			@Override
			public void onSuccess(List<ShopOwner> owners) {
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
