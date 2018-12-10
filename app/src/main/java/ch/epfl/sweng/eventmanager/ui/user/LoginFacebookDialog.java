package ch.epfl.sweng.eventmanager.ui.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.DeviceLoginButton;
import com.facebook.login.widget.LoginButton;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginFacebookDialog extends DialogFragment {

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_login_facebook, null);
        dialogBuilder.setView(dialogView);
        loginButton = dialogView.findViewById(R.id.loginButton);

        callbackManager = CallbackManager.Factory.create();


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("SUCESS", "passe");
                Intent intent = new Intent(getActivity(), EventPickingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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

/*
        builder.setPositiveButton("aaaaa", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ButterKnife.bind(this, inflater.inflate(R.layout.activity_login_facebook, null));
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

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
        });
*/
        return dialogBuilder.create();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
