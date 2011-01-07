package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.StrutsStatics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * Disable caching of the pages
 */
public class CacheControlInterceptor extends AbstractInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CacheControlInterceptor.class);

    public String intercept(ActionInvocation invocation) throws Exception {

        logger.debug("Processing cache control tags");
        final ActionContext context = invocation.getInvocationContext();
        HttpServletResponse response = (HttpServletResponse) context.get(StrutsStatics.HTTP_RESPONSE);
        if (response != null) {
            response.setHeader("Cache-control", "no-cache, no-store");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "-1");
            logger.debug("Added cache control headers");
        }
        return invocation.invoke();
    }
}