import org.junit.jupiter.api.*;
import shared.Activity;
import shared.ActivityRegister;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class TestActivityRegister {

    ActivityRegister ar;

    @BeforeEach
    public void setup() {
        this.ar = new ActivityRegister("files/activities.txt");
    }

    @AfterEach
    public void tearDown() {
        this.ar = null;
    }

    @Test
    public void testGetActivityRegister() {
        Activity act = ar.getActivityRegister().getFirst();

        assertEquals(false, act.getActivityName().equals(""));
    }

}
