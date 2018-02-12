package com.vitalityactive.va.vhc.learnmore;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItem;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreActivity;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreItem;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public abstract class BaseVHCLearnMoreActivity extends LearnMoreActivity {

    @Inject
    VHCHealthAttributeContent content;

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
                                navigationCoordinator.navigateAfterVHCLearnMoreItemTapped(BaseVHCLearnMoreActivity.this, (String)menuItemType.getValue());
                            }
                        })
                        .addMenuItems(getConfiguredMenuItems())
                        .build();
    }

    private List<MenuItem> getConfiguredMenuItems() {
        List<MenuItem> configuredMenuItems = new ArrayList<>();

        if (hasBMI()) {
            configuredMenuItems.add(MenuItem.Builder.bodyMassIndex(content.getBmiGroupTitle(), new MenuItemType<>(content.getBmiGroupTitle())));
        }
        if (hasWaistCircumference()) {
            configuredMenuItems.add(MenuItem.Builder.waistCircumference(content.getWaistCircumferenceGroupTitle(), new MenuItemType<>(content.getWaistCircumferenceGroupTitle())));
        }
        if (hasBloodGlucose()) {
            configuredMenuItems.add(MenuItem.Builder.bloodGlucose(content.getGlucoseGroupTitle(), new MenuItemType<>(content.getGlucoseGroupTitle())));
        }
        if (hasBloodPressure()) {
            configuredMenuItems.add(MenuItem.Builder.bloodPressure(content.getBloodPressureGroupTitle(), new MenuItemType<>(content.getBloodPressureGroupTitle())));
        }
        if (hasCholesterol()) {
            configuredMenuItems.add(MenuItem.Builder.cholesterol(content.getCholesterolGroupTitle(), new MenuItemType<>(content.getCholesterolGroupTitle())));
        }
        if (hasHbA1c()) {
            configuredMenuItems.add(MenuItem.Builder.hbA1C(content.getHbA1cGroupTitle(), new MenuItemType<>(content.getHbA1cGroupTitle())));
        }
        if (hasUrinaryProtein()) {
            configuredMenuItems.add(MenuItem.Builder.urinaryProtein(content.getUrinaryProteinGroupTitle(), new MenuItemType<>(content.getUrinaryProteinGroupTitle())));
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

    private boolean hasUrinaryProtein() {
        return !TextUtilities.isNullOrWhitespace(content.getUrinaryProteinSection1Title()) && insurerConfigurationRepository.hasFeatureTypeUrinaryProtein();
    }

    private boolean hasHbA1c() {
        return !TextUtilities.isNullOrWhitespace(content.getHba1cSection1Title()) && insurerConfigurationRepository.hasFeatureTypeHbA1c();
    }

    private boolean hasCholesterol() {
        return !TextUtilities.isNullOrWhitespace(content.getCholesterolSection1Title()) && insurerConfigurationRepository.hasFeatureTypeCholesterol();
    }

    private boolean hasBloodPressure() {
        return !TextUtilities.isNullOrWhitespace(content.getBloodPressureSection1Title()) && insurerConfigurationRepository.hasFeatureTypeBloodPressure();
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

}
