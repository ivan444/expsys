package imaing.expsys.server;

import javax.servlet.http.HttpSession;

public interface SessionAware {
	
	void setSession(HttpSession session);

}
