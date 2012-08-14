package imaing.expsys.client.presenter;


import imaing.expsys.client.services.ShopOwnerServiceAsync;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class WelcomePagePresenter implements Presenter {

	private final HandlerManager eventBus;
	private final Display display;
	private final ShopOwnerServiceAsync shopOwnerSrv;
	
	
	public interface Display {
		HasClickHandlers getHelloButton();
		TextBox getNameField();
		void sendGreet(String greet);
		Widget asWidget();
	}
	
	public WelcomePagePresenter(ShopOwnerServiceAsync rpcService, HandlerManager eventBus, Display view){
		this.eventBus = eventBus;
		this.display = view;
		this.shopOwnerSrv = rpcService;
		display.getNameField().setFocus(true);
		
	}
	
	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
	}

	private void bind() {
		
		display.getHelloButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doGreet();
			}
		});
		
		display.getNameField().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				display.getNameField().setText("");
			}
		});
		
	}
	
	private void doGreet() {
		//History.newItem("register");
		
		shopOwnerSrv.say(new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				display.sendGreet(result);
			}
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to greet", caught);
			}
		});
	}
	
}
