import static org.mockito.Mockito.*;
import server.LogEvent;
import server.Logger;
import server.ServerController;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.LinkedList;
import org.junit.jupiter.api.*;
import shared.Activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class TestLogger {

    ServerController sc;
    Logger l;
    PropertyChangeSupport changeSupport;

    @BeforeEach
    public void setup() {
        this.sc = mock(ServerController.class);
        this.l = new Logger(sc);
        this.changeSupport = new PropertyChangeSupport(sc);
        changeSupport.addPropertyChangeListener(l);
    }

    @AfterEach
    public void tearDown() {
        this.sc = null;
        this.l = null;
    }

    @Test
    public void testLogStart() {
        LocalDateTime start = null;
        LocalDateTime end = null;

        l.logServerStart();
        l.writeToFile();

        LinkedList<LogEvent> listAfter = l.searchLogs(start, end);
        assertEquals("Server started", listAfter.getFirst().getEvent());
    }

    @Test
    public void testSearchLog() {
        LocalDateTime start = null;
        LocalDateTime end = null;
        LinkedList list = l.searchLogs(start, end);
        assertEquals(true, list.getFirst() instanceof LogEvent);
    }

    @Test
    public void testPropertyChangeLogout() {
        changeSupport.firePropertyChange("User logged out: ", null, "TEST");
        LinkedList<LogEvent> listAfter = l.searchLogs(null, null);
        assertEquals("User logged out: TEST", listAfter.getFirst().getEvent());
    }

    @Test
    public void testPropertyChangeSendingActivity() {
        Activity act = new Activity("testActivity");
        changeSupport.firePropertyChange("Sending activity: ", act.getActivityName(), "TEST");
        LinkedList<LogEvent> listAfter = l.searchLogs(null, null);
        assertEquals("Sending activity: testActivity to user TEST", listAfter.getFirst().getEvent());
    }

    @Test
    public void testPropertyChangeLogin() {
        changeSupport.firePropertyChange("New login: ", null, null);
        LinkedList<LogEvent> listAfter = l.searchLogs(null, null);

        assertEquals("New login: null", listAfter.getFirst().getEvent());
    }

    @Test
    public void testPropertyChangeInterval() {
        changeSupport.firePropertyChange("User interval: ", "TEST", "15 min");
        LinkedList<LogEvent> listAfter = l.searchLogs(null, null);
        assertEquals("User TEST changed interval to 15 min", listAfter.getFirst().getEvent());
    }

    @Test
    public void testPropertyChangeActivityCompleted() {
        Activity act = new Activity("testActivity");
        changeSupport.firePropertyChange("Activity completed: ", "TEST", act.getActivityName());
        LinkedList<LogEvent> listAfter = l.searchLogs(null, null);
        assertEquals("User TEST completed testActivity", listAfter.getFirst().getEvent());
    }

    @Test
    public void testPropertyChangeActivityDelayed() {
        Activity act = new Activity("testActivity");
        changeSupport.firePropertyChange("Activity delayed: ", "TEST", act.getActivityName());
        LinkedList<LogEvent> listAfter = l.searchLogs(null, null);
        assertEquals("User TEST delayed testActivity", listAfter.getFirst().getEvent());
    }

    @Test
    public void testPropertyChangeActivityRequested() {
        changeSupport.firePropertyChange("User wants activity: ", null, "TEST");
        LinkedList<LogEvent> listAfter = l.searchLogs(null, null);
        assertEquals("User TEST asked for activity", listAfter.getFirst().getEvent());
    }
}
