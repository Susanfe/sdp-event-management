package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ScheduleViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Fragment with viewPager holding Schedule with different rooms and mySchedule fragments
 */
public class ScheduleParentFragment extends Fragment {

    private static final String TAG = "ScheduleParentFragment";
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager_empty)
    TextView emptyText;
    private ScheduleViewModel scheduleViewModel;
    private ViewPagerAdapter viewPagerAdapter;
    private Bundle savedInstanceState;
    static final String TAB_NB_KEY = "TAB_NB_KEY";

    public static ScheduleParentFragment newInstance() {
        return new ScheduleParentFragment();
    }

    /**
     * Returns a new ScheduleParentFragment with the given room displayed.
     *
     * @param roomName The name of the room we want do display
     * @return ScheduleParentFragment
     */
    public static ScheduleParentFragment newInstance(String roomName) {
        if (roomName == null) {
            throw new IllegalArgumentException("The name of the room cannot be null");
        }
        Bundle args = new Bundle();
        args.putString(TAB_NB_KEY, roomName);
        ScheduleParentFragment fragment = new ScheduleParentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Create view and bindings
        View view = inflater.inflate(R.layout.fragment_schedule_parent, container, false);
        ButterKnife.bind(this, view);

        //Setup the ViewPager
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        if (savedInstanceState != null) {
            this.savedInstanceState = savedInstanceState;
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewPagerAdapter = createViewPagerAdapter();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("current_item", viewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (scheduleViewModel == null) {
            scheduleViewModel = ViewModelProviders.of(requireActivity()).get(ScheduleViewModel.class);
        }

        Log.i(TAG, "Resumed model from memory : " + scheduleViewModel);

        scheduleViewModel.getScheduleItemsByRoom().observe(this, roomSchedules -> {
            if (roomSchedules != null) {
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                emptyText.setVisibility(View.GONE);
                updateTabs(roomSchedules.keySet());
                viewPagerAdapter.notifyDataSetChanged();
                if (savedInstanceState != null) {
                    int page = savedInstanceState.getInt("current_item");
                    viewPager.setCurrentItem(page);
                }
                Bundle args = getArguments();
                if (args != null) {
                    String room = getArguments().getString(TAB_NB_KEY, "");
                    int cond = viewPagerAdapter.getTitlePage(room);
                    if (cond != -1) {
                        viewPager.postDelayed(() -> viewPager.setCurrentItem(cond), 100);
                    }
                }
            } else {
                tabLayout.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                emptyText.setVisibility(View.VISIBLE);
                destroyUnusedFragments(Collections.emptySet(), true);
                viewPagerAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Take care of setting up the viewPager Adapter and binding it to the viewPager
     */
    protected ViewPagerAdapter createViewPagerAdapter() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new MyScheduleFragment(), "My Schedule");
        return viewPagerAdapter;
    }

    /**
     * Update viewPager tabs according to rooms (locations) in which there are events registered. Will remove tab
     * corresponding to the room if there is no more event happening in that room and will create new tab for the room
     * if events are added in a new room which was not previously created.
     *
     * @param rooms Set of rooms
     */
    private void updateTabs(Set<String> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            return;
        }
        for (String room : rooms) {
            String fragmentName = (room != null && !room.isEmpty()) ? room : "Schedule";
            if (!viewPagerAdapter.mFragmentTitleList.contains(fragmentName)) {
                ScheduleFragment fragment = new ScheduleFragment();
                fragment.setRoom(room);
                fragment.setRetainInstance(true);
                viewPagerAdapter.addFragment(fragment, fragmentName);
            }
        }
        destroyUnusedFragments(rooms, false);
    }

    /**
     * Destroy all unused fragment in case of removal of Room/Events in DB
     *
     * @param rooms             current known rooms
     * @param destroyMySchedule true if mySchedule need to be destroyed, false otherwise
     */
    private void destroyUnusedFragments(@NonNull Set<String> rooms,
                                        Boolean destroyMySchedule) {
        for (Fragment fragment : viewPagerAdapter.mFragmentList) {
            if (fragment instanceof ScheduleFragment && !rooms.contains(((ScheduleFragment) fragment).getRoom())) {
                requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                viewPagerAdapter.mFragmentList.remove(fragment);
            }
            if (destroyMySchedule && fragment instanceof MyScheduleFragment) {
                requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                viewPagerAdapter.mFragmentList.remove(fragment);
            }
        }
    }

    /**
     * Basic Adapter for ViewPager
     */
    protected static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            fragment.setRetainInstance(true);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        int getTitlePage(String str) {
            return mFragmentTitleList.indexOf(str);
        }

    }

}
