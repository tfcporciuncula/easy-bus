package com.arctouch.easybus.route.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arctouch.easybus.R;
import com.arctouch.easybus.data.ServiceApi;
import com.arctouch.easybus.route.RouteActivity;
import com.arctouch.easybus.route.RouteFragment;

import java.util.List;

/**
 * Fragment that presents the schedule for the routes, with a tab for each Calendar (weekday, saturday, sunday).
 *
 * It uses an AsyncTaskLoader to make sure orientation changes are properly handled.
 */
public class SchedulesFragment extends RouteFragment implements LoaderManager.LoaderCallbacks<List<ScheduleItem>> {

    private static final int LOADER_ID = 2;

    private ViewPager viewPager;

    private Schedule[] schedules;

    public static RouteFragment newInstance(long routeId) {
        return newInstance(routeId, new SchedulesFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (viewPager == null) {
            initRoutesLoader().forceLoad();
        }

        return inflater.inflate(R.layout.fragment_schedules, container, false);
    }

    private void setupViewPager() {
        viewPager = (ViewPager) getActivity().findViewById(R.id.schedule_view_pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ScheduleFragment.newInstance(schedules[position]);
            }

            @Override
            public int getCount() {
                return schedules.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return schedules[position].getCalendar().toString();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initRoutesLoader();
    }

    private Loader initRoutesLoader() {
        return getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<List<ScheduleItem>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<ScheduleItem>>(getActivity()) {

            @Override
            public List<ScheduleItem> loadInBackground() {
                return ServiceApi.instance(getContext()).findDeparturesByRouteId(routeId);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<ScheduleItem>> loader, List<ScheduleItem> data) {
        Schedule weekdayFragments  = ScheduleHelper.buildSpecificSchedule(data, ScheduleItem.Calendar.WEEKDAY);
        Schedule saturdayFragments = ScheduleHelper.buildSpecificSchedule(data, ScheduleItem.Calendar.SATURDAY);
        Schedule sundayFragments   = ScheduleHelper.buildSpecificSchedule(data, ScheduleItem.Calendar.SUNDAY);

        schedules = new Schedule[]{weekdayFragments, saturdayFragments, sundayFragments};

        setupViewPager();
        ((RouteActivity) getActivity()).dismissProgressDialog();
    }

    @Override
    public void onLoaderReset(Loader<List<ScheduleItem>> loader) {}

}
