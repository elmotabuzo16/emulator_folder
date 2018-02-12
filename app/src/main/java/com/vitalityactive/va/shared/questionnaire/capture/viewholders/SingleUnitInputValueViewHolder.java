package com.vitalityactive.va.shared.questionnaire.capture.viewholders;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.questionnaire.types.QuestionBasicInputValueDto;
import com.vitalityactive.va.questionnaire.validations.ValidationResult;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;
import com.vitalityactive.va.utilities.implemented.TextWatcherWithDefaultBeforeAndAfter;
import com.vitalityactive.va.vhc.dto.UnitAbbreviationDescription;

import java.util.regex.Pattern;

class SingleUnitInputValueViewHolder extends BasicInputValueViewHolder {
    private final TextView inputUnit;

    private final TextInputLayout inputSecondLayout;
    private final TextInputEditText inputSecondText;

    public SingleUnitInputValueViewHolder(View itemView, QuestionnaireCapturePresenter presenter) {
        super(itemView, presenter);
        inputUnit = itemView.findViewById(R.id.vhr_input_unit);

        inputSecondText = itemView.findViewById(R.id.vhr_input_value_second);
        inputSecondLayout = itemView.findViewById(R.id.vhr_input_layout_second);

        if (inputSecondLayout != null) {
            inputSecondLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setupUnit(QuestionBasicInputValueDto question) {
        inputUnit.setText(question.getSingleUnit());

        if (!question.getUnits().isEmpty()) {
            UnitAbbreviationDescription unitItem = question.getUnits().get(0);

            assessTwoFieldUnitOfMeasure(unitItem);
            setupTextAndFocusListeners(question);
            setupInputField(question);
        }
    }

    private void assessTwoFieldUnitOfMeasure(UnitAbbreviationDescription selected) {
        if (inputSecondLayout != null) {
            if (selected.isASpecialMultiValuedUnit()) {
                inputSecondLayout.setVisibility(View.VISIBLE);
                inputLayout.setHint(selected.getUnit1().name);
                inputSecondLayout.setHint(selected.getUnit2().name);
                inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputSecondText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                inputSecondLayout.setVisibility(View.GONE);
                inputLayout.setHint("");
                inputText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
        }
    }

    @Override
    protected void setupTextAndFocusListeners(QuestionBasicInputValueDto question) {
        if (inputSecondLayout != null && inputSecondLayout.getVisibility() == View.VISIBLE) {
            inputText.removeTextChangedListener(textChangedListener);
            inputSecondText.removeTextChangedListener(textChangedListener);

            textChangedListener = new TextWatcherWithDefaultBeforeAndAfter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String value1 = inputText.getText().toString();
                    String value2 = inputSecondText.getText().toString();
                    if (question.setValue(createValuesFromMultiUnit(value1, value2))) {
                        ValidationResult validationResult = presenter.setQuestionAnswered(question);
                        clearIsPrePopulated();
                        setError(validationResult);
                    }
                }
            };

            inputText.addTextChangedListener(textChangedListener);
            inputSecondText.addTextChangedListener(textChangedListener);
        } else {
            super.setupTextAndFocusListeners(question);
        }
    }

    private String createValuesFromMultiUnit(String value1, String value2) {
        String final1 = TextUtils.isEmpty(value1) ? "0" : String.valueOf(Integer.parseInt(value1));
        String final2 = TextUtils.isEmpty(value2) ? "0" : String.valueOf(Integer.parseInt(value2));
        ;
        return final1 + "." + final2;
    }


    @Override
    protected void setupInputField(QuestionBasicInputValueDto question) {
        if (inputSecondLayout != null && inputSecondLayout.getVisibility() == View.VISIBLE) {
            String valueAsText = TextUtils.isEmpty(question.getValue()) ? "" : question.getValue();
            String[] splitValue = valueAsText.split(Pattern.quote("."));

            if (splitValue.length >= 1) {
                inputText.setText(splitValue[0]);
            }

            if (splitValue.length >= 2) {
                inputSecondText.setText(splitValue[1]);
            } else {
                if (!TextUtils.isEmpty(question.getValue())) {
                    inputSecondText.setText("0");
                }
            }
        } else {
            super.setupInputField(question);
        }
    }
}
