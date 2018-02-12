package com.vitalityactive.va.home.snv;


import android.graphics.drawable.Drawable;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.home.cards.HomeScreenCardFragment;

import javax.inject.Inject;

public class SNVCardFragment extends HomeScreenCardFragment {
    @Inject
    SNVCardRepository repository;
    private HomeCardDTO card;

    @Override
    protected void setupDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected void loadData() {
        card = repository.getHomeCard();
    }

    //intent when card tapped
    @Override
    protected void onClicked() {
        navigationCoordinator.navigateAfterSNVSelected(getActivity());
    }

    @Override
    protected void configureView(View view) {
        showGetStartedButton(view, card.hasNotStarted());
        showCompletedOnMainLogo(view, card.isDone());
        setupTitle(view);
        setupSubTitle(view, R.id.subtitle, card.isDone(), card.earnedPoints);

    }

    private void setupTitle(View view) {
        if (card.hasNotStarted()) {
            setFormattedString(view, R.id.title, R.string.home_card_card_earn_up_to_points_message_124, card.potentialPoints);
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
        return R.layout.fragment_snv_card;
    }

    private void showGetStartedButton(View rootView, boolean show) {
        rootView.findViewById(R.id.get_started_buttons).setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void showCompletedOnMainLogo(View rootView, boolean completed) {
        Drawable drawable = completed ?
                getTintedDrawable(R.drawable.completed_main, R.color.jungle_green) :
                getTintedDrawable(R.drawable.screenings_large, R.color.jungle_green);
        setImage(rootView, R.id.logo, drawable);
    }
    public static class Factory implements HomeScreenCardFragment.Factory {
        @Override
        public HomeScreenCardFragment buildFragment() {
            return new SNVCardFragment();
        }
    }

}
