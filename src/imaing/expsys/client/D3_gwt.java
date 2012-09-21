package imaing.expsys.client;

import imaing.expsys.client.domain.FuzzySet;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class D3_gwt implements EntryPoint {

	public void onModuleLoad() {
		Element parent = RootPanel.getBodyElement();

		// assume this is in a widget class: create DOM element & attach to
		// parent
		Element div = DOM.createDiv();
		div.setId("graph");
		parent.appendChild(div);

		// load data from somewhere (here randomly generated)
		double[] data = new double[10];
		for (int i = 0; i < data.length; i++) {
			data[i] = Math.random() + .1;
		}

		// convert data into jsarray (see
		// https://developers.google.com/web-toolkit/doc/latest/DevGuideCodingBasicsJSNI
		// for more documentation on overlay types etc.
		//
		// the idea is that a native js object gets passed into the js code
		// so it runs fairly fast in dev mode
//		JsArrayNumber jsData = JavaScriptObject.createArray().cast();
//		for (int i = 0; i < data.length; i++) {
//			jsData.push(data[i]);
//		}
		
		JsArray<FuzzySet> fSets = JavaScriptObject.createArray().cast();
		
		JsArrayNumber fs1xData = JavaScriptObject.createArray().cast();
		fs1xData.push(50.0);
		fs1xData.push(100.0);
		fs1xData.push(200.0);
		fs1xData.push(300.0);
		JsArrayNumber fs1yData = JavaScriptObject.createArray().cast();
		fs1yData.push(110.0);
		fs1yData.push(10.0);
		fs1yData.push(10.0);
		fs1yData.push(110.0);
		FuzzySet fs1 = FuzzySet.instance(fs1xData, fs1yData);
		fSets.push(fs1);
		
		JsArrayNumber fs2xData = JavaScriptObject.createArray().cast();
		fs2xData.push(250.0);
		fs2xData.push(350.0);
		fs2xData.push(350.0);
		fs2xData.push(650.0);
		JsArrayNumber fs2yData = JavaScriptObject.createArray().cast();
		fs2yData.push(110.0);
		fs2yData.push(10.0);
		fs2yData.push(10.0);
		fs2yData.push(110.0);
		FuzzySet fs2 = FuzzySet.instance(fs2xData, fs2yData);
		fSets.push(fs2);

		createDragCirc(div, fSets);
	}

	// call d3 with dom element & data
	private native void createDragCirc(Element div, JsArray<FuzzySet> fSets)/*-{
		$wnd.dragCirc(div, fSets);
	}-*/;

}