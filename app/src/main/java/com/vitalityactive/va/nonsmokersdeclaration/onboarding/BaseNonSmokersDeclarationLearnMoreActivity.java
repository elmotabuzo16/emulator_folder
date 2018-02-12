package com.vitalityactive.va.nonsmokersdeclaration.onboarding;

import com.vitalityactive.va.R;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersDeclarationContent;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreActivity;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public abstract class BaseNonSmokersDeclarationLearnMoreActivity extends LearnMoreActivity {
    @Inject
    NonSmokersDeclarationContent content;

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
        List<LearnMoreItem> items = new ArrayList<>();
        items.add(new LearnMoreItem(content.getLearnMoreSection1Title(), content.getLearnMoreSection1Content(), R.drawable.doc_icon_40));
        items.add(new LearnMoreItem(content.getLearnMoreSection2Title(), content.getLearnMoreSection2Content(), R.drawable.exsmoker_icon_40));
        if (shouldHaveCurrentSmokerItem()) {
            items.add(new LearnMoreItem(content.getLearnMoreSection3Title(), content.getLearnMoreSection3Content(), R.drawable.smoker_icon_40));
        }
        return Collections.singletonList(items);
    }

    protected boolean shouldHaveCurrentSmokerItem() {
        return true;
    }

    @Override
    protected void injectDependency() {
        getDependencyInjector().inject(this);
    }
}
