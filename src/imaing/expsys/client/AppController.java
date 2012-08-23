package imaing.expsys.client;

import java.util.List;

import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.presenter.AdminPagePresenter;
import imaing.expsys.client.presenter.Presenter;
import imaing.expsys.client.presenter.ShopPresenter;
import imaing.expsys.client.presenter.WelcomePagePresenter;
import imaing.expsys.client.services.ShopServiceAsync;
import imaing.expsys.client.view.AdminPageView;
import imaing.expsys.client.view.ShopView;
import imaing.expsys.client.view.WelcomePage;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.event.shared.EventBus;

public class AppController implements Presenter, ValueChangeHandler<String> {
	
	public final EventBus eventBus;
	private final ShopServiceAsync shopRpcSrv;
	private HasWidgets container;
	
	public AppController(ShopServiceAsync shopRpcSrv, EventBus eventBus) {
		this.eventBus = eventBus;
		this.shopRpcSrv = shopRpcSrv;
		
		bind();
	}

	@Override
	public void go(HasWidgets container) {
		this.container = container;
		String token = History.getToken();
		
		// Tokens "welcome" and "" have the same function.
		if (token == null || "".equals(token)) {
			History.newItem("welcome");
		} else {
			History.fireCurrentHistoryState();
		}
	}

	private void bind() {
		History.addValueChangeHandler(this);

//		eventBus.addHandler(RequestSportEvent.TYPE, new RequestSportEventHandler() {
//			
//			@Override
//			public void onRequestSport(RequestSportEvent event) {
//				doSendNewSportRequest(event.getSport(), event.getSender());
//			}
//		});
	}

//	private void doLogout() {
//		this.activeUser = null;
//		History.newItem("welcome");
//	}

	private void doEditProfile(Shop customer) {
		History.newItem("myprofile");
	}

	public void onHistoryChange(String token){
		
		if (token == null) token = "welcome";
		if ("".equals(token)) return; // Dummy token!

		Presenter presenter = null;

		if (token.equals("admin")) {
			presenter = new AdminPagePresenter(shopRpcSrv, eventBus, new AdminPageView());
		} else if (token.equals("welcome")) {
			presenter = new WelcomePagePresenter(shopRpcSrv, eventBus, new WelcomePage());
		} else if (token.equals("shop")) {
			//@TEST!!!! TODO: REMOVE
			shopRpcSrv.listShops(new AsyncCallback<List<Shop>>() {
				@Override
				public void onSuccess(List<Shop> result) {
					Presenter p = new ShopPresenter(shopRpcSrv, eventBus, new ShopView(), result.get(0)); // TODO remove test shop!!
					p.go(container);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
		}	

		if (presenter != null) {
			presenter.go(container);
		}
	}


	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		onHistoryChange(token);
	}
	
	void disableLogoutLink(){
		DOM.getElementById("upperBar").setInnerHTML("<br/>");
	}
	void enableLogoutLink(){
		DOM.getElementById("upperBar").setInnerHTML("<a href=\"#logout\">Logout</a>");
	}
	

}
