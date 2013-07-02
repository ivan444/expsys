package imaing.expsys.client;

import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.presenter.AdminPagePresenter;
import imaing.expsys.client.presenter.Presenter;
import imaing.expsys.client.presenter.ShopPresenter;
import imaing.expsys.client.services.ShopServiceAsync;
import imaing.expsys.client.view.AdminPageView;
import imaing.expsys.client.view.ShopView;

import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.event.shared.EventBus;

public class AppController implements Presenter, ValueChangeHandler<String> {
	private final String defaultToken = "shop";
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
		
		// Tokens defaultToken and "" have the same function.
		if (token == null || "".equals(token)) {
			History.newItem(defaultToken);
		} else {
			History.fireCurrentHistoryState();
		}
	}

	private void bind() {
		History.addValueChangeHandler(this);
	}

//	private void doLogout() {
//		this.activeUser = null;
//		History.newItem(defaultToken);
//	}

	public void onHistoryChange(String token){
		
		if (token == null) token = defaultToken;
		if ("".equals(token)) return; // Dummy token!

		Presenter presenter = null;

		if (token.equals("admin")) {
			presenter = new AdminPagePresenter(shopRpcSrv, new AdminPageView());
			
		} else if (token.equals("shop")) {
			shopRpcSrv.listShops(new AsyncCallback<List<Shop>>() {
				@Override
				public void onSuccess(List<Shop> result) {
					Shop shop = result.get(0);
					Presenter p = new ShopPresenter(shopRpcSrv, new ShopView(shop), shop);
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
