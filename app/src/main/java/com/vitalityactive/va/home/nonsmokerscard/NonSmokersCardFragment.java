package com.vitalityactive.va.home.nonsmokerscard;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.cards.HomeScreenCardFragment;

import javax.inject.Inject;

public class NonSmokersCardFragment extends HomeScreenCardFragment {
    @Inject
    protected NonSmokersCardRepository repository;
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
        if (card.status != HomeCardType.StatusType.DONE) {
            navigationCoordinator.navigateAfterNonSmokersDeclarationSelected(getActivity());
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.non_smokers_home_card;
    }

    @Override
    protected void configureView(View view) {
        TextView title = (TextView) view.findViewById(R.id.title);
        View button = view.findViewById(R.id.confirm_status_button);
        TextView subtitle = (TextView) view.findViewById(R.id.subtitle);

        if (card.status == HomeCardType.StatusType.DONE) {
            button.setVisibility(View.GONE);
            title.setText(R.string.confirmation_completed_title_117);
            configureSubtitle(card, subtitle);
            setImage(view, R.id.logo, getCompletedDrawable());
        } else {
            button.setVisibility(View.VISIBLE);
            subtitle.setVisibility(View.GONE);
            title.setText(getString(R.string.home_card_card_potential_points_message_97, card.potentialPoints));
            setImage(view, R.id.logo, getInProgressDrawable());
        }
    }

    private void configureSubtitle(HomeCardDTO card, TextView subtitle) {
        subtitle.setVisibility(View.VISIBLE);
        subtitle.setText(getString(R.string.home_card_card_earned_points_message_121, card.earnedPoints));
    }

    private Drawable getInProgressDrawable() {
        return getTintedDrawable(R.drawable.non_smokers_main, R.color.jungle_green);
    }

    private Drawable getCompletedDrawable() {
        return getTintedDrawable(R.drawable.completed_main, R.color.jungle_green);
    }

    public static class Factory implements HomeScreenCardFragment.Factory {
        @Override
        public HomeScreenCardFragment buildFragment() {
            return new NonSmokersCardFragment();
        }
    }
}
