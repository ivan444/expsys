package imaing.expsys.server;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopOwnerService;
import imaing.expsys.server.model.ShopDAO;
import imaing.expsys.shared.exceptions.InvalidDataException;

public class ShopOwnerServiceImpl implements ShopOwnerService, SessionAware {
	@Autowired private ShopDAO shpOwnerRepos;
	
	public ShopOwnerServiceImpl() {
	}
	
	@Override
	public void setSession(HttpSession session) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Shop> list() {
		return shpOwnerRepos.list();
	}

	@Override
	public Shop save(Shop shopOwner) throws InvalidDataException {
		shpOwnerRepos.save(shopOwner);
		return null;
	}

}
