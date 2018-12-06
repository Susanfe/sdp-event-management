package ch.epfl.sweng.eventmanager.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventCreateActivity;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.users.Session;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

public class DisplayAccountActivity extends AppCompatActivity {

    @BindView(R.id.display_account_main_text)
    TextView helpText;
    @BindView(R.id.display_account_logout_button)
    Button logoutButton;
    @BindView(R.id.display_account_create_event_button)
    Button createButton;
    @BindString(R.string.display_account_activity_logged_as)
    String loggedAs;

    @Inject
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_account);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        loggedAs = String.format("%s %s", loggedAs, session.getCurrentUser().getEmail());
        logoutButton.setOnClickListener(this::logoutThenRedirectToEventSelector);
        createButton.setOnClickListener(this::redirectToEventCreator);
        helpText.setText(loggedAs);
    }

    private void logoutThenRedirectToEventSelector(View view) {
        session.logout();

        Toast toast = Toast.makeText(
                this, getString(R.string.logout_toast), Toast.LENGTH_SHORT
        );
        toast.show();

        Intent intent = new Intent(this, EventPickingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void redirectToEventCreator(View view) {
        Intent intent = new Intent(this, EventCreateActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
