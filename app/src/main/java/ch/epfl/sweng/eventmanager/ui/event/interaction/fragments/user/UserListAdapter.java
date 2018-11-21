package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.user;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.FirebaseBackedUser;
import ch.epfl.sweng.eventmanager.repository.data.User;
import ch.epfl.sweng.eventmanager.users.Role;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private Map<String, List<Role>> mUsers;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @BindView(R.id.uid)
        TextView userUid;
        @BindView(R.id.role)
        TextView userRole;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    UserListAdapter(Event event) {

        // Build our internal User to Roles representation
        Map<Role, List<String>> raw = event.getPermissions();

        mUsers = new HashMap<>();
        for (Role role : raw.keySet()) {
            // TODO handle null pointer exception
            for (String uid : raw.get(role)) {
                List roleList = mUsers.get(uid);
                if (roleList == null) roleList = new ArrayList();
                roleList.add(role);

                User user = new FirebaseBackedUser(uid);
                // FIXME: fetch email instead of Uid once our FirebaseBackedUser suports it
                mUsers.put(user.getUid(), roleList);
            }
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    @NonNull
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> index = new ArrayList<>(mUsers.keySet());
        String uid = index.get(position);
        holder.userUid.setText(uid);
        // TODO handle null pointer exception
        holder.userRole.setText(mUsers.get(uid).toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}

