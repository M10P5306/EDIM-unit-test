import client.*;
import org.junit.Before;
import org.junit.*;
import client.gui.*;
import shared.*;


import static org.mockito.Mockito.*;

public class TestFP11 {
    private ClientController clientController;
    private MainFrame mainFrame;

    @Before
    public void setUp() {
        clientController = new ClientController();
        mainFrame = mock(MainFrame.class);
    }

    @After
    public void tearDown(){
        clientController = null;
        mainFrame = null;
    }

    @Test
    public void testWelcomeMessageValid() {
        User testUser = new User("testUser");
        testUser.setUserType(UserType.SENDWELCOME);
        clientController.setMainFrame(mainFrame);
        clientController.receiveObject(testUser);
        verify(mainFrame).sendWelcomeMessage();
    }

    @Test
    public void testWelcomeMessageInvalid1() {
        User testUser = new User("testUser");
        testUser.setUserType(UserType.LOGIN);
        clientController.setMainFrame(mainFrame);
        clientController.receiveObject(testUser);
        verify(mainFrame, never()).sendWelcomeMessage();
    }


    @Test
    public void testWelcomeMessageInvalid2() {
        User testUser = new User("testUser");
        testUser.setUserType(UserType.OFFLINE);
        clientController.setMainFrame(mainFrame);
        clientController.receiveObject(testUser);
        verify(mainFrame, never()).sendWelcomeMessage();
    }
}
