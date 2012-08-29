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

        User user = (User) obj;
        boolean allow = false;

        for (Location location : user.getActiveLocations()) {
            if (location.getLocationUKey() == WebConstants.HEAD_OFFICE_UKEY) {
                allow = true;
            }
        }
        if (allow) {
            invocation.getInvocationContext().setSession(session);
        } else {
            logger.debug("User not authorised to access : {} - {}", actionName, allow);
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
