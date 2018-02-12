package com.vitalityactive.va.shared.questionnaire.viewholder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class QuestionnaireSetViewHolder
        extends GenericRecyclerViewAdapter.ViewHolder<QuestionnaireDTO> {

    private final GenericRecyclerViewAdapter.OnItemClickListener<QuestionnaireDTO> itemClickListener;
    private Button startButton;
    private TextView itemTitle;
    private TextView itemSubtitle;
    private Button editButton;

    private QuestionnaireSetViewHolder(View itemView,
                                       GenericRecyclerViewAdapter.OnItemClickListener<QuestionnaireDTO> itemClickListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;

        assignViews(itemView);
    }

    private void assignViews(View itemView) {
        itemTitle = (TextView) itemView.findViewById(R.id.vhr_questionnaire_item_title);
        startButton = (Button) itemView.findViewById(R.id.vhr_item_button_start);
        editButton = (Button) itemView.findViewById(R.id.vhr_item_button_edit);
        itemSubtitle = (TextView) itemView.findViewById(R.id.subtitle);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindWith(final QuestionnaireDTO dataItem) {
        itemTitle.setText(dataItem.getTitle());

        if (!dataItem.isCompleted()) {
            setItemAsNotCompleted(dataItem);
        } else {
            setItemAsCompleted();
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClicked(-1, dataItem);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClicked(-1, dataItem);
            }
        });
    }

    private void setItemAsCompleted() {
        showEditButton();

        itemSubtitle.setText(R.string.landing_screen_completed_message_327);
        itemView.findViewById(R.id.vhr_completed_icon).setVisibility(View.VISIBLE);
    }

    private void showEditButton() {
        editButton.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.INVISIBLE);
    }

    private void setItemAsNotCompleted(QuestionnaireDTO dataItem) {
        showStartButton();

        itemSubtitle.setText(dataItem.getSubtitle());

        if (dataItem.isInProgress()) {
            startButton.setText(R.string.landing_screen_continue_button_306);
        } else {
            startButton.setText(R.string.landing_screen_start_button_305);
        }
    }

    private void showStartButton() {
        startButton.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.INVISIBLE);
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<QuestionnaireDTO, QuestionnaireSetViewHolder> {
        private final GenericRecyclerViewAdapter.OnItemClickListener<QuestionnaireDTO> itemClickListener;

        public Factory(GenericRecyclerViewAdapter.OnItemClickListener<QuestionnaireDTO> itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public QuestionnaireSetViewHolder createViewHolder(View itemView) {
            return new QuestionnaireSetViewHolder(itemView, itemClickListener);
        }
    }
}
