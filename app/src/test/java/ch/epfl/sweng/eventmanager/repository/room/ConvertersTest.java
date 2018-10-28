package ch.epfl.sweng.eventmanager.repository.room;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class ConvertersTest {
    private final String strUuid = "22a9bb84-3972-43ac-a2b0-4e80fcb72279";
    private final UUID uuid = UUID.fromString(strUuid);

    @Test
    public void fromStringTest() throws Exception {
        assertEquals(uuid, Converters.fromString(strUuid));
    }

    @Test
    public void toStringTest() throws Exception {
        assertEquals(strUuid, Converters.toString(uuid));
    }
}