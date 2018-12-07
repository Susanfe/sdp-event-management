package ch.epfl.sweng.eventmanager.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;

public class LoginFacebookActivity extends AppCompatActivity {
    private static final String TAG = "LoginFacebookActivity";
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_facebook);


        loginButton = findViewById(R.id.loginButton);
        callbackManager = CallbackManager.Factory.create();



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                changeScreen();
            }

            @Override
            public void onCancel() {
                //FIXME: use ressource
                Toast.makeText(getApplicationContext(), "cancel login", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                //FIXME: use ressource
                Toast.makeText(getApplicationContext(), "error login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeScreen() {
        //FIXME: which screen to go ?
        Intent intent = new Intent(this, EventPickingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
