package com.vitalityactive.va.activerewards;

import com.vitalityactive.va.R;
import com.vitalityactive.va.menu.MenuItem;
import com.vitalityactive.va.menu.MenuItemType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActiveRewardsLearnMoreActivity extends BaseActiveRewardsLearnMoreActivity {

    @Override
    protected List<List<MenuItem>> createMenuData() {
        return Collections.singletonList(Arrays.asList(
                new MenuItem(MenuItemType.Activity, R.drawable.participating_partners, R.string.AR_learn_more_participating_partners_title_696)
        ));
    }

    @Override
    public void onClicked(int position, MenuItem item) {
        if (position == 0) {
            navigationCoordinator.navigateOnParticipatingPartnersFromActiveRewardsLearnMore(this);
        } else {
            super.onClicked(position, item);
        }
    }
}
