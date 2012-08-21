package imaing.expsys.server.model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseDAO {
	
	@PersistenceContext
	protected EntityManager em;

	public void setEntityManager (EntityManager em) {
		this.em = em;
	}

}
