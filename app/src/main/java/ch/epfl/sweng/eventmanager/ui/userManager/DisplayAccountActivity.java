package ch.epfl.sweng.eventmanager.ui.userManager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.users.Session;

public class DisplayAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_account);

        // We assume the user is logged in when the activity is opened.
        TextView helpText = (TextView) findViewById(R.id.main_text);
        helpText.setText("Logged as: " + Session.getCurrentUser().getEmail());


        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setText(R.string.logout_button);
        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setText(R.string.back_button);
    }

    public void openEventSelector(View view) {
        Intent intent = new Intent(this, EventPickingActivity.class);
        startActivity(intent);
    }

    public void logoutThenRedirectToEventSelector(View view) {
        Session.logout();

        Toast toast = Toast.makeText(
                this, getString(R.string.logout_toast), Toast.LENGTH_SHORT
        );
        toast.show();

        openEventSelector(view);
    }
}
