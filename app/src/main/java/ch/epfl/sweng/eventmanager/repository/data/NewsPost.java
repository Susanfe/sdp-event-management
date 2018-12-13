package ch.epfl.sweng.eventmanager.repository.data;

/**
 * @author Louis Vialar
 */
public class NewsPost extends SocialNetworkPost<News> {
    public static final int VIEW_TYPE = 2;

    public NewsPost(News post) {
        super(post.getDate(), post);
    }

    @Override
    public int getItemType() {
        return VIEW_TYPE;
    }
}
