package com.vitalityactive.va.shared.questionnaire.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class CompletedAssessmentViewHolder extends GenericRecyclerViewAdapter.ViewHolder<QuestionnaireDTO> {
    private final GenericRecyclerViewAdapter.OnItemClickListener<QuestionnaireDTO> onItemClickListener;

    private CompletedAssessmentViewHolder(View itemView, GenericRecyclerViewAdapter.OnItemClickListener<QuestionnaireDTO> onItemClickListener) {
        super(itemView);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void bindWith(final QuestionnaireDTO dataItem) {
        ((TextView)itemView.findViewById(R.id.vhr_questionnaire_item_title)).setText(dataItem.getTitle());
        ((TextView)itemView.findViewById(R.id.subtitle)).setText(dataItem.getSubtitle());

        Button itemButton = (Button) itemView.findViewById(R.id.vhr_start_button);
        
        if (dataItem.isInProgress()) {
            itemButton.setText(R.string.landing_screen_continue_button_306);
        }

        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClicked(-1, dataItem);
            }
        });
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<QuestionnaireDTO,
            CompletedAssessmentViewHolder> {

        private final GenericRecyclerViewAdapter.OnItemClickListener<QuestionnaireDTO> onItemClickListener;

        public Factory(GenericRecyclerViewAdapter.OnItemClickListener<QuestionnaireDTO> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public CompletedAssessmentViewHolder createViewHolder(View itemView) {
            return new CompletedAssessmentViewHolder(itemView, onItemClickListener);
        }
    }
}
