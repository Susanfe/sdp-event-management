package ch.epfl.sweng.eventmanager.users;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;

import ch.epfl.sweng.eventmanager.repository.data.User;

public interface InMemorySession {
    /**
     * Start the login process and execute action when finished.
     *
     * Note: the callback will be ignored if the activity is closed before the end of the login
     * process. This is intentional since the callback is supposed to be used to handle UI
     * animations.
     *
     * @param email email used to identify the account
     * @param password password to check against the account
     * @param context activity making the call
     * @param callback code to execute when the login process returns
     */
    void login(String email, String password, Activity context, OnCompleteListener callback);

    /**
     * Start the registration process and execute action when finished.
     *
     * Note: the callback will be ignored if the activity is closed before the end of the registration
     * process. This is intentional since the callback is supposed to be used to handle UI
     * animations.
     *
     * @param email email used to identify the account
     * @param password password to set
     * @param context activity making the call
     * @param callback code to execute when the registration process returns
     */
    void registerAndLogin(String email, String password, Activity context, OnCompleteListener callback);

    /**
     * Get you  the uid of any currently logged user.
     *
     * @return a String, or null
     */
    String getCurrentUserUid();

    /**
     * Log out any currently logged user.
     */
    void logout();

    /**
     * @return true if there currently is an user logged in
     */
    boolean isLoggedIn();
}
