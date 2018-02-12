package com.vitalityactive.va.shared.questionnaire.capture.viewholders;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.questionnaire.types.OptionQuestionDto;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;

class SingleSelectOptionViewHolder extends OptionViewHolder {
    protected SingleSelectOptionViewHolder(View itemView, QuestionnaireCapturePresenter presenter) {
        super(itemView, presenter);
    }

    @Override
    protected boolean setItemChecked(OptionQuestionDto.Item dataItem, boolean isChecked) {
        if (isChecked && !dataItem.checked) {
            clearAllCheckedItems();
            dataItem.checked = true;
            adapter.notifyDataSetChanged();
            return true;
        }

        return false;
    }

    @Override
    protected boolean getNewCheckStateAfterClicked(boolean isCurrentlyChecked) {
        // set this as checked for radio buttons
        return true;
    }

    @Override
    @LayoutRes
    protected int getItemLayoutId() {
        return R.layout.vhr_question_radio_button_option;
    }
}
