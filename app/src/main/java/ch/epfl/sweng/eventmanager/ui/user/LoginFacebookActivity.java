package ch.epfl.sweng.eventmanager.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.NewsFragment;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginFacebookActivity extends AppCompatActivity {
    public static final String SELECTED_EVENT_ID = "ch.epfl.sweng.SELECTED_EVENT_ID";
    private static final int TAG_NUMBER_FACEBOOK = 1;
    private static final String TAG = "LoginFacebookActivity";
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_facebook);

        Bundle bundle = getIntent().getExtras();
        eventId = bundle.getInt(getString(R.string.tag_eventID__from_news), -1);

        callbackManager = CallbackManager.Factory.create();

        loginButton = findViewById(R.id.loginButton);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                changeScreen();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.cancel_login_facebook), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_login_facebook), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeScreen() {
        Intent intent = new Intent(this, EventShowcaseActivity.class);
        intent.putExtra(getString(R.string.tag_facebook_login_back_button), TAG_NUMBER_FACEBOOK);
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        changeScreen();
    }

}
