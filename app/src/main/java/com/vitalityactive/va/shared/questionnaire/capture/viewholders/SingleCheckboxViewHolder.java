package com.vitalityactive.va.shared.questionnaire.capture.viewholders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.Space;

import com.vitalityactive.va.R;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.SingleCheckboxQuestionDto;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;

class SingleCheckboxViewHolder extends BaseViewHolder {
    private final CheckBox checkbox;
    private final View itemView;
    private final Space detailSpace;

    protected SingleCheckboxViewHolder(View itemView, QuestionnaireCapturePresenter presenter) {
        super(itemView, presenter);
        this.itemView = itemView;
        checkbox = (CheckBox) itemView.findViewById(R.id.vhr_question_checkbox);
        detailSpace = (Space) itemView.findViewById(R.id.vhr_question_detail_space);
        detailSpace.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setupQuestion(Question question) {
        setupQuestion((SingleCheckboxQuestionDto) question);
    }

    protected void setupQuestion(final SingleCheckboxQuestionDto question) {
        updateViewFromQuestionValue(question);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                question.setValue(!question.getValue());
                presenter.setQuestionAnswered(question);
                clearIsPrePopulated();
                updateViewFromQuestionValue(question);
            }
        });
    }

    private void updateViewFromQuestionValue(SingleCheckboxQuestionDto question) {
        checkbox.setChecked(question.getValue());
    }
}
