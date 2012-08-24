package imaing.expsys.server.model;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.ProdChr;
import imaing.expsys.client.domain.Product;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.List;

public interface ProdChrDAO extends GenericDAO<ProdChrEnt, ProdChr> {
	List<ProdChr> listProdChrForProduct(Product product);
	ProdChr getProdChrForProductAndCharacteristic(Product product, Characteristic chr);
	void deleteAllForProduct(Product product) throws InvalidDataException;
}
