package com.vitalityactive.va.shared.questionnaire.capture.viewholders;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionDateInputDto;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

class DateInputViewHolder extends BaseViewHolder {
    private final TextView dateText;

    protected DateInputViewHolder(View itemView,
                                  QuestionnaireCapturePresenter presenter,
                                  int themeBlack) {

        super(itemView, presenter);

        dateText = itemView.findViewById(R.id.vhr_question_date_picker);

        ViewUtilities.setSpinnerColor(dateText, themeBlack);
    }

    @Override
    protected void setupQuestion(Question question) {
        setupQuestion((QuestionDateInputDto) question);
    }

    private void setupQuestion(final QuestionDateInputDto question) {
        dateText.setText(getFormattedDateStringFromLocalDate(question.getValue()));

        setOnClickListener(question);
    }

    private void setOnClickListener(final QuestionDateInputDto question) {
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        question.setValue(new LocalDate(year, getMonthInOneIndex(month), dayOfMonth));
                        presenter.setQuestionAnswered(question);
                        clearIsPrePopulated();

                        updateViewFromQuestionValue(question);
                    }
                };

                buildDatePicker(listener, question).show();
            }
        });
    }


    private DatePickerDialog buildDatePicker(DatePickerDialog.OnDateSetListener listener,
                                             QuestionDateInputDto question) {
        final LocalDate questionDate = getDefaultDate(question);

        DatePickerDialog datePickerDialog = new DatePickerDialog(dateText.getContext(),
                listener,
                questionDate.getYear(),
                getMonthInZeroIndex(questionDate.getMonth()),
                questionDate.getDayOfMonth());

        datePickerDialog.getDatePicker().setMaxDate(TimeUtilities.getCurrentTimestamp());

        return datePickerDialog;
    }

    @NonNull
    private LocalDate getDefaultDate(QuestionDateInputDto question) {
        return question.getValue() == null ? LocalDate.now() : question.getValue();
    }

    private int getMonthInZeroIndex(int month) {
        return month - 1;
    }

    private int getMonthInOneIndex(int month) {
        return month + 1;
    }

    private void updateViewFromQuestionValue(QuestionDateInputDto question) {
        dateText.setText(getFormattedDateStringFromLocalDate(question.getValue()));
    }

    private String getFormattedDateStringFromLocalDate(LocalDate date) {
        return date == null ? null : DateFormattingUtilities.formatDateMonthAbbreviatedYear(itemView.getContext(), date);
    }
}
