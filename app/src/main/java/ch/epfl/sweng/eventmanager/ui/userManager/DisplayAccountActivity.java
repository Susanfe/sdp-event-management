package ch.epfl.sweng.eventmanager.ui.userManager;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ch.epfl.sweng.eventmanager.InMemorySession;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;

public class DisplayAccountActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_account);

        InMemorySession session = InMemorySession.getInstance();

        // We assume the user is logged in.
        TextView helpText = (TextView) findViewById(R.id.main_text);
        helpText.setText("Logged as: " + session.getUser().get().getEmail());


        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setText("Logout");
        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setText("Back");
    }

    public void openEventSelector(View view) {
        Intent intent = new Intent(this, EventPickingActivity.class);
        startActivity(intent);
    }

    public void logoutThenRedirectToEventSelector(View view) {
        InMemorySession session = InMemorySession.getInstance();
        session.logout();
        openEventSelector(view);
    }
}
