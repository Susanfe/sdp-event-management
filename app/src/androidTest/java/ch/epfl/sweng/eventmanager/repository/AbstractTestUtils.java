package ch.epfl.sweng.eventmanager.repository;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import ch.epfl.sweng.eventmanager.repository.room.AppDataBase;
import ch.epfl.sweng.eventmanager.repository.room.daos.GenericDAO;
import org.junit.After;
import org.junit.Before;

import java.util.List;

public abstract class AbstractTestUtils<Dao extends GenericDAO, E> extends LiveDataTestUtil {
    protected Dao dao;
    protected AppDataBase mDb;

    abstract Dao getDao();

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDataBase.class).allowMainThreadQueries().build();
        dao = getDao();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    /**
     * Create a list of joinedEvents with following format JoinedEvent(id, "Event#id)
     *
     * @param numItems number of events to create
     * @return a list containing all events in numerical order (1 -> numEvents)
     */
    protected abstract List<E> createItems(int numItems);

    protected abstract List<E> insertItems(int numItems);

}
