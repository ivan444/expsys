package imaing.expsys.client;

import imaing.expsys.shared.service.ExpsysRequestFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class Expsys implements EntryPoint {

	@Override
	public void onModuleLoad() {
		final EventBus eventBus = new SimpleEventBus();

		ExpsysRequestFactory requestFactory = GWT.create(ExpsysRequestFactory.class);
		requestFactory.initialize(eventBus);
		
	    AppController appController = new AppController(requestFactory, eventBus);
	    appController.go(RootPanel.get("content_SLOT"));
	  }
}
