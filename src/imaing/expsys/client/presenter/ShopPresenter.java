package imaing.expsys.client.presenter;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyChrCls;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Rule;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopServiceAsync;
import imaing.expsys.client.view.BasicDisplay;
import imaing.expsys.shared.Tuple;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * Presenter for administration of the shop's content.
 */
public class ShopPresenter implements Presenter {

	private final Display display;
	private final ShopServiceAsync shopSrv;
	private final Shop shop;
	
	private List<Characteristic> characteristics;
	private List<Product> products;
	
	public interface Display extends BasicDisplay {
		HasClickHandlers getAddCharacteristic();
		HasClickHandlers getDeleteCharacteristic();
		String getCharacteristicName();
		int getCharacteristicFClsNum() throws ParseException;
		long getSelectedCharacteristicId();
		void listCharacteristics(List<Characteristic> chrs);
		
//		void listFuzzyClasses(List<FuzzyClass> fcls);
		void setFuzzyClassUpdateHandler(FuzzyClassUpdateHandler handl);
		
//		void listFuzzyClassesDefinitions(List<FuzzyChrCls> fcls);
		void setFuzzyClassDefUpdateHandler(FuzzyClassDefUpdateHandler handl);
		
		void listFuzzySetsAndVals(Map<Characteristic, Tuple<List<FuzzyChrCls>, List<FuzzyClass>>> fuzSets);
		
		void setRuleManager(RuleManager man);
		void listRules(List<Rule> rules);
		
		HasClickHandlers getAddProducts();
		String getNewProductsJSON();
		HasClickHandlers getDeleteProduct();
		long getSelectedProductId();
		void listProducts(List<Product> prods);
		
		void reportSuccess(String string);
	}
	
	public interface FuzzyClassDefUpdateHandler {
		void doUpdate(List<FuzzyChrCls> fcls);
	}
	
	public interface FuzzyClassUpdateHandler {
		void doUpdate(List<FuzzyClass> fcls);
	}
	
	public interface RuleManager {
		void doDelete(Rule rule);
		void doSave(Rule rule, RuleUpdater ru);
	}
	
	public interface RuleUpdater {
		void updateRule(Rule r);
	}
	
