package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.room.JoinedScheduleItemDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

@Singleton
public class JoinedScheduleItemRepository extends AbstractRoomRepository<JoinedScheduleItem, JoinedScheduleItemDao> {
    @Inject
    public JoinedScheduleItemRepository(JoinedScheduleItemDao joinedScheduleItemDao){
        super(joinedScheduleItemDao);
    }

    public LiveData<List<UUID>> findAllIds(){
        return dao.getAllIds();
    }

    public LiveData<JoinedScheduleItem> findById(UUID scheduleItemUUID) {
        return dao.findById(scheduleItemUUID);
    }

    public LiveData<List<JoinedScheduleItem>> findByEventId(int id) {
        return dao.findByEventId(id);
    }
}
