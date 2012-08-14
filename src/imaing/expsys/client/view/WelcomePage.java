package imaing.expsys.client.view;


import imaing.expsys.client.presenter.WelcomePagePresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class WelcomePage extends Composite implements WelcomePagePresenter.Display {

	public interface WelcomePageUiBinder extends UiBinder<Widget, WelcomePage> {
	}
	
	private static WelcomePageUiBinder uiBinder = GWT.create(WelcomePageUiBinder.class);

	@UiField
	TextBox name;
	
	@UiField
	Button btnHello;
	
	@UiField
	Label greet;
	
	public WelcomePage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasClickHandlers getHelloButton() {
		return btnHello;
	}

	@Override
	public TextBox getNameField() {
		return name;
	}

	@Override
	public void sendGreet(String greet) {
		this.greet.setText(greet);
	}

}
