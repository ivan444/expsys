package imaing.expsys.client.presenter;


import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopServiceAsync;
import imaing.expsys.client.view.BasicDisplay;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Presenter for administration of the shop's content.
 */
public class ShopPresenter implements Presenter {

	private final EventBus eventBus;
	private final Display display;
	private final ShopServiceAsync shopSrv;
	private final Shop shop;
	
	private List<Characteristic> characteristics;
	private List<Product> products;
	private List<FuzzyClass> fuzzClasses;
	
	public interface Display extends BasicDisplay {
		HasClickHandlers getAddCharacteristic();
		HasClickHandlers getDeleteCharacteristic();
		String getCharacteristicName();
		int getCharacteristicFClsNum() throws ParseException;
		long getSelectedCharacteristicId();
		void listCharacteristics(List<Characteristic> chrs);
		
		void listFuzzyClasses(List<FuzzyClass> fcls);
		
		HasClickHandlers getAddProducts();
		String getNewProductsJSON();
		HasClickHandlers getDeleteProduct();
		long getSelectedProductId();
		void listProducts(List<Product> prods);
	}
	
	public ShopPresenter(ShopServiceAsync rpcService, EventBus eventBus, Display view, Shop shop){
		this.eventBus = eventBus;
		this.display = view;
		this.shopSrv = rpcService;
		this.shop = shop;
		
		this.characteristics = new ArrayList<Characteristic>();
		this.products = new ArrayList<Product>();
		this.fuzzClasses = new ArrayList<FuzzyClass>();
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

	protected void addProducts(String prodsJson) {
		List<Product> parsedProds = null;
		try {
			parsedProds = Product.parseProductsJSON(prodsJson, shop, characteristics);
		} catch (ParseException e) {
			display.reportError(e.getMessage());
			return;
		}
		
		for (Product p : parsedProds) {
			shopSrv.addProduct(p, new AsyncCallback<Product>() {
				@Override
				public void onSuccess(Product result) {
					products.add(result);
					display.listProducts(products);
				}
				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Failed to add product", caught);
					Window.alert("Failed to add product");
				}
			});
		}
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
		shopSrv.listFuzzyClassesForShop(shop, new AsyncCallback<List<FuzzyClass>>() {
			@Override
			public void onSuccess(List<FuzzyClass> result) {
				fuzzClasses.clear();
				fuzzClasses.addAll(result);
				display.listFuzzyClasses(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				String errMsg = "Failed to list fuzzy classes";
				GWT.log(errMsg, caught);
				Window.alert(errMsg);
			}
		});
	}
	
}
