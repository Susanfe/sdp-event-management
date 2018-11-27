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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.impl.FirebaseCloudFunction;
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

            mUserListAdapter = new UserListAdapter(this, ev);
            mUserList.setAdapter(mUserListAdapter);

            // Set handler on addUser form
            mAddUserButton.setOnClickListener(v -> addUser(ev));

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
        setInProgressState(mAddUserButton, false);

        return view;
    }

    /**
     * Set the AddUser form in a 'working' state.
     * @param button button to disable, or null
     * @param state true if a request is being processed, false otherwise
     */
    private void setInProgressState(Button button, boolean state) {
        button.setEnabled(!state);
        int visibility = View.INVISIBLE;
        if (state) visibility = View.VISIBLE;
        mAddUserProgressbar.setVisibility(visibility);
    }

    /**
     * Callback used by the AddUser form to add an user to the current event at a given role.
     * @param ev
     */
    public void addUser(Event ev) {
        setInProgressState(mAddUserButton, true);
        String email = mAddUserEmailField.getText().toString();
        String role = mAddUserSpinner.getSelectedItem().toString().toLowerCase();
        FirebaseCloudFunction.addUserToEvent(email, ev.getId(), role)
                .addOnCompleteListener(task -> {
                    String toastText;
                    if (task.isSuccessful()) toastText = getString(R.string.add_user_success);
                    else toastText = getString(R.string.add_user_failure);
                    Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
                    setInProgressState(mAddUserButton, false);
                });
    }

    /**
     * Callback used to remove an user from the current event at a given role.
     * @param v
     * @param ev
     */
    protected void removeUser(View v, Event ev, String uidKey, Role role) {
        Button removeButton = v.findViewById(R.id.remove_button);
        setInProgressState(removeButton, true);

        FirebaseCloudFunction.removeUserFromEvent(uidKey, ev.getId(), role.toString().toLowerCase())
                .addOnCompleteListener(task -> {
                    String toastText;
                    if (task.isSuccessful()) toastText = getString(R.string.remove_user_success);
                    else toastText = getString(R.string.remove_user_failure);
                    Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
                    setInProgressState(removeButton, false);
                });
    }
}
