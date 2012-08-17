package imaing.expsys.server.dao;

/**
 * Interface for data object with methods for use with GWT bean. 
 *
 * @param <G> GWT bean type.
 */
public interface DAOobject<G> {
	/** Get domain object from DAO object. */
	G getCleaned();
	
	G getCleaned(Object caller);
	
	void fill(G g);
	Long getId();
}
