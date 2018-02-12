package com.vitalityactive.va.home.cards;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class HomeScreenCardSectionViewHolder extends GenericRecyclerViewAdapter.ViewHolder<HomeScreenCardSection> {
    private static final int CARD_PADDING_LEFT_DP = 16;
    private static final int CARD_PADDING_RIGHT_DP = 34;
    private static final int CARD_PADDING_ABOVE_DP = 4;
    private static final int GAP_BETWEEN_CARDS_DP = 8;

    private final TextView sectionName;
    private final ViewPager viewPager;
    private FragmentManager fragmentManager;

    private HomeScreenCardSectionViewHolder(View itemView, FragmentManager fragmentManager) {
        super(itemView);
        sectionName = itemView.findViewById(R.id.heading);
        viewPager = itemView.findViewById(R.id.home_card_section_view_pager);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void bindWith(HomeScreenCardSection section) {
        sectionName.setText(section.nameResourceId);
        setupViewPager(section);
    }

    private void setupViewPager(HomeScreenCardSection section) {
        setupAUniqueIdThisSection(section);
        viewPager.setAdapter(new PagerAdapter(fragmentManager, section));
        viewPager.setOffscreenPageLimit(3);
        setupViewPagerPeeking();
        viewPager.setPageTransformer(false, getPageTransformer());
    }

    private void setupAUniqueIdThisSection(HomeScreenCardSection section) {
        // use a unique id for each of the viewPagers, since Android requires this for multiple view pagers
        // in a single fragment manager
        // see https://groups.google.com/forum/#!topic/android-developers/cKdvKyneHYY
        viewPager.setId(section.nameResourceId);
    }

    @NonNull
    private ViewPager.PageTransformer getPageTransformer() {
        return new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                int lastAdapterIndex = viewPager.getAdapter().getCount() - 1;

                if (viewPager.getCurrentItem() == lastAdapterIndex && lastAdapterIndex > 0) {
                    page.setTranslationX(ViewUtilities.pxFromDp(CARD_PADDING_RIGHT_DP) / 2);
                } else {
                    page.setTranslationX(0);
                }
            }
        };
    }

    private void setupViewPagerPeeking() {
        setupViewPadding();
        setupViewMarginsBetweenCards();

        viewPager.setClipToPadding(false);
    }

    private void setupViewMarginsBetweenCards() {
        int gapBetweenPages = ViewUtilities.pxFromDp(GAP_BETWEEN_CARDS_DP);
        viewPager.setPageMargin(gapBetweenPages);
    }

    private void setupViewPadding() {
        int paddingOnLeft = ViewUtilities.pxFromDp(CARD_PADDING_LEFT_DP);
        int paddingOnRight = ViewUtilities.pxFromDp(CARD_PADDING_RIGHT_DP);
        int paddingAboveBelow = ViewUtilities.pxFromDp(CARD_PADDING_ABOVE_DP);
        viewPager.setPadding(paddingOnLeft, paddingAboveBelow, paddingOnRight, paddingAboveBelow);
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<HomeScreenCardSection, HomeScreenCardSectionViewHolder> {
        private FragmentManager fragmentManager;

        public Factory(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }

        @Override
        public HomeScreenCardSectionViewHolder createViewHolder(View itemView) {
            return new HomeScreenCardSectionViewHolder(itemView, fragmentManager);
        }
    }

    private static class PagerAdapter extends FragmentStatePagerAdapter {
        private final HomeScreenCardSection section;

        PagerAdapter(FragmentManager fragmentManager, HomeScreenCardSection section) {
            super(fragmentManager);
            this.section = section;
        }

        @Override
        public Fragment getItem(int position) {
            return section.cardFactories.get(position).buildFragment();
        }

        @Override
        public int getCount() {
            return section.cardFactories.size();
        }
    }
}
