package ch.epfl.sweng.eventmanager.repository.data;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.Date;

/**
 * @author Louis Vialar
 */
public final class TweetWrapper extends SocialNetworkPost<Tweet> {
    public static final int VIEW_TYPE = 1;

    public TweetWrapper(Tweet post) {
        super(Date.parse(post.createdAt), post);
    }

    @Override
    public int getItemType() {
        return VIEW_TYPE;
    }
}
