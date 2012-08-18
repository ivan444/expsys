package imaing.expsys.server.model;

import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

/**
 * Abstract implementation of DAO generic repository.
 * 
 * @param <E> DAO class type for entity.
 */
public abstract class GenericDAOImpl<E extends BaseEntity> extends Locator<E, Long> implements
		GenericDAO<E>, ServiceLocator {
	
	protected EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager (EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E create(Class<? extends E> clazz) {
		return (E) getInstance(clazz);
	}

	@Override
	public E find(Class<? extends E> clazz, Long id) {
		return (E) entityManager.find(clazz, id);
	}

	@Override
	public Class<E> getDomainType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<Long> getIdType() {
		return Long.class;
	}

	@Override
	public Object getVersion(E e) {
		return e.getVersion();
	}
	
	public Long getId(E e) {
		return e.getId();
	}

	private Class<E> type;

	public GenericDAOImpl(Class<E> type) {
		this.type = type;
	}

	@Override
	public Object getInstance(Class<?> cls) {
		try {
			return cls.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public E findById(Long id) {
		E ent = (E) entityManager.find(type, id);
		return ent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll() {
		List<E> allEnts = entityManager.createQuery("from " + type.getName()).getResultList();
		return allEnts;
	}

	@Override
	@Transactional(readOnly = false)
	public void save(E e) throws InvalidDataException {
		if (e == null)
			throw new InvalidDataException("Trying to save null object!");
		try {
			if (e.getId() == null) {
				entityManager.persist(e);
			} else {
				entityManager.merge(e);
			}
		} catch (Exception ex) {
			throw new InvalidDataException(ex);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(E daoObject) throws InvalidDataException {
		if (daoObject == null)
			throw new InvalidDataException("Trying to delete null object!");

		entityManager.remove(daoObject);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Long id) throws InvalidDataException {
		if (id == null)
			throw new InvalidDataException(
					"Trying to delete object with null ID!");
		E dao = (E) entityManager.find(type, id);

		delete(dao);
	}
	
//	private void init() {
//        HttpServletRequest request = RequestFactoryServlet.getThreadLocalRequest();
//        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
//        EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);
//        entityManager = entityManagerFactory.createEntityManager();
//    }


}
