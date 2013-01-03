package imaing.expsys.server.controllers;

import imaing.expsys.server.SessionAware;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GwtRpcController extends RemoteServiceServlet implements
		Controller, ServletContextAware {

	private static final long serialVersionUID = -4964736126857065787L;
	
	private ServletContext servletContext;
	private RemoteService remoteService;
	@SuppressWarnings("rawtypes")
	private Class remoteServiceClass;

	public void setRemoteService(RemoteService remoteService) {
		this.remoteService = remoteService;
		this.remoteServiceClass = this.remoteService.getClass();
	}
	
	public RemoteService getRemoteService() {
		return remoteService;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;

	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		doPost(request, response);
		return null; // response handled by GWT RPC over XmlHttpRequest
	}

	
	@Override
	public String processCall(String payload) throws SerializationException {
		
		HttpServletRequest request = this.getThreadLocalRequest(); // not to be confused with rpcRequest!!
		
		try {
			RPCRequest rpcRequest = RPC.decodeRequest(payload,
					this.remoteServiceClass);
			
			// inject session variable into remote services handler if session aware
			if (remoteService instanceof SessionAware) {
				((SessionAware) remoteService).setSession(request.getSession());
			}

			// delegate work to the spring injected service
			return RPC.invokeAndEncodeResponse(this.remoteService, rpcRequest
					.getMethod(), rpcRequest.getParameters());
		} catch (IncompatibleRemoteServiceException e) {
			return RPC.encodeResponseForFailure(null, e);
		}
	}

}
