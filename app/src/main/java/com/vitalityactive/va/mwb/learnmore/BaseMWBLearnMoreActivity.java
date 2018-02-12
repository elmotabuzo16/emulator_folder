package com.vitalityactive.va.mwb.learnmore;

import android.util.Log;
import android.view.Menu;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItem;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.mwb.content.MWBHealthAttributeContent;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreActivity;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreItem;
import com.vitalityactive.va.utilities.TextUtilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by christian.j.p.capin on 2/6/2018.
 */

public abstract class BaseMWBLearnMoreActivity extends LearnMoreActivity {

    @Inject
    MWBHealthAttributeContent content;

    @Inject
    InsurerConfigurationRepository insurerConfigurationRepository;

    @Override
    protected void injectDependency() {
        getDependencyInjector().inject(this);
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.learn_more_button_title_104;
    }

    @Override
    protected String getHeaderTitle() {
        return content.getLearnMoreTitle();
    }

    @Override
    protected String getHeaderSubTitle() {
        return content.getLearnMoreContent();
    }

    @Override
    protected List<List<LearnMoreItem>> createListData() {
        return Collections.singletonList(Arrays.asList(
                new LearnMoreItem.Builder(content.getLearnMoreSection1Title(), content.getLearnMoreSection1Content())
                        .icon(R.drawable.device_card_icon_copy)
                        .resourceTintColor(R.color.jungle_green).iconTinted(false)
                        .build(),
                new LearnMoreItem(content.getLearnMoreSection2Title(), content.getLearnMoreSection2Content(), R.drawable.vhc_completed_big),
                new LearnMoreItem(content.getLearnMoreSection3Title(), content.getLearnMoreSection3Content(), R.drawable.vhc_earn_points_big)
        ));
    }

    @Override
    protected GenericRecyclerViewAdapter createSettingsAdapter() {
        return new MenuBuilder(this)
                .setClickListener(new MenuContainerViewHolder.OnMenuItemClickedListener() {
                    @Override
                    public void onClicked(MenuItemType menuItemType) {
                        //Log.e("cjc","HUEHUEHUE");
                        if(menuItemType.equals(MenuItemType.Disclaimer)){
                            navigationCoordinator.navigateAfterVHRDisclaimerTapped(BaseMWBLearnMoreActivity.this);
                        }else {
                            navigationCoordinator.navigateAfterVHCLearnMoreItemTapped(BaseMWBLearnMoreActivity.this, (String) menuItemType.getValue());
                        }
                    }
                })
                .addMenuItems(getConfiguredMenuItems())
                .addMenuItem(MenuItem.Builder.disclaimer())
                .build();
    }

    private List<MenuItem> getConfiguredMenuItems() {
        List<MenuItem> configuredMenuItems = new ArrayList<>();

        if (hasBMI()) {
            configuredMenuItems.add(MenuItem.Builder.bodyMassIndex("Stressor", new MenuItemType<>(content.getBmiGroupTitle())));
        }
        if (hasWaistCircumference()) {
            configuredMenuItems.add(MenuItem.Builder.waistCircumference("Psychological", new MenuItemType<>(content.getWaistCircumferenceGroupTitle())));
        }
        if (hasBloodGlucose()) {
            configuredMenuItems.add(MenuItem.Builder.bloodGlucose("Social", new MenuItemType<>(content.getGlucoseGroupTitle())));
        }

        marketUpdateUi(configuredMenuItems);

        return configuredMenuItems;
    }

    protected abstract void marketUpdateUi(List<MenuItem> configuredMenuItems);

    protected void sortAlphabetically(List<MenuItem> configuredMenuItems) {
        Collections.sort(configuredMenuItems, new Comparator<MenuItem>() {
            @Override
            public int compare(MenuItem first, MenuItem second) {
                return first.getText().compareTo(second.getText());
            }
        });
    }


    private boolean hasBloodGlucose() {
        return !TextUtilities.isNullOrWhitespace(content.getGlucoseSection1Title()) && insurerConfigurationRepository.hasFeatureTypeGlucose();
    }

    private boolean hasWaistCircumference() {
        return !TextUtilities.isNullOrWhitespace(content.getWaistCircumferenceSection1Title()) && insurerConfigurationRepository.hasFeatureTypeWaistCircumference();
    }

    private boolean hasBMI() {
        return !TextUtilities.isNullOrWhitespace(content.getBmiSection1Title()) && insurerConfigurationRepository.hasFeatureTypeBMI();
    }

    @Override
    protected boolean showDisclaimer() {
        return true;
    }
}
