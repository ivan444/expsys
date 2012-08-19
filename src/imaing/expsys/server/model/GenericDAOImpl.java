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
		E ent = (E) entityManager.find(type, id);
		
		if (ent == null) return null;
		else return ent.getCleaned();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<G> list() {
		List<E> allEnts = entityManager.createQuery("from " + type.getName()).getResultList();
		
		List<G> allGs = new ArrayList<G>();
		
		if (allEnts != null) {
			for (E ent : allEnts) {
				allGs.add(ent.getCleaned());
			}
		}
		
		return allGs;
	}

	@Override
	@Transactional(readOnly=false)
	public void save(G gwtObject) throws InvalidDataException {
		if (gwtObject == null) throw new InvalidDataException("Trying to save null object!");
		E ent = null;
		try {
			ent = type.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		ent.fill(gwtObject);
		try {
			if (ent.getId() == null) {
				entityManager.persist(ent);
			} else {
				entityManager.merge(ent);
			}
		} catch (Exception e) {
			throw new InvalidDataException(e);
		}
	}
	
	@Override
	@Transactional(readOnly=false)
	public void delete(E ent) throws InvalidDataException {
		if (ent == null) throw new InvalidDataException("Trying to delete null object!");
		
		entityManager.remove(ent);
	}
	
	@Override
	@Transactional(readOnly=false)
	public void delete(Long id) throws InvalidDataException {
		if (id == null) throw new InvalidDataException("Trying to delete object with null ID!");
		E ent = (E) entityManager.find(type, id);
		
		delete(ent);
	}

}
