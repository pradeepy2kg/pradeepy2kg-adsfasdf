package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.*;

import java.util.Map;

import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.Link;
import lk.rgd.common.api.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Struts Interceptor will be executed before all action calls to ensure the user has logged in and has privileges to where he is going
 */
public class SecurityInterceptor extends AbstractInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    public String intercept(ActionInvocation invocation) throws Exception {
        Map session = invocation.getInvocationContext().getSession();
        Object obj = session.get(WebConstants.SESSION_USER_BEAN);
        String actionName = invocation.getInvocationContext().getName() + ".do";
        if (obj == null) {
            logger.debug("User not logged in. : {}", actionName);
            addActionError(invocation, "authenticate.required.message");
            return Action.LOGIN;
        }

        Map<String, Map> map = (Map<String, Map>) session.get(WebConstants.SESSION_USER_MENUE_LIST);
        
        User user = (User) obj;
        boolean found = false;
        String cat = null;
        int key = 0;
        for (Map.Entry<String, Map> category : map.entrySet()) {
            cat = category.getKey();
            Map<Integer, Link> links = (Map<Integer, Link>) category.getValue();
            Link link = links.get(actionName);
            if (link != null) {
                key = link.getPermissionKey();
                found = true;
                break;
            }    
        }

        if (found && user.isAuthorized(key)) {
            session.put(WebConstants.SESSION_REQUEST_CONTEXT, cat);         // for the menu to expand
            invocation.getInvocationContext().setSession(session);
        } else {
            logger.debug("User not authorised to access : {} - {}", actionName, found);
            addActionError(invocation, "permission.notavailalbe.message");
            return Action.NONE;
        }

        return invocation.invoke();
    }

    private void addActionError(ActionInvocation invocation, String message) {
        Object action = invocation.getAction();
        if (action instanceof ValidationAware) {
            ((ValidationAware) action).addActionError(((TextProvider) invocation.getAction()).getText(message));
        }
    }
}
