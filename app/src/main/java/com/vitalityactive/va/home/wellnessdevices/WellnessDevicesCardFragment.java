package com.vitalityactive.va.home.wellnessdevices;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.home.cards.HomeScreenCardFragment;

import javax.inject.Inject;

public class WellnessDevicesCardFragment extends HomeScreenCardFragment {
    @Inject
    protected WellnessDevicesCardRepository repository;

    @Override
    protected void setupDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void onClicked() {
        navigationCoordinator.navigateAfterWellnessDevicesTapped(getActivity());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.non_smokers_home_card;
    }

    @Override
    protected void configureView(View view) {
        TextView heading = view.findViewById(R.id.heading);
        TextView title = view.findViewById(R.id.title);
        view.findViewById(R.id.confirm_status_button).setVisibility(View.GONE);
        view.findViewById(R.id.subtitle).setVisibility(View.GONE);

        heading.setText(R.string.WDA_title_414);
        title.setText(R.string.WDA_home_card_title_415);
        ImageView smallLogo = view.findViewById(R.id.small_logo);
        smallLogo.setColorFilter(ContextCompat.getColor(getContext(), R.color.wellnessdevices_blue)); // 14.06.17 this line can be removed after refactoring of the layout file
        setImage(view, R.id.small_logo, getTintedDrawable(R.drawable.device_icon, R.color.wellnessdevices_blue));
        setImage(view, R.id.logo, getDrawable(R.drawable.device_icon_med));

    }

    public static class Factory implements HomeScreenCardFragment.Factory {
        @Override
        public HomeScreenCardFragment buildFragment() {
            return new WellnessDevicesCardFragment();
        }
    }
}
