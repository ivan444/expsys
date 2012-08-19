package imaing.expsys.client;

import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.presenter.Presenter;
import imaing.expsys.client.presenter.WelcomePagePresenter;
import imaing.expsys.client.services.ShopServiceAsync;
import imaing.expsys.client.view.WelcomePage;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.event.shared.EventBus;

public class AppController implements Presenter, ValueChangeHandler<String> {
	
	public final EventBus eventBus;
	private final ShopServiceAsync shopOwnerRpcSrv;
	private HasWidgets container;
//	private Shop activeUser;
	
	public AppController(ShopServiceAsync shopOwnerRpcSrv, EventBus eventBus) {
		this.eventBus = eventBus;
		this.shopOwnerRpcSrv = shopOwnerRpcSrv;
		
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

		if (token.equals("register")) {
//			presenter = new RegistrationPresenter(shopOwnerRpcSrv, eventBus, new RegistrationView());
		} else if (token.equals("welcome")) {
			presenter = new WelcomePagePresenter(shopOwnerRpcSrv, eventBus, new WelcomePage());
		}	

		if (presenter != null) {
//			activatePresenter(presenter);
			presenter.go(container);
		}
	}


	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		onHistoryChange(token);
	}
	
//	/**
//	 * Method for activating the presenter. The active presenter should only be
//	 * set through this method to ensure consistency of the object.
//	 * 
//	 * @param presenter The new active presenter
//	 */
//	private void activatePresenter(Presenter presenter) {
//		presenter.go(container);
//	}

	void disableLogoutLink(){
		DOM.getElementById("upperBar").setInnerHTML("<br/>");
	}
	void enableLogoutLink(){
		DOM.getElementById("upperBar").setInnerHTML("<a href=\"#logout\">Logout</a>");
	}
	

}
