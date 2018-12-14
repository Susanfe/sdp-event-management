package ch.epfl.sweng.eventmanager.repository.data;

import androidx.annotation.NonNull;

/**
 * @author Louis Vialar
 */
public abstract class SocialNetworkPost<T> implements Comparable<SocialNetworkPost> {
    private final long time;
    private final T post;

    public SocialNetworkPost(long time, T post) {
        this.time = time;
        this.post = post;
    }

    @Override
    public int compareTo(@NonNull SocialNetworkPost o) {
        return Long.compare(o.time, time);
    }

    public T getPost() {
        return post;
    }

    public abstract int getItemType();
}
