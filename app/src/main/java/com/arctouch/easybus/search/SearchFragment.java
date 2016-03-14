package com.arctouch.easybus.search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arctouch.easybus.R;
import com.arctouch.easybus.data.ServiceApi;
import com.arctouch.easybus.map.MapsActivity;
import com.arctouch.easybus.route.Route;
import com.arctouch.easybus.route.RouteActivity;
import com.arctouch.easybus.util.ConnectivityHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that allows searches for routes based on street names.
 *
 * It uses an AsyncTaskLoader to make sure orientation changes are properly handled.
 */
public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Route>> {

    private static final String TAG = SearchFragment.class.getSimpleName();

    private static final int LOADER_ID = 0;

    private static final int REQUEST_CODE_MAP = 0;

    private SearchView searchView;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    private String query;
    private List<Route> routes = new ArrayList<>();
    private boolean isLoaderRunning = false;
    private boolean hasShowedNoRoutesFoundMessage = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RouteAdapter(routes));
        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*
         * It seems that due to a known bug, I can't use manager.hasRunningLoaders() like in did in RouteActivity.
         *
         * The LoaderManager is getting destroyed after we get back from RouteActivity after changing orientation there
         * and then changing orientation again here. That's why we're using a flag (isLoaderRunning) instead.
         *
         *     https://code.google.com/p/android/issues/detail?id=20791
         *     https://code.google.com/p/android/issues/detail?id=183783
         */
        if (isLoaderRunning && isProgressDialogNotShowing()) {
            showProgressDialog();
        }
        initRoutesLoader();
    }


    private boolean isProgressDialogNotShowing() {
        return progressDialog == null || !progressDialog.isShowing();
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.search_progress_dialog_message),
                true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
            }
        });
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        searchView = getSearchView(menu);
        searchView.setQueryHint(getString(R.string.search_menu_item_hint));
        if (!TextUtils.isEmpty(query)) {
            searchView.post(new Runnable() {
                @Override
                public void run() {
                    updateSearchView();
                }
            });
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchRoutes();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                return true;
            }
        });
    }

    private SearchView getSearchView(Menu menu) {
        MenuItem searchMenuItem = menu.findItem(R.id.search_menu_item);
        return (SearchView) searchMenuItem.getActionView();
    }

    private void updateSearchView() {
        searchView.setIconified(false);
        searchView.setQuery(query, false);
        searchView.clearFocus();
    }

    private void searchRoutes() {
        initRoutesLoader().forceLoad();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.map_menu_item) {
            startActivityForResult(new Intent(getActivity(), MapsActivity.class), REQUEST_CODE_MAP);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_MAP) {
            query = MapsActivity.getSelectedStreetName(data);
            updateSearchView();
            searchRoutes();
        }
    }

    @Override
    public Loader<List<Route>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Route>>(getActivity()) {

            @Override
            public List<Route> loadInBackground() {
                if (ConnectivityHelper.isInternetConnectionAvailable(getActivity())) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isProgressDialogNotShowing()) {
                                showProgressDialog();
                            }
                        }
                    });
                    isLoaderRunning = true;
                    hasShowedNoRoutesFoundMessage = false;
                    return ServiceApi.instance(getContext()).findRoutesByStopName(query);
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), R.string.no_internet_connection_message, Toast.LENGTH_LONG).show();
                        }
                    });
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Route>> loader, List<Route> data) {
        isLoaderRunning = false;
        if (data != null) {
            routes = data;
            recyclerView.setAdapter(new RouteAdapter(routes));
            progressDialog.dismiss();

            if (data.size() == 0 && !hasShowedNoRoutesFoundMessage) {
                Toast.makeText(getActivity(), R.string.no_routes_found_message, Toast.LENGTH_LONG).show();
                hasShowedNoRoutesFoundMessage = true;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Route>> loader) {}


    private class RouteHolder extends RecyclerView.ViewHolder {

        private TextView routeTextView;

        private Route route;

        public RouteHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ConnectivityHelper.isInternetConnectionAvailable(getActivity())) {
                        startActivity(RouteActivity.newIntent(getActivity(), route));
                    } else {
                        Toast.makeText(getActivity(), R.string.no_internet_connection_message, Toast.LENGTH_LONG).show();
                    }
                }
            });
            routeTextView = (TextView) itemView;
        }

        public void bindRoute(Route route) {
            this.route = route;
            routeTextView.setText(route.toString());
        }

    }

    private class RouteAdapter extends RecyclerView.Adapter<RouteHolder> {

        private List<Route> routes = new ArrayList<>();

        public RouteAdapter(List<Route> routes) {
            this.routes = routes;
        }

        @Override
        public RouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new RouteHolder(view);
        }

        @Override
        public void onBindViewHolder(RouteHolder holder, int position) {
            holder.bindRoute(routes.get(position));
        }

        @Override
        public int getItemCount() {
            return routes.size();
        }

    }

}
