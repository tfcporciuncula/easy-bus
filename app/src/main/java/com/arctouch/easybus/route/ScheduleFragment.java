package com.arctouch.easybus.route;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arctouch.easybus.R;

// TODO: this
public class ScheduleFragment extends RouteFragment {

    public static RouteFragment newInstance(long routeId) {
        return newInstance(routeId, new ScheduleFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        return view;
    }

}
