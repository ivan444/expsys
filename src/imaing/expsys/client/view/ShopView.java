package imaing.expsys.client.view;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.FuzzySet;
import imaing.expsys.client.domain.JsCharacteristicFcls;
import imaing.expsys.client.domain.JsCharacteristicValue;
import imaing.expsys.client.domain.ProdChr;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Rule;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.presenter.ShopPresenter;
import imaing.expsys.client.presenter.ShopPresenter.FuzzyClassUpdateHandler;
import imaing.expsys.client.presenter.ShopPresenter.RuleManager;
import imaing.expsys.client.presenter.ShopPresenter.RuleUpdater;
import imaing.expsys.client.view.widgets.FuzzyClassesWidget;
import imaing.expsys.client.view.widgets.FuzzyClassesWidget.SaveHandler;
import imaing.expsys.client.view.widgets.RuleWidget;
import imaing.expsys.client.view.widgets.RuleWidget.WidgetRuleManager;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
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
	
	@UiField
	Button btnAddProds;
	
	@UiField
	Button btnDelProd;
	
	@UiField
	TextArea newProdsJson;
	
	@UiField
	HTMLPanel prodsPanel;
	
	@UiField
	VerticalPanel fclsPane;
	
	@UiField
	VerticalPanel rulesPane;
	
	@UiField
	Button btnNewRule;
	
	private CellTable<Characteristic> chrsTable;
	private CellTable<Product> prodsTable;
	
	private Characteristic selectedChr = null;
	private Product selectedProd = null;
	private FuzzyClassUpdateHandler fclsUpdateHandl;
	private RuleManager ruleManager;
	private final Shop shop;
	private List<Characteristic> chrs;
	
	public ShopView(Shop shop) {
		initWidget(uiBinder.createAndBindUi(this));
		this.shop = shop;
		init();
	}

	private void init() {
		initCharacteristics();
		initProducts();
	}
	
	// TODO remove this!
	private void showFuzzyClasses() {
//		Element fclsContainer = DOM.getElementById("fcls");
//		Element div = DOM.createDiv();
//		p.insertFirst(div);
//		fclsDiv.appendChild(div);
		
//		DOM.appendChild(fclsContainer, div);
		
		JsArray<FuzzySet> fSets = JavaScriptObject.createArray().cast();
		
		JsArrayNumber fs1xData = JavaScriptObject.createArray().cast();
		fs1xData.push(50.0);
		fs1xData.push(100.0);
		fs1xData.push(200.0);
		fs1xData.push(300.0);
		FuzzySet fs1 = FuzzySet.instance(fs1xData, 0);
		fSets.push(fs1);
		
		JsArrayNumber fs2xData = JavaScriptObject.createArray().cast();
		fs2xData.push(250.0);
		fs2xData.push(350.0);
		fs2xData.push(350.0);
		fs2xData.push(650.0);
		FuzzySet fs2 = FuzzySet.instance(fs2xData, 1);
		fSets.push(fs2);
		
		
		JsArray<JsCharacteristicValue> chrVals = JavaScriptObject.createArray().cast();
		chrVals.push(JsCharacteristicValue.instance("prva", 10));
		chrVals.push(JsCharacteristicValue.instance("druga", 30));
		chrVals.push(JsCharacteristicValue.instance("treća", 100));
		chrVals.push(JsCharacteristicValue.instance("četvrta neka", 300));
		
		JsCharacteristicFcls chr = JsCharacteristicFcls.instance("test", chrVals, fSets);

		callJsShowFuzzyClasses("fcls", chr);
		callJsShowFuzzyClasses("fcls2", chr);
	}
	
	// call d3 with dom element & data
	private native void callJsShowFuzzyClasses(String div, JsCharacteristicFcls chr)/*-{
		// TODO: @Test
		// TODO: Remove!! fsets should be binded! This is for test ONLY!
		var cloned = JSON.parse(JSON.stringify(chr));
		$wnd.showFuzzyClasses(div, cloned);
	}-*/;
	
	private void initCharacteristics() {
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
	
	private void initProducts() {
		prodsTable = new CellTable<Product>();

		TextColumn<Product> descColumn = new TextColumn<Product>() {
			@Override
			public String getValue(Product p) {
				return p.getDescription();
			}
		};

		TextColumn<Product> integColumn = new TextColumn<Product>() {
			@Override
			public String getValue(Product p) {
				return p.getIntegrationId();
			}
		};

		TextColumn<Product> charsColumn = new TextColumn<Product>() {
			@Override
			public String getValue(Product p) {
				StringBuilder sb = new StringBuilder();
				for (ProdChr pc : p.getCharacteristics()) {
					sb.append(pc.getChr().getName())
					  .append(": ")
					  .append(pc.getValue())
					  .append(", ");
				}
				return sb.toString();
			}
		};

	    prodsTable.addColumn(descColumn, "Description");
	    prodsTable.addColumn(integColumn, "Integration ID");
	    prodsTable.addColumn(charsColumn, "Characteristics");
	    
	    prodsTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<Product> selectionModel = new SingleSelectionModel<Product>();
	    prodsTable.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		Product selected = selectionModel.getSelectedObject();
	    		if (selected != null) {
	    			selectedProd = selected;
	    		}
	    	}
	    });
	    
	    this.prodsPanel.add(prodsTable);
	}
	
	@Override
	public void listCharacteristics(List<Characteristic> chrs) {
		this.chrs = chrs;
		
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

	@Override
	public void reportError(String errorMsg) {
		Window.alert(errorMsg);
	}

	@Override
	public HasClickHandlers getAddProducts() {
		return btnAddProds;
	}

	@Override
	public String getNewProductsJSON() {
		return newProdsJson.getText();
	}

	@Override
	public HasClickHandlers getDeleteProduct() {
		return btnDelProd;
	}

	@Override
	public long getSelectedProductId() {
		if (selectedProd == null) {
			String errMsg = "No product is selected!";
			Window.alert(errMsg);
			throw new IllegalStateException(errMsg);
		}
		
		return selectedProd.getId().longValue();
	}

	@Override
	public void listProducts(List<Product> prods) {
		prodsTable.setRowCount(prods.size(), true);
		prodsTable.setRowData(0, prods);
		prodsTable.redraw();
		
		selectedProd = null;
	}
	
	@Override
	public void listFuzzyClasses(List<FuzzyClass> fcls) {
		Map<Characteristic, List<FuzzyClass>> fclsByChr = new HashMap<Characteristic, List<FuzzyClass>>();
		
		for (FuzzyClass fc : fcls) {
			if (!fclsByChr.containsKey(fc.getChr())) {
				fclsByChr.put(fc.getChr(), new LinkedList<FuzzyClass>());
			}
			
			fclsByChr.get(fc.getChr()).add(fc);
		}
		
		for (Characteristic c : fclsByChr.keySet()) {
			FuzzyClassesWidget fcWdgt = new FuzzyClassesWidget(c, fclsByChr.get(c));
			fcWdgt.setSaveHandl(new SaveHandler() {
				@Override
				public void doSave(List<FuzzyClass> fcls) {
					fclsUpdateHandl.doUpdate(fcls);
				}
			});
			this.fclsPane.add(fcWdgt);
		}
		
	}

	@Override
	public void setFuzzyClassUpdateHandler(FuzzyClassUpdateHandler handl) {
		this.fclsUpdateHandl = handl;
	}

	@Override
	public void reportSuccess(String string) {
	}

	@Override
	public void setRuleManager(RuleManager man) {
		this.ruleManager = man;
	}

	@Override
	public void listRules(List<Rule> rules) {
		for (Rule r : rules) {
			final RuleWidget rWdgt = new RuleWidget(r);
			rWdgt.setwRuleMan(new WidgetRuleManager() {
				@Override
				public void saveRule(Rule r) {
					ruleManager.doSave(r, new RuleUpdater() {
						@Override
						public void updateRule(Rule r) {
							rWdgt.updateRuleId(r.getId());
						}
					});
				}
				
				@Override
				public void deleteRule(Rule r) {
					ruleManager.doDelete(r);
				}
			});
			
			this.rulesPane.add(rWdgt);
		}
	}
	
	@UiHandler("btnNewRule")
	void handleNewRuleClick(ClickEvent e) {
		final RuleWidget freshRWdgt = new RuleWidget(shop, chrs);
		freshRWdgt.setwRuleMan(new WidgetRuleManager() {
			
			@Override
			public void saveRule(Rule r) {
				if (ruleManager != null) {
					ruleManager.doSave(r, new RuleUpdater() {
						@Override
						public void updateRule(Rule r) {
							freshRWdgt.updateRuleId(r.getId());
						}
					});
				} else {
					GWT.log("ruleManager is not set!");
				}
			}
			
			@Override
			public void deleteRule(Rule r) {
				if (ruleManager != null) {
					ruleManager.doDelete(r);
				} else {
					GWT.log("ruleManager is not set!");
				}
			}
		});
		
		this.rulesPane.add(freshRWdgt);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		showFuzzyClasses();
	}

}
