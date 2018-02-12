package com.vitalityactive.va.shared.questionnaire.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;
import com.vitalityactive.va.shared.questionnaire.viewholder.QuestionnaireSetViewHolder;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.List;

public class QuestionnaireSetTitledList
        extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {

    private final List<QuestionnaireDTO> questionnaires;
    private final GenericRecyclerViewAdapter.OnItemClickListener<QuestionnaireDTO> itemClickListener;

    public QuestionnaireSetTitledList(String title,
                                      String setTextNote,
                                      List<QuestionnaireDTO> questionnaires,
                                      GenericRecyclerViewAdapter.OnItemClickListener<QuestionnaireDTO> itemClickListener) {
        super(title, setTextNote);
        this.questionnaires = questionnaires;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        return new GenericRecyclerViewAdapter<>(context,
                questionnaires,
                R.layout.vhr_questionnaire_item,
                new QuestionnaireSetViewHolder.Factory(itemClickListener));
    }
}
