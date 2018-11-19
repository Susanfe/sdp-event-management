package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.EventShowcaseModel;

/**
 * @author Louis Vialar
 */
public abstract class AbstractShowcaseFragment extends Fragment {
    private final int resource;
    protected EventShowcaseModel model;
    protected View view;

    AbstractShowcaseFragment(int resource) {
        this.resource = resource;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO handle null exception
        model = ViewModelProviders.of(getActivity()).get(EventShowcaseModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(resource, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (model == null) {
            // TODO Handle null exception
            model = ViewModelProviders.of(getActivity()).get(EventShowcaseModel.class);
        }
    }

    public EventShowcaseActivity getParentActivity() {
        return (EventShowcaseActivity) getActivity();
    }
}
