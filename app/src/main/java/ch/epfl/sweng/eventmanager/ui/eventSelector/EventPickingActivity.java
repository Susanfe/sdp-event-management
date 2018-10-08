package ch.epfl.sweng.eventmanager.ui.eventSelector;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import ch.epfl.sweng.eventmanager.InMemorySession;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.userManager.DisplayAccountActivity;
import ch.epfl.sweng.eventmanager.ui.userManager.LoginActivity;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

public class EventPickingActivity extends AppCompatActivity {
    private EventPickingModel model;
    public static final String SELECTED_EVENT_ID = "ch.epfl.sweng.SELECTED_EVENT_ID";
    @Inject
    ViewModelFactory factory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_picking);

        InMemorySession session = InMemorySession.getInstance();

        // Help text
        TextView helpText = (TextView) findViewById(R.id.help_text);
        helpText.setTypeface(helpText.getTypeface(), Typeface.BOLD);
        helpText.setText("Please select an event to continue.");

        // Event list
        RecyclerView eventList = (RecyclerView) findViewById(R.id.event_list);
        eventList.setHasFixedSize(true);
        LinearLayoutManager eventListLayoutManager = new LinearLayoutManager(this);
        eventList.setLayoutManager(eventListLayoutManager);
        DividerItemDecoration eventListDividerItemDecoration = new DividerItemDecoration(
                eventList.getContext(),
                eventListLayoutManager.getOrientation()
        );
        eventList.addItemDecoration(eventListDividerItemDecoration);

        this.model = ViewModelProviders.of(this, factory).get(EventPickingModel.class);
        this.model.init();
        this.model.getEvents().observe(this, list -> {
            EventListAdapter eventListAdapter = new EventListAdapter(list);
            eventList.setAdapter(eventListAdapter);
        });

        // Login button
        Button loginButton = (Button) findViewById(R.id.login_button);
        if (session.isLoggedIn()) {
            loginButton.setText("My account »");
        } else {
            loginButton.setText("Sign in »");
        }
    }

    public void openLoginOrAccountActivity(View view) {
       InMemorySession session = InMemorySession.getInstance();
       Class nextActivity;
       if (session.isLoggedIn()) {
           nextActivity = DisplayAccountActivity.class;
       } else {
           nextActivity = LoginActivity.class;
       }
       Intent intent = new Intent(this, nextActivity);
       startActivity(intent);
    }
}
