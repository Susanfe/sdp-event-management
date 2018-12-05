package ch.epfl.sweng.eventmanager.ui.event.selection;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
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
import ch.epfl.sweng.eventmanager.ui.user.SignUpActivity;
import ch.epfl.sweng.eventmanager.users.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.circularreveal.CircularRevealRelativeLayout;
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
    FrameLayout darkenBackground;
    @BindView(R.id.event_picking_toolbar)
    Toolbar toolbar;
    @BindView(R.id.event_picking_main_layout) // Parent layout
    CoordinatorLayout mainLayout;
    @BindView(R.id.event_picking_login_account) // Account/Login button
    ImageButton loginAccountButton;
    @BindView(R.id.event_picking_under_empty_list)
    AppCompatImageView empty_joined_list_image;

    // Login/Signup layout components
    @BindView(R.id.layout_login_signup_logged) // Logged UI
    LinearLayout loggedUI;
    @BindView(R.id.layout_login_signup_not_logged) // Logged out UI
    LinearLayout notLoggedUi;
    @BindView(R.id.layout_login_or_signup) // parent layout of login/account buttons
    CircularRevealRelativeLayout loginAccountUI;



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


        //BottomSheet
        setupBottomSheet();

        setupAdapters();

        onPrepareSignupLoginLayout();

    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        onPrepareSignupLoginLayout();
    }

    /**
     * Setup lists layout and adapters
     */
    private void setupAdapters() {
        LinearLayoutManager eventListLayoutManager = new LinearLayoutManager(this);
        eventList.setLayoutManager(eventListLayoutManager);
        eventsAdapter = EventListAdapter.newInstance(EventListAdapter.ItemType.Event, this);
        setupRecyclerView(eventList, eventsAdapter, new OvershootInRightAnimator());
        joinedEventsAdapter = EventListAdapter.newInstance(EventListAdapter.ItemType.JoinedEvents, this);
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
                        darkenBackground.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        arrowUp.setVisibility(View.VISIBLE);
                        arrowDown.setVisibility(View.INVISIBLE);
                        darkenBackground.setVisibility(View.INVISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        darkenBackground.setVisibility(View.VISIBLE);
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

    /**
     * Verifies wether the activity to launch is the Login or Account one, depending on the
     * current Session's status.
     */
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
                empty_joined_list_image.setVisibility(View.VISIBLE);
                helpText.setText(getString(R.string.help_text_go_join_events));
                joinedHelpText.setVisibility(View.GONE);
            } else {
                empty_joined_list_image.setVisibility(View.GONE);
                helpText.setText(getString(R.string.help_text_activity_event_picking));
                joinedHelpText.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * On click behavior for all the login/account related views
     * i.e. MyAccount/Signin button and all the buttons in the account/signin layout
     * @param v view clicked on
     */
    @OnClick({R.id.layout_login_signup_login_button,
            R.id.layout_login_signup_signup_button,
            R.id.layout_login_signup_account_button,
            R.id.layout_login_signup_logout_button,
            R.id.event_picking_login_account,
            R.id.layout_login_or_signup})
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.event_picking_login_account:
                revealCircular(false);
                break;
            case R.id.layout_login_signup_login_button:
                openLoginOrAccountActivity();
                break;

            case R.id.layout_login_signup_signup_button:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;

            case R.id.layout_login_signup_logout_button:
                Session.logout();
                loggedUI.setVisibility(View.GONE);
                notLoggedUi.setVisibility(View.VISIBLE);
                revealCircular(true);
                break;

            case R.id.layout_login_signup_account_button:
                openLoginOrAccountActivity();
                break;

            case R.id.layout_login_or_signup:
                onBackPressed();
                break;
        }
    }

    /**
     * Depending on Android's version on the device, the method launches an animation to reveal the
     * account/signin layout
     */
    public void revealCircular(boolean hide){
        int viewState = hide ? View.GONE : View.VISIBLE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int x = loginAccountButton.getRight();
            int y = loginAccountButton.getTop();

            int startRadius = 0;
            int endRadius = (int) Math.hypot(mainLayout.getWidth(), mainLayout.getHeight());

            Animator anim;

            // We want to hide the layout
            if (hide) {
                anim = ViewAnimationUtils.createCircularReveal(loginAccountUI, x, y, endRadius, startRadius);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loginAccountUI.setVisibility(View.GONE);
                    }

                    // Non redefined methods
                    @Override
                    public void onAnimationStart(Animator animation) {}
                    @Override
                    public void onAnimationCancel(Animator animation) {}
                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                    // -----

                });
            } else { // We want to reveal the layout
                anim = ViewAnimationUtils.createCircularReveal(loginAccountUI, x, y, startRadius, endRadius);
                loginAccountUI.setVisibility(viewState);
            }

            anim.start();
        } else {
            loginAccountUI.setVisibility(viewState);
        }
    }

    /**
     * Sets the account/signin layout according to the Session's state
     */
    public void onPrepareSignupLoginLayout() {
        loginAccountUI.setVisibility(View.GONE);
        if (Session.isLoggedIn()) {
            loggedUI.setVisibility(View.VISIBLE);
            notLoggedUi.setVisibility(View.GONE);
        } else {
            loggedUI.setVisibility(View.GONE);
            notLoggedUi.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        if (loginAccountUI.getVisibility()==View.VISIBLE){
            revealCircular(true);
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

    /**
     * Sets the state of the Event to joined or unjoined according to the boolean. True means join
     * and false means unjoin
     * @param event event to join or unjoin
     * @param join desired state of the event
     */
    void joinOrUnjoinEvent(Event event, boolean join) {
        Snackbar bar;

        if (join) {
            this.model.joinEvent(event);
            bar = Snackbar.make(mainLayout, R.string.event_successfully_joined, Snackbar.LENGTH_SHORT).setAction(R.string.undo
                    , v -> joinOrUnjoinEvent(event, false));
        } else {
            this.model.unjoinEvent(event);
            bar = Snackbar.make(mainLayout, R.string.event_successfully_unjoined, Snackbar.LENGTH_SHORT);
        }

        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) bar.getView();
        layout.setBackgroundResource(R.color.colorSecondary);

        TextView text = layout.findViewById(com.google.android.material.R.id.snackbar_text);
        text.setTextColor(getResources().getColor(R.color.colorPrimary));

        Button button = layout.findViewById(com.google.android.material.R.id.snackbar_action);
        button.setTextColor(getResources().getColor(R.color.colorPrimary));

        bar.show();
    }
}
