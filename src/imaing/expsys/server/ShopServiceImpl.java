package imaing.expsys.server;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopService;
import imaing.expsys.server.model.CharacteristicDAO;
import imaing.expsys.server.model.ProductDAO;
import imaing.expsys.server.model.ShopDAO;
import imaing.expsys.shared.exceptions.InvalidDataException;

public class ShopServiceImpl implements ShopService, SessionAware {
	
	@Autowired private ShopDAO shpDao;
	@Autowired private ProductDAO prodDao;
	@Autowired private CharacteristicDAO chrDao;
	
	public ShopServiceImpl() {
	}
	
	@Override
	public void setSession(HttpSession session) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Shop> listShops() {
		return shpDao.list();
	}

	@Override
	public Shop saveShop(Shop shopOwner) throws InvalidDataException {
		Shop saved = shpDao.save(shopOwner);
		return saved;
	}

	@Override
	public Product addProduct(Product p) throws InvalidDataException {
		return prodDao.save(p);
	}

	@Override
	public void deleteShop(long shopId) throws InvalidDataException {
		shpDao.delete(Long.valueOf(shopId));
	}

	@Override
	public List<Characteristic> listCharacteristics(Shop shop) {
		return chrDao.listCharacteristicsForShop(shop);
	}

	@Override
	public Characteristic saveCharacteristic(Characteristic chr)
			throws InvalidDataException {
		return chrDao.save(chr);
	}

	@Override
	public void deleteCharacteristic(long chrId) throws InvalidDataException {
		chrDao.delete(Long.valueOf(chrId));
	}

}
