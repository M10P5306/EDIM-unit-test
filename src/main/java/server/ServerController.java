package server;

import shared.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This class handles the logic of the in and out coming objects from the clients.
 *
 * @author Carolin Nordstrom, Oscar Kareld, Chanon Borgstrom, Sofia Hallberg.
 * @version 1.0
 */

public class ServerController {
    private HashMap<String, ConnectionStream> socketHashMap;
    private ReceiverServer receiverServer;
    private UserRegister userRegister;
    private ActivityRegister activityRegister;
    private Random rand;
    private String userFilePath = "files/users.dat";
    private Logger log;
    private PropertyChangeSupport changeSupport;
    private LoggerGUI loggerGUI;
    private ArrayList<String> usersOnline;

    public ServerController() {
        socketHashMap = new HashMap();
        userRegister = new UserRegister();
        usersOnline = new ArrayList<>();
        readUsers(userFilePath);
        activityRegister = new ActivityRegister("files/activities.txt");
        rand = new Random();
        this.changeSupport = new PropertyChangeSupport(this);
        log = new Logger(this);
        loggerGUI = new LoggerGUI(this);
        callSearchLogger(null, null);}
    /**
     * Constructs all the buffers and servers and HashMaps that is needed.
     *
     * @param port the received port number.
     */
    public ServerController(int port) {
        socketHashMap = new HashMap();
        receiverServer = new ReceiverServer(port, this);
        userRegister = new UserRegister();
        usersOnline = new ArrayList<>();
        readUsers(userFilePath);
        activityRegister = new ActivityRegister("files/activities.txt");
        rand = new Random();
        this.changeSupport = new PropertyChangeSupport(this);
        log = new Logger(this);
        loggerGUI = new LoggerGUI(this);
        callSearchLogger(null, null);
    }

    public void addListener(PropertyChangeListener pcl) {
        changeSupport.addPropertyChangeListener(pcl);
    }

    /**
     * Requirement: F.S.6.2
     */
    public LinkedList<LogEvent> callSearchLogger(LocalDateTime startTime, LocalDateTime endTime) {
        LinkedList<LogEvent> logs = log.searchLogs(startTime, endTime);
        ArrayList<String> formattedStrings = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (LogEvent log : logs) {
            formattedStrings.add(String.format("[%s] %s", log.getTime().format(formatter), log.getEvent()));
        }
        loggerGUI.listLog(formattedStrings.toArray());
        return logs;
    }

