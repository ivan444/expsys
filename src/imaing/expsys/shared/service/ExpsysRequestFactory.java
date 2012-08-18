package imaing.expsys.shared.service;

import imaing.expsys.client.service.ShopOwnerRequest;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface ExpsysRequestFactory extends RequestFactory {
	ShopOwnerRequest shopRequest();
}
