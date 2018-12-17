package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;

/**
 * Allows an administrator a manage the users allowed to work with this event (= access to admin
 * features).
 */
public class EventAdminMainFragment extends AbstractShowcaseFragment {
    private static final String TAG = "EventAdminMain";

    @BindView(R.id.help_text)
    TextView helpText;

    public EventAdminMainFragment() {
        super(R.layout.fragment_event_admin_main);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        helpText.setText(getString(R.string.admin_help_text));

        return view;
    }
}
