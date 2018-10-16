package ch.epfl.sweng.eventmanager.ui.schedule;

import android.test.mock.MockContext;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.vipulasri.timelineview.LineType;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Concert;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TimeLineAdapterTest {

    private List<Concert> l = Collections.EMPTY_LIST;
    private TimeLineAdapter a = new TimeLineAdapter();
   // private Concert c1 = new Concert();

    @Test
    public void getItemViewType() {
        a.setDataList(l);
        int actual = a.getItemViewType(0);
        int expected = LineType.ONLYONE;
        assertEquals(expected, actual);

        //l.add(c1);

        actual = a.getItemViewType(0);
        expected = LineType.BEGIN;
        assertEquals(expected, actual);

        actual = a.getItemViewType(-1);
        expected = LineType.NORMAL;
        assertEquals(expected, actual);

        actual = a.getItemViewType(1);
        expected = LineType.END;
        assertEquals(expected, actual);
    }

    @Test
    public void onCreateViewHolder() {
        MockContext context = new MockContext();

        // Mocking every action of the onCreateViewHolder method.
        ViewGroup parent = mock(ViewGroup.class);
        when(parent.getContext()).thenReturn(context);

        LayoutInflater inflater = mock(LayoutInflater.class);
        when(LayoutInflater.from(context)).thenReturn(inflater);

        int viewType = LineType.NORMAL;
        View v = mock(View.class);

        when(inflater.inflate(R.layout.item_timeline, parent)).thenReturn(v);

        TimeLineViewHolder actual = a.onCreateViewHolder(parent, viewType);
        TimeLineViewHolder expected = new TimeLineViewHolder(v, viewType);

        assertEquals(expected, actual);
        // If check passes, method acts as needed.
    }

    @Test
    public void onBindViewHolder() {

    }

    @Test
    public void getItemCount() {
    }
}