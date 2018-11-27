package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.FirebaseBackedUser;
import ch.epfl.sweng.eventmanager.repository.data.User;
import ch.epfl.sweng.eventmanager.users.Role;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private Map<String, Pair<String, Role>> mUsers;
    public EventUserManagementFragment mContext;
    private Event mEvent;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @BindView(R.id.uid)
        public TextView userUid;
        @BindView(R.id.role)
        public TextView userRole;
        @BindView(R.id.remove_button)
        public Button removeButton;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserListAdapter(EventUserManagementFragment context, Event event) {
        this.mContext = context;
        this.mEvent = event;

        // Build our internal User to Roles representation
        Map<Role, List<Pair<String, String>>> raw = mEvent.getStructuredPermissions();

        mUsers = new HashMap<>();
        for (Role role : raw.keySet()) {
            for (Pair<String, String> pair : raw.get(role)) { // Pair of key to uid
                User user = new FirebaseBackedUser(pair.second);
                // FIXME: fetch email instead of Uid once our FirebaseBackedUser suports it
                mUsers.put(user.getUid(), new Pair(pair.first, role));
            }
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<String> index = new ArrayList<String>(mUsers.keySet());
        String uid = index.get(position);
        holder.userUid.setText(uid);
        holder.userRole.setText(mUsers.get(uid).second.toString());

        holder.removeButton.setOnClickListener(
                v -> mContext.removeUser(v, mEvent, mUsers.get(uid).first, mUsers.get(uid).second)
        );
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}

