package imaing.expsys.server;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import imaing.expsys.client.domain.ShopOwner;
import imaing.expsys.client.services.ShopOwnerService;
import imaing.expsys.server.dao.ShopOwnerRepository;
import imaing.expsys.shared.exceptions.InvalidDataException;

public class ShopOwnerServiceImpl implements ShopOwnerService, SessionAware {
	@Autowired private ShopOwnerRepository shpOwnerRepos;
	
	public ShopOwnerServiceImpl() {
	}
	
	@Override
	public void setSession(HttpSession session) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<ShopOwner> list() {
		return shpOwnerRepos.list();
	}

	@Override
	public ShopOwner save(ShopOwner shopOwner) throws InvalidDataException {
		shpOwnerRepos.save(shopOwner);
		return null;
	}

}
