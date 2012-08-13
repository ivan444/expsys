package imaing.expsys.server.dao;


import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of DAO generic repository.
 * Idea came from: http://www.ibm.com/developerworks/java/library/j-genericdao.html
 *
 * @param <E> DAO class type for entity.
 * @param <G> GWT class type for entity.
 */
public class GenericRepositoryImpl<E extends DAOobject<G>, G>
		extends BaseRepository implements GenericRepository<E, G> {
	
	private Class<E> type;
	
	public GenericRepositoryImpl(Class<E> type) {
		this.type = type;
	}
	
	@Override
	public G getById(Long id) {
		E dao = (E) hibernateTemplate.get(type, id);
		
		if (dao == null) return null;
		else return dao.getCleaned();
	}

	@Override
	public List<G> list() {
		List<E> allDaos = hibernateTemplate.loadAll(type);
		
		List<G> allGs = new ArrayList<G>();
		
		if (allDaos != null) {
			for (E dao : allDaos) {
				allGs.add(dao.getCleaned());
			}
		}
		
		return allGs;
	}

	@Override
	public void save(G gwtObject) throws InvalidDataException {
		if (gwtObject == null) throw new InvalidDataException("Trying to save null object!");
		E dao = null;
		try {
			dao = type.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		dao.fill(gwtObject);
		try {
			hibernateTemplate.saveOrUpdate(dao);
		} catch (Exception e) {
			throw new InvalidDataException(e);
		}
	}
	
	@Override
	public void delete(E daoObject) throws InvalidDataException {
		if (daoObject == null) throw new InvalidDataException("Trying to delete null object!");
		
		hibernateTemplate.delete(daoObject);
	}
	
	@Override
	public void delete(Long id) throws InvalidDataException {
		if (id == null) throw new InvalidDataException("Trying to delete object with null ID!");
		E dao = (E) hibernateTemplate.get(type, id);
		
		hibernateTemplate.delete(dao);
	}

}
