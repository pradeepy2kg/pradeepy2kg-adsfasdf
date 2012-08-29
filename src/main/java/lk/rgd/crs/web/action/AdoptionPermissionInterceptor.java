package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.Link;
import lk.rgd.crs.web.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Duminda Dharmakeerthi
 */
public class AdoptionPermissionInterceptor extends AbstractInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AdoptionPermissionInterceptor.class);

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
        boolean allow = false;
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

        for (Location location : user.getActiveLocations()) {
            if (location.getLocationUKey() == WebConstants.HEAD_OFFICE_UKEY) {
                allow = true;
            }
        }
        if (allow && found && user.isAuthorized(key)) {
            logger.debug("User Allowed. {} for {}", user.getUserId(), actionName);
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
