package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.*;

import java.util.Map;

import lk.rgd.crs.web.WebConstants;
import lk.rgd.common.api.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;

/**
 * This Struts Interceptor will be executed before all action calls to ensure the user has logged in and has priviledges to where he is going
  */
public class SecurityInterceptor extends AbstractInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    public String intercept(ActionInvocation invocation) throws Exception {
        Map session = invocation.getInvocationContext().getSession();
        if(session.get(WebConstants.SESSION_USER_BEAN) == null) {
            addActionError(invocation, "authenticate.required.message");
            return Action.LOGIN;
        }

        logger.debug("Setting Invocation Context Name : {}", invocation.getInvocationContext().getName());
        return invocation.invoke();
    }

    private void addActionError(ActionInvocation invocation, String message) {
        Object action = invocation.getAction();
        if (action instanceof ValidationAware) {
            ((ValidationAware) action).addActionError(((TextProvider)invocation.getAction()).getText(message));
        }
    }
}
