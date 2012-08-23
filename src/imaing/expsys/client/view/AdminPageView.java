package imaing.expsys.client.view;


import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.presenter.AdminPagePresenter;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class AdminPageView extends Composite implements AdminPagePresenter.Display {

	public interface AdminPageUiBinder extends UiBinder<Widget, AdminPageView> {
	}
	
	private static AdminPageUiBinder uiBinder = GWT.create(AdminPageUiBinder.class);

	@UiField
	TextBox shopName;
	
	@UiField
	TextBox shopEmail;
	
	@UiField
	PasswordTextBox shopPassword;
	
	@UiField
	Button btnAddShop;
	
	@UiField
	Button btnDelShop;
	
	@UiField
	HTMLPanel shopsPanel;
	
	private CellTable<Shop> shopsTable;
	
	private Shop selectedShop = null;
	
	public AdminPageView() {
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	@Override
	public void listShops(List<Shop> shops) {
	    // Set the total row count. This isn't strictly necessary, but it affects
	    // paging calculations, so its good habit to keep the row count up to date.
		shopsTable.setRowCount(shops.size(), true);

	    // Push the data into the widget.
		shopsTable.setRowData(0, shops);
		
		shopsTable.redraw();
		
		selectedShop = null;
	}
	
	private void init() {
		shopsTable = new CellTable<Shop>();

	    // Create name column.
	    TextColumn<Shop> nameColumn = new TextColumn<Shop>() {
	      @Override
	      public String getValue(Shop shop) {
	        return shop.getShopName();
	      }
	    };

	    // Create email column.
	    TextColumn<Shop> emailColumn = new TextColumn<Shop>() {
	      @Override
	      public String getValue(Shop shop) {
	        return shop.getEmail();
	      }
	    };

	    // Add the columns.
	    shopsTable.addColumn(nameColumn, "Name");
	    shopsTable.addColumn(emailColumn, "E-mail");
	    
	    shopsTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<Shop> selectionModel = new SingleSelectionModel<Shop>();
	    shopsTable.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        Shop selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	        	selectedShop = selected;
	        }
	      }
	    });
	    
	    this.shopsPanel.add(shopsTable);
	}

	@Override
	public HasClickHandlers getAddShop() {
		return btnAddShop;
	}

	@Override
	public HasClickHandlers getDeleteShop() {
		return btnDelShop;
	}

	@Override
	public String getShopName() {
		return shopName.getText();
	}

	@Override
	public String getShopEmail() {
		return shopEmail.getText();
	}

	@Override
	public String getShopPassword() {
		return shopPassword.getText();
	}

	@Override
	public long getSelectedShopId() {
		if (selectedShop == null) {
			Window.alert("No shop is selected!");
			return -1;
		}
		
		return selectedShop.getId().longValue();
	}

}
