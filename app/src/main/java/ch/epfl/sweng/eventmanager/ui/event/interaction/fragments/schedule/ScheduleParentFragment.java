package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ScheduleViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Fragment with viewPager holding Schedule with different rooms and mySchedule fragments
 */
public class ScheduleParentFragment extends Fragment {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private ScheduleViewModel scheduleViewModel;
    private ViewPagerAdapter viewPagerAdapter;
    private static String TAG = "ScheduleParentFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Create view and bindings
        View view = inflater.inflate(R.layout.fragment_schedule_parent, container, false);
        ButterKnife.bind(this, view);

        //Setup the ViewPager
        createViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (scheduleViewModel == null) {
            scheduleViewModel = ViewModelProviders.of(requireActivity()).get(ScheduleViewModel.class);
        }

        Log.i(TAG, "Resumed model from memory : " + scheduleViewModel);

        scheduleViewModel.getScheduleItemsByRoom().observe(this, map -> {
            if (map != null) {
                tabLayout.setVisibility(View.VISIBLE);
                updateTabs(map.keySet());
                viewPagerAdapter.notifyDataSetChanged();
            } else {
                tabLayout.setVisibility(View.GONE);
                destroyUnusedFragments(viewPagerAdapter.mFragmentList, Collections.emptySet(), true);
                viewPagerAdapter.addFragment(new ScheduleFragment(), "");
                viewPagerAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Take care of setting up the viewPager Adapter and binding it to the viewPager
     *
     * @param viewPager viewpager
     */
    private void createViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new MyScheduleFragment(), "My Schedule");
        viewPager.setAdapter(viewPagerAdapter);
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
                viewPagerAdapter.addFragment(fragment, fragmentName);
            }
        }
        destroyUnusedFragments(viewPagerAdapter.mFragmentList, rooms, false);
    }

    /**
     * Destroy all unused fragment in case of removal of Room/Events in DB
     *
     * @param allFragments      list of all registered fragments
     * @param rooms             current known rooms
     * @param destroyMySchedule true if mySchedule need to be destroyed, false otherwise
     */
    private void destroyUnusedFragments(@NonNull List<Fragment> allFragments, @NonNull Set<String> rooms, Boolean destroyMySchedule) {
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
    private static class ViewPagerAdapter extends FragmentPagerAdapter {
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
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
