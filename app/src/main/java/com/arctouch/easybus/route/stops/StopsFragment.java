package com.arctouch.easybus.route.stops;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arctouch.easybus.R;
import com.arctouch.easybus.data.ServiceApi;
import com.arctouch.easybus.route.RouteFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that presents all the stops (or the itinerary) of a given route.
 *
 * It uses an AsyncTaskLoader to make sure orientation changes are properly handled.
 */
public class StopsFragment extends RouteFragment implements LoaderManager.LoaderCallbacks<List<Stop>> {

    private static final int LOADER_ID = 1;

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    private List<Stop> stops;

    public static RouteFragment newInstance(long routeId) {
        return newInstance(routeId, new StopsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (stops == null) {
            initRoutesLoader().forceLoad();
        }

        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new StopAdapter(new ArrayList<Stop>()));
        return recyclerView;
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
    public void onPause() {
        super.onPause();
        avoidWindowLeaking();
    }

    private void avoidWindowLeaking() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public Loader<List<Stop>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Stop>>(getActivity()) {

            @Override
            public List<Stop> loadInBackground() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(
                                getActivity(), null, getString(R.string.route_progress_dialog_message), true);
                    }
                });
                return ServiceApi.instance(getContext()).findStopsByRouteId(routeId);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Stop>> loader, List<Stop> data) {
        stops = data;
        recyclerView.setAdapter(new StopAdapter(stops));
        progressDialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<List<Stop>> loader) {}


    private class StopHolder extends RecyclerView.ViewHolder {

        private TextView stopTextView;

        public StopHolder(View itemView) {
            super(itemView);
            stopTextView = (TextView) itemView;
        }

        public void bindStop(Stop stop) {
            stopTextView.setText(stop.toString());
        }

    }

    private class StopAdapter extends RecyclerView.Adapter<StopHolder> {

        private List<Stop> stops = new ArrayList<>();

        public StopAdapter(List<Stop> stops) {
            this.stops = stops;
        }

        @Override
        public StopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            // TODO: maybe define my own layout instead of simple_list_item_1
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new StopHolder(view);
        }

        @Override
        public void onBindViewHolder(StopHolder holder, int position) {
            holder.bindStop(stops.get(position));
        }

        @Override
        public int getItemCount() {
            return stops.size();
        }

    }

}
