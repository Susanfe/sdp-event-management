package ch.epfl.sweng.eventmanager.repository.data;

/**
 * @author Louis Vialar
 */
public final class FacebookPost extends SocialNetworkPost<Feed> {
    private static final int VIEW_TYPE = 3;

    public FacebookPost(Feed facebook) {
        super(facebook.getTime().getTime(), facebook);
    }

    @Override
    public int getItemType() {
        return VIEW_TYPE;
    }
}
