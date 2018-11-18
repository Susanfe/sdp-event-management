package ch.epfl.sweng.eventmanager.ui.user;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.users.Session;

public class DisplayAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_account);

        // We assume the user is logged in when the activity is opened.
        TextView helpText = findViewById(R.id.main_text);
        helpText.setText("Logged as: " + Session.getCurrentUser().getEmail());


        Button logoutButton = (Button) findViewById(R.id.logout_btn);
        logoutButton.setText(R.string.logout_button);
    }

    public void logoutThenRedirectToEventSelector(View view) {
        Session.logout();

        Toast toast = Toast.makeText(
                this, getString(R.string.logout_toast), Toast.LENGTH_SHORT
        );
        toast.show();

        Intent intent = new Intent(this,EventPickingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
