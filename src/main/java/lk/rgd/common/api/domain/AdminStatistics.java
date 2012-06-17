package lk.rgd.common.api.domain;

import lk.rgd.crs.web.WebConstants;

import java.util.*;

/**
 * Represents statistics of the system
 *
 * @author Shan Chathuranga
 *         Date: Dec 3, 2010
 *         Time: 1:42:22 PM
 */
public class AdminStatistics {

    private List<User> userCreations;
    private List<User> passwordResetting;
    private List<User> inactiveUsers;

    /**
     * returns inactive user list filtered by given time range (duration)
     * duration should be WEEK, MONTH or YEAR according to the definitions of WebConstants
     *
     * @param duration the time range
     * @return List of inactive users
     */
    public List<User> getInactiveUsers(int duration) {
        List<User> temp = new ArrayList<User>();
        Iterator<User> it = inactiveUsers.iterator();
        while (it.hasNext()) {
            User user = it.next();
            if (getFilteredListByDayRange(user.getLifeCycleInfo().getCreatedTimestamp()) == duration) {
                temp.add(user);
            }
        }
        return temp;
    }

    /**
     * @return List of all the inactive users
     */
    public List<User> getAllInactiveUsers() {
        return inactiveUsers;
    }

    public void setInactiveUsers(List<User> inactiveUsers) {
        this.inactiveUsers = inactiveUsers;
    }

    /**
     * returns password changed users list filtered by given time range (duration)
     * duration should be WEEK, MONTH or YEAR according to the definitions of WebConstants
     *
     * @param duration the time range
     * @return List of password changed users
     */
    public List<User> getPasswordResetting(int duration) {
        List<User> temp = new ArrayList<User>();
        Iterator<User> it = passwordResetting.iterator();
        while (it.hasNext()) {
            User user = it.next();
            if (getFilteredListByDayRange(user.getLifeCycleInfo().getCreatedTimestamp()) == duration) {
                temp.add(user);
            }
        }
        return temp;
    }

    /**
     * @return List of all the password changed users
     */
    public List<User> getAllPasswordResetting() {
        return passwordResetting;
    }

    public void setPasswordResetting(List<User> passwordResetting) {
        this.passwordResetting = passwordResetting;
    }

    /**
     * returns newly created users list filtered by given time range (duration)
     * duration should be WEEK, MONTH or YEAR according to the definitions of WebConstants
     *
     * @param duration the time range
     * @return List of newly created users
     */
    public List<User> getUserCreation(int duration) {
        List<User> temp = new ArrayList<User>();
        Iterator<User> it = userCreations.iterator();
        while (it.hasNext()) {
            User user = it.next();
            if (getFilteredListByDayRange(user.getLifeCycleInfo().getCreatedTimestamp()) == duration) {
                temp.add(user);
            }
        }
        return temp;
    }

    /**
     * @return List of all the newly created users
     */
    public List<User> getAllUserCreations() {
        return userCreations;
    }

    public void setUserCreations(List<User> userCreations) {
        this.userCreations = userCreations;
    }

    /**
     * categorizes given date to appropriate time range according to the definitions of the WebConstants
     * returns time range
     *
     * @param oldDate users createdTimeStamp
     * @return time range
     */
    public int getFilteredListByDayRange(Date oldDate) {
        Date today = new Date();
        long timeRange = oldDate.getTime() - today.getTime();

        int days = (int) (timeRange / WebConstants.DAY_IN_MILLISECONDS);

        if (days >= 0 && days <= 7) {
            return WebConstants.WEEK;
        } else if (days >= 0 && days <= 31) {
            return WebConstants.MONTH;
        } else if (days >= 0 && days <= 365) {
            return WebConstants.YEAR;
        } else {
            return -1;
        }
    }
}
