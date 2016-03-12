package com.arctouch.easybus.search;

import android.app.ProgressDialog;
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

import com.arctouch.easybus.R;
import com.arctouch.easybus.data.ServiceApi;
import com.arctouch.easybus.model.Route;
import com.arctouch.easybus.route.RouteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that allows searches for routes based on street names.
 *
 * It uses an AsyncTaskLoader to make sure orientation changes are properly handled.
 */
// TODO: handle empty results
// TODO: handle no internet connection
public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Route>> {

    private static final int LOADER_ID = 0;

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    private String query;
    private List<Route> routes = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.routes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RouteAdapter(routes));
        return view;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        final SearchView searchView = getSearchView(menu);
        searchView.setQueryHint(getString(R.string.search_menu_item_hint));
        if (!TextUtils.isEmpty(query)) {
            searchView.post(new Runnable() {
                @Override
                public void run() {
                    searchView.setIconified(false);
                    searchView.setQuery(query, false);
                    searchView.clearFocus();
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

    private void searchRoutes() {
        initRoutesLoader().forceLoad();
    }

    @Override
    public Loader<List<Route>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Route>>(getActivity()) {

            @Override
            public List<Route> loadInBackground() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(
                                getActivity(), null, getString(R.string.search_progress_dialog_message), true);
                    }
                });
                return ServiceApi.instance(getContext()).findRoutesByStopName(query);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Route>> loader, List<Route> data) {
        routes = data;
        recyclerView.setAdapter(new RouteAdapter(routes));
        progressDialog.dismiss();
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
                    startActivity(RouteActivity.newIntent(getActivity(), route));
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
            // TODO: maybe define my own layout instead of simple_list_item_1
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
