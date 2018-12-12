package ch.epfl.sweng.eventmanager.repository.data;

import ch.epfl.sweng.eventmanager.repository.data.SocialNetworkPost;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.Date;

/**
 * @author Louis Vialar
 */
public class TwitterPost extends SocialNetworkPost<Tweet> {
    public static final int VIEW_TYPE = 1;

    public TwitterPost(Tweet post) {
        super(Date.parse(post.createdAt), post);
    }

    @Override
    public int getItemType() {
        return VIEW_TYPE;
    }
}
