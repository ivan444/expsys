package imaing.expsys.client.domain;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;

/**
 * JavaScript object for single fuzzy set.
 * Fuzzy set is represented with with four points which define
 * polygon (with two parallel, horizontal lines).
 * The ordinate values are predefined (to min and max values).
 *
 */
public class FuzzySet extends JavaScriptObject {
	
	// Overlay types always have protected, zero-arg ctors
	protected FuzzySet() {}
	
	public static native FuzzySet instance(JsArrayNumber xArr, int fuzzyClassIdx) /*-{
		if (xArr.length != 4) {
			console.error("Invalid array length. Expected 4, got " + xArr.length);
			return {xs: [], fclsIdx: fuzzyClassIdx};
		} else {
			return {xs: xArr, fclsIds: fuzzyClassIdx};
		}
	}-*/;
	
}
