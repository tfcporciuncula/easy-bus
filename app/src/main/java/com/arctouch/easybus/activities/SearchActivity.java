package com.arctouch.easybus.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.arctouch.easybus.R;
import com.arctouch.easybus.model.Route;
import com.arctouch.easybus.rest.RoutesService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final String KEY_QUERY = "query";
    private static final String KEY_ROUTES = "routes";

    private RecyclerView recyclerView;

    private String query;
    private List<Route> routes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState != null) {
            routes = (ArrayList<Route>) savedInstanceState.get(KEY_ROUTES);
            query = savedInstanceState.getString(KEY_QUERY);
        }

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.routes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RouteAdapter(routes));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_ROUTES, (Serializable) routes);
        outState.putString(KEY_QUERY, query);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

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
                searchRoutes(query, searchView);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                return true;
            }
        });

        return true;
    }

    private SearchView getSearchView(Menu menu) {
        MenuItem searchMenuItem = menu.findItem(R.id.search_menu_item);
        return (SearchView) searchMenuItem.getActionView();
    }

    private void searchRoutes(String query, SearchView searchView) {
        routes = RoutesService.findRoutesByStopName(query.toString());
        recyclerView.setAdapter(new RouteAdapter(routes));
        hideKeyboard(searchView);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class RouteHolder extends RecyclerView.ViewHolder {

        private TextView routeTextView;

        private Route route;

        public RouteHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // start the details activity
                }
            });
            routeTextView = (TextView) itemView;
        }

        public void bindRoute(Route route) {
            this.route = route;
            routeTextView.setText(route.getName());
        }

    }

    private class RouteAdapter extends RecyclerView.Adapter<RouteHolder> {

        private List<Route> routes = new ArrayList<>();

        public RouteAdapter(List<Route> routes) {
            this.routes = routes;
        }

        @Override
        public RouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SearchActivity.this);
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
