package com.vitalityactive.va.activerewards.rewards;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;

public class RewardsListActivity extends BaseActivity {
    private static final String TAG = "RewardsListActivity";

    private enum Tabs {CURRENT, HISTORY}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_rewards_rewardslist);
        setUpActionBarWithTitle(R.string.AR_landing_rewards_cell_title_695)
                .setDisplayHomeAsUpEnabled(true);
        setupTabLayout((TabLayout) findViewById(R.id.rewards_tab));
    }

    private void setupTabLayout(TabLayout tabLayout) {
        ViewPager viewPager = findViewById(R.id.rewards_view_pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "loading fragment for position=" + position);
            if (position == Tabs.CURRENT.ordinal()) {
                return new CurrentRewardsFragment();
            } else if (position == Tabs.HISTORY.ordinal()) {
                return new RewardsHistoryFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == Tabs.CURRENT.ordinal()) {
                return getString(R.string.AR_rewards_current_segment_title_688);
            } else if (position == Tabs.HISTORY.ordinal()) {
                return getString(R.string.AR_rewards_history_segment_title_670);
            }
            return null;
        }
    }
}
