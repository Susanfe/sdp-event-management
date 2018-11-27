package ch.epfl.sweng.eventmanager.repository.internal;

/**
 * @author Louis Vialar
 */
public interface TwitterWrapper {
    UserTimelineWrapper getUserTimeline(String screenName);
}