    /**
     * Opens a stream and writes the user objects to the stream and then creates a file.
     *
     * Requirement: F.P.1.1
     *
     * @param filename the name of the created file.
     */
    public synchronized void writeUsers(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            oos.writeInt(userRegister.getUserArrayList().size());
            for (User user : userRegister.getUserArrayList()) {
                oos.writeObject(user);
            }
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a text file from the files folder and adds them to the contacts array. Then sets the contacts array to oldContactList array.
     *
     * @param filename the read filename.
     */
    public void readUsers(String filename) {
        File newFile = new File(filename);
        if (newFile.length() != 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                int size = ois.readInt();
                for (int i = 0; i < size; i++) {
                    try {
                        User user = (User) ois.readObject();
                        userRegister.getUserHashMap().put(user.getUsername(), user);
                        userRegister.getUserArrayList().add(user);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Requirement: F.S.1
     * Checks whether the User who recently logged in is a new user or not.
     *
     * @param user the logged in User.
     * @return an updated User.
     */
    public User checkLoginUser(User user) {
        if (userRegister.getUserHashMap().size() != 0) {

            if (userRegister.getUserHashMap().containsKey(user.getUsername())) { //userRegister.getUserHashMap().get(user.getUserName()).getUserName().equals(user.getUserName())
                user = userRegister.getUserHashMap().get(user.getUsername());
                user.setUserType(UserType.SENDUSER);
            } else {
                user.setUserType(UserType.SENDWELCOME);
                userRegister.getUserHashMap().put(user.getUsername(), user);
                userRegister.getUserArrayList().add(user);
                writeUsers(userFilePath);
            }
        } else {
            user.setUserType(UserType.SENDWELCOME);
            userRegister.getUserHashMap().put(user.getUsername(), user);
            userRegister.getUserArrayList().add(user);
            writeUsers(userFilePath);
        }
        return user;
    }

    /**
     * Requirement: F.A.1, F.A.2, F.A.3
     * Receives a username and gets a User-object from the HashMap. If the user's activity has been delayed a delayed activity is sent to the sendBuffer,
     * else a new activity is sent to the client.
     *
     * @param username the received username.
     */
    public synchronized void sendActivity(String username) {
        int nbrOfActivities = activityRegister.getActivityRegister().size();
        int activityNbr = rand.nextInt(nbrOfActivities);
        Activity activityToSend = activityRegister.getActivityRegister().get(activityNbr);
        activityToSend.setActivityUser(username);

        socketHashMap.get(username).sendObject(activityToSend);
        System.out.println("Sending activity: " + activityToSend.getActivityName());  //TODO: Test - ta bort
        changeSupport.firePropertyChange("Sending activity: ", activityToSend.getActivityName(), username);
    }

    public synchronized void receiveObject(Object object) {
        if (object instanceof User) {
            System.out.println("CHECKPOINT 6");
            User user = (User) object;
            String userName = user.getUsername();
            UserType userType = user.getUserType();

            switch (userType) {
                case LOGIN:
                    User updatedUser = checkLoginUser(user);
                    changeSupport.firePropertyChange("New login: ", null, userName);
                    socketHashMap.get(userName).sendObject(updatedUser);
                    if (!usersOnline.contains(updatedUser.getUsername())) {
                        System.out.println("Added " + updatedUser.getUsername() + " to the onlinelist");
                        usersOnline.add(updatedUser.getUsername());
                    }

                    if (!usersOnline.isEmpty()) {
                        for (String key : socketHashMap.keySet()) {
                            boolean objectSent = socketHashMap.get(key).sendObject(usersOnline);
                            System.out.println("Objekt skickat: " + objectSent);
                            System.out.println("Login. Skickar uppdaterad (" + usersOnline.size() + " st) lista till: " + key); //TODO: Test - ta bort
                            System.out.println(usersOnline);  //TODO: Test - ta bort
                        }
                    }
                    break;
                case LOGOUT:
                    changeSupport.firePropertyChange("User logged out: ", null, userName);
                    writeUsers(userFilePath);
                    usersOnline.remove(userName);
                    System.out.println("Removed " + userName + " to the onlinelist");

                    if (!usersOnline.isEmpty()) {
                        for (String key : socketHashMap.keySet()) {
                            socketHashMap.get(key).sendObject(usersOnline);
                            System.out.println("LogOut. Skickar uppdaterad (" + usersOnline.size() + " st) lista till: " + key); //TODO: Test - ta bort
                            System.out.println(usersOnline);  //TODO: Test - ta bort
                        }
                    }
                    break;
                case SENDINTERVAL:
                    userRegister.updateUser(user);
                    if (user.getNotificationInterval() == 0) {
                        changeSupport.firePropertyChange("User interval: ", userName, user.getNotificationInterval() + " min");
                    }
                    else {
                        changeSupport.firePropertyChange("User interval: ", userName,  " want an activity now");
                    }
                    break;
                case WANTACTIVITY:
                    System.out.println("TEST CHECKPOINT");
                    changeSupport.firePropertyChange("User wants activity: ", null, userName);
                    sendActivity(userName);
                    break;
                case SENDCHALLENGEREQUEST:
                    System.out.println("CHECKPOINT 7");
                    System.out.println("User wants to send challenge request");
                    User userToSend = (User)object;
                    String usernameToChallenge = user.getUsernameToChallenge();
                    changeSupport.firePropertyChange("Challenge request sent: ", userToSend.getUsername(),  usernameToChallenge);
                    sendChallengeRequest(userToSend);
                    break;
                case SENDCHALLENGE:
                    changeSupport.firePropertyChange("Challenge sent: ", user.getUsername(),  user.getUsernameToChallenge());
                    sendChallengeToUsers(user);
                    break;
                case CHALLENGEDENIED:
                    sendChallengeDeniedToChallengingUser(user);
                    changeSupport.firePropertyChange("Challenge denied: ", user.getUsername(),  user.getUsernameToChallenge());
                    break;
            }
        } else if (object instanceof Activity) {
            Activity activity = (Activity) object;
            String username = activity.getActivityUser();

            System.out.println("#2 - activityToChallengedUser.getActivityUser: " + username);

            if (activity.isCompleted()) {
                changeSupport.firePropertyChange("Activity completed: ", username, activity.getActivityName());
            } else {
                changeSupport.firePropertyChange("Activity delayed: ", username, activity.getActivityName());
            }
        } else {
            System.out.println("CHECKPOINT 8");
        }

    }

    /**
     * Requirement: F.S.3
     */
    private void sendChallengeDeniedToChallengingUser(User user) {
        socketHashMap.get(user.getUsername()).sendObject(user);
    }

    /**
     * Requirement: F.S.3
     */
    private void sendChallengeToUsers(User user) {
        String client1 = user.getUsername();
        String client2 = user.getUsernameToChallenge();

        System.out.println("+++ client1: " + client1);
        System.out.println("+++ client2: " + client2);

        int nbrOfActivities = activityRegister.getActivityRegister().size();
        int activityNbr = rand.nextInt(nbrOfActivities);

        Activity activity1 = activityRegister.getActivityRegister().get(activityNbr);
        activity1.setActivityUser(client1);
        activity1.setIsChallenge(true);

        socketHashMap.get(client1).sendObject(activity1);

        Activity activity2 = activityRegister.getActivityRegister().get(activityNbr);
        activity2.setActivityUser(client2);
        activity2.setIsChallenge(true);

        socketHashMap.get(client2).sendObject(activity2);

        changeSupport.firePropertyChange("Sending activity: ", activity1.getActivityName(), client1);
        changeSupport.firePropertyChange("Sending activity: ", activity2.getActivityName(), client2);
    }


    public void addNewConnection(String userName, ConnectionStream connectionStream) {
        socketHashMap.put(userName, connectionStream);
    }

    /**
     * Receives the socket with the username and closes it and removes the socket from the HashMap.
     *
     * @param username
     */
    public synchronized void logOutUser(String username) {
        socketHashMap.remove(username);
        System.out.println("User logged out: " + username);  //TODO: Test - ta bort
    }

    /**
     * Requirement: F.S.3
     */
    public void sendChallengeRequest(User userToSend){
        socketHashMap.get(userToSend.getUsernameToChallenge()).sendObject(userToSend);
        System.out.println("Sending challenge request from: " + userToSend.getUsername() + " to: " + userToSend.getUsernameToChallenge());
    }

    public PropertyChangeSupport getChangeSupport() {
        return this.changeSupport;
    }

    public HashMap getSocketHashMap() {
        return socketHashMap;
    }
}
