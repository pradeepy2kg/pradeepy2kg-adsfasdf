package lk.rgd.crs.web.util;

import lk.rgd.crs.web.WebConstants;
import lk.rgd.AppConstants;

import java.util.Hashtable;

/**
 * @author Duminda Dharmakeerthi
 * @author indunil Moremada
 * @author Chathranga
 * @author Amith
 */
public class LoginBD {
    private static Hashtable<String, String> users = new Hashtable<String, String>();

    /**
     * Checks for the hash table of the user information and creates a hash table
     * according to the user information (if the hash table is empty).
     */
    public LoginBD() {
        if (users.isEmpty())
            this.createHashTable();
    }

    /**
     * Returns True if the login process is successful. False otherwise.
     *
     * @param userName The username of the user.
     * @param password Password of the user.
     * @return True if successful. False otherwise.
     */
    public boolean login(String userName, String password) {
        if (users.containsKey(userName)) {
            String info = users.get(userName);
            if (info.substring(0, info.indexOf(',')).equals(password))
                return true;
        }
        return false;
    }

    /**
     * Returns a String that indicate the preffered language of the user.
     *
     * @param userName The username of the logged user.
     * @return String that indicate the language of the user.
     */
    public String getLanguage(String userName) {
        if (users.containsKey(userName)) {
            String info = users.get(userName);
            return info.substring(info.indexOf(',') + 1);
        }
        return AppConstants.ENGLISH;
    }

    /**
     * Creates a simple hashtable to store information of the users.     *
     */
    private void createHashTable() {
        users.put("ashoka", "ashoka,si");
        users.put("asanka", "asanka,si");
        users.put("duminda", "duminda,si");
        users.put("indunil", "indunil,si");
        users.put("amith", "amith,ta");
        users.put("chathuranga", "chathuranga,en");
    }
}
