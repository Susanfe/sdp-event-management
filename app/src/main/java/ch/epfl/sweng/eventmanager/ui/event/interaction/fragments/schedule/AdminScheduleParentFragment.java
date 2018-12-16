package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import android.os.Bundle;

/**
 * @author Louis Vialar
 */
public class AdminScheduleParentFragment extends ScheduleParentFragment {

    public static AdminScheduleParentFragment newInstance() {
        return new AdminScheduleParentFragment();
    }

    public static AdminScheduleParentFragment newInstance(String roomName) {
        if (roomName == null) {
            throw new IllegalArgumentException("The name of the room cannot be null");
        }
        Bundle args = new Bundle();
        args.putString(ScheduleParentFragment.TAB_NB_KEY, roomName);
        AdminScheduleParentFragment fragment = new AdminScheduleParentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Take care of setting up the viewPager Adapter and binding it to the viewPager
     */
    protected ViewPagerAdapter createViewPagerAdapter() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new MyScheduleFragment(), "Schedule new item"); // TODO: add create instead
        return viewPagerAdapter;
    }

}
