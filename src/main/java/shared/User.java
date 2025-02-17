package shared;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * This class handles the information about a user-object.
 *
 * @version 1.0
 * @author Carolin Nordstrom, Oscar Kareld, Chanon Borgstrom, Sofia Hallberg.
 */

public class User implements Serializable {
    private static final long serialVersionUID = 42L; //489241266336029083L;//-6356381908430432467L;
    private LinkedList<Activity> completedActivities;
    private String username;
     private int notificationInterval = 45;
    private UserType userType;
    private Activity delayedActivity;
    private String usernameToChallenge;

    public User(String username) {
        this.username = username;
        completedActivities = new LinkedList<>();
    }

    public LinkedList<Activity> getCompletedActivities() {
        return completedActivities;
    }

    public void addActivityToList(Activity activity) {
        completedActivities.add(activity);
    }

    public void setCompletedActivities(LinkedList<Activity> completedActivities) {
        this.completedActivities = completedActivities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernameToChallenge() {
        return usernameToChallenge;
    }

    public void setUsernameToChallenge(String usernameToChallenge) {
        this.usernameToChallenge = usernameToChallenge;
    }

    public int getNotificationInterval() {
        return notificationInterval;
    }

    public void setNotificationInterval(int notificationInterval) {
        this.notificationInterval = notificationInterval;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Activity getDelayedActivity() {
        return delayedActivity;
    }

    /**
     * Requirement: F.A.2
     * @param delayedActivity
     */
    public void setDelayedActivity(Activity delayedActivity) {
        this.delayedActivity = delayedActivity;
    }
}
