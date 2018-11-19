package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetBuilder;
import com.twitter.sdk.android.tweetui.CompactTweetView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.News;
import ch.epfl.sweng.eventmanager.repository.data.NewsOrTweet;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.NewsViewModel;
import ch.epfl.sweng.eventmanager.users.Role;
import ch.epfl.sweng.eventmanager.users.Session;

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
    @BindView(R.id.news_create_button)
    Button newsCreateButton;
    private NewsAdapter newsAdapter;

    public NewsFragment() {
        super(R.layout.fragment_news);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);

        newsCreateButton.setOnClickListener(v -> {
            getParentActivity().changeFragment(new SendNewsFragment(), true);
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

        super.model.getEvent().observe(this, ev -> {
            if (Session.isClearedFor(Role.ADMIN, ev)) {
                newsCreateButton.setVisibility(View.VISIBLE);
            } else {
                newsCreateButton.setVisibility(View.GONE);
            }
        });


        LiveData<String> twitterName = Transformations.map(super.model.getEvent(), event -> event.getTwitterName());

        this.model.getNews(twitterName).observe(this, news -> {
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

        private List<NewsOrTweet> news;

        public NewsAdapter() {
            this.news = Collections.emptyList();
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

            if (viewType == NewsOrTweet.TYPE_TWEET) {
                final Tweet tweet = new TweetBuilder().build();
                final CompactTweetView compactTweetView = new CompactTweetView(parent.getContext(), tweet);

                return new TweetViewHolder(compactTweetView);
            } else {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_news, parent, false);
                return new NewsViewHolder(v);
            }
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof NewsViewHolder)
                ((NewsViewHolder) holder).bind(news.get(position).getNews());
            else if (holder instanceof TweetViewHolder)
                ((CompactTweetView) ((TweetViewHolder) holder).itemView).setTweet(news.get(position).getTweet());
        }

        @Override
        public int getItemViewType(int position) {
            return news.get(position).getItemType();
        }

        @Override
        public int getItemCount() {
            return news.size();
        }

        public void setContent(List<NewsOrTweet> news) {
            this.news = news;

            Collections.sort(this.news);


            this.notifyDataSetChanged();
        }

        protected static final class TweetViewHolder extends RecyclerView.ViewHolder {
            public TweetViewHolder(CompactTweetView itemView) {
                super(itemView);
            }
        }

        protected static final class NewsViewHolder extends RecyclerView.ViewHolder {
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
