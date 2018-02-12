package com.vitalityactive.va.shared.questionnaire.capture.viewholders;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionFreeTextDto;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;
import com.vitalityactive.va.utilities.implemented.TextWatcherWithDefaultBeforeAndAfter;

class FreeTextInputViewHolder extends BaseViewHolder {
    private final TextInputEditText inputText;
    private final TextInputLayout inputTextLayout;
    private TextWatcherWithDefaultBeforeAndAfter textChangedListener;

    FreeTextInputViewHolder(View itemView, QuestionnaireCapturePresenter presenter) {
        super(itemView, presenter);

        inputText = (TextInputEditText) itemView.findViewById(R.id.vhr_input_value);
        inputTextLayout = (TextInputLayout) itemView.findViewById(R.id.vhr_input_layout);
    }

    @Override
    protected void setupQuestion(final Question question) {
        setupQuestion((QuestionFreeTextDto) question);
    }

    private void setupQuestion(final QuestionFreeTextDto question) {
        resetInputField();
        inputText.setText(question.getValue());
        setupTextAndFocusListeners(question);
        setOverflowCounter(question);
    }

    private void setOverflowCounter(QuestionFreeTextDto question) {
        Integer maxLength = question.getMaxLength();
        if (maxLength != 0) {
            inputTextLayout.setCounterEnabled(true);
            inputTextLayout.setCounterMaxLength(question.getMaxLength());
        }
    }

    private void resetInputField() {
        clearTextAndFocusChangeListeners();
        inputText.setText(null);
    }

    private void setupTextAndFocusListeners(final QuestionFreeTextDto question) {
        textChangedListener = new TextWatcherWithDefaultBeforeAndAfter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = inputText.getText().toString();
                if (question.setValue(value)) {
                    boolean valid = presenter.setQuestionAnswered(question).isValid();
                    clearIsPrePopulated();

                    // todo: free text questions have no validation except length
                    if (!valid && question.getValue().length() <= question.getMaxLength()) {
                        inputTextLayout.setError("Required");
                    } else {
                        inputTextLayout.setError(null);
                    }
                }
            }
        };
        inputText.addTextChangedListener(textChangedListener);
    }

    private void clearTextAndFocusChangeListeners() {
        inputText.setOnFocusChangeListener(null);
        if (textChangedListener != null) {
            inputText.removeTextChangedListener(textChangedListener);
        }
    }

    @Override
    protected void setupDetails(String text, boolean visible) {
        super.setupDetails("", false);
        // TODO: 2017/07/05 Where does this value come from?
        inputTextLayout.setHint(itemView.getContext().getString(R.string.vhr_free_text_placeholder));
    }
}
