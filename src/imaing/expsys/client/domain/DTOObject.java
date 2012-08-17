package imaing.expsys.client.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class DTOObject implements IsSerializable {
	protected Long id;
	
	public DTOObject() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
