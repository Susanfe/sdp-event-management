package ch.epfl.sweng.eventmanager.repository.internal;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;

/**
 * @author Louis Vialar
 */
public interface UserTimelineWrapper {
    void next(Long sinceId, Callback<TimelineResult<Tweet>> callback);
}
