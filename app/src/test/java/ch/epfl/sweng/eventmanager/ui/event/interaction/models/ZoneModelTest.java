package ch.epfl.sweng.eventmanager.ui.event.interaction.models;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.mock.repository.MockZoneRepository;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Zone;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(MockitoJUnitRunner.class)
public class ZoneModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private EventRepository eventRepository;

    private ZoneModel zoneModel;
    private MockZoneRepository mockZoneRepository = new MockZoneRepository();
    private LiveData<List<Zone>> data;

    @Before
    public void setUp() {
        data = mockZoneRepository.getZone(2);
        MockitoAnnotations.initMocks(this);
        Mockito.when(eventRepository.getZones(anyInt())).thenReturn(data);
        zoneModel = new ZoneModel(eventRepository);
        zoneModel.init(anyInt());
    }

    @Test
    public void getZones() {
        assertEquals(data.getValue(), zoneModel.getZone().getValue());
    }
}
