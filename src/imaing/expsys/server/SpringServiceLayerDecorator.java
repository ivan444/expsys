package imaing.expsys.server;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.Locator;

public class SpringServiceLayerDecorator extends ServiceLayerDecorator {
	
	private ApplicationContext ctx;

	@Override
    public <T extends Locator<?, ?>> T createLocator(Class<T> clazz) {
		if (ctx == null) {
			HttpServletRequest request = RequestFactoryServlet.getThreadLocalRequest();
			this.ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		}
        return ctx.getBean(clazz);
    }

//	@Override
//	public void setApplicationContext(ApplicationContext ctx)
//			throws BeansException {
//		this.ctx = ctx;
//	}

//	@Override
//	public Object createServiceInstance(
//			Class<? extends RequestContext> requestContext) {
//		// TODO Auto-generated method stub
//		return super.createServiceInstance(requestContext);
//	}

//	@Override
//	public <T extends ServiceLocator> T createServiceLocator(Class<T> clazz) {
//		// TODO Auto-generated method stub
//		return super.createServiceLocator(clazz);
//	}

}
