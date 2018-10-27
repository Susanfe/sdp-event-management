package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.News;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.NewsViewModel;

import java.util.Collections;
import java.util.List;

public class NewsFragment extends AbstractShowcaseFragment {
    private static String TAG = "NewsFragment";
    protected NewsViewModel model;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.news_empty_tv)
    TextView emptyListTextView;
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

        this.model.getNews().observe(this, news -> {
            if (news != null && news.size() > 0) {
                emptyListTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                newsAdapter.setNews(news);
            } else {
                emptyListTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                newsAdapter.setNews(Collections.emptyList());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = ViewModelProviders.of(requireActivity()).get(NewsViewModel.class);
    }

    public static class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
        private List<News> news;

        public NewsAdapter() {
            this.news = Collections.emptyList();
        }

        // Create new views (invoked by the layout manager)
        @Override
        public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news, parent, false);
            return new NewsViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(NewsAdapter.NewsViewHolder holder, int position) {
            holder.bind(news.get(position));
        }

        @Override
        public int getItemCount() {
            return news.size();
        }

        public void setNews(List<News> news) {
            this.news = news;
        }

        final class NewsViewHolder extends RecyclerView.ViewHolder {
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
