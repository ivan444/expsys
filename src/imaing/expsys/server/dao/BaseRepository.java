package imaing.expsys.server.dao;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;


/*
 * NOTE: Ovo je base repository, svi ostali repository ga nasljeduju.
 * Dependency injectionom se u svaki pojedini repository ubacuje session factory,
 * nakon cega ova dolje sunkcija konstruira hibernate template iz tog factory-a
 * Vidi CustomerRepositoryImpl.java za detalje o dependency injectionu
 * 
 * Za komunikaciju s bazom se koristi HibernateTemplate, Springova helper klasa
 * koja dosta pojednostavljuje komunikaciju s bazom. Ako vam iz nekog razloga treba
 * klasicni Hibernate session, mozete ga dobiti ovako:
 * hibernateTemplate.getSessionFactory().openSession();
 * 
 */

public abstract class BaseRepository {
	protected HibernateTemplate hibernateTemplate;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}
}
