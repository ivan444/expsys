package imaing.expsys.server.model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseDAO {
	protected EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager (EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
