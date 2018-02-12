package com.vitalityactive.va.home;

import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.home.cards.HomeScreenCardFragment;

public class NotImplementedCardFragment extends HomeScreenCardFragment {
    @Override
    protected void setupDependencies(DependencyInjector dependencyInjector) {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onClicked() {

    }

    @Override
    protected void configureView(View view) {
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.not_implemented_home_card;
    }

    public static class Factory implements HomeScreenCardFragment.Factory {
        @Override
        public HomeScreenCardFragment buildFragment() {
            return new NotImplementedCardFragment();
        }
    }
}
