package com.vitalityactive.va.uicomponents.learnmore;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.vitalityactive.va.shared.content.OnboardingContent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class LearnMoreActivityWithContent extends LearnMoreActivity {
    @Override
    protected String getHeaderTitle() {
        return getContent().getLearnMoreTitle();
    }

    @Override
    protected String getHeaderSubTitle() {
        return getContent().getLearnMoreContent();
    }

    protected List<List<LearnMoreItem>> createListData(@ColorRes int tintColor,
                                                       @DrawableRes int section1Icon,
                                                       @DrawableRes int section2Icon,
                                                       @DrawableRes int section3Icon) {
        OnboardingContent content = getContent();
        return Collections.singletonList(Arrays.asList(
                new LearnMoreItem.Builder(content.getLearnMoreSection1Title(), content.getLearnMoreSection1Content())
                        .icon(section1Icon).resourceTintColor(tintColor).iconTinted(true).build(),
                new LearnMoreItem.Builder(content.getLearnMoreSection2Title(), content.getLearnMoreSection2Content())
                        .icon(section2Icon).resourceTintColor(tintColor).iconTinted(true).build(),
                new LearnMoreItem.Builder(content.getLearnMoreSection3Title(), content.getLearnMoreSection3Content())
                        .icon(section3Icon).resourceTintColor(tintColor).iconTinted(true).build()
        ));
    }

    protected abstract OnboardingContent getContent();
}
