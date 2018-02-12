package com.vitalityactive.va.shared.questionnaire.capture.viewholders;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.R;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionType;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final LayoutInflater inflater;
    private final QuestionnaireCapturePresenter presenter;
    private final int themeBlack;
    private final int selectedColor;
    private final int deselectedColor;
    private List<Question> questions;

    public Adapter(Context context,
                   QuestionnaireCapturePresenter presenter,
                   List<Question> questions,
                   int selectedColor,
                   int deselectedColor,
                   int themeBlack) {
        this.inflater = LayoutInflater.from(context);
        this.presenter = presenter;
        this.questions = questions;
        this.themeBlack = themeBlack;
        this.selectedColor = selectedColor;
        this.deselectedColor = deselectedColor;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        QuestionType type = QuestionType.fromValue(viewType);
        View view = inflater.inflate(getLayoutId(type), parent, false);
        return getViewHolder(type, view);
    }

    @LayoutRes
    private int getLayoutId(QuestionType type) {
        switch (type) {
            case FREE_TEXT:
                return R.layout.vhr_question_input_free_text;
            case BASIC_INPUT_VALUE:
                return R.layout.vhr_question_input_value_fixed_unit;
            case INPUT_VALUE_WITH_UNIT:
                return R.layout.vhr_question_input_value_selectable_unit;
            case SINGLE_SELECT_OPTION:
            case MULTI_SELECT_OPTION:
                return R.layout.vhr_question_multi_option_list;
            case LABEL:
                return R.layout.vhr_question_label;
            case YES_NO:
                return R.layout.vhr_question_yes_no;
            case SINGLE_CHECKBOX:
                return R.layout.vhr_question_single_checkbox;
            case DATE:
                return R.layout.vhr_question_date;
            case LABEL_WITH_ASSOCIATIONS:
                return R.layout.vhr_question_label_with_associations;
            case SECTION_DESCRIPTION:
                return R.layout.assessment_section_description;
        }
        return 0;
    }

    @Nullable
    private BaseViewHolder getViewHolder(QuestionType type, View view) {
        switch (type) {
            case BASIC_INPUT_VALUE:
                return new SingleUnitInputValueViewHolder(view, presenter);
            case FREE_TEXT:
                return new FreeTextInputViewHolder(view, presenter);
            case INPUT_VALUE_WITH_UNIT:
                return new MultiUnitInputValueViewHolder(view, presenter);
            case SINGLE_SELECT_OPTION:
                return new SingleSelectOptionViewHolder(view, presenter);
            case MULTI_SELECT_OPTION:
                return new MultiSelectOptionViewHolder(view, presenter);
            case LABEL:
                return new LabelViewHolder(view, presenter);
            case YES_NO:
                return new YesNoViewHolder(view, presenter, selectedColor, deselectedColor);
            case SINGLE_CHECKBOX:
                return new SingleCheckboxViewHolder(view, presenter);
            case DATE:
                return new DateInputViewHolder(view, presenter, themeBlack);
            case LABEL_WITH_ASSOCIATIONS:
                return new LabelWithAssociationsViewHolder(view, presenter);
            case SECTION_DESCRIPTION:
                return new LabelViewHolder(view, presenter);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return questions.get(position).getQuestionType().getValue();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindWith(questions.get(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public void replaceItems(List<Question> questions) {
        for (Question question : questions) {
            int indexOf = this.questions.indexOf(question);
            if (indexOf >= 0) {
                boolean canBeAnsweredChanged = canBeAnsweredChanged(question, indexOf);
                setItem(question, indexOf);
                if (canBeAnsweredChanged) {
                    notifyItemChanged(indexOf);
                }
            }
        }
    }

    private boolean canBeAnsweredChanged(Question question, int indexOf) {
        return question.getCanBeAnswered() != this.questions.get(indexOf).getCanBeAnswered();
    }

    private void setItem(Question question, int indexOf) {
        this.questions.set(indexOf, question);
    }
}
