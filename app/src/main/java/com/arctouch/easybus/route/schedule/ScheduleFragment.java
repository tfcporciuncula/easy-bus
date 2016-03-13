package com.arctouch.easybus.route.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arctouch.easybus.R;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {

    private static final String ARG_SCHEDULE = "schedule";

    private Schedule schedule;

    public static ScheduleFragment newInstance(Schedule schedule) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_SCHEDULE, schedule);

        ScheduleFragment scheduleFragment = new ScheduleFragment();
        scheduleFragment.setArguments(bundle);
        return scheduleFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        schedule = (Schedule) getArguments().getSerializable(ARG_SCHEDULE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ScheduleAdapter(schedule.getScheduleItems()));
        return recyclerView;
    }

    private class ScheduleHolder extends RecyclerView.ViewHolder {

        private TextView scheduleItemTextView;

        public ScheduleHolder(View itemView) {
            super(itemView);
            scheduleItemTextView = (TextView) itemView;
        }

        public void bindSchedule(ScheduleItem scheduleItem) {
            scheduleItemTextView.setText(scheduleItem.getTime());
        }

    }

    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleHolder> {

        private List<ScheduleItem> scheduleItems = new ArrayList<>();

        public ScheduleAdapter(List<ScheduleItem> scheduleItems) {
            this.scheduleItems = scheduleItems;
        }

        @Override
        public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            // TODO: maybe define my own layout instead of simple_list_item_1
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ScheduleHolder(view);
        }

        @Override
        public void onBindViewHolder(ScheduleHolder holder, int position) {
            holder.bindSchedule(scheduleItems.get(position));
        }

        @Override
        public int getItemCount() {
            return scheduleItems.size();
        }

    }

}
