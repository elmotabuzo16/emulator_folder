package com.vitalityactive.va.shared.questionnaire.capture.viewholders;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.questionnaire.types.QuestionBasicInputValueDto;
import com.vitalityactive.va.questionnaire.validations.ValidationResult;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;
import com.vitalityactive.va.utilities.implemented.OnItemSelectedPositionChangedListener;
import com.vitalityactive.va.utilities.implemented.TextWatcherWithDefaultBeforeAndAfter;
import com.vitalityactive.va.vhc.dto.UnitAbbreviationDescription;

import java.util.List;
import java.util.regex.Pattern;

class MultiUnitInputValueViewHolder extends BasicInputValueViewHolder {
    private final Spinner inputUnit;

    private final TextInputLayout inputSecondLayout;
    private final TextInputEditText inputSecondText;

    MultiUnitInputValueViewHolder(View itemView, QuestionnaireCapturePresenter presenter) {
        super(itemView, presenter);
        inputUnit = itemView.findViewById(R.id.vhr_input_unit);

        inputSecondText = itemView.findViewById(R.id.vhr_input_value_second);
        inputSecondLayout = itemView.findViewById(R.id.vhr_input_layout_second);

        if (inputSecondLayout != null) {
            inputSecondLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setupUnit(final QuestionBasicInputValueDto question) {
        final List<UnitAbbreviationDescription> availableUnits = question.getUnits();
        ArrayAdapter<UnitAbbreviationDescription> adapter =
                new ArrayAdapter<UnitAbbreviationDescription>(
                        itemView.getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        availableUnits) {
                    @NonNull
                    @Override
                    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                        TextView v = (TextView) super.getView(position, convertView, parent);
                        v.setText(availableUnits.get(position).getAbbreviation());
                        return v;
                    }
                };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputUnit.setAdapter(adapter);
        inputUnit.setSelection(availableUnits.indexOf(question.getSelectedUnit()));

        inputUnit.setOnItemSelectedListener(new OnItemSelectedPositionChangedListener() {
            @Override
            protected void onSelectedItemChanged(int position) {
                final List<UnitAbbreviationDescription> availableUnits = question.getUnits();
                final boolean wasChanged = question.setSelectedUnit(availableUnits.get(position));

                UnitAbbreviationDescription selected = availableUnits.get(position);
                assessTwoFieldUnitOfMeasure(selected);
                setupTextAndFocusListeners(question);
                setupInputField(question);

                if (wasChanged) {
                    question.setValue(null);
                    updateView(question);
                    presenter.setQuestionAnswered(question);
                    clearIsPrePopulated();
                }
            }
        });

        assessTwoFieldUnitOfMeasure(availableUnits.get(0));
        setupTextAndFocusListeners(question);
        setupInputField(question);

    }


    private void updateView(QuestionBasicInputValueDto question) {
        ((TextView) itemView.findViewById(R.id.vhr_input_value)).setText(question.getValue());
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