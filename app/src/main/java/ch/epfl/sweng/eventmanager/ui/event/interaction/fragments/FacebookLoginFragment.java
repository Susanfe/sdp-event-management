package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FacebookLoginFragment extends AbstractShowcaseFragment {

    private CallbackManager callbackManager;
    @BindView(R.id.loginButtonFacebookFragment)
    LoginButton loginButton;

    public FacebookLoginFragment() {
        super(R.layout.fragment_facebook_login);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                goBackToNewsFragment();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();

    }

    private void goBackToNewsFragment() {
        ((EventShowcaseActivity)getActivity()).changeFragment(
                new NewsFragment(), true);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
