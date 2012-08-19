package imaing.expsys.client;

import imaing.expsys.client.services.ShopService;
import imaing.expsys.client.services.ShopServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class Expsys implements EntryPoint {

	@Override
	public void onModuleLoad() {
		ShopServiceAsync shopOwnerService = GWT.create(ShopService.class);
		
	    EventBus eventBus = new SimpleEventBus();
	    AppController appController = new AppController(shopOwnerService, eventBus);
	    appController.go(RootPanel.get("content_SLOT"));
	  }
}
