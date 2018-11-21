package ch.epfl.sweng.eventmanager.repository.data;

import android.os.Parcel;
import android.os.Parcelable;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class EventTicketingConfigurationTest {
    private EventTicketingConfiguration configuration = new EventTicketingConfiguration("login", "config", "scan");

    @Test
    public void getters() {
        assertEquals("login", configuration.getLoginUrl());
        assertEquals("config", configuration.getConfigurationsUrl());
        assertEquals("scan", configuration.getScanUrl());
    }


    @Test
    public void describeContents() {
        assertEquals(0, configuration.describeContents());
    }

    @Test
    public void testEquals() {
        EventTicketingConfiguration alternate = new EventTicketingConfiguration("login", "config", "scan");

        assertEquals(configuration, alternate);
        assertEquals(configuration.hashCode(), alternate.hashCode());
    }
}