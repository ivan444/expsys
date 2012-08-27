package imaing.expsys.client.view.widgets;

import imaing.expsys.client.domain.FuzzyClass;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FuzzyClsTxtBox extends Composite {
	
	private static FuzzyClsTxtBoxUiBinder uiBinder = GWT.create(FuzzyClsTxtBoxUiBinder.class);

	interface FuzzyClsTxtBoxUiBinder extends UiBinder<Widget, FuzzyClsTxtBox> {
	}
	
	@UiField(provided=true)
	Label value;
	
	@UiField
	HorizontalPanel valuesPane;
	
	private FuzzyClass fcls;
	
	public FuzzyClsTxtBox(FuzzyClass fcls) {
		this.value = new Label(fcls.getValue());
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.fcls = fcls;
		
		init();
	}

	private void init() {
		for (int i = 0; i < fcls.getMembershipVal().length; i++) {
			DoubleBox box = new DoubleBox();
			box.addChangeHandler(instanceMValChangeHandl(i));
			box.setValue(fcls.getMembershipVal()[i], true);
			valuesPane.add(box);
		}
	}
	
	private ChangeHandler instanceMValChangeHandl(final int idx) {
		return new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Double newVal = ((DoubleBox) event.getSource()).getValue();
				if (newVal != null) {
					fcls.setMembershipVal(idx, newVal.doubleValue());
				}
			}
		};
	}


}
