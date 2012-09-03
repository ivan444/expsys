package imaing.expsys.server.api;

public class APIResponse {
	public enum Status {
		OK,
		ERROR
	}
	
	private Status status;
	private String msg;
	
	public APIResponse() {
	}

	public APIResponse(Status status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
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
}
