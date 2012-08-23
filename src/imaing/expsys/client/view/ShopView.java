package imaing.expsys.client.view;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.presenter.ShopPresenter;

import java.text.ParseException;
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
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ShopView extends Composite implements ShopPresenter.Display {

	public interface ShopUiBinder extends UiBinder<Widget, ShopView> {
	}
	
	private static ShopUiBinder uiBinder = GWT.create(ShopUiBinder.class);

	@UiField
	TextBox chrName;
	
	@UiField
	IntegerBox chrFCls;
	
	@UiField
	Button btnAddChr;
	
	@UiField
	Button btnDelChr;
	
	@UiField
	HTMLPanel chrsPanel;
	
	private CellTable<Characteristic> chrsTable;
	
	private Characteristic selectedChr = null;
	
	public ShopView() {
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	private void init() {
		chrsTable = new CellTable<Characteristic>();

	    // Create name column.
	    TextColumn<Characteristic> nameColumn = new TextColumn<Characteristic>() {
	      @Override
	      public String getValue(Characteristic chr) {
	        return chr.getName();
	      }
	    };

	    // Create fcls column.
	    TextColumn<Characteristic> emailColumn = new TextColumn<Characteristic>() {
	      @Override
	      public String getValue(Characteristic chr) {
	        return Integer.toString(chr.getfClsNum());
	      }
	    };

	    // Add the columns.
	    chrsTable.addColumn(nameColumn, "Name");
	    chrsTable.addColumn(emailColumn, "Number of fuzzy classes");
	    
	    chrsTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<Characteristic> selectionModel = new SingleSelectionModel<Characteristic>();
	    chrsTable.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	    	  Characteristic selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	        	selectedChr = selected;
	        }
	      }
	    });
	    
	    this.chrsPanel.add(chrsTable);
	}
	
	@Override
	public void listCharacteristics(List<Characteristic> chrs) {
		chrsTable.setRowCount(chrs.size(), true);
		chrsTable.setRowData(0, chrs);
		chrsTable.redraw();
		
		selectedChr = null;
	}
	

	@Override
	public HasClickHandlers getAddCharacteristic() {
		return btnAddChr;
	}

	@Override
	public HasClickHandlers getDeleteCharacteristic() {
		return btnDelChr;
	}

	@Override
	public String getCharacteristicName() {
		return chrName.getText();
	}

	@Override
	public long getSelectedCharacteristicId() {
		if (selectedChr == null) {
			String errMsg = "No characteristic is selected!";
			Window.alert(errMsg);
			throw new IllegalStateException(errMsg);
		}
		
		return selectedChr.getId().longValue();
	}

	@Override
	public int getCharacteristicFClsNum() throws ParseException {
		return this.chrFCls.getValueOrThrow();
	}

}
