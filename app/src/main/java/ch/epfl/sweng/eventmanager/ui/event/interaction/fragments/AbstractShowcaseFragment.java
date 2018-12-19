package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import ch.epfl.sweng.eventmanager.ui.event.interaction.MultiFragmentActivity;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.EventInteractionModel;

/**
 * @author Louis Vialar
 */
public abstract class AbstractShowcaseFragment extends Fragment {
    private final int resource;
    protected EventInteractionModel model;
    protected View view;

    protected AbstractShowcaseFragment(int resource) {
        this.resource = resource;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO handle null exception
        model = ViewModelProviders.of(requireActivity()).get(EventInteractionModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(resource, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (model == null) {
            // TODO Handle null exception
            model = ViewModelProviders.of(requireActivity()).get(EventInteractionModel.class);
        }
    }

    MultiFragmentActivity getParentActivity() {
        return (MultiFragmentActivity) getActivity();
    }
}
