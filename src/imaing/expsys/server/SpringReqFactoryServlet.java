package imaing.expsys.server;

import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

/**
 * Wrapper around {@code RequestFactoryServlet} to enable injection of
 * custom {@code ServiceLayerDecorator}s through Spring.
 */
public class SpringReqFactoryServlet extends RequestFactoryServlet {
	private static final long serialVersionUID = 1L;

	public SpringReqFactoryServlet(ExceptionHandler exceptionHandler,
            ServiceLayerDecorator... serviceDecorators) {
        super(exceptionHandler, serviceDecorators);
    }
	
	public SpringReqFactoryServlet() {
        super(new DefaultExceptionHandler(), new SpringServiceLayerDecorator());
    }

}
