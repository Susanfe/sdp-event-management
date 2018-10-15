package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedScheduleItemDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

@Singleton
public class JoinedScheduleItemRepository extends AbstractEventRepository<JoinedScheduleItem, JoinedScheduleItemDao, UUID> {
    @Inject
    public JoinedScheduleItemRepository(JoinedScheduleItemDao joinedScheduleItemDao){
        super(joinedScheduleItemDao);
    }

    public LiveData<List<JoinedScheduleItem>> findByEventId(int id) {
        return dao.findByEventId(id);
    }
}
