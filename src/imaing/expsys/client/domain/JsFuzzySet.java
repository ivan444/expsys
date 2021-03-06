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
public class JsFuzzySet extends JavaScriptObject {
	// Overlay types always have protected, zero-arg ctors
	protected JsFuzzySet() {}
	
	public static native JsFuzzySet instance(JsArrayNumber xArr, int fuzzyClassIdx) /*-{
		if (xArr.length != 4) {
			console.error("Invalid array length. Expected 4, got " + xArr.length);
			return {xs: [], fclsIdx: fuzzyClassIdx};
		} else {
			return {xs: xArr, fclsIdx: fuzzyClassIdx};
		}
	}-*/;
	
	public static JsFuzzySet instance(FuzzyChrCls ownerObject, int fuzzySetIdx) {
		JsArrayNumber fuzSetData = JavaScriptObject.createArray().cast();
		fuzSetData.push(ownerObject.getxLeftUp());
		fuzSetData.push(ownerObject.getxLeftDown());
		fuzSetData.push(ownerObject.getxRightUp());
		fuzSetData.push(ownerObject.getxRightDown());
		
		JsFuzzySet jsFuzzySet = JsFuzzySet.instance(fuzSetData, fuzzySetIdx);
		return jsFuzzySet;
	}
	
	public native final double get(int idx) /*-{
		return this.xs[idx];
	}-*/;
}
