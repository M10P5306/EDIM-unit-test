import org.junit.jupiter.api.*;
import shared.User;
import server.UserRegister;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUserRegister {

    UserRegister ur;
    User u;

    @BeforeEach
    public void setup() {
        this.ur = new UserRegister();
        this.u =  new User("hej");
    }

    @AfterEach
    public void tearDown() {
        this.ur = null;
        this.u = null;
    }

    @Test
    public void TestUpdateUser() {
        assertEquals(false, ur.getUserArrayList().contains(u));
        assertEquals(false, ur.getUserHashMap().containsKey(u.getUsername()));

        ur.updateUser(u);

        assertEquals(true, ur.getUserArrayList().contains(u));
        assertEquals(true, ur.getUserHashMap().containsKey(u.getUsername()));

        ur.updateUser(u);

        assertEquals(true, ur.getUserArrayList().contains(u));
        assertEquals(true, ur.getUserHashMap().containsKey(u.getUsername()));
    }

}
