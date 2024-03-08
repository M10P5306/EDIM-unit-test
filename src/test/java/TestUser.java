import org.junit.jupiter.api.*;
import shared.Activity;
import shared.User;
import shared.UserType;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class TestUser {

    User u;

    @BeforeEach
    public void setup() {
        this.u = new User("TEST");
    }

    @AfterEach
    public void tearDown() {
        this.u = null;
    }

    @Test
    public void testSetUsername() {
        assertEquals("TEST", u.getUsername());
        u.setUsername("NewTestName");
        assertEquals("NewTestName", u.getUsername());
    }
    @Test
    public void testSetActivity() {
        assertEquals(null, u.getDelayedActivity());
        u.setDelayedActivity(new Activity("testActivity"));
        assertEquals("testActivity", u.getDelayedActivity().getActivityName());
    }

    @Test
    public void testNotificationInterval() {
        u.setNotificationInterval(15);
        assertEquals(15, u.getNotificationInterval());
    }

    @Test
    public void testCompletedActivities() {
        int before = u.getCompletedActivities().size();
        u.addActivityToList(new Activity());
        assertEquals(before+1,u.getCompletedActivities().size());
    }

    @Test
    public void testSetUserType() {
        assertEquals(null, u.getUserType());
        u.setUserType(UserType.OFFLINE);
        assertEquals(UserType.OFFLINE, u.getUserType());
    }



}
