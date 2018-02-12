package com.vitalityactive.va.home.vna;

import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.home.cards.HomeScreenCardFragment;

import javax.inject.Inject;

public class VNACardFragment extends HomeScreenCardFragment {
    @Inject
    VNACardRepository repository;
    private HomeCardDTO card;

    @Override
    protected void setupDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected void loadData() {
        card = repository.getHomeCard();
    }

    @Override
    protected void onClicked() {
        navigationCoordinator.navigateAfterVNACardTapped(getActivity());
    }

    @Override
    protected void configureView(View view) {
        configureViewForVNA(view);
        // todo: confirm string ids
        showCompletedIconOrCircularProgressBar(card, view, R.color.jungle_green, R.color.light_divider_12);
        setupTitle(card, view,
                R.string.vhr_card_title_earn_d_points, R.string.confirmation_completed_title_117);
        setupSubTitle(card, view,
                R.string.home_card_edit_update_message_253, R.string.vhr_sections_remaining, R.string.get_started_button_title_103);
    }

    private void configureViewForVNA(View view) {
        setImage(view, R.id.small_logo, getTintedDrawable(R.drawable.vna_24, R.color.jungle_green));
        setText(view, R.id.heading, R.string.vna_button_title);
    }

    @Override
    protected int getLayoutResourceId() {
        // todo: generalize so that this can be used in vhr and vna
        return R.layout.vhr_home_card;
    }

    public static class Factory implements HomeScreenCardFragment.Factory {
        @Override
        public HomeScreenCardFragment buildFragment() {
            return new VNACardFragment();
        }
    }
}
