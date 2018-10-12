package ch.epfl.sweng.eventmanager.repository.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "joined_schedule_activities")
public class JoinedScheduleActivity {


    public JoinedScheduleActivity(int uid) {
        this.uid = uid;
    }

    @PrimaryKey
    @ColumnInfo(name = "schedule_activity_id")
    private int uid;


    /* Getters and Setters */

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "JoinedScheduleActivity{" + "uid=" + uid + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinedScheduleActivity that = (JoinedScheduleActivity) o;
        return uid == that.uid;
    }

    @Override
    public int hashCode() {
        return uid;
    }
}


