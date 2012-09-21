package imaing.expsys.client.domain;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;

public class FuzzySet extends JavaScriptObject {

	// Overlay types always have protected, zero-arg ctors
	protected FuzzySet() {}
	
	public static native FuzzySet instance(JsArrayNumber xArr, JsArrayNumber yArr) /*-{
		if (xArr.length != yArr.length || xArr.length != 4) console.error("Invalid index: " + i);
		return {xs: xArr, ys: yArr};
	}-*/;
	
	public final native double getX(int i) /*-{
		if (i < 0 || i > 3) console.error("Invalid index: " + i);
    	return this.xs[i];
  	}-*/;
	
	public final native double getY(int i) /*-{
		if (i < 0 || i > 3) console.error("Invalid index: " + i);
		return this.ys[i];
	}-*/;
	
	// TODO get/set name
}
