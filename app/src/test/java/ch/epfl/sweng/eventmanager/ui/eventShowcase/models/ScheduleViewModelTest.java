package ch.epfl.sweng.eventmanager.ui.eventShowcase.models;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import ch.epfl.sweng.eventmanager.mock.repository.MockConcertRepository;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.JoinedScheduleItemRepository;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private EventRepository eventRepository;
    @Mock
    private JoinedScheduleItemRepository joinedScheduleItemRepository;

    private ScheduleViewModel scheduleViewModel;
    private MockConcertRepository mockConcertRepository = new MockConcertRepository();
    private ArrayList<JoinedScheduleItem> joinedScheduleItems = new ArrayList<>();
    private LiveData<List<ScheduledItem>> data;
    private MutableLiveData<List<JoinedScheduleItem>> joinedData = new MutableLiveData<>();
    private ArrayList<ScheduledItem> polyv = new ArrayList<>();


    @Before
    public void setUp() {
        initMock();
        MockitoAnnotations.initMocks(this);
        Mockito.when(eventRepository.getScheduledItems(anyInt())).thenReturn(data);
        Mockito.when(joinedScheduleItemRepository.findByEventId(anyInt())).thenReturn(joinedData);
        scheduleViewModel = new ScheduleViewModel(eventRepository, joinedScheduleItemRepository);
        scheduleViewModel.init(anyInt());
        Observer obs = mock(Observer.class);
        scheduleViewModel.getJoinedScheduleItems().observeForever(obs);
        scheduleViewModel.getScheduleItemsByRoom().observeForever(obs);
    }

    @Test
    public void initOnEmptyScheduledItems() {
        ScheduleViewModel scheduleViewModel = new ScheduleViewModel(eventRepository,joinedScheduleItemRepository);
        scheduleViewModel.init(2);
        assertEquals(data,scheduleViewModel.getScheduledItems());
    }

    @Test
    public void initOnNonEmptyScheduleItem() {
        ScheduleViewModel scheduleViewModel = new ScheduleViewModel(eventRepository,joinedScheduleItemRepository);
        scheduleViewModel.init(2);
        scheduleViewModel.init(2);
        assertEquals(data,scheduleViewModel.getScheduledItems());
    }


    @Test
    public void getScheduledItems() {
        assertEquals(data.getValue(), scheduleViewModel.getScheduledItems().getValue());
    }

    @Test
    public void getScheduleItemsByRoom() {
        System.out.println(scheduleViewModel.getScheduleItemsByRoom().getValue());
        assertEquals(polyv, scheduleViewModel.getScheduleItemsByRoom().getValue().get("Polyv"));
    }

    @Test
    public void getJoinedScheduleItems() {
        assertEquals(joinedScheduleItems.get(0).getUid(), scheduleViewModel.getJoinedScheduleItems().getValue().get(0).getId());
    }

    @Test
    public void  buildJoinedScheduledItemsListOnNull() {
        ScheduleViewModel scheduleViewModel = new ScheduleViewModel(null,null);
        assertEquals(null,scheduleViewModel.getJoinedScheduleItems());
    }

    @Test
    public void getScheduleItemForRoomOnNull() {
        ScheduleViewModel scheduleViewModel = new ScheduleViewModel(null,null);
        assertEquals(null,scheduleViewModel.getScheduleItemsByRoom());
    }

    @Test
    public void buildScheduleItemByRoomOnNull() {
        ScheduleViewModel scheduleViewModel = new ScheduleViewModel(null,null);
        assertEquals(null,scheduleViewModel.getScheduleItemsByRoom());
    }

    @Test
    public void toggleMySchedule() {
        Context context = Mockito.mock(Context.class);
        scheduleViewModel.toggleMySchedule(data.getValue().get(2).getId(), null);
        assert (scheduleViewModel.getJoinedScheduleItems().getValue().contains(data.getValue().get(2)));
    }

    private void initMock() {
        data = mockConcertRepository.getConcerts(2);
        JoinedScheduleItem j = new JoinedScheduleItem(data.getValue().get(2).getId(), 2);
        joinedScheduleItems.add(j);
        joinedData.setValue(joinedScheduleItems);
        polyv.add(mockConcertRepository.getConcerts(2).getValue().get(0));
        polyv.add(mockConcertRepository.getConcerts(2).getValue().get(1));
    }

}