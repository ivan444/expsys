package imaing.expsys.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface BasicDisplay {
	void reportError(String errorMsg);
	Widget asWidget();
}
