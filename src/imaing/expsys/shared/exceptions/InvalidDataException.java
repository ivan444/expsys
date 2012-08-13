package imaing.expsys.shared.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Work with invalid data exception.
 *
 */
public class InvalidDataException extends Exception implements IsSerializable {
	private static final long serialVersionUID = -1220409250358120166L;

	public InvalidDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDataException() {
		super();
	}

	public InvalidDataException(String message) {
		super(message);
	}

	public InvalidDataException(Throwable cause) {
		super(cause);
	}

}
