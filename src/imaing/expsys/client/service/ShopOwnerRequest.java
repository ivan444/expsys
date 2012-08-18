package imaing.expsys.client.service;

import imaing.expsys.client.domain.ShopOwnerProxy;
import imaing.expsys.server.model.ShopOwner;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(ShopOwner.class)
public interface ShopOwnerRequest extends RequestContext {
	Request<List<ShopOwnerProxy>> list();
	
	InstanceRequest<ShopOwnerProxy, Void> save() throws InvalidDataException;
}
