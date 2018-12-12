package ch.epfl.sweng.eventmanager.notifications;

import android.content.Context;
import java.util.Objects;

/**
 * A notification strategy defines the scheduling of a notification depending on an {@param <Item>}
 */
abstract class NotificationStrategy<Item> {
    Context context;

    NotificationStrategy(Context context) {
        this.context = Objects.requireNonNull(context);
    }

    abstract void scheduleNotification(Item item);

    abstract void unscheduleNotification(Item item);
}
