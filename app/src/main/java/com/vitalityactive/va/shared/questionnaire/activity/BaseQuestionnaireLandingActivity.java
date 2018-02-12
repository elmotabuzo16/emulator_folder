package com.vitalityactive.va.shared.questionnaire.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.vitalityactive.va.BasePresentedActivityWithConnectionAlert;
import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItem;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.shared.questionnaire.QuestionnaireLandingUserInterface;
import com.vitalityactive.va.shared.questionnaire.adapters.QuestionnaireSetTitledList;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireSetInformation;
import com.vitalityactive.va.shared.questionnaire.viewholder.QuestionnaireLandingHeaderViewHolder;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseQuestionnaireLandingActivity<UI extends QuestionnaireLandingUserInterface, P extends Presenter<UI>>
        extends BasePresentedActivityWithConnectionAlert<UI, P>
        implements GenericRecyclerViewAdapter.OnItemClickListener<QuestionnaireDTO>,
        MenuContainerViewHolder.OnMenuItemClickedListener,
        QuestionnaireLandingUserInterface {
    @Override
    public abstract void onClicked(int position, QuestionnaireDTO questionnaireDTO);

    @Override
    public abstract void onClicked(MenuItemType menuItemType);

    public void showQuestionnaireSetAndQuestionnaires(QuestionnaireSetInformation questionnaireSet,
                                                      List<QuestionnaireDTO> questionnaires) {
        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        List<GenericRecyclerViewAdapter> adapters = getAdapters(questionnaireSet, questionnaires);
        ContainersRecyclerViewAdapter container = new ContainersRecyclerViewAdapter(adapters);
        recyclerView.setAdapter(container);
        //   ViewUtilities.scrollToTop(recyclerView);
    }

    @NonNull
    private List<GenericRecyclerViewAdapter> getAdapters(QuestionnaireSetInformation questionnaireSet,
                                                         List<QuestionnaireDTO> questionnaires) {
        List<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(getHeaderAdapter(questionnaireSet));
        adapters.add(getQuestionnaireListAdapter(questionnaires, questionnaireSet.getFooter()));
        adapters.add(getMenuAdapter());

        return adapters;
    }

    @NonNull
    private GenericRecyclerViewAdapter<GenericTitledListContainerWithAdapter.TitledListWithAdapter,
            GenericTitledListContainerWithAdapter<GenericTitledListContainerWithAdapter.TitledListWithAdapter>> getQuestionnaireListAdapter(List<QuestionnaireDTO> questionnaires, String setTextNote) {

      /*  final QuestionnaireSetTitledList questionnaireSetTitledList =
      .          new QuestionnaireSetTitledList(getString(R.string.landing_screen_group_heading_300),
                        setTextNote,
                        questionnaires,
                        this);*/


      //cjc
        for(int i =0;i< questionnaires.toArray().length; i++){
            Log.e("cjc","toArray getTitle: "+ questionnaires.get(i).getTitle());
            Log.e("cjc","toArray getSubtitle: "+ questionnaires.get(i).getSubtitle());
            Log.e("cjc","toArray isCompleted: "+ questionnaires.get(i).isCompleted());
        }
        Log.e("cjc","setTextNote: "+setTextNote);

        final QuestionnaireSetTitledList questionnaireSetTitledList =
                new QuestionnaireSetTitledList(getString(R.string.events_category_assessment_1129),
                        setTextNote,
                        questionnaires,
                        this);
        return new GenericRecyclerViewAdapter<>(this,
                questionnaireSetTitledList,
                R.layout.vhr_questionnaire_list,
                new GenericTitledListContainerWithAdapter.Factory<>());
    }

    private GenericRecyclerViewAdapter<CardMarginSettings, MenuContainerViewHolder> getMenuAdapter() {
        return new MenuBuilder(this)
                //.setMenuItems(MenuBuilder.MenuItemSet.LearnMore)
                .addMenuItem(MenuItem.Builder.learnMore())
                .addMenuItem(MenuItem.Builder.help())
                .setClickListener(this)
                .build();
    }

    @NonNull
    private GenericRecyclerViewAdapter<QuestionnaireSetInformation, QuestionnaireLandingHeaderViewHolder> getHeaderAdapter(QuestionnaireSetInformation questionnaireSet) {
        Log.e("cjc","QuestionnaireLandingHeaderViewHolder getDescription:  "+questionnaireSet.getDescription());
        Log.e("cjc","QuestionnaireLandingHeaderViewHolder getText:  "+questionnaireSet.getText());
        return new GenericRecyclerViewAdapter<>(this,
                questionnaireSet,
                R.layout.vhr_landing_header,
                new QuestionnaireLandingHeaderViewHolder.Factory());
    }
}
