package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.CloudFunction;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.AbstractShowcaseFragment;
import ch.epfl.sweng.eventmanager.users.Role;
import dagger.android.support.AndroidSupportInjection;

import javax.inject.Inject;

/**
 * Allows an administrator a manage the users allowed to work with this event (= access to admin
 * features).
 */
public class EventUserManagementFragment extends AbstractShowcaseFragment {
    private static final String TAG = "UserManagement";

    private UserListAdapter mUserListAdapter;
    @BindView(R.id.user_management_user_list)
    RecyclerView mUserList;
    @BindView(R.id.user_managament_user_mail_field)
    EditText mAddUserEmailField;
    @BindView(R.id.user_management_add_user_spinner)
    Spinner mAddUserSpinner;
    @BindView(R.id.user_management_add_user_button)
    Button mAddUserButton;
    @BindView(R.id.user_management_progress_bar)
    ProgressBar mAddUserProgressbar;
    @BindView(R.id.user_management_help_text)
    TextView helpText;
    @BindView(R.id.user_management_user_list_left_header)
    TextView leftHeader;
    @BindView(R.id.user_management_user_list_right_header)
    TextView rightHeader;

    @Inject
    CloudFunction cloudFunction;

    public EventUserManagementFragment() {
        // Required empty public constructor
        super(R.layout.fragment_event_user_management);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        model.getEvent().observe(this, ev -> {
            if (ev == null) {
                Log.e(TAG, "Got null model from parent activity");
            }

            // TODO handle null pointer exception
            mUserListAdapter = new UserListAdapter(ev);
            mUserList.setAdapter(mUserListAdapter);

            // Set handler on addUser form
            mAddUserButton.setOnClickListener(v -> addUser(ev));

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        // Help text
        helpText.setText(R.string.user_management_help_text);

        // User lis header
        leftHeader.setText(getString(R.string.user_list_left_header));
        rightHeader.setText(getString(R.string.user_list_right_header));

        // User list
        mUserList.setHasFixedSize(true);
        RecyclerView.LayoutManager userListLayoutManager = new LinearLayoutManager(getActivity());
        mUserList.setLayoutManager(userListLayoutManager);

        // Add user/role form
        mAddUserEmailField.setHint(getString(R.string.email_field));

        String[] roles = Role.asArrayOfString();
        // TODO handle null pointer exception
        ArrayAdapter<String> addUserSpinnerAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, roles);
        addUserSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAddUserSpinner.setAdapter(addUserSpinnerAdapter);

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
     * @param ev Event to which to add the user
     */
    private void addUser(Event ev) {
        setInProgressState(true);
        String email = mAddUserEmailField.getText().toString();
        String role = mAddUserSpinner.getSelectedItem().toString().toLowerCase();
        cloudFunction.addUserToEvent(email, ev.getId(), role)
                .addOnCompleteListener(task -> {
                    String toastText;
                    if (task.isSuccessful()) toastText = getString(R.string.add_user_success);
                    else toastText = getString(R.string.add_user_failure);
                    Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
                    setInProgressState(false);
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

        cloudFunction.removeUserFromEvent(uidKey, ev.getId(), role.toString().toLowerCase())
                .addOnCompleteListener(task -> {
                    String toastText;
                    if (task.isSuccessful()) toastText = getString(R.string.remove_user_success);
                    else toastText = getString(R.string.remove_user_failure);
                    Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
                    setInProgressState(removeButton, false);
                });
    }
}
