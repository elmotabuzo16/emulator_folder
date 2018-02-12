package com.vitalityactive.va.shared.questionnaire.capture.viewholders;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.questionnaire.types.LabelOptionDto;
import com.vitalityactive.va.questionnaire.types.LabelWithAssociationsQuestionDto;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.validations.ValidationResult;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.implemented.TextWatcherWithDefaultBeforeAndAfter;

import java.util.List;

public abstract class LabelOptionViewHolder extends BaseViewHolder {

    private final RecyclerView listView;
    protected Adapter adapter;

    public LabelOptionViewHolder(View itemView, QuestionnaireCapturePresenter presenter) {
        super(itemView, presenter);
        listView = itemView.findViewById(R.id.vhr_question_label_list);
    }

    @Override
    protected void setupQuestion(Question question) {
        if (question instanceof LabelOptionDto) {
            setupQuestion((LabelOptionDto) question);
        }
    }

    private void setupQuestion(LabelOptionDto question) {
        setListViewAdapter(question);
    }

    private void setListViewAdapter(LabelOptionDto question) {
        adapter = new Adapter(listView.getContext(),
                question.getItems(),
                getItemLayoutId(),
                new Factory(question));
        listView.setAdapter(adapter);
    }

    @LayoutRes
    protected abstract int getItemLayoutId();

    static class Adapter extends GenericRecyclerViewAdapter<LabelOptionDto.Item, ItemViewHolder> {
        public Adapter(Context context, List<LabelOptionDto.Item> data, int viewResourceId, Factory factory) {
            super(context, data, viewResourceId, factory);
        }
    }

    class ItemViewHolder extends GenericRecyclerViewAdapter.ViewHolder<LabelOptionDto.Item> {
        private final TextInputLayout itemLayout;
        private final TextInputEditText itemEditText;
        private TextWatcherWithDefaultBeforeAndAfter itemWatcher;
        private final LabelOptionDto question;

        public ItemViewHolder(View itemView, LabelOptionDto question) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.vhr_label_input_layout);
            itemEditText = itemView.findViewById(R.id.vhr_label_input_text);
            this.question = question;
        }

        @Override
        public void bindWith(final LabelOptionDto.Item dataItem) {
            setupQuestion(dataItem);
        }

        private void setupQuestion(LabelOptionDto.Item dataItem) {
            // This order is critical.
            // setText() causes onTextChanged() to be called, so if the text and focus listeners have not been overridden or cleared yet,
            // we end up answering the previous question
            // TODO: add validate(answer) method to QuestionnaireStateManager so we can validate an answer without having to re-answer the question every time a view holder gets re-used
            clearTextAndFocusChangeListeners();
            setupInputField(dataItem);
            setupTextAndFocusListeners(dataItem);
        }

        private void clearTextAndFocusChangeListeners() {
            itemEditText.setOnFocusChangeListener(null);
            if (itemWatcher != null) {
                itemEditText.removeTextChangedListener(itemWatcher);
            }
        }

        private void setupInputField(LabelOptionDto.Item dataItem) {
            itemEditText.setText(dataItem.textValue);
            setupError(dataItem);
            itemEditText.setInputType(dataItem.valueInputType);
            itemLayout.setHint(dataItem.hintLabel);
        }

        private void setupError(LabelOptionDto.Item dataItem) {
            if (TextUtilities.isNullOrWhitespace(dataItem.textValue)) {
                itemLayout.setError(null);
            } else {
                if (question.getAssociatedChildTypeKeys() != null && question.getAssociatedChildTypeKeys().size() > 0
                        && question instanceof LabelWithAssociationsQuestionDto) {
                        dataItem.validationInProgress = true;
                        question.setActiveChildTypeKeyForValidation(dataItem.typeKey);
                        setError(presenter.validate(question));
                        dataItem.validationInProgress = false;
                } else {
                    setError(presenter.validate(question));
                }
            }
        }

        private void setupTextAndFocusListeners(final LabelOptionDto.Item dataItem) {
            itemWatcher = new TextWatcherWithDefaultBeforeAndAfter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String value = itemEditText.getText().toString();
                    if (dataItem.setValue(value)) {
                        dataItem.validationInProgress = true;

                        question.setActiveChildTypeKeyForValidation(dataItem.typeKey);
                        ValidationResult validationResult = presenter.setQuestionAnswered(question);
                        clearIsPrePopulated();
                        if (!TextUtilities.isNullOrWhitespace(value)) {
                            setError(validationResult);
                        }
                        dataItem.validationInProgress = false;
                    }
                }
            };
            itemEditText.addTextChangedListener(itemWatcher);
        }

        private void setError(ValidationResult validationResult) {
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
            itemLayout.setError(errorMessage);
        }
    }

    class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<LabelOptionDto.Item, ItemViewHolder> {
        private final LabelOptionDto question;

        public Factory(LabelOptionDto question) {
            this.question = question;
        }

        @Override
        public ItemViewHolder createViewHolder(View itemView) {
            return new ItemViewHolder(itemView, question);
        }
    }
}
