package imaing.expsys.server.dao;

/**
 * Interface for data object with methods for use with GWT bean. 
 *
 * @param <G> GWT bean type.
 */
public interface DAOobject<G> {
	/** Get domain object from DAO object. */
	G getCleaned();
	
	/** 
	 * Get domain object from DAO object but don't
	 * clean {@code skipMember} attribute. Instead
	 * of cleaning it, put object {@code member}
	 * which is an GWT DTO object.
	 */
	G getCleaned(String skipMember, Object member);
	
	void fill(G g);
	Long getId();
}
