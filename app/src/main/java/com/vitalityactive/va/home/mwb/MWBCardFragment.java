package com.vitalityactive.va.home.mwb;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.home.cards.HomeScreenCardFragment;


import javax.inject.Inject;
/**
 * Created by christian.j.p.capin on 2/6/2018.
 */

public class MWBCardFragment extends HomeScreenCardFragment {

    @Inject
    MWBCardRepository repository;
    private HomeCardDTO card;


    public MWBCardFragment() {
    }


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
        navigationCoordinator.navigateAfterMWBSelected(getActivity());
    }

    @Override
    protected void configureView(View view) {
        showCompletedIconOrCircularProgressBar(card, view, R.color.jungle_green, R.color.light_divider_12);
        setupTitle(view);
        setupSubTitle(view, R.id.subtitle, card.isDone(), card.earnedPoints);


    }



    private void setupTitle(View view) {
        if (card.hasNotStarted()) {
            setFormattedString(view, R.id.title, R.string.home_card_card_section_title_9999, card.potentialPoints);
        } else if (card.isInProgress()) {
            setFormattedString(view, R.id.title, R.string.home_card_card_section_title_365, card.earnedPoints, card.potentialPoints);
        } else if (card.isDone()) {
            setText(view, R.id.title, R.string.confirmation_completed_title_117);
        }

    }

    private void setupSubTitle(View rootView, int textViewId, boolean done, long value) {
        rootView.findViewById(textViewId).setVisibility(done ? View.VISIBLE : View.GONE);
        setFormattedString(rootView, textViewId, R.string.home_card_edit_update_message_253, value);
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_mwbcard;
    }

    private void showGetStartedButton(View rootView, boolean show) {
        rootView.findViewById(R.id.get_started_buttons).setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public static class Factory implements HomeScreenCardFragment.Factory {
        @Override
        public HomeScreenCardFragment buildFragment() {
            return new MWBCardFragment();
        }
    }

}
