package imaing.expsys.shared.service;

import imaing.expsys.client.domain.ShopOwnerProxy;
import imaing.expsys.server.ShopOwnerService;
import imaing.expsys.server.SpringServiceLocator;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = ShopOwnerService.class, locator = SpringServiceLocator.class)
public interface ShopOwnerRequest extends RequestContext {
	
	Request<List<ShopOwnerProxy>> list();
	
//	InstanceRequest<ShopOwnerProxy, Void> save();
	Request<ShopOwnerProxy> save(ShopOwnerProxy ownr);
}
