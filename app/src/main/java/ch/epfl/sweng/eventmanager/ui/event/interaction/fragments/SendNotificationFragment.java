package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.notifications.NotificationRequest;
import ch.epfl.sweng.eventmanager.repository.CloudFunction;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.user.EventUserManagementFragment;
import dagger.android.support.AndroidSupportInjection;

import javax.inject.Inject;

/**
 * Allows an administrator to write and send notifications to user having joined the event.
 */
public class SendNotificationFragment extends AbstractShowcaseFragment {
    private static String TAG = "SendNotificationFragment";

    @Inject
    CloudFunction cloudFunction;

    @BindView(R.id.notification_title)
    TextView title;
    @BindView(R.id.notification_content)
    TextView body;
    @BindView(R.id.notification_send)
    Button send;

    public SendNotificationFragment() {
        super(R.layout.fragment_send_notification);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);

        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view != null) ButterKnife.bind(this, view);

        model.getEvent().observe(this, ev -> {
            send.setOnClickListener(l -> {
                NotificationRequest notificationRequest = new NotificationRequest(title.getText().toString(), body.getText().toString(), ev.getId());

                send.setClickable(false);

                cloudFunction.sendNotificationToUsers(notificationRequest).addOnSuccessListener(e -> {
                            Toast.makeText(getContext(), R.string.send_notification_success, Toast.LENGTH_SHORT).show();
                            getParentActivity().changeFragment(new EventUserManagementFragment(), true);
                        }
                ).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), R.string.send_notification_fails, Toast.LENGTH_LONG).show();
                    send.setClickable(true);
                });
            });
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
