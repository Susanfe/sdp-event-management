package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment with viewPager holding Schedule and mySchedule fragments
 */
public class ScheduleParentFragment extends Fragment {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Create view and bindings
        View view = inflater.inflate(R.layout.fragment_schedule_parent,container, false);
        ButterKnife.bind(this,view);

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


    /**
     * Take care of setting up the viewPager Adapter and binding it to the viewPager
     * @param viewPager viewpager
     */
    private void createViewPager (ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new ScheduleFragment(), "Schedule");
        viewPagerAdapter.addFragment(new MyScheduleFragment(), "My Schedule");
        viewPager.setAdapter(viewPagerAdapter);

    }

    /**
     * Basic Adapter for ViewPager
     */
    static class ViewPagerAdapter extends FragmentPagerAdapter {
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
