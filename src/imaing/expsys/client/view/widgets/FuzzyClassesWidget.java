package imaing.expsys.client.view.widgets;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyClass;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FuzzyClassesWidget extends Composite {
	
	private static FuzzyClassesWidgetUiBinder uiBinder = GWT.create(FuzzyClassesWidgetUiBinder.class);

	interface FuzzyClassesWidgetUiBinder extends UiBinder<Widget, FuzzyClassesWidget> {
	}

	/**
	 * A registered handler will be notified when an item is selected.
	 */
	public interface SaveHandler {
		void doSave(List<FuzzyClass> fcls);
	}
	
	@UiField(provided=true)
	Label chrName;
	
	@UiField
	VerticalPanel valuesPane;
	
	@UiField
	Button btnSave;
	
	private SaveHandler saveHandl = null;
	private List<FuzzyClass> fcls;
	
	public FuzzyClassesWidget(Characteristic chr, List<FuzzyClass> fcls) {
		chrName = new Label(chr.getName());
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.fcls = fcls;
		
		init();
	}
	
	private void init() {
		for (FuzzyClass fc : fcls) {
			valuesPane.add(new FuzzyClsTxtBox(fc));
		}
	}
	
	@UiHandler("btnSave")
	void handleClick(ClickEvent e) {
		if (saveHandl != null) {
			saveHandl.doSave(fcls);
		}
	}

	public void setSaveHandl(SaveHandler saveHandl) {
		this.saveHandl = saveHandl;
	}
}
