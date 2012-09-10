package imaing.expsys.server.model;

import imaing.expsys.client.domain.DTOObject;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * Abstract implementation of DAO generic repository.
 * Idea came from: http://www.ibm.com/developerworks/java/library/j-genericdao.html
 *
 * @param <E> DAO class type for entity.
 * @param <G> GWT class type for entity.
 */
public class GenericDAOImpl<E extends BaseEntity<G>, G extends DTOObject>
		extends BaseDAO implements GenericDAO<E, G> {
	
	private Class<E> type;
	
	public GenericDAOImpl(Class<E> type) {
		this.type = type;
	}
	
	@Override
	public G getById(Long id) {
		E ent = (E) em.find(type, id);
		
		if (ent == null) return null;
		else return ent.getCleaned();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<G> list() {
		List<E> allEnts = em.createQuery("from " + type.getName()).getResultList();
		
		List<G> allGs = new ArrayList<G>();
		
		if (allEnts != null) {
			for (E ent : allEnts) {
				allGs.add(ent.getCleaned());
			}
		}
		
		return allGs;
	}
	
//	public void save2(G gwtObject) throws InvalidDataException {
//		if (gwtObject == null) throw new InvalidDataException("Trying to save null object!");
//		E ent = null;
//		try {
//			ent = type.newInstance();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//		
//		Method[] methods = type.getDeclaredMethods();
//		List<Method> setters = new LinkedList<Method>();
//		List<Method> getters = new LinkedList<Method>();
//		for (int i = 0; i < methods.length; i++) {
//			Method m = methods[i];
//			if (m.getName().startsWith("set")) {
//				setters.add(m);
//			} else if (m.getName().startsWith("get")) {
//				getters.add(m);
//			}
//			
//			m.invoke(ent, args)
//		}
//		
//		gwtObject.getClass()
//		
//		ent.fill(gwtObject);
//		try {
//			if (ent.getId() == null) {
//				entityManager.persist(ent);
//			} else {
//				entityManager.merge(ent);
//			}
//		} catch (Exception e) {
//			throw new InvalidDataException(e);
//		}
//	}

	@Override
	@Transactional(readOnly=false)
	public G save(G gwtObject) throws InvalidDataException {
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
			ent = em.merge(ent);
		} catch (Exception e) {
			throw new InvalidDataException(e);
		}
		
		return ent.getCleaned();
	}
	
	@Override
	@Transactional(readOnly=false)
	public void delete(E ent) throws InvalidDataException {
		if (ent == null) throw new InvalidDataException("Trying to delete null object!");
		else if (ent.getId() == null) throw new InvalidDataException("Trying to delete object with null ID!");
		
		if (!em.contains(ent)) {
			ent = (E) em.find(type, ent.getId());
		}
		
		extraDeleteOperations(ent);
		
		em.remove(ent);
	}

	/**
	 * This function is called right before deleting an entity.
	 * It enables adding extra functionality in delete function skeleton
	 * (this is template method).
	 * 
	 * @param ent Entity to be deleted. This entity MUST be inside entity manager.
	 */
	protected void extraDeleteOperations(E ent) throws InvalidDataException {
	}
	
	@Override
	@Transactional(readOnly=false)
	public void delete(Long id) throws InvalidDataException {
		if (id == null) throw new InvalidDataException("Trying to delete object with null ID!");
		E ent = (E) em.find(type, id);
		
		delete(ent);
	}

	@Override
	@Transactional(readOnly=false)
	public List<G> saveAll(Collection<G> gs) throws InvalidDataException {
		if (gs == null) throw new InvalidDataException("Trying to save null collection!");
		
		int objsNum = gs.size();
		
		List<E> ents = new ArrayList<E>(objsNum);
		try {
			for (G g : gs) {
				E ent = type.newInstance();
				ent.fill(g);
				ents.add(ent);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		List<G> clean = new ArrayList<G>(objsNum);
		try {
			for (E ent : ents) {
				ent = em.merge(ent);
				clean.add(ent.getCleaned());
			}
		} catch (Exception e) {
			throw new InvalidDataException(e);
		}
		
		return clean;
	}
	
	@Override
	@Transactional(readOnly=false)
	public void deleteAll(Collection<G> gs) throws InvalidDataException {
		if (gs == null) throw new InvalidDataException("Trying to delete null collection!");
		
		for (G g : gs) {
			if (g.getId() == null) throw new InvalidDataException("Trying to delete object without ID!");
			delete(g.getId());
		}
	}

}
