package com.vitalityactive.va.vhr;

import com.vitalityactive.va.R;
import com.vitalityactive.va.shared.content.OnboardingContent;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreActivityWithContent;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreItem;
import com.vitalityactive.va.vhr.content.VHRContent;

import java.util.List;

import javax.inject.Inject;

public class VHRLearnMoreActivity extends LearnMoreActivityWithContent {
    @Inject
    VHRContent content;

    @Override
    protected void injectDependency() {
        getDependencyInjector().inject(this);
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.learn_more_button_title_104;
    }

    @Override
    protected OnboardingContent getContent() {
        return content;
    }

    @Override
    protected List<List<LearnMoreItem>> createListData() {
        return createListData(R.color.jungle_green,
                R.drawable.learn_more_earn_points, R.drawable.learn_more_vitality_age, R.drawable.learn_more_time_to_complete);
    }
}
