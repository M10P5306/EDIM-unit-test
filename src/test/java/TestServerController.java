import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import server.ConnectionStream;
import server.ServerController;
import server.Logger;
import shared.User;
import shared.UserType;
import java.security.SecureRandom;

public class TestServerController {

    ServerController sc;

    SecureRandom sr;

    @BeforeEach
    public void setup() {
        this.sc = new ServerController();
        this.sr = new SecureRandom();
    }

    @AfterEach
    public void tearDown() {
        this.sc = null;
    }

    @Test
    public void testCheckLoginUser() {
        int randomNumber = sr.nextInt();
        String username = "Test" + randomNumber;
        User u = new User(username);
        assertEquals(UserType.SENDWELCOME,sc.checkLoginUser(u).getUserType());

        assertEquals(UserType.SENDUSER, sc.checkLoginUser(u).getUserType());
    }
    @Test
    public void testAddListender() {
        assertEquals(2, sc.getChangeSupport().getPropertyChangeListeners().length);
        Logger testLog = new Logger(sc);
        assertEquals(3, sc.getChangeSupport().getPropertyChangeListeners().length);
    }

    @Test
    public void testCallSearchLogger() {
        assertEquals(true,sc.callSearchLogger(null, null).size()>0);
    }

    @Test
    public void testAddNewConnection() {
        String testUser = "TEST";
        ConnectionStream cs = mock(ConnectionStream.class);
        int before = sc.getSocketHashMap().size();
        sc.addNewConnection(testUser, cs);
        int after = sc.getSocketHashMap().size();
        assertEquals(after, before+1);
    }

    @Test
    public void testLogOutUser() {
        String testUser = "TEST";
        ConnectionStream cs = mock(ConnectionStream.class);
        int before = sc.getSocketHashMap().size();
        sc.addNewConnection(testUser, cs);
        int after = sc.getSocketHashMap().size();
        assertEquals(after, before+1);
        sc.logOutUser(testUser);
        assertEquals(sc.getSocketHashMap().size(), before);
    }

}
