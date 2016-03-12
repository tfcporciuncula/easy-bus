package com.arctouch.easybus.search;

import android.support.v4.app.Fragment;

import com.arctouch.easybus.util.SingleFragmentActivity;

/**
 * Activity that serves as a simple holder for the SearchFragment.
 */
public class SearchActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new SearchFragment();
    }

}
