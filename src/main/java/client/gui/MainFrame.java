package client.gui;

import client.ClientController;
import shared.Activity;
import shared.User;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Requirement: K.G.1
 * This class starts the Login window and then awaits the user's input, and finally starts the main GUI.
 *
 * @version 1.0
 * @author Carolin Nordstrom, Oscar Kareld, Chanon Borgstrom, Sofia Hallberg.
 */

public class MainFrame extends JFrame {
    private ClientController clientController;
    private MainPanel mainPanel;
    private String userName;


    /**
     * Receives a clientController object and opens call for the method which opens a GUI window.
     *
     * @param clientController The received ClientController object.
     */
    public MainFrame(ClientController clientController) {
        this.clientController = clientController;

        //Oscars test:
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        createLoginFrame();
    }

    /**
     * Creates the login window.
     */
    public void createLoginFrame() {
        LogInFrame loginFrame = new LogInFrame(this);
    }

    /**
     * Sets up the main frame for the GUI.
     */
    public void setupFrame() {
        setBounds(0, 0, 842, 420);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                clientController.logOut();
                createLoginFrame();
            }
        });
        setLayout(null);
        setTitle("EDIM");
        setResizable(true);            
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Creates the main panel for the GUI.
     */
    public void createMainPanel() {
        setupFrame();
        String status = "ONLINE";
        if (clientController.isOffline()) {
            status = "OFFLINE";
        }
        mainPanel = new MainPanel(this, userName, status);
        setContentPane(mainPanel);
    }

    /**
     * Sends the user name to the {@link ClientController}
     * @param userName the received user name
     */
    public void sendUser(String userName) {
        this.userName = userName;
        clientController.createUser(userName);
    }

    public boolean checkIfOffline() {
        return clientController.isOffline();
    }

    /**
     * Requirement: F.P.1.4
     *
     * Notifies the {@link ClientController} and closes the GUI window.
     */
    public void logOut() {
        clientController.logOut();
        dispose();
        new LogInFrame(this);
    }

    /**
     * Displays a new notification in the GUI.
     *
     * Requirement: F.A.1
     *
     * @param activity the received object.
     */
    public void showNotification(Activity activity) {
        mainPanel.getAppPanel().showNotification(activity);
    }

    /**
     * Requirement: F.S.2
     *
     * @param usersOnline
     */
    public void showUsersOnline(ArrayList<String> usersOnline){
        mainPanel.getAppPanel().displayOnlineList(usersOnline);
    }

    /**
     * Sends a received activity object to the {@link ClientController}.
     *
     * Requirement: F.A.1
     *
     * @param activity the received object.
     */
    public void sendActivityFromGUI(Activity activity) {
        clientController.sendActivityToCCC(activity);
    }

    /**
     * Sends a welcome message to a new user.
     *
     * Requirement: F.P.1.1
     */
    public void sendWelcomeMessage() {
        mainPanel.getAppPanel().showWelcomeMessage(userName);
    }

    /**
     * Sends a welcome message to a new offline user.
     *
     * Requirement: F.O.5
     */
    public void sendOfflineWelcomeMessage() {
        mainPanel.getAppPanel().showOfflineWelcomeMessage(userName);
    }

    /**
     * Sends the received interval from the GUI to the {@link ClientController}.
     *
     * Requirement: F.A.4
     *
     * @param interval the integer chosen by the user.
     */
    public void sendChosenInterval(int interval) {
        clientController.setInterval(interval);
    }

    public ClientController getClientController() {
        return clientController;
    }


    /**
     * Requirement: F.K.2
     */
    public void sendChallengeRequestToUser(String usernameToChallenge) {
        clientController.sendChallengeRequestToUser(usernameToChallenge);
    }

    /**
     * Requirement: F.K.2
     */
    public boolean showChallengeRequest(User user) {
        return mainPanel.showChallengeRequest(user);
    }

    /**
     * Requirement: F.K.2
     */
    public void disposeWaitingWindow() {
        mainPanel.disposeWaitingWindow();
    }

    /**
     * Requirement: F.K.2
     */
    public void showChallengeDeniedMessage() {
        mainPanel.showChallengeDeniedMessage();
    }

    public String getUserName() {
        return userName;
    }
}
