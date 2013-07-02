package imaing.expsys.client.domain;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Concrete value of characteristic with it's position in
 * fuzzy classes graph.  
 */
public class JsCharacteristicValue extends JavaScriptObject {
//	private FuzzyClass ownerObject;
//	
//	public FuzzyClass getOwnerObject() {
//		return ownerObject;
//	}
//	
//	public void setOwnerObject(FuzzyClass ownerObject) {
//		this.ownerObject = ownerObject;
//	}
//	
//	/**
//	 * Update FuzzySet coordinates with contents
//	 * of JsFuzzySet.
//	 */
//	public void updateFromOwner() {
//		ownerObject.setxPos(getXPos());
//	}
	
	// Overlay types always have protected, zero-arg ctors
	protected JsCharacteristicValue() {}
	
	/**
	 * @param value Concrete value of characteristic.
	 * @param position Position of the value in characteristic's fuzzy classes graph. 
	 * @return JsCharacteristicValue instance.
	 */
	public static native JsCharacteristicValue instance(String value, int position) /*-{
		return {val: value, x: position};
	}-*/;
	
	public static JsCharacteristicValue instance(FuzzyClass ownerObject) {
		JsCharacteristicValue jsChrVal = instance(ownerObject.getValue(), ownerObject.getxPos());
//		jsChrVal.setOwnerObject(ownerObject);
		return jsChrVal;
	};
	
	public native final int getXPos() /*-{
		return this.x;
	}-*/;
}
