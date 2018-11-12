package ch.epfl.sweng.eventmanager.ui.userManager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.users.Session;

public class DisplayAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_account);

        // We assume the user is logged in when the activity is opened.
        TextView helpText = findViewById(R.id.main_text);
        helpText.setText("Logged as: " + Session.getCurrentUser().getEmail());


        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setText(R.string.logout_button);
    }

    public void logoutThenRedirectToEventSelector(View view) {
        Session.logout();
        finish();
    }

}