	public ShopPresenter(ShopServiceAsync rpcService, Display view, Shop shop){
		this.display = view;
		this.shopSrv = rpcService;
		this.shop = shop;
		
		this.characteristics = new ArrayList<Characteristic>();
		this.products = new ArrayList<Product>();
	}
	
	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
		init();
	}
	
	private void init() {
		listCharacteristics();
		listProducts();
		listFuzzyClasses();
		listRules();
		
		display.setFuzzyClassUpdateHandler(new FuzzyClassUpdateHandler() {
			@Override
			public void doUpdate(List<FuzzyClass> fcls) {
				updateFuzzyClasses(fcls);
			}
		});
		
		display.setRuleManager(new RuleManager() {
			@Override
			public void doSave(Rule rule, RuleUpdater ru) {
				saveRule(rule, ru);
			}
			
			@Override
			public void doDelete(Rule rule) {
				deleteRule(rule);
			}
		});
	}

	protected void deleteRule(Rule rule) {
		shopSrv.deleteRule(rule, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				display.reportSuccess("Rule deleted");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				display.reportError("Failed to delete rule");
			}
		});
	}

	protected void saveRule(Rule rule, final RuleUpdater ru) {
		shopSrv.saveRule(rule, new AsyncCallback<Rule>() {
			@Override
			public void onSuccess(Rule result) {
				display.reportSuccess("Rule saved");
				ru.updateRule(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				display.reportError("Failed to save rule");
			}
		});
	}

	private void bind() {
		display.getAddCharacteristic().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addCharacteristic();
			}
		});
		
		display.getDeleteCharacteristic().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				try {
					long selId = display.getSelectedCharacteristicId();
					deleteCharacteristic(selId);
				} catch (IllegalStateException e) {
					return; // TODO report error
				}
			}
		});
		
		display.getAddProducts().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addProducts(display.getNewProductsJSON());
			}
		});
		
		display.getDeleteProduct().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				try {
					long selId = display.getSelectedProductId();
					deleteProduct(selId);
				} catch (IllegalStateException e) {
					return; // TODO report error
				}
			}
		});
		
	}
	
	protected void deleteProduct(final long selId) {
		shopSrv.deleteProduct(selId, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				int toDeleteIdx = -1;
				for (int i = 0; i < products.size(); i++) {
					if (products.get(i).getId().longValue() == selId) {
						toDeleteIdx = i;
						break;
					}
				}
				
				if (toDeleteIdx == -1) {
					Window.alert("Failed to remove product! Couldn't find id!");
				} else {
					products.remove(toDeleteIdx);
					display.listProducts(products);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to delete product", caught);
				Window.alert("Failed to delete product");
			}
		});
	}
	
	protected void updateFuzzyClasses(List<FuzzyClass> fcls) {
		shopSrv.updateFuzzyClasses(fcls, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				display.reportSuccess("Fuzzy classes updated!");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				display.reportError("Failed to update fuzzy classes");
			}
		});
		
	}

	protected void addProducts(String prodsJson) {
//		List<Product> parsedProds = null;
//		try {
//			parsedProds = Product.parseProductsJSON(prodsJson, shop, characteristics);
//		} catch (ParseException e) {
//			display.reportError(e.getMessage());
//			return;
//		}
//		
//		for (Product p : parsedProds) {
//			shopSrv.addProduct(p, new AsyncCallback<Product>() {
//				@Override
//				public void onSuccess(Product result) {
//					products.add(result);
//					display.listProducts(products);
//				}
//				@Override
//				public void onFailure(Throwable caught) {
//					GWT.log("Failed to add product", caught);
//					Window.alert("Failed to add product");
//				}
//			});
//		}
	}

	/**
	 * Delete characteristic from database.
	 * ID of characteristic to be deleted is provided by
	 * display.getSelectedCharacteristicId().
	 * 
	 */
	private void deleteCharacteristic(final long selCharacteristicId) {
		shopSrv.deleteCharacteristic(selCharacteristicId, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				int toDeleteIdx = -1;
				for (int i = 0; i < characteristics.size(); i++) {
					if (characteristics.get(i).getId().longValue() == selCharacteristicId) {
						toDeleteIdx = i;
						break;
					}
				}
				
				if (toDeleteIdx == -1) {
					Window.alert("Failed to remove characteristic! Couldn't find id!");
				} else {
					characteristics.remove(toDeleteIdx);
					display.listCharacteristics(characteristics);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to delete characteristic", caught);
				Window.alert("Failed to delete characteristic");
			}
		});
	}

	/**
	 * Add new shop to database.
	 */
	private void addCharacteristic() {
		Characteristic chr = new Characteristic();
		try {
			chr.setfClsNum(display.getCharacteristicFClsNum());
		} catch (ParseException e) {
			String errMsg = "Failed to parse number of fuzzy classes.";
			GWT.log(errMsg, e);
			Window.alert(errMsg);
			return;
		}
		chr.setShop(shop);
		chr.setName(display.getCharacteristicName());
		
		shopSrv.saveCharacteristic(chr, new AsyncCallback<Characteristic>() {
			@Override
			public void onSuccess(Characteristic result) {
				characteristics.add(result);
				display.listCharacteristics(characteristics);
			}
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to add characteristic", caught);
				Window.alert("Failed to add characteristic");
			}
		});
	}
	

	private void listRules() {
		shopSrv.listRulesForShop(shop, new AsyncCallback<List<Rule>>() {
			@Override
			public void onSuccess(List<Rule> result) {
				display.listRules(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				display.reportError("Failed to load rules");
			}
		});
		
	}
	
	private void listCharacteristics() {
		shopSrv.listCharacteristics(shop, new AsyncCallback<List<Characteristic>>() {
			@Override
			public void onSuccess(List<Characteristic> result) {
				characteristics.clear();
				characteristics.addAll(result);
				display.listCharacteristics(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				String errMsg = "Failed to list characteristics";
				GWT.log(errMsg, caught);
				Window.alert(errMsg);
			}
		});
		
	}
	
	private void listProducts() {
		shopSrv.listProducts(shop, new AsyncCallback<List<Product>>() {
			@Override
			public void onSuccess(List<Product> result) {
				products.clear();
				products.addAll(result);
				display.listProducts(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				String errMsg = "Failed to list products";
				GWT.log(errMsg, caught);
				Window.alert(errMsg);
			}
		});
	}


	private void listFuzzyClasses() {
//		shopSrv.listFuzzySetsAndValsForShop(shop, new AsyncCallback<Map<Characteristic,Tuple<List<FuzzyChrCls>,List<FuzzyClass>>>>() {
//			
//			@Override
//			public void onSuccess(
//					Map<Characteristic, Tuple<List<FuzzyChrCls>, List<FuzzyClass>>> result) {
//				display.listFuzzySetsAndVals(result);
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				
//				
//			}
//		});
		
		
		shopSrv.listFuzzyClassesForShop(shop, new AsyncCallback<List<FuzzyClass>>() {
			@Override
			public void onSuccess(final List<FuzzyClass> fuzzyVals) {
				shopSrv.listFuzzyClassesDefinitions(shop, new AsyncCallback<List<FuzzyChrCls>>() {
					
					@Override
					public void onSuccess(List<FuzzyChrCls> fuzzySets) {
						GWT.log(Arrays.toString(fuzzySets.toArray()));
						GWT.log(Arrays.toString(fuzzyVals.toArray()));
						showFuzzySets(fuzzySets, fuzzyVals);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						String errMsg = "Failed to list fuzzy classes";
						GWT.log(errMsg, caught);
						Window.alert(errMsg);
					}
				});
			}
			
			@Override
			public void onFailure(Throwable caught) {
				String errMsg = "Failed to list fuzzy classes";
				GWT.log(errMsg, caught);
				Window.alert(errMsg);
			}
		});
	}
	
	private void showFuzzySets(List<FuzzyChrCls> fuzzySets, List<FuzzyClass> fuzzyVals) {
		Set<Characteristic> allChrs = new HashSet<Characteristic>();
		
		Map<Characteristic, List<FuzzyChrCls>> mapChrFuzSets = new HashMap<Characteristic, List<FuzzyChrCls>>();
		for (FuzzyChrCls fuzzyChrCls : fuzzySets) {
			Characteristic chr = fuzzyChrCls.getChr();
			allChrs.add(chr);
			List<FuzzyChrCls> fuzSetLst = mapChrFuzSets.get(chr);
			if (fuzSetLst == null) {
				fuzSetLst = new LinkedList<FuzzyChrCls>();
				mapChrFuzSets.put(chr, fuzSetLst);
			}
			fuzSetLst.add(fuzzyChrCls);
		}
		
		Map<Characteristic, List<FuzzyClass>> mapChrFuzVals = new HashMap<Characteristic, List<FuzzyClass>>();
		for (FuzzyClass fuzzyVal : fuzzyVals) {
			Characteristic chr = fuzzyVal.getChr();
			allChrs.add(chr);
			List<FuzzyClass> fuzValLst = mapChrFuzVals.get(chr);
			if (fuzValLst == null) {
				fuzValLst = new LinkedList<FuzzyClass>();
				mapChrFuzVals.put(chr, fuzValLst);
			}
			fuzValLst.add(fuzzyVal);
		}
		
		Map<Characteristic, Tuple<List<FuzzyChrCls>, List<FuzzyClass>>> fuzChrDesc = new HashMap<Characteristic, Tuple<List<FuzzyChrCls>,List<FuzzyClass>>>();
		for (Characteristic chr : allChrs) {
			Tuple<List<FuzzyChrCls>, List<FuzzyClass>> fuzDesc =
					new Tuple<List<FuzzyChrCls>, List<FuzzyClass>>(mapChrFuzSets.get(chr), mapChrFuzVals.get(chr));
			fuzChrDesc.put(chr, fuzDesc);
		}
		
		display.listFuzzySetsAndVals(fuzChrDesc);
	}
	
}
