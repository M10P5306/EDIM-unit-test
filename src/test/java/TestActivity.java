import org.junit.jupiter.api.*;
import shared.Activity;
import javax.swing.*;
import java.util.Calendar;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class TestActivity {

    Activity act;

    @BeforeEach
    public void setup() {
        this.act = new Activity();
    }

    @AfterEach
    public void tearDown() {
        this.act = null;
    }

    @Test
    public void testSetName() {
        assertEquals(null, act.getActivityName());
        act.setActivityName("testActivity");
        assertEquals("testActivity", act.getActivityName());
    }

    @Test
    public void testSetActivityUser() {
        assertEquals(null, act.getActivityUser());
        act.setActivityUser("TEST");
        assertEquals("TEST", act.getActivityUser());
    }

    @Test
    public void testCreateActivityImage() {
        assertEquals(false, act.getActivityImage() != null);
        act.createActivityImage("imagesServer/knaboj.jpg");
        assertEquals(true, act.getActivityImage() != null);
    }

    @Test
    public void testSetActivityImage() {
        assertEquals(false, act.getActivityImage() != null);
        act.setActivityImage(new ImageIcon("imagesServer/knaboj.jpg"));
        assertEquals(true, act.getActivityImage() != null);
    }

    @Test
    public void testCompleted() {
        assertEquals(false, act.isCompleted());
        act.setCompleted(true);
        assertEquals(true, act.isCompleted());
    }

    @Test
    public void testSetActivityInstruction() {
        assertEquals(null, act.getActivityInstruction());
        act.setActivityInstruction("testInstruction");
        assertEquals("testInstruction", act.getActivityInstruction());
    }

    @Test
    public void testSetActivityName() {
        assertEquals(null, act.getActivityName());
        act.setActivityName("testName");
        assertEquals("testName", act.getActivityName());
    }

    @Test
    public void testSetActivityInfo() {
        assertEquals(null, act.getActivityInfo());
        act.setActivityInfo("testInfo");
        assertEquals("testInfo", act.getActivityInfo());
    }
}
