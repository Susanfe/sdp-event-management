package ch.epfl.sweng.eventmanager.repository.data;

/**
 * @author Louis Vialar
 */
public final class FacebookPostWrapper extends SocialNetworkPost<Feed> {
    public static final int VIEW_TYPE = 3;

    public FacebookPostWrapper(Feed facebook) {
        super(facebook.getTime().getTime(), facebook);
    }

    @Override
    public int getItemType() {
        return VIEW_TYPE;
    }
}
