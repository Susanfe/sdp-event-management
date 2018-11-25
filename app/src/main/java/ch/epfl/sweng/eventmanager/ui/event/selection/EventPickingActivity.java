package ch.epfl.sweng.eventmanager.ui.event.selection;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.ui.user.DisplayAccountActivity;
import ch.epfl.sweng.eventmanager.ui.user.LoginActivity;
import ch.epfl.sweng.eventmanager.users.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import dagger.android.AndroidInjection;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator;

import javax.inject.Inject;

public class EventPickingActivity extends AppCompatActivity {
    public static final String SELECTED_EVENT_ID = "ch.epfl.sweng.SELECTED_EVENT_ID";
    @Inject
    ViewModelFactory factory;
    @BindView(R.id.event_picking_joined_events_text)
    TextView joinedHelpText;
    @BindView(R.id.event_picking_bottom_sheet_text)
    TextView bottomSheetText;
    @BindView(R.id.event_picking_help_text)
    TextView helpText;
    @BindView(R.id.no_more_events)
    TextView noMoreEventsText;
    @BindView(R.id.joined_events_list)
    RecyclerView joinedEventsList;
    @BindView(R.id.not_joined_event_list)
    RecyclerView eventList;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.event_linear_layout)
    CoordinatorLayout content;
    @BindView(R.id.event_picking_list_layout)
    LinearLayout layoutBottomSheet;
    @BindView(R.id.event_picking_bottom_sheet_arrow_up)
    ImageView arrowUp;
    @BindView(R.id.event_picking_bottom_sheet_arrow_down)
    ImageView arrowDown;
    @BindView(R.id.event_picking_darken_background)
    FrameLayout darken_background;
    @BindView(R.id.event_picking_toolbar)
    Toolbar toolbar;

    private Boolean doubleBackToExitPressedOnce = false;
    private EventPickingModel model;
    private BottomSheetBehavior bottomSheetBehavior;
    private EventListAdapter eventsAdapter;
    private EventListAdapter joinedEventsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_event_picking);
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        this.model = ViewModelProviders.of(this, factory).get(EventPickingModel.class);
        this.model.init();
        ButterKnife.bind(this);
        setupObservers();

        content.setVisibility(View.GONE);
        layoutBottomSheet.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.ic_launcher_foreground);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //BottomSheet
        setupBottomSheet();

        setupAdapters();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    /**
     * Setup lists layout and adapters
     */
    private void setupAdapters() {
        LinearLayoutManager eventListLayoutManager = new LinearLayoutManager(this);
        eventList.setLayoutManager(eventListLayoutManager);
        eventsAdapter = EventListAdapter.newInstance(EventListAdapter.ItemType.Event);
        setupRecyclerView(eventList, eventsAdapter, new OvershootInRightAnimator());
        joinedEventsAdapter = EventListAdapter.newInstance(EventListAdapter.ItemType.JoinedEvents);
        setupRecyclerView(joinedEventsList, joinedEventsAdapter, new LandingAnimator());
    }


    /**
     * Setup bottom sheet and related behavior
     */
    private void setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        arrowUp.setVisibility(View.INVISIBLE);
                        arrowDown.setVisibility(View.VISIBLE);
                        darken_background.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        arrowUp.setVisibility(View.VISIBLE);
                        arrowDown.setVisibility(View.INVISIBLE);
                        darken_background.setVisibility(View.INVISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        darken_background.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    private void openLoginOrAccountActivity() {
        Class nextActivity;
        if (Session.isLoggedIn()) {
            nextActivity = DisplayAccountActivity.class;
        } else {
            nextActivity = LoginActivity.class;
        }
        Intent intent = new Intent(this, nextActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setupRecyclerView(RecyclerView view, EventListAdapter adapter, RecyclerView.ItemAnimator animator) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        view.setLayoutManager(layoutManager);
        view.setAdapter(adapter);
        view.setItemAnimator(animator);
    }

    private void setupObservers() {
        this.model.getEventsPair().observe(this, list -> {
            if (list == null) {
                return;
            }
            eventsAdapter.update(list.getOtherEvents());
            joinedEventsAdapter.update(list.getJoinedEvents());
            progressBar.setVisibility(View.GONE);

            //once data is loaded
            helpText.setVisibility(View.VISIBLE);
            layoutBottomSheet.setVisibility(View.VISIBLE);
            layoutBottomSheet.setAlpha(0.95f);
            content.setVisibility(View.VISIBLE);

            if (list.getOtherEvents().isEmpty()) {
                eventList.setVisibility(View.GONE);
                noMoreEventsText.setVisibility(View.VISIBLE);
            } else {
                eventList.setVisibility(View.VISIBLE);
                noMoreEventsText.setVisibility(View.GONE);
            }
            if (list.getJoinedEvents().isEmpty()) {
                helpText.setText(getString(R.string.help_text_go_join_events));
                joinedHelpText.setVisibility(View.GONE);
            } else {
                helpText.setText(getString(R.string.help_text_activity_event_picking));
                joinedHelpText.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_picking, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.activity_login_login_button:
                openLoginOrAccountActivity();
                break;

            case R.id.logout_button:
                Session.logout();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (Session.isLoggedIn()) {
            menu.findItem(R.id.activity_login_login_button).setTitle(R.string.account_button);
            menu.findItem(R.id.logout_button).setVisible(true);
        } else {
            menu.findItem(R.id.activity_login_login_button).setTitle(R.string.login_button);
            menu.findItem(R.id.logout_button).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast toast = Toast.makeText(this, R.string.double_back_press_to_exit, Toast.LENGTH_SHORT);
        ((TextView) (toast.getView().findViewById(android.R.id.message))).setTextColor(ContextCompat.getColor(this,
                R.color.colorSecondary));
        toast.getView().getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN);
        toast.show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @OnClick(R.id.event_picking_bottom_sheet_text)
    void openOrCloseBottomSheet(View view) {
        switch (bottomSheetBehavior.getState()) {
            case BottomSheetBehavior.STATE_COLLAPSED:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            default:
        }
    }

    void joinEvent(Event event) {
        this.model.joinEvent(event);
        View contextView = findViewById(R.id.event_picking_main_layout);
        Snackbar.make(contextView, R.string.event_successfully_joined, Snackbar.LENGTH_SHORT).setAction(R.string.undo
                , v -> unjoinEvent(event)).show();
    }

    private void unjoinEvent(Event event) {
        this.model.unjoinEvent(event);
        View contextView = findViewById(R.id.event_picking_main_layout);
        Snackbar.make(contextView, R.string.event_successfully_unjoined, Snackbar.LENGTH_SHORT).show();
    }
}
