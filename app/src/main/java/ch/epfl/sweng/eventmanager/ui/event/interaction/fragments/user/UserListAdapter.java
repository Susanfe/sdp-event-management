package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.users.Role;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private Map<String, List<Role>> mUsers;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @BindView(R.id.uid)
        public TextView userUid;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,itemView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserListAdapter(Event event) {

        // Build our internal User to Roles representation
        Map<String, List<String>> raw = event.getUsers();
        mUsers = new HashMap<>();
        for (String rawRole : raw.keySet()) {
            for (String uid : raw.get(rawRole)) {
                List roleList = mUsers.get(rawRole);
                if (roleList == null) roleList = new ArrayList();

                roleList.add(Role.valueOf(rawRole.toUpperCase()));
                mUsers.put(uid, roleList);
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
        holder.userUid.setText(index.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}

