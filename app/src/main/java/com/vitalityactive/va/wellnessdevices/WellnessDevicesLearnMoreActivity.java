package com.vitalityactive.va.wellnessdevices;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreActivity;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreItem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WellnessDevicesLearnMoreActivity extends LearnMoreActivity {
    @Override
    protected int getActionBarTitle() {
        return R.string.learn_more_button_title_104;
    }

    @Override
    protected String getHeaderTitle() {
        return getString(R.string.WDA_learn_more_item1_heading_427);
    }

    @Override
    protected String getHeaderSubTitle() {
        return getString(R.string.WDA_learn_more_item1_428);
    }

    @Override
    protected List<List<LearnMoreItem>> createListData() {
        return Collections.singletonList(Arrays.asList(
                new LearnMoreItem(getString(R.string.WDA_learn_more_item2_heading_429), getString(R.string.WDA_learn_more_item2_430, (Object)null), R.drawable.device_icon_med_small),
                new LearnMoreItem.Builder(getString(R.string.WDA_learn_more_item3_heading_431), getString(R.string.WDA_learn_more_item3_432))
                        .icon(R.drawable.points_med).resourceTintColor(R.color.waterBlue).iconTinted(true).build()
        ));
    }

}
