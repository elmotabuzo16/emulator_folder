package com.vitalityactive.va.vitalitystatus;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreActivity;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreItem;

import java.util.Collections;
import java.util.List;

public class VitalityStatusLearnMoreActivity extends LearnMoreActivity {

    @Override
    protected int getActionBarTitle() {
        return R.string.learn_more_button_title_104;
    }

    @Override
    protected String getHeaderTitle() {
        return getString(R.string.Status_learn_more_section1_title_794);
    }

    @Override
    protected String getHeaderSubTitle() {
        return getString(R.string.Status_learn_more_subtitle_741);
    }

    @Override
    protected List<List<LearnMoreItem>> createListData() {
        return Collections.singletonList(Collections.singletonList(
                new LearnMoreItem(getString(R.string.Status_learn_more_section2_title_795),
                        getString(R.string.Status_learn_more_section1_content_743),
                        R.drawable.earn_points_40,
                        "",
                        R.color.colorPrimary,0, true)
        ));
    }
}
