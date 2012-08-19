package imaing.expsys.server;

import imaing.expsys.server.model.ShopOwner;
import imaing.expsys.server.model.ShopOwnerDAO;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class ShopOwnerService {
	 @Autowired ShopOwnerDAO ownrDao;
	 
	 public ShopOwnerService() {
	 }
	
	public List<ShopOwner> list() {
		return ownrDao.findAll();
	}
	
	public ShopOwner save(ShopOwner ownr) throws InvalidDataException {
		ownrDao.save(ownr);
		return ownr;
	}
}
