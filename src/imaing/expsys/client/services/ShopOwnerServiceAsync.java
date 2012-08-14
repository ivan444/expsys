package imaing.expsys.client.services;


import imaing.expsys.client.domain.ShopOwner;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ShopOwnerServiceAsync {

	void say(AsyncCallback<String> callback);
	
	void list(AsyncCallback<List<ShopOwner>> callback);
	
	void save(ShopOwner shopOwner, AsyncCallback<ShopOwner> callback);
}
