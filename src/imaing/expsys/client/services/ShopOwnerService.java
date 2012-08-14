package imaing.expsys.client.services;


import imaing.expsys.client.domain.ShopOwner;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("shopowner.rpc")
public interface ShopOwnerService extends RemoteService {

	String say();
	
	List<ShopOwner> list();
	
	ShopOwner save(ShopOwner shopOwner) throws InvalidDataException;
}
