package imaing.expsys.client.domain;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * JavaScript object representing list of fuzzy classes
 * for single characteristic.
 */
public class JsCharacteristicFcls extends JavaScriptObject {
	
	// Overlay types always have protected, zero-arg ctors
	protected JsCharacteristicFcls() {}

	/**
	 * 
	 * @param chrName Name of characteristic binded to fuzzy sets in this object.
	 * @param chrValues Concrete values which characteristic can have with their position.
	 * @param fuzzySets
	 * @return
	 */
	public static native JsCharacteristicFcls instance(String chrName, JsArray<JsCharacteristicValue> chrValues, JsArray<JsFuzzySet> fuzzySets) /*-{
		return {fcls: fuzzySets, chrName: chrName, chrValues: chrValues};
	}-*/;
}
