package imaing.expsys.server;

import java.util.List;

import javax.servlet.http.HttpSession;

import imaing.expsys.client.domain.ShopOwner;
import imaing.expsys.client.services.ShopOwnerService;
import imaing.expsys.shared.exceptions.InvalidDataException;

public class ShopOwnerServiceImpl implements ShopOwnerService, SessionAware {

	public ShopOwnerServiceImpl() {
	}
	
	@Override
	public void setSession(HttpSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String say() {
		return "I greet you";
	}

	@Override
	public List<ShopOwner> list() {
		return null;
	}

	@Override
	public ShopOwner save(ShopOwner shopOwner) throws InvalidDataException {
		return null;
	}

}
