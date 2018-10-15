package ch.epfl.sweng.eventmanager.repository;

import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedScheduleItemDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class JoinedScheduleItemRepository extends AbstractEventRepository<JoinedScheduleItem, JoinedScheduleItemDao, UUID> {
    @Inject
    public JoinedScheduleItemRepository(JoinedScheduleItemDao joinedScheduleItemDao){
        super(joinedScheduleItemDao);
    }
}
