package imaing.expsys.client.view.widgets;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyChrCls;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.JsCharacteristicFcls;
import imaing.expsys.client.domain.JsCharacteristicValue;
import imaing.expsys.client.domain.JsFuzzySet;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class FuzzySetWidget extends Composite {
	
	private static FuzzySetWidgetUiBinder uiBinder = GWT.create(FuzzySetWidgetUiBinder.class);

	interface FuzzySetWidgetUiBinder extends UiBinder<Widget, FuzzySetWidget> {
	}

	/**
	 * A registered handler will be notified when an item is selected.
	 */
//	public interface SaveHandler {
//		void doSave(List<FuzzyChrCls> fcls);
//	}
	
	@UiField
	DivElement container;
	
	@UiField
	Button btnSave;
	
//	private SaveHandler saveHandl = null;
	private List<FuzzyClass> fuzzyVals;
	private List<FuzzyChrCls> fuzzySets;
	private Characteristic chr;
	
	
	public FuzzySetWidget(Characteristic chr, List<FuzzyChrCls> fuzzySets, List<FuzzyClass> fuzzyVals) {
		initWidget(uiBinder.createAndBindUi(this));
		
		container.setId(Document.get().createUniqueId());
		
		this.chr = chr;
		this.fuzzySets = fuzzySets;
		this.fuzzyVals = fuzzyVals;
	}
	
	private void showWidget() {
		if (fuzzyVals == null || fuzzySets == null) {
			GWT.log(chr.getName() + ": fuzzyVals == null: " + (fuzzyVals == null)
					+ ", fuzzySets == null: " + (fuzzySets == null));
			this.setVisible(false);
			return;
		}
		
		JsArray<JsFuzzySet> fSets = JavaScriptObject.createArray().cast();
		for (int i = 0; i < fuzzySets.size(); i++) {
			fSets.push(JsFuzzySet.instance(fuzzySets.get(i), i));
		}
		
		JsArray<JsCharacteristicValue> chrVals = JavaScriptObject.createArray().cast();
		for (int i = 0; i < fuzzyVals.size(); i++) {
			chrVals.push(JsCharacteristicValue.instance(fuzzyVals.get(i)));
		}
		
		JsCharacteristicFcls jsChr = JsCharacteristicFcls.instance(chr.getName(), chrVals, fSets);
		
		jsShowFuzzyClasses(container.getId(), jsChr);
	}
	
	// call d3 with dom element & data
	private native void jsShowFuzzyClasses(String div, JsCharacteristicFcls chr)/*-{
		// TODO: @Test
		// TODO: Remove!! fsets should be binded! This is for test ONLY!
		//var cloned = JSON.parse(JSON.stringify(chr));
		$wnd.showFuzzyClasses(div, chr);
	}-*/;
	
	@Override
	protected void onLoad() {
		super.onLoad();
		showWidget();
	}
	
	@UiHandler("btnSave")
	void handleClick(ClickEvent e) {
//		if (saveHandl != null) {
//			saveHandl.doSave(fcls);
//		}
	}

//	public void setSaveHandl(SaveHandler saveHandl) {
//		this.saveHandl = saveHandl;
//	}
}
