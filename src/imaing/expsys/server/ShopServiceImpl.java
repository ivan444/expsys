package imaing.expsys.server;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopService;
import imaing.expsys.server.model.ProductDAO;
import imaing.expsys.server.model.ShopDAO;
import imaing.expsys.shared.exceptions.InvalidDataException;

public class ShopServiceImpl implements ShopService, SessionAware {
	
	@Autowired private ShopDAO shpDao;
	@Autowired private ProductDAO prodDao;
	
	public ShopServiceImpl() {
	}
	
	@Override
	public void setSession(HttpSession session) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Shop> list() {
		return shpDao.list();
	}

	@Override
	public Shop save(Shop shopOwner) throws InvalidDataException {
		shpDao.save(shopOwner);
		Shop saved = shpDao.getShopForEmail(shopOwner.getEmail());
		return saved;
	}

	@Override
	public void addProduct(Product p) throws InvalidDataException {
		prodDao.save(p);
	}

}
