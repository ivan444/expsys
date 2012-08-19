package imaing.expsys.server.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity<G> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * Primary key of entity. Long is fixed pk type for all entities.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "pkGen")
    @TableGenerator(allocationSize = 1, initialValue = 0, name = "pkGen", table = "PRIMARY_KEYS")
    protected Long id;

    @Version
    protected Integer version;
    
    public BaseEntity() {
    }
    
    public BaseEntity(G g) {
    	fill(g);
    }
    
    /** Get domain object from DAO object. */
    @Transient
	public abstract G getCleaned();
	
    @Transient
	public abstract void fill(G g);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	// Force reimplementation of hashCode and equals
	public abstract int hashCode();
	public abstract boolean equals(Object obj);
	
}
