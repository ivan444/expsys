package imaing.expsys.client.services;


import imaing.expsys.client.domain.Shop;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ShopOwnerServiceAsync {

	void list(AsyncCallback<List<Shop>> callback);
	
	void save(Shop shopOwner, AsyncCallback<Shop> callback);
}
