package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.AbstractShowcaseFragment;

/**
 * Allows an administrator a manage the users allowed to work with this event (= access to admin
 * features).
 */
public class EventUserManagementFragment extends AbstractShowcaseFragment {
    private static final String TAG = "UserManagement";

    private RecyclerView mUserList;
    private UserListAdapter mUserListAdapter;

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

            return;
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        TextView helpText = view.findViewById(R.id.help_text);
        helpText.setText(R.string.user_management_help_text);

        mUserList = view.findViewById(R.id.user_list);
        mUserList.setHasFixedSize(true);
        RecyclerView.LayoutManager userListLayoutManager = new LinearLayoutManager(getActivity());
        mUserList.setLayoutManager(userListLayoutManager);

        return view;
    }
}
