package ch.epfl.sweng.eventmanager.repository.data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class EventLocationTest {
    private final String name = "TestLocation";
    private final Position position = new Position(1, 2);
    private final EventLocation location = new EventLocation(name, position);

    @Test
    public void getNameTest() throws Exception {
        assertEquals(name, location.getName());
    }

    @Test
    public void getPositionTest() throws Exception {
        assertEquals(position, location.getPosition());
    }
}