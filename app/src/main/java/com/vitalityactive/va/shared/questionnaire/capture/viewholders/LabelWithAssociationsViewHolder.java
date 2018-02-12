package com.vitalityactive.va.shared.questionnaire.capture.viewholders;

import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;

public class LabelWithAssociationsViewHolder extends LabelOptionViewHolder {

    public LabelWithAssociationsViewHolder(View itemView, QuestionnaireCapturePresenter presenter) {
        super(itemView, presenter);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.vhr_question_label_child_detail_numeric;
    }
}
