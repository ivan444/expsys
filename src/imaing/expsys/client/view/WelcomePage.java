package imaing.expsys.client.view;


import java.util.List;

import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.presenter.WelcomePagePresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class WelcomePage extends Composite implements WelcomePagePresenter.Display {

	public interface WelcomePageUiBinder extends UiBinder<Widget, WelcomePage> {
	}
	
	private static WelcomePageUiBinder uiBinder = GWT.create(WelcomePageUiBinder.class);

	@UiField
	TextBox title;
	
	@UiField
	TextBox email;
	
	@UiField
	Button btnAdd;
	
	@UiField
	Button btnList;
	
	@UiField
	VerticalPanel ownersPane;
	
	public WelcomePage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasClickHandlers getAddButton() {
		return btnAdd;
	}

	@Override
	public HasClickHandlers getListButton() {
		return btnList;
	}

	@Override
	public TextBox getTitleField() {
		return title;
	}

	@Override
	public TextBox getEmailField() {
		return email;
	}

	@Override
	public void listShopOwners(List<Shop> owners) {
		ownersPane.clear();
		
		for (Shop ownr : owners) {
			ownersPane.add(new Label(ownr.getShopName() + " " + ownr.getEmail()));
		}
	}


}
