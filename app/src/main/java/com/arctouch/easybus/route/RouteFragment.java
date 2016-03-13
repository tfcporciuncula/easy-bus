package com.arctouch.easybus.route;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Base implementation for the fragments that live within RouteActivity.
 *
 * @see com.arctouch.easybus.route.stops.StopsFragment StopsFragment
 * @see com.arctouch.easybus.route.schedule.SchedulesFragment SchedulesFragment
 */
public abstract class RouteFragment extends Fragment {

    private static final String ARG_ROUTE_ID = "routeId";

    protected long routeId;

    protected static RouteFragment newInstance(long routeId, RouteFragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putLong(ARG_ROUTE_ID, routeId);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeId = getArguments().getLong(ARG_ROUTE_ID);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

}
