package com.arctouch.easybus.route;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.arctouch.easybus.R;
import com.arctouch.easybus.route.schedule.SchedulesFragment;
import com.arctouch.easybus.route.stops.StopsFragment;

/**
 * Activity that works as the host of the StopsFragment and SchedulesFragment.
 */
public class RouteActivity extends AppCompatActivity {

    private static final String EXTRA_ROUTE = "com.arctouch.easybus.route";

    private static final String KEY_IS_STOPS_FRAGMENT_VISIBLE = "isStopsFragmentsVisible";

//    private ProgressDialog progressDialog;

    private boolean isStopsFragmentsVisible = true;

    public static Intent newIntent(Context context, Route route) {
        Intent intent = new Intent(context, RouteActivity.class);
        intent.putExtra(EXTRA_ROUTE, route);
        return intent;
    }

//    public ProgressDialog getProgressDialog() {
//        return progressDialog;
//    }
//
//    public void showProgressDialog() {
//        if (progressDialog != null && !progressDialog.isShowing()) {
//            progressDialog = ProgressDialog.show(
//                    this, null, getString(R.string.route_progress_dialog_message), true);
//        }
//    }
//
//    public void dismissProgressDialog() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        Route route = (Route) getIntent().getSerializableExtra(EXTRA_ROUTE);
        setTitle(route.toString());
        addFragmentsIfNecessary(route);

        if (savedInstanceState != null) {
            isStopsFragmentsVisible = savedInstanceState.getBoolean(KEY_IS_STOPS_FRAGMENT_VISIBLE);
            updateFragmentsVisibility();
        }
    }

    private void addFragmentsIfNecessary(Route route) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment stopsFragment = fragmentManager.findFragmentById(R.id.fragment_stops_container);
        if (stopsFragment == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_stops_container, StopsFragment.newInstance(route.getId()))
                    .add(R.id.fragment_schedule_container, SchedulesFragment.newInstance(route.getId()))
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_STOPS_FRAGMENT_VISIBLE, isStopsFragmentsVisible);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        dismissProgressDialog();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_route, menu);
        MenuItem item = menu.findItem(R.id.itinerary_and_schedule_menu_item);

        if (isStopsFragmentsVisible) {
            item.setTitle(R.string.schedule_menu_item_title);
            item.setIcon(R.drawable.ic_menu_schedule);
        } else {
            item.setTitle(R.string.itinerary_menu_item_title);
            item.setIcon(R.drawable.ic_menu_itinerary);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.itinerary_and_schedule_menu_item) {
            return super.onOptionsItemSelected(item);
        }

        isStopsFragmentsVisible = !isStopsFragmentsVisible;
        updateFragmentsVisibility();
        invalidateOptionsMenu();
        return true;
    }

    private void updateFragmentsVisibility() {
        FrameLayout stopsFragmentContainer = (FrameLayout) findViewById(R.id.fragment_stops_container);
        FrameLayout scheduleFragmentContainer = (FrameLayout) findViewById(R.id.fragment_schedule_container);
        if (isStopsFragmentsVisible) {
            stopsFragmentContainer.setVisibility(View.VISIBLE);
            scheduleFragmentContainer.setVisibility(View.GONE);
        } else {
            stopsFragmentContainer.setVisibility(View.GONE);
            scheduleFragmentContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
