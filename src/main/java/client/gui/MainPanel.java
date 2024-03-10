package client.gui;

import shared.Activity;
import shared.User;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Requirement: K.G.1
 */
public class MainPanel extends JPanel {

    private MainFrame mainFrame;
    private AppPanel appPanel;
    private String userName;
    private Color backGroundColor;
    private String title;

    public MainPanel(MainFrame mainFrame, String userName, String status) {
        this.mainFrame = mainFrame;
        this.userName = userName;
        backGroundColor = new Color(134, 144, 154, 145); //64, 87, 139
        setupPanel(status);
        appPanel = new AppPanel(this);
        showAppPanel();
    }

    public void setupPanel(String status) {
        setSize(new Dimension(819, 438));
        setBackground(backGroundColor);
        TitledBorder tb = BorderFactory.createTitledBorder("Välkommen, " + userName + " - " + status);
        if (status.equals("OFFLINE")) {
            tb.setTitleColor(Color.RED);
        } else {
            tb.setTitleColor(new Color(0x339F02));
        }
        title = tb.getTitle();
        setBorder(tb);
    }

    public void showAppPanel() {
        add(appPanel);
    }

    public AppPanel getAppPanel() {
        return appPanel;
    }

    /**
     * Requirement: F.P.1.4
     */
    public void logOut() {
        mainFrame.logOut();
    }

    /**
     * Requirement: F.A.1
     */
    public void sendActivityFromGUI(Activity activity) {
        mainFrame.sendActivityFromGUI(activity);
    }

    /**
     * Requirement: F.A.4
     */
    public void sendChosenInterval(int interval) {
        mainFrame.sendChosenInterval(interval);
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Requirement: F.K.2
     */
    public void sendChallengeRequestToUser(String usernameToChallenge) {
        mainFrame.sendChallengeRequestToUser(usernameToChallenge);
    }

    /**
     * Requirement: F.K.2
     */
    public boolean showChallengeRequest(User user) {
        return appPanel.showChallengeRequest(user);
    }

    /**
     * Requirement: F.K.2
     */
    public void disposeWaitingWindow() {
        appPanel.disposeWaitingWindow();
    }

    /**
     * Requirement: F.K.2
     */
    public void showChallengeDeniedMessage() {
        appPanel.showChallengeDeniedMessage();
    }


    public String getUserName() {
        return mainFrame.getUserName();
    }

    public String getTitle() {
        return this.title;
    }
}
