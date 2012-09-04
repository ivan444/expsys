package imaing.expsys.server.api;

public class APIExtendedResponse<P> {
	private Status status;
	private String msg;
	private P payload;
	
	public enum Status {
		OK,
		ERROR
	}
	
	public APIExtendedResponse() {
	}

	public APIExtendedResponse(Status status, String msg, P payload) {
		super();
		this.status = status;
		this.msg = msg;
		this.payload = payload;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public P getPayload() {
		return payload;
	}

	public void setPayload(P payload) {
		this.payload = payload;
	}
}
