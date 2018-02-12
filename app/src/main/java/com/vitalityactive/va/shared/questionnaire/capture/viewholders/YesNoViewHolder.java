package com.vitalityactive.va.shared.questionnaire.capture.viewholders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.YesNoQuestionDto;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;

class YesNoViewHolder extends BaseViewHolder {
    private final TextView yes;
    private final TextView no;
    private final int selectedColor;
    private final int deselectedColor;

    protected YesNoViewHolder(View itemView, QuestionnaireCapturePresenter presenter, int selectedColor, int deselectedColor) {
        super(itemView, presenter);
        yes = (TextView) itemView.findViewById(R.id.vhr_question_yes);
        no = (TextView) itemView.findViewById(R.id.vhr_question_no);
        this.selectedColor = selectedColor;
        this.deselectedColor = deselectedColor;
    }

    @Override
    protected void setupQuestion(Question question) {
        setupQuestion((YesNoQuestionDto) question);
    }

    private void setupQuestion(final YesNoQuestionDto question) {
        yes.setText(question.getYesDisplayString());
        no.setText(question.getNoDisplayString());
        yes.setOnClickListener(getClickListener(question, true));
        no.setOnClickListener(getClickListener(question, false));
        updateView(question);
    }

    private void updateView(YesNoQuestionDto question) {
        setViewSelected(yes, question.isAnswered() && question.getValue());
        setViewSelected(no, question.isAnswered() && !question.getValue());
    }

    private void setViewSelected(TextView view, Boolean selected) {
        view.setTextColor(selected ? selectedColor : deselectedColor);
    }

    @NonNull
    private View.OnClickListener getClickListener(final YesNoQuestionDto question, final boolean valueOnClick) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                question.setValue(valueOnClick);
                presenter.setQuestionAnswered(question);
                clearIsPrePopulated();
                updateView(question);
            }
        };
    }
}
