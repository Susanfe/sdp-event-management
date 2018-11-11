package ch.epfl.sweng.eventmanager.ui.eventSelector;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.userManager.DisplayAccountActivity;
import ch.epfl.sweng.eventmanager.ui.userManager.LoginActivity;
import ch.epfl.sweng.eventmanager.userManagement.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;

public class EventPickingActivity extends AppCompatActivity {
    private EventPickingModel model;
    public static final String SELECTED_EVENT_ID = "ch.epfl.sweng.SELECTED_EVENT_ID";
    @Inject
    ViewModelFactory factory;

    @BindView(R.id.joined_help_text)
    TextView joinedHelpText;
    @BindView(R.id.not_joined_help_text)
    TextView notJoinedHelpText;
    @BindView(R.id.help_text)
    TextView helpText;
    @BindView(R.id.joined_events_list)
    RecyclerView joinedEvents;
    @BindView(R.id.not_joined_event_list)
    RecyclerView eventList;

    private void setupRecyclerView(RecyclerView view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        view.setLayoutManager(layoutManager);
        // Set an empty list adapter
        view.setAdapter(new EventListAdapter(Collections.emptyList()));
    }

    private void setupObservers() {
        this.model = ViewModelProviders.of(this, factory).get(EventPickingModel.class);
        this.model.init();
        this.model.getEventsPair().observe(this, list -> {
            if (list == null) {
                return;
            }

            eventList.setAdapter(new EventListAdapter(list.getOtherEvents()));
            joinedEvents.setAdapter(new EventListAdapter(list.getJoinedEvents()));

            if (!list.getJoinedEvents().isEmpty()) {
                if(list.getOtherEvents().isEmpty()) {
                    joinedHelpText.setVisibility(View.VISIBLE);
                    notJoinedHelpText.setVisibility(View.GONE);
                } else {
                    joinedHelpText.setVisibility(View.VISIBLE);
                    notJoinedHelpText.setVisibility(View.VISIBLE);
                }
            } else {
                joinedHelpText.setVisibility(View.GONE);
                notJoinedHelpText.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_picking);
        ButterKnife.bind(this);

        // Help text
        // Both invisible by default
        joinedHelpText.setVisibility(View.INVISIBLE);
        helpText.setTypeface(helpText.getTypeface(), Typeface.BOLD);
        helpText.setText(R.string.help_text_activity_event_picking);

        // Event list
        eventList.setHasFixedSize(true);
        LinearLayoutManager eventListLayoutManager = new LinearLayoutManager(this);
        eventList.setLayoutManager(eventListLayoutManager);

        notJoinedHelpText.setVisibility(View.INVISIBLE);

        // Event lists

        setupRecyclerView(eventList);
        setupRecyclerView(joinedEvents);

        setupObservers();

        // Login button
        Button loginButton = (Button) findViewById(R.id.login_button);
        if (Session.isLoggedIn()) {
            loginButton.setText(R.string.account_button);
        } else {
            loginButton.setText(R.string.login_button);
        }
    }

    public void openLoginOrAccountActivity(View view) {
       Class nextActivity;
       if (Session.isLoggedIn()) {
           nextActivity = DisplayAccountActivity.class;
       } else {
           nextActivity = LoginActivity.class;
       }
       Intent intent = new Intent(this, nextActivity);
       startActivity(intent);
    }
}
