package ch.epfl.sweng.eventmanager.repository.data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class UserTest {
    private final int id = 1;
    private final String name = "user1";
    private final String mail = "user@test";
    private final String password = "secret";
    private final User user = new User(id, name, mail);


    @Test
    public void getIdTest() throws Exception {
        assertEquals(id, user.getId());
    }

    @Test
    public void getNameTest() throws Exception {
        assertEquals(name, user.getName());
    }

    @Test
    public void getEmailTest() throws Exception {
        assertEquals(mail, user.getEmail());
    }

    @Test
    public void checkPasswordTest() throws Exception {
        assertTrue(user.checkPassword(password));
        assertFalse(user.checkPassword(password + password));
    }
}