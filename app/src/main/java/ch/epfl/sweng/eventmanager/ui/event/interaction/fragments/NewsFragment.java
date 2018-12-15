package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.*;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.NewsViewModel;
import ch.epfl.sweng.eventmanager.ui.user.LoginFacebookActivity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetBuilder;
import com.twitter.sdk.android.tweetui.CompactTweetView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Display an event's news feed.
 */
public class NewsFragment extends AbstractShowcaseFragment {
    private static String TAG = "NewsFragment";
    protected NewsViewModel model;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.news_empty_tv)
    TextView emptyListTextView;
    @BindView(R.id.go_facebook)
    Button facebookButton;

    private NewsAdapter newsAdapter;

    public NewsFragment() {
        super(R.layout.fragment_news);
    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view != null) ButterKnife.bind(this, view);

        // TODO handle null pointer exception
        recyclerView.setLayoutManager(new LinearLayoutManager(
                Objects.requireNonNull(view).getContext()));
        recyclerView.setHasFixedSize(true);

        facebookButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginFacebookActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        newsAdapter = new NewsAdapter();
        recyclerView.setAdapter(newsAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (model == null) {
            model = ViewModelProviders.of(requireActivity()).get(NewsViewModel.class);
        }

        LiveData<String> twitterName = Transformations.map(super.model.getEvent(), Event::getTwitterName);
        LiveData<String> facebookName = Transformations.map(super.model.getEvent(), Event::getFacebookName);

        this.model.getNews(twitterName, facebookName).observe(this, news -> {
            if (news != null && news.size() > 0) {
                emptyListTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                newsAdapter.setContent(news);
            } else {
                emptyListTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                newsAdapter.setContent(Collections.emptyList());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = ViewModelProviders.of(requireActivity()).get(NewsViewModel.class);
    }

    public static class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<SocialNetworkPost> news;

        NewsAdapter() {
            this.news = Collections.emptyList();
        }

        // Create new views (invoked by the layout manager)
        @Override
        @NonNull
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                          int viewType) {

            if (viewType == TwitterPost.VIEW_TYPE) {
                final Tweet tweet = new TweetBuilder().build();
                final CompactTweetView compactTweetView = new CompactTweetView(parent.getContext(), tweet);

                return new TweetViewHolder(compactTweetView);
            } else if(viewType == NewsPost.VIEW_TYPE){
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_news, parent, false);
                return new NewsViewHolder(v);
            } else {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.facebook_item_news, parent, false);
                return new FacebookViewHolder(v);
            }
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof NewsViewHolder)
                ((NewsViewHolder) holder).bind((News) news.get(position).getPost());
            else if (holder instanceof TweetViewHolder)
                ((CompactTweetView) ((TweetViewHolder) holder).itemView).setTweet((Tweet) news.get(position).getPost());
            else if (holder instanceof FacebookViewHolder)
                ((FacebookViewHolder) holder).bind((Feed) news.get(position).getPost());
        }

        @Override
        public int getItemViewType(int position) {
            return news.get(position).getItemType();
        }

        @Override
        public int getItemCount() {
            return news.size();
        }

        public void setContent(List<SocialNetworkPost> news) {
            this.news = news;

            Collections.sort(this.news);


            this.notifyDataSetChanged();
        }

        static final class FacebookViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.text_facebook_news_content)
            TextView content;
            @BindView(R.id.text_facebook_news_date)
            TextView date;

            FacebookViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            final void bind(Feed feed) {
                this.content.setText(feed.getContent());
                this.date.setText(feed.dateAsString());
            }
        }

        static final class TweetViewHolder extends RecyclerView.ViewHolder {
            TweetViewHolder(CompactTweetView itemView) {
                super(itemView);
            }
        }

        static final class NewsViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.text_news_content)
            TextView content;
            @BindView(R.id.text_news_date)
            TextView date;
            @BindView(R.id.text_news_title)
            TextView title;

            NewsViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            final void bind(News news) {
                this.content.setText(news.getContent());
                this.date.setText(news.dateAsString());
                this.title.setText(news.getTitle());
            }
        }
    }
}
