package imaing.expsys.server.model;

import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * Abstract implementation of DAO generic repository.
 * Idea came from: http://www.ibm.com/developerworks/java/library/j-genericdao.html
 *
 * @param <E> DAO class type for entity.
 * @param <G> GWT class type for entity.
 */
public class GenericDAOImpl<E extends BaseEntity<G>, G>
		extends BaseDAO implements GenericDAO<E, G> {
	
	private Class<E> type;
	
	public GenericDAOImpl(Class<E> type) {
		this.type = type;
	}
	
	@Override
	public G getById(Long id) {
		E dao = (E) entityManager.find(type, id);
		
		if (dao == null) return null;
		else return dao.getCleaned();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<G> list() {
		List<E> allDaos = entityManager.createQuery("from " + type.getName()).getResultList();
		
		List<G> allGs = new ArrayList<G>();
		
		if (allDaos != null) {
			for (E dao : allDaos) {
				allGs.add(dao.getCleaned());
			}
		}
		
		return allGs;
	}

	@Override
	@Transactional(readOnly=false)
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
			if (dao.getId() == null) {
				entityManager.persist(dao);
			} else {
				entityManager.merge(dao);
			}
		} catch (Exception e) {
			throw new InvalidDataException(e);
		}
	}
	
	@Override
	@Transactional(readOnly=false)
	public void delete(E daoObject) throws InvalidDataException {
		if (daoObject == null) throw new InvalidDataException("Trying to delete null object!");
		
		entityManager.remove(daoObject);
	}
	
	@Override
	@Transactional(readOnly=false)
	public void delete(Long id) throws InvalidDataException {
		if (id == null) throw new InvalidDataException("Trying to delete object with null ID!");
		E dao = (E) entityManager.find(type, id);
		
		delete(dao);
	}

}
