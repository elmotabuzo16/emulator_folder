package com.vitalityactive.va.eventsfeed.views.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vitalityactive.va.eventsfeed.views.fragments.EmptyStateFragment;
import com.vitalityactive.va.eventsfeed.data.EventsFeedMonth;
import com.vitalityactive.va.eventsfeed.views.fragments.EventsFeedMonthFragment;
import com.vitalityactive.va.networking.RequestFailedEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayellos on 11/17/17.
 */

public class AdapterEventsFeed extends FragmentStatePagerAdapter {

    public RequestFailedEvent.Type errorState;
    @NonNull
    private List<EventsFeedMonth> data = new ArrayList<>();
    private String globalTintColor;

    public AdapterEventsFeed(FragmentManager fm, String globalTintColor) {
        super(fm);
        this.globalTintColor = globalTintColor;
    }

    @Override
    public Fragment getItem(int position) {
        if (errorState != null) {
            return EmptyStateFragment.newInstance(errorState, Color.parseColor(globalTintColor));
        }

        return EventsFeedMonthFragment.newInstance(position, globalTintColor);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getMonthName() + "\n";
    }

    @NonNull
    public List<EventsFeedMonth> getData() {
        return data;
    }

    public void setData(@NonNull List<EventsFeedMonth> data) {
        this.data = data;
    }
}
