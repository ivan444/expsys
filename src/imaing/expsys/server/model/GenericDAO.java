package imaing.expsys.server.model;


import imaing.expsys.shared.exceptions.InvalidDataException;

import java.util.List;

/**
 * Generic repository for DAO classes.
 *
 * @param <E> DAO class type for entity.
 * @param <G> GWT class type for entity.
 */
public interface GenericDAO<E extends BaseEntity<G>, G> {
	
	/**
	 * Return data with id equal to {@code id}.
	 * 
	 * @param id Id of needed data.
	 * @return Data with id equal to {@code id}.
	 */
	public G getById(Long id);
	
	/**
	 * Save data from {@code gwtObject}.
	 * 
	 * @param gwtObject
	 * @throws InvalidDataException 
	 */
	public void save(G gwtObject) throws InvalidDataException;
	
	/**
	 * @return All saved data from objects of type G as a list.
	 */
	public List<G> list();
	
	/**
	 * Delete object {@code daoObject} from database.
	 * 
	 * @param daoObject Object to delete.
	 * @throws InvalidDataException
	 */
	public void delete(E daoObject) throws InvalidDataException;
	
	/**
	 * Delete object with ID={@code id} from database.
	 * 
	 * @param id ID of Object to delete.
	 * @throws InvalidDataException
	 */
	public void delete(Long id) throws InvalidDataException;
	
}
