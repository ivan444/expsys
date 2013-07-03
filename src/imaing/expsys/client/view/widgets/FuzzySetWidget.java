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
	public interface SaveHandler {
		void doSave(List<FuzzyChrCls> fuzzySets, List<FuzzyClass> fuzzyVals);
	}
	
	@UiField
	DivElement container;
	
	@UiField
	Button btnSave;
	
	private SaveHandler saveHandl = null;
	private List<FuzzyClass> fuzzyVals;
	private List<FuzzyChrCls> fuzzySets;
	private Characteristic chr;
	private JsCharacteristicFcls jsChr;
	private JsArray<JsCharacteristicValue> chrVals;
	private JsArray<JsFuzzySet> fSets;
	
	
	public FuzzySetWidget(Characteristic chr, List<FuzzyChrCls> fuzzySets, List<FuzzyClass> fuzzyVals) {
		initWidget(uiBinder.createAndBindUi(this));
		
		container.setId(Document.get().createUniqueId());
		
		this.chr = chr;
		this.fuzzySets = fuzzySets;
		this.fuzzyVals = fuzzyVals;
		
		if (fuzzyVals != null && fuzzySets != null) {
			init();
		}
	}
	
	private void init() {
		
		fSets = JavaScriptObject.createArray().cast();
		for (int i = 0; i < fuzzySets.size(); i++) {
			fSets.push(JsFuzzySet.instance(fuzzySets.get(i), i));
		}
		
		chrVals = JavaScriptObject.createArray().cast();
		for (int i = 0; i < fuzzyVals.size(); i++) {
			chrVals.push(JsCharacteristicValue.instance(fuzzyVals.get(i)));
		}
		
		jsChr = JsCharacteristicFcls.instance(chr.getName(), chrVals, fSets);
	}
		
	private void showWidget() {
		if (fuzzyVals == null || fuzzySets == null) {
			GWT.log(chr.getName() + ": fuzzyVals == null: " + (fuzzyVals == null)
					+ ", fuzzySets == null: " + (fuzzySets == null));
			this.setVisible(false);
			return;
		}
		
		jsShowFuzzyClasses(container.getId(), jsChr);
	}
	
	private void updateDataFromJs() {
		updateFuzzyValsFromJs();
		updateFuzzySetsFromJs();
		adjustMembershipVals();
	}
	
	private void adjustMembershipVals() {
		for (FuzzyClass val : fuzzyVals) {
			int xpos = val.getxPos();
			for (int i = 0; i < fuzzySets.size(); i++) {
				FuzzyChrCls set = fuzzySets.get(i);
				if (xpos < set.getxLeftDown() || xpos > set.getxRightDown()) {
					val.setMembershipVal(i, 0.0);
				} else if (xpos < set.getxLeftUp()) {
					val.setMembershipVal(i, (xpos - set.getxLeftDown())/(set.getxLeftUp() - set.getxLeftDown()));
				} else if (xpos > set.getxRightUp()) {
					val.setMembershipVal(i, (set.getxRightDown() - xpos)/(set.getxRightDown() - set.getxRightUp()));
				} else {
					val.setMembershipVal(i, 1.0);
				}
			}
		}
	}

	private void updateFuzzySetsFromJs() {
		for (int i = 0; i < fuzzySets.size(); i++) {
			FuzzyChrCls set = fuzzySets.get(i);
			JsFuzzySet jsSet = fSets.get(i);
			set.setxLeftUp(jsSet.get(0));
			set.setxLeftDown(jsSet.get(1));
			set.setxRightUp(jsSet.get(2));
			set.setxRightDown(jsSet.get(3));
		}
	}
	
	private void updateFuzzyValsFromJs() {
		for (int i = 0; i < fuzzyVals.size(); i++) {
			FuzzyClass val = fuzzyVals.get(i);
			JsCharacteristicValue jsVal = chrVals.get(i);
			val.setxPos(jsVal.getXPos());
		}
	}
	
	// call d3 with dom element & data
	private native void jsShowFuzzyClasses(String div, JsCharacteristicFcls chr)/*-{
		$wnd.showFuzzyClasses(div, chr);
	}-*/;
	
	@Override
	protected void onLoad() {
		super.onLoad();
		showWidget();
	}
	
	@UiHandler("btnSave")
	void handleClick(ClickEvent e) {
		updateDataFromJs();
//		GWT.log("SETS: " + Arrays.toString(fuzzySets.toArray())
//				+",  VALS: " + Arrays.toString(fuzzyVals.toArray()));
		if (saveHandl != null) {
			saveHandl.doSave(fuzzySets, fuzzyVals);
		}
	}

	public void setSaveHandl(SaveHandler saveHandl) {
		this.saveHandl = saveHandl;
	}
}
