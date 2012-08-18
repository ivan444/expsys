package imaing.expsys.client;

import imaing.expsys.client.service.ShopOwnerService;
import imaing.expsys.client.service.ShopOwnerServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

public class Expsys implements EntryPoint {

	@Override
	public void onModuleLoad() {
		ShopOwnerServiceAsync shopOwnerService = GWT.create(ShopOwnerService.class);
		
	    HandlerManager eventBus = new HandlerManager(null);
	    AppController appController = new AppController(shopOwnerService, eventBus);
	    appController.go(RootPanel.get("content_SLOT"));
	  }
}
