package ch.epfl.sweng.eventmanager.ui.ticketing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;

import java.util.Collections;
import java.util.List;

public final class TicketingConfigurationPickerActivity extends TicketingActivity {
    private static final String TAG = "ConfigurationPicker";

    private ScrollView mPickerView;
    private ProgressBar mProgressBar;
    private TextView mProgressText;
    private View mLoadingView;
    private RecyclerView mRecycler;
    private ConfigurationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing_configuration_picker);

        this.mPickerView = findViewById(R.id.configuration_list);
        this.mLoadingView = findViewById(R.id.loading_progress);
        this.mRecycler = findViewById(R.id.recylcer);
        this.mProgressBar = findViewById(R.id.loading_progress_bar);
        this.mProgressText = findViewById(R.id.loading_text_view);

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reset progress bar state
        this.mPickerView.setVisibility(View.GONE); // Hide picker
        this.mProgressBar.setVisibility(View.VISIBLE);
        this.mProgressText.setVisibility(View.VISIBLE);
        this.mLoadingView.setVisibility(View.VISIBLE);
        this.mProgressText.setText(R.string.loading_text);

        this.service.getConfigurations(new TicketingService.ApiCallback<List<ScanConfiguration>>() {
            @Override
            public void onSuccess(List<ScanConfiguration> data) {
                adapter.setConfigurations(data);

                mPickerView.setVisibility(View.VISIBLE);
                mLoadingView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(List<ApiResult.ApiError> errors) {
                mProgressBar.setVisibility(View.GONE); // Hide loading spinner

                // TODO: add retry button
                // TODO: parse errors

                mProgressText.setText(getResources().getString(R.string.loading_failed, errors.get(0).getKey()));
            }
        });
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

        public ConfigurationViewHolder(View item, TicketingConfigurationPickerActivity activity) {
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
