package ch.epfl.sweng.eventmanager.ui.ticketing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ticketing.NotAuthenticatedException;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;

import java.util.Collections;
import java.util.List;

public final class TicketingConfigurationPickerActivity extends TicketingActivity {
    private static final String TAG = "ConfigurationPicker";

    private ScrollView mPickerView;
    private RecyclerView mRecycler;
    private TextView mTitle;
    private ConfigurationAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing_configuration_picker);

        this.mRecycler = findViewById(R.id.recylcer);
        this.mTitle = findViewById(R.id.pick_config);
        this.swipeRefreshLayout = findViewById(R.id.swipe_refresher);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);

        // Set up items borders
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecycler.getContext(),
                layoutManager.getOrientation()
        );
        mRecycler.addItemDecoration(dividerItemDecoration);

        this.adapter = new ConfigurationAdapter(this);
        this.mRecycler.setAdapter(adapter);

        // Setup refresh handler
        this.swipeRefreshLayout.setOnRefreshListener(this::fetchData);
    }

    private void fetchData() {
        try {
            this.service.getConfigurations(new TicketingService.ApiCallback<List<ScanConfiguration>>() {
                @Override
                public void onSuccess(List<ScanConfiguration> data) {
                    adapter.setConfigurations(data);
                    swipeRefreshLayout.setRefreshing(false);
                    mTitle.setText(getResources().getString(R.string.pick_configuration));

                }

                @Override
                public void onFailure(List<ApiResult.ApiError> errors) {
                    // TODO: parse errors

                    swipeRefreshLayout.setRefreshing(false);
                    mTitle.setText(getResources().getString(R.string.loading_failed, errors.get(0).getMessages().get(0)));
                }
            });
        } catch (NotAuthenticatedException e) {
            Toast.makeText(TicketingConfigurationPickerActivity.this, R.string.ticketing_requires_login, Toast.LENGTH_LONG).show();
            e.printStackTrace();

            startActivity(switchActivity(TicketingLoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        swipeRefreshLayout.setRefreshing(true);

        this.fetchData();
    }

    private static class ConfigurationAdapter extends RecyclerView.Adapter<ConfigurationViewHolder> {
        private List<ScanConfiguration> configurations = Collections.emptyList();
        private TicketingConfigurationPickerActivity activity;

        private ConfigurationAdapter(TicketingConfigurationPickerActivity activity) {
            this.activity = activity;
        }

        @NonNull
        @Override
        public ConfigurationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.content_ticketing_configuration_item, viewGroup, false);

            Log.i(TAG, "Create view holder...");

            return new ConfigurationViewHolder(view, activity);
        }

        @Override
        public void onBindViewHolder(@NonNull ConfigurationViewHolder configurationViewHolder, int i) {
            configurationViewHolder.setConfiguration(configurations.get(i));

            Log.i(TAG, "Bind view holder to " + i);
            System.out.println(configurations);

        }

        @Override
        public int getItemCount() {
            return configurations.size();
        }

        public void setConfigurations(List<ScanConfiguration> configurations) {
            this.configurations = configurations;
            this.notifyDataSetChanged();
        }
    }

    private static class ConfigurationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ScanConfiguration configuration;
        private TextView configName;
        private TicketingConfigurationPickerActivity activity;

        ConfigurationViewHolder(View item, TicketingConfigurationPickerActivity activity) {
            super(item);

            this.configName = item.findViewById(R.id.configuration);
            this.activity = activity;
            item.setOnClickListener(this);
        }

        public void setConfiguration(ScanConfiguration configuration) {
            this.configuration = configuration;
            this.configName.setText(configuration.getName());
        }

        @Override
        public void onClick(View v) {
            Intent i = activity.switchActivity(TicketingScanActivity.class);
            i.putExtra(TicketingScanActivity.SELECTED_CONFIG_ID, configuration.getId());
            activity.startActivity(i);
        }
    }
}
