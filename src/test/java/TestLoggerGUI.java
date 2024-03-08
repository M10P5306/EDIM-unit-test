import org.junit.jupiter.api.*;
import server.LoggerGUI;
import server.ServerController;
import shared.User;

import javax.swing.*;

import java.beans.PropertyChangeSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestLoggerGUI {

    LoggerGUI lg;
    ServerController sc;
    PropertyChangeSupport changeSupport;


    @BeforeEach
    public void setup() {
        sc = mock(ServerController.class);
        lg = new LoggerGUI(sc);
        this.changeSupport = new PropertyChangeSupport(sc);
        changeSupport.addPropertyChangeListener(lg);
    }

    @AfterEach
    public void tearDown() {
        sc = null;
        lg = null;
    }

    @Test
    public void testPropertyChange() {
        DefaultListModel dlm = lg.getDefaultListModel();
        User u = new User("TEST");
        changeSupport.firePropertyChange("New login: ", null, u.getUsername());
        assertEquals("TEST", dlm.get(0));
        changeSupport.firePropertyChange("User logged out: ", null, u.getUsername());
        assertEquals(0, dlm.size());
    }
}
