package com.vitalityactive.va.shared.questionnaire.capture.viewholders;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.questionnaire.types.OptionQuestionDto;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;

class MultiSelectOptionViewHolder extends OptionViewHolder {

    public MultiSelectOptionViewHolder(View itemView, QuestionnaireCapturePresenter presenter) {
        super(itemView, presenter);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.vhr_question_check_button_option;
    }

    @Override
    protected boolean setItemChecked(OptionQuestionDto.Item dataItem, boolean isChecked) {
        if(dataItem.optionText.equalsIgnoreCase(getContext().getString(R.string.vhr_med_history_none_of_above_9999))){
            clearAllCheckedItems();
        } else {
            if(isNonSelected()){
                adapter.getData().get(adapter.getData().size()-1).checked = false;
            }
        }
        dataItem.checked = isChecked;
        return true;
    }

    private boolean isNonSelected() {
        for (OptionQuestionDto.Item item : adapter.getData()) {
            if(item.optionText.equalsIgnoreCase(getContext().getString(R.string.vhr_med_history_none_of_above_9999)) && item.checked == true){
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean getNewCheckStateAfterClicked(boolean isCurrentlyChecked) {
        // toggle state for checkbox
        return !isCurrentlyChecked;
    }
}
