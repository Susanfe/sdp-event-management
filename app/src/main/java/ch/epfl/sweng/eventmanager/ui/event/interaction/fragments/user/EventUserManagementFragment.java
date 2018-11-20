package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.AbstractShowcaseFragment;
import ch.epfl.sweng.eventmanager.users.Role;

/**
 * Allows an administrator a manage the users allowed to work with this event (= access to admin
 * features).
 */
public class EventUserManagementFragment extends AbstractShowcaseFragment {
    private static final String TAG = "UserManagement";

    private RecyclerView mUserList;
    private UserListAdapter mUserListAdapter;
    private EditText mAddUserEmailField;
    private Spinner mAddUserSpinner;
    private Button mAddUserButton;
    private ProgressBar mAddUserProgressbar;

    public EventUserManagementFragment() {
        // Required empty public constructor
        super(R.layout.fragment_event_user_management);
    }

    @Override
    public void onResume() {
        super.onResume();

        model.getEvent().observe(this, ev -> {
            if (ev == null) {
                Log.e(TAG, "Got null model from parent activity");
            }

            mUserListAdapter = new UserListAdapter(ev);
            mUserList.setAdapter(mUserListAdapter);

            // Set handler on addUser form
            mAddUserButton.setOnClickListener(v -> addUser(v, ev));

            return;
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Help text
        TextView helpText = view.findViewById(R.id.help_text);
        helpText.setText(R.string.user_management_help_text);

        // User lis header
        TextView left_header = view.findViewById(R.id.user_list_left_header);
        left_header.setText(getString(R.string.user_list_left_header));
        TextView right_header = view.findViewById(R.id.user_list_right_header);
        right_header.setText(getString(R.string.user_list_right_header));

        // User list
        mUserList = view.findViewById(R.id.user_list);
        mUserList.setHasFixedSize(true);
        RecyclerView.LayoutManager userListLayoutManager = new LinearLayoutManager(getActivity());
        mUserList.setLayoutManager(userListLayoutManager);

        // Add user/role form
        mAddUserEmailField = view.findViewById(R.id.add_user_mail_field);
        mAddUserEmailField.setHint(getString(R.string.email_field));

        mAddUserSpinner = view.findViewById(R.id.add_user_spinner);
        String[] roles = Role.asArrayOfString();
        ArrayAdapter<String> addUserSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, roles);
        addUserSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAddUserSpinner.setAdapter(addUserSpinnerAdapter);

        mAddUserButton = view.findViewById(R.id.add_user_button);
        mAddUserButton.setText(getString(R.string.add_button));

        mAddUserProgressbar = view.findViewById(R.id.add_user_progress_bar);
        setInProgressState(false);

        return view;
    }

    /**
     * Set the AddUser form in a 'working' state.
     * @param state true if a request is being processed, false otherwise
     */
    private void setInProgressState(boolean state) {
        mAddUserButton.setEnabled(!state);
        int visibility = View.INVISIBLE;
        if (state) visibility = View.VISIBLE;
        mAddUserProgressbar.setVisibility(visibility);
    }

    /**
     * Callback used by the AddUser form to add an user to the current event at a given role.
     * @param v
     * @param ev
     */
    public void addUser(View v, Event ev) {
        setInProgressState(true);
        String email = mAddUserEmailField.getText().toString();
        String role = mAddUserSpinner.getSelectedItem().toString().toLowerCase();
        addUser(email, ev.getId(), role)
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(Task<Boolean> task) {
                        Toast toast;
                        if (task.isSuccessful()) {
                            toast = Toast.makeText(getActivity(),
                                    getString(R.string.add_user_success), Toast.LENGTH_LONG);
                        } else {
                            toast = Toast.makeText(getActivity(),
                                    getString(R.string.add_user_failure), Toast.LENGTH_LONG);
                        }
                        toast.show();
                        setInProgressState(false);
                    }
                });
    }

    /**
     * Calls a dedicated FireBase Cloud Function allowing an event's administrator to add an user to
     * its event.
     *
     * FIXME: move out of this fragment?
     *
     * @param email email of the target user
     * @param eventId target event
     * @param role string representation role to be assigned to the target user
     * @return the related task
     */
    private Task<Boolean> addUser(String email, int eventId, String role) {
        // Prepare parameters for the Firebase Cloud Function
        Map<String, Object> data = new HashMap<>();
        data.put("eventId", eventId);
        data.put("userEmail", email);
        data.put("role", role);
        data.put("push", true);

        return FirebaseFunctions.getInstance()
                .getHttpsCallable("addUserToEvent")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, Boolean>() {
                    @Override
                    public Boolean then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        Boolean result = (Boolean) task.getResult().getData();
                        return result;
                    }
                });
    }
}
