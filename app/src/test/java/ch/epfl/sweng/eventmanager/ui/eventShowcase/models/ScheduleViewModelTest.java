package ch.epfl.sweng.eventmanager.ui.eventShowcase.models;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import ch.epfl.sweng.eventmanager.repository.JoinedScheduleItemRepository;
import ch.epfl.sweng.eventmanager.repository.ScheduledItemRepository;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private ScheduledItemRepository scheduledItemRepository;
    @Mock
    private JoinedScheduleItemRepository joinedScheduleItemRepository;

    private ScheduleViewModel scheduleViewModel;
    private ArrayList<ScheduledItem> concerts = new ArrayList<>();
    private ArrayList<JoinedScheduleItem> joinedScheduleItems = new ArrayList<>();
    private MutableLiveData<List<ScheduledItem>> data = new MutableLiveData<>();
    private MutableLiveData<List<JoinedScheduleItem>> joinedData = new MutableLiveData<>();
    private ArrayList<ScheduledItem> polyv = new ArrayList<>();

    @Before
    public void setUp() {
        initMock();
        MockitoAnnotations.initMocks(this);
        Mockito.when(scheduledItemRepository.getConcerts(anyInt())).thenReturn(data);
        Mockito.when(joinedScheduleItemRepository.findByEventId(anyInt())).thenReturn(joinedData);
        scheduleViewModel = new ScheduleViewModel(scheduledItemRepository,joinedScheduleItemRepository);
        scheduleViewModel.init(anyInt());
        Observer obs = mock(Observer.class);
        scheduleViewModel.getJoinedScheduleItems().observeForever(obs);
        scheduleViewModel.getScheduleItemsByRoom().observeForever(obs);
    }

    @Test
    public void getScheduledItems() {
        assertEquals(data.getValue(), scheduleViewModel.getScheduledItems().getValue());
    }

    @Test
    public void getScheduleItemsByRoom() {
        System.out.println(scheduleViewModel.getScheduleItemsByRoom().getValue());
        assertEquals(polyv,scheduleViewModel.getScheduleItemsByRoom().getValue().get("Polyv"));
    }

    @Test
    public void isItemJoined() {
        assertTrue(scheduleViewModel.isItemJoined(joinedScheduleItems.get(0).getUid()));
    }

    @Test
    public void getJoinedScheduleItems() {
        assertEquals(joinedScheduleItems.get(0).getUid(),scheduleViewModel.getJoinedScheduleItems().getValue().get(0).getId());
    }

    @Test
    public void toggleMySchedule() {
        Context context = Mockito.mock(Context.class);
        scheduleViewModel.toggleMySchedule(concerts.get(1).getId(),context);
        assert(scheduleViewModel.getJoinedScheduleItems().getValue().contains(concerts.get(1)));
    }


    private void initMock() {
        ScheduledItem c1 = new ScheduledItem(new Date(2018, 16, 5), "Michael Jackson", "Pop", "Il est rescussité !", 3, UUID.randomUUID(), "Concert", "Polyv");
        ScheduledItem c2 = new ScheduledItem(new Date(2018, 16, 6), "Daft Punk", "Pop", "Les frenchies en force", 1.75, UUID.randomUUID(), "Concert", "Polyv");
        ScheduledItem c3 = new ScheduledItem(new Date(2018, 16, 7), "Donna Summer", "Disco", "Disco is love", 1.5, UUID.randomUUID(), "Concert", "CO");
        ScheduledItem c4 = new ScheduledItem(new Date(2018, 16, 11), "Booba", "Rap", "Fuck tout le monde", 2.6, UUID.randomUUID(), "Concert", "CE");
        ScheduledItem c5 = new ScheduledItem(new Date(2018, 16, 10), "Black M", "R&B", "Le retour...", 1, UUID.randomUUID(), "Concert", "CE");
        JoinedScheduleItem j = new JoinedScheduleItem(c2.getId(),2);
        concerts.add(c1);
        concerts.add(c2);
        concerts.add(c3);
        concerts.add(c4);
        concerts.add(c5);
        joinedScheduleItems.add(j);
        data.setValue(concerts);
        joinedData.setValue(joinedScheduleItems);
        polyv.add(c1);
        polyv.add(c2);
    }

}