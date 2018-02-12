package com.vitalityactive.va.shared.questionnaire.capture.viewholders;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionBasicInputValueDto;
import com.vitalityactive.va.questionnaire.validations.ValidationResult;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.implemented.TextWatcherWithDefaultBeforeAndAfter;

abstract class BasicInputValueViewHolder extends BaseViewHolder {
    protected final TextInputLayout inputLayout;
    protected final TextInputEditText inputText;
    protected TextWatcherWithDefaultBeforeAndAfter textChangedListener;

    public BasicInputValueViewHolder(View itemView, QuestionnaireCapturePresenter presenter) {
        super(itemView, presenter);
        inputText = itemView.findViewById(R.id.vhr_input_value);
        inputLayout = itemView.findViewById(R.id.vhr_input_layout);
    }

    @Override
    protected void setupQuestion(Question question) {
        setupQuestion((QuestionBasicInputValueDto) question);
    }

    private void setupQuestion(QuestionBasicInputValueDto question) {
        // This order is critical.
        // setText() causes onTextChanged() to be called, so if the text and focus listeners have not been overridden or cleared yet,
        // we end up answering the previous question
        // TODO: add validate(answer) method to QuestionnaireStateManager so we can validate an answer without having to re-answer the question every time a view holder gets re-used
        clearTextAndFocusChangeListeners();
        setupInputField(question);
        setupUnit(question);
        setupTextAndFocusListeners(question);
    }

    protected void setupInputField(QuestionBasicInputValueDto question) {
        inputText.setText(question.getValue());
        setupError(question);
        inputText.setInputType(question.getValueInputType());
        inputLayout.setHint(question.getHintText());
    }

    protected abstract void setupUnit(QuestionBasicInputValueDto question);

    protected void setupTextAndFocusListeners(final QuestionBasicInputValueDto question) {
        if (textChangedListener != null){
            inputText.removeTextChangedListener(textChangedListener);
        }

        textChangedListener = new TextWatcherWithDefaultBeforeAndAfter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = inputText.getText().toString();
                if (question.setValue(value)) {
                    ValidationResult validationResult = presenter.setQuestionAnswered(question);
                    clearIsPrePopulated();
                    setError(validationResult);
                }
            }
        };
        inputText.addTextChangedListener(textChangedListener);
    }

    private void setupError(QuestionBasicInputValueDto question) {
        if (TextUtilities.isNullOrWhitespace(question.getValue())) {
            inputLayout.setError(null);
        } else {
            setError(presenter.validate(question));
        }
    }

    protected void setError(ValidationResult validationResult) {
        boolean valid = validationResult.isValid();
        String errorMessage = null;
        if (!valid) {
            if (validationResult.getLowerLimit() != null && validationResult.getUpperLimit() != null) {
                errorMessage = getString(R.string.error_range_180, validationResult.getLowerLimit(), validationResult.getUpperLimit());
            } else if (validationResult.getLowerLimit() != null) {
                errorMessage = getString(R.string.error_range_bigger_281, validationResult.getLowerLimit());
            } else if (validationResult.getUpperLimit() != null) {
                errorMessage = getString(R.string.error_range_smaller_282, validationResult.getUpperLimit());
            } else {
                // TODO: should not happen
            }
        }
        inputLayout.setError(errorMessage);
    }

    private void clearTextAndFocusChangeListeners() {
        inputText.setOnFocusChangeListener(null);
        if (textChangedListener != null) {
            inputText.removeTextChangedListener(textChangedListener);
        }
    }
}
