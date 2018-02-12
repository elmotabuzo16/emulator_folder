package com.vitalityactive.va.vna;

import com.vitalityactive.va.R;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItem;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.shared.content.OnboardingContent;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreActivityWithContent;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreItem;
import com.vitalityactive.va.vna.content.VNAContent;

import java.util.List;

import javax.inject.Inject;

public class VNALearnMoreActivity extends LearnMoreActivityWithContent {
    @Inject
    VNAContent content;

    @Override
    protected void injectDependency() {
        getDependencyInjector().inject(this);
    }

    @Override
    protected OnboardingContent getContent() {
        return content;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.learn_more_button_title_104;
    }

    @Override
    protected List<List<LearnMoreItem>> createListData() {
        return createListData(R.color.jungle_green,
                R.drawable.time_to_complete_40, R.drawable.earn_points_40, R.drawable.nutrition_40);
    }

    @Override
    protected GenericRecyclerViewAdapter createSettingsAdapter() {
        return new MenuBuilder(this)
                .setClickListener(new MenuContainerViewHolder.OnMenuItemClickedListener() {
                    @Override
                    public void onClicked(MenuItemType menuItemType) {
                        navigationCoordinator.navigateAfterVNADisclaimerTapped(VNALearnMoreActivity.this);
                    }
                })
                .addMenuItem(MenuItem.Builder.disclaimer())
                .build();
    }
}
