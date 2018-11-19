package ch.epfl.sweng.eventmanager.ui.eventShowcase.models;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import ch.epfl.sweng.eventmanager.mock.repository.MockSpotRepository;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Spot;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(MockitoJUnitRunner.class)
public class SpotModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private EventRepository eventRepository;

    private SpotsModel spotsModel;
    private MockSpotRepository mockSpotRepository = new MockSpotRepository();
    private LiveData<List<Spot>> data;

    @Before
    public void setUp() {
        data = mockSpotRepository.getSpot(2);
        MockitoAnnotations.initMocks(this);
        Mockito.when(eventRepository.getSpots(anyInt())).thenReturn(data);
        spotsModel = new SpotsModel(eventRepository);
        spotsModel.init(anyInt());
    }

    @Test
    public void getSpots() {
        assertEquals(data.getValue(), spotsModel.getSpots().getValue());
    }
}
