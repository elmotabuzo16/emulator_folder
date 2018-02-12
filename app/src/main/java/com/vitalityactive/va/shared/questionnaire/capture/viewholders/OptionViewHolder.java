package com.vitalityactive.va.shared.questionnaire.capture.viewholders;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Space;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.questionnaire.types.OptionQuestionDto;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.List;

abstract class OptionViewHolder extends BaseViewHolder {
    private final RecyclerView listView;
    protected Adapter adapter;

    public OptionViewHolder(View itemView, QuestionnaireCapturePresenter presenter) {
        super(itemView, presenter);
        listView = (RecyclerView) itemView.findViewById(R.id.vhr_question_list);
        Space spaceView = (Space) itemView.findViewById(R.id.vhr_question_detail_space);
        spaceView.setVisibility(View.VISIBLE);
        ViewUtilities.addDividers(listView.getContext(), listView, ViewUtilities.pxFromDp(16));
    }

    @Override
    protected void setupQuestion(Question question) {
        if (question instanceof OptionQuestionDto) {
            setupQuestion((OptionQuestionDto) question);
        }
    }

    private void setupQuestion(OptionQuestionDto question) {
        setListViewAdapter(question);
    }

    private void setListViewAdapter(OptionQuestionDto question) {
        adapter = new Adapter(listView.getContext(),
                question.getItems(),
                getItemLayoutId(),
                new Factory(question));
        listView.setAdapter(adapter);
    }


    @LayoutRes
    protected abstract int getItemLayoutId();

    private void setItemChecked(OptionQuestionDto question, OptionQuestionDto.Item dataItem, boolean isChecked) {
        if (setItemChecked(dataItem, isChecked)) {
            question.onCheckedItemsChanged();
            presenter.setQuestionAnswered(question);
            clearIsPrePopulated();

            setListViewAdapter(question);
        }
    }

    protected abstract boolean setItemChecked(OptionQuestionDto.Item dataItem, boolean isChecked);

    protected abstract boolean getNewCheckStateAfterClicked(boolean isCurrentlyChecked);

    protected void clearAllCheckedItems() {
        for (OptionQuestionDto.Item item : adapter.getData()) {
            item.checked = false;
        }
    }

    static class Adapter extends GenericRecyclerViewAdapter<OptionQuestionDto.Item, SingleSelectOptionViewHolder.ItemViewHolder> {
        public Adapter(Context context, List<OptionQuestionDto.Item> data, int viewResourceId, SingleSelectOptionViewHolder.Factory factory) {
            super(context, data, viewResourceId, factory);
        }
    }

    class ItemViewHolder extends GenericRecyclerViewAdapter.ViewHolder<OptionQuestionDto.Item> {
        private final TextView text;
        private final TextView detail;
        private final CompoundButton check;
        private final TextView note;
        private final OptionQuestionDto question;

        public ItemViewHolder(View itemView, OptionQuestionDto question) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.vhr_option_text);
            detail = (TextView) itemView.findViewById(R.id.vhr_option_detail);
            check = (CompoundButton) itemView.findViewById(R.id.vhr_option_check);
            note = (TextView) itemView.findViewById(R.id.vhr_option_hint);
            this.question = question;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = OptionViewHolder.this.getNewCheckStateAfterClicked(check.isChecked());
                    check.setChecked(checked);
                }
            });
        }

        @Override
        public void bindWith(final OptionQuestionDto.Item dataItem) {
            text.setText(dataItem.optionText);
            note.setText(dataItem.note);
            
            setDetailTextIfAvailable(dataItem);
            setHintVisibility(dataItem.hasNoteWhenSelected() && dataItem.checked);

            check.setChecked(dataItem.checked);

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    OptionViewHolder.this.setItemChecked(question, dataItem, isChecked);
                    setHintVisibility(dataItem.hasNoteWhenSelected() && isChecked);
                }
            });
        }

        private void setDetailTextIfAvailable(OptionQuestionDto.Item dataItem) {
            if (dataItem.detailText == null || dataItem.detailText.isEmpty()) {
                detail.setVisibility(View.GONE);
            } else {
                detail.setVisibility(View.VISIBLE);
                detail.setText(dataItem.detailText);
            }
        }

        private void setHintVisibility(boolean isChecked) {
            note.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        }
    }

    class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<OptionQuestionDto.Item, ItemViewHolder> {
        private final OptionQuestionDto question;

        public Factory(OptionQuestionDto question) {
            this.question = question;
        }

        @Override
        public ItemViewHolder createViewHolder(View itemView) {
            return new ItemViewHolder(itemView, question);
        }
    }
}
