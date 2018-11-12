package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.repository.data.News;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.AbstractShowcaseFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.NewsFragment;
import dagger.android.support.AndroidSupportInjection;

import javax.inject.Inject;

public class SendNewsFragment extends AbstractShowcaseFragment {
    private static String TAG = "SendNewsFragment";

    @Inject
    protected NewsRepository repository;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.send)
    Button send;

    public SendNewsFragment() {
        super(R.layout.fragment_send_news);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);

        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        ButterKnife.bind(this, view);

        send.setOnClickListener(l -> {
            int id = getParentActivity().getEventID();

            News news = new News(title.getText().toString(), System.currentTimeMillis(), content.getText().toString());

            send.setClickable(false);

            repository.publishNews(id, news).addOnSuccessListener(e -> {
                getParentActivity().changeFragment(new NewsFragment(), true);
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), R.string.send_news_failed, Toast.LENGTH_LONG).show();

                send.setClickable(true);
            });
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
