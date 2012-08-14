package imaing.expsys.server.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseRepository {
	protected EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager (EntityManager entityManager) {
		this.entityManager = entityManager;
	}
//	public void setSessionFactory(SessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}
//	
//	protected Session session() {
//		return this.sessionFactory.getCurrentSession();
//	}
//	
//	protected Session open() {
//		return this.sessionFactory.openSession();
//	}
}
