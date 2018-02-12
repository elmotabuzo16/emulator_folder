package com.vitalityactive.va.home.vhr;

import android.util.Log;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.home.cards.HomeScreenCardFragment;

import javax.inject.Inject;

public class VHRCardFragment extends HomeScreenCardFragment {
    @Inject
    VHRCardRepository repository;
    private HomeCardDTO card;

    @Override
    protected void setupDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected void loadData() {
        card = repository.getHomeCard();
        Log.e("cjc","loadData:"+String.valueOf(card.isDone()));
    }

    @Override
    protected void onClicked() {
        Log.e("cjc","VHRCardFragment onClicked");
        navigationCoordinator.navigateAfterVHRCardTapped(getActivity());
    }

    @Override
    protected void configureView(View view) {
        showCompletedIconOrCircularProgressBar(card, view, R.color.jungle_green, R.color.light_divider_12);
        setupTitle(card, view,
                R.string.home_card_card_earn_title_292, R.string.confirmation_completed_title_117);
        setupSubTitle(card, view,
                R.string.home_card_edit_update_message_253, R.string.home_card_sections_remaining_title_9995, R.string.get_started_button_title_103);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.vhr_home_card;
    }

    public static class Factory implements HomeScreenCardFragment.Factory {
        @Override
        public HomeScreenCardFragment buildFragment() {
            return new com.vitalityactive.va.home.vhr.VHRCardFragment();
        }
    }
}
