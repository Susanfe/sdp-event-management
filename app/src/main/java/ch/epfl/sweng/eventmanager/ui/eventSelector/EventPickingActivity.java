package ch.epfl.sweng.eventmanager.ui.eventSelector;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.gms.common.data.DataBufferObserver;
import dagger.android.AndroidInjection;

import javax.inject.Inject;
import java.util.Collections;

public class EventPickingActivity extends AppCompatActivity {
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
    private Menu menu;
    private EventPickingModel model;

    private void setupRecyclerView(RecyclerView view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        view.setLayoutManager(layoutManager);
        // Set an empty list adapter
        view.setAdapter(new EventListAdapter(Collections.emptyList()));
    }

    private void setupObservers() {
        this.model.getEventsPair().observe(this, list -> {
            if (list == null) {
                return;
            }
            eventList.setAdapter(new EventListAdapter(list.getOtherEvents()));
            joinedEvents.setAdapter(new EventListAdapter(list.getJoinedEvents()));

            //once data is loaded
            helpText.setVisibility(View.VISIBLE);

            if (!list.getJoinedEvents().isEmpty()) {
                if (list.getOtherEvents().isEmpty()) {
                    joinedEvents.setVisibility(View.VISIBLE);
                    eventList.setVisibility(View.GONE);
                    joinedHelpText.setVisibility(View.VISIBLE);
                    notJoinedHelpText.setVisibility(View.GONE);
                } else {
                    joinedEvents.setVisibility(View.VISIBLE);
                    eventList.setVisibility(View.VISIBLE);
                    joinedHelpText.setVisibility(View.VISIBLE);
                    notJoinedHelpText.setVisibility(View.VISIBLE);
                }
            } else {
                joinedEvents.setVisibility(View.GONE);
                eventList.setVisibility(View.VISIBLE);
                joinedHelpText.setVisibility(View.GONE);
                notJoinedHelpText.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_event_picking);
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        this.model = ViewModelProviders.of(this, factory).get(EventPickingModel.class);
        this.model.init();
        ButterKnife.bind(this);
        setupObservers();



        // Help text
        // Both invisible by default
        joinedHelpText.setVisibility(View.GONE);
        helpText.setTypeface(helpText.getTypeface(), Typeface.BOLD);
        helpText.setText(R.string.help_text_activity_event_picking);
        helpText.setVisibility(View.GONE);

        // Event list
        eventList.setHasFixedSize(true);
        LinearLayoutManager eventListLayoutManager = new LinearLayoutManager(this);
        eventList.setLayoutManager(eventListLayoutManager);
        notJoinedHelpText.setVisibility(View.GONE);

        // Event lists

        setupRecyclerView(eventList);
        eventList.setVisibility(View.GONE);
        setupRecyclerView(joinedEvents);
        eventList.setVisibility(View.GONE);
    }

    private void openLoginOrAccountActivity() {
        Class nextActivity;
        if (Session.isLoggedIn()) {
            nextActivity = DisplayAccountActivity.class;
        } else {
            nextActivity = LoginActivity.class;
        }
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_event_picking,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.login_button :
                openLoginOrAccountActivity();
                break;

            case R.id.logout_button :
                Session.logout();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (Session.isLoggedIn()) {
            menu.findItem(R.id.login_button).setTitle(R.string.account_button);
            menu.findItem(R.id.logout_button).setVisible(true);
        } else {
            menu.findItem(R.id.login_button).setTitle(R.string.login_button);
            menu.findItem(R.id.logout_button).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
