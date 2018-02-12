package com.vitalityactive.va.myhealth.vitalityageprofile;

import com.vitalityactive.va.myhealth.content.FeedbackTipItem;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public interface MyHealthFeedbackTipClickListener<F> extends GenericRecyclerViewAdapter.OnItemClickListener<FeedbackTipItem> {
    void onClicked(int position, int section, int sectionTypeKey, int attributeTypeKey);
}