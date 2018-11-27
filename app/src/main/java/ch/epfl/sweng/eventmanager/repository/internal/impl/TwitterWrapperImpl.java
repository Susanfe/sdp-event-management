package ch.epfl.sweng.eventmanager.repository.internal.impl;

import ch.epfl.sweng.eventmanager.repository.internal.TwitterWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.UserTimelineWrapper;
import com.twitter.sdk.android.tweetui.UserTimeline;

import javax.inject.Inject;

/**
 * @author Louis Vialar
 */
public class TwitterWrapperImpl implements TwitterWrapper {
    @Inject
    public TwitterWrapperImpl() {
    }

    @Override
    public UserTimelineWrapper getUserTimeline(String screenName) {
        final UserTimeline timeline = new UserTimeline.Builder().screenName(screenName).build();
        return timeline::next;
    }
}
