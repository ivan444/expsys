package imaing.expsys.server.model;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyChrCls;

import java.util.LinkedList;
import java.util.List;

public class FuzzyChrClsDAOImpl extends GenericDAOImpl<FuzzyChrClsEnt, FuzzyChrCls> implements FuzzyChrClsDAO {
	
	public FuzzyChrClsDAOImpl(Class<FuzzyChrClsEnt> type) {
		super(type);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FuzzyChrCls> listForCharacteristic(Characteristic chr) {
		List<FuzzyChrClsEnt> ents = (List<FuzzyChrClsEnt>) em.createNamedQuery("FuzzyChrClsEnt.listForCharacteristic")
																   .setParameter("chr", new CharacteristicEnt(chr)).getResultList();
		List<FuzzyChrCls> chrsClean = new LinkedList<FuzzyChrCls>();
		if (ents != null){
			for (FuzzyChrClsEnt cd : ents) {
				chrsClean.add(cd.getCleaned());
			}
		}
		return chrsClean;
	}
}
