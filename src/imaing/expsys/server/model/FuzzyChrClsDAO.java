package imaing.expsys.server.model;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyChrCls;
import imaing.expsys.client.domain.Shop;

import java.util.List;

public interface FuzzyChrClsDAO extends GenericDAO<FuzzyChrClsEnt, FuzzyChrCls> {
	List<FuzzyChrCls> listForCharacteristic(Characteristic chr);
	List<FuzzyChrCls> listForShop(Shop shop);
}
