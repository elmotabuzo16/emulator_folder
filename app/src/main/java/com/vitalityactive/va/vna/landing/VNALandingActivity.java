package com.vitalityactive.va.vna.landing;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.shared.questionnaire.QuestionnaireLandingPresenter;
import com.vitalityactive.va.shared.questionnaire.QuestionnaireLandingUserInterface;
import com.vitalityactive.va.shared.questionnaire.activity.BaseQuestionnaireLandingActivity;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;

import javax.inject.Inject;
import javax.inject.Named;

public class VNALandingActivity extends BaseQuestionnaireLandingActivity<QuestionnaireLandingUserInterface, QuestionnaireLandingPresenter> {
    @Inject
    @Named(DependencyNames.VNA)
    QuestionnaireLandingPresenter presenter;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        // todo: using the same layout as vhr, refactor/rename to generalize for all questionnaire types
        setContentView(R.layout.activity_vhr_landing);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected QuestionnaireLandingUserInterface getUserInterface() {
        return this;
    }

    @Override
    protected QuestionnaireLandingPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onClicked(int position, QuestionnaireDTO questionnaireDTO) {
        navigationCoordinator.navigateAfterVNAQuestionnaireStartTapped(this, questionnaireDTO.getTypeKey());
    }

    @Override
    public void onClicked(MenuItemType menuItemType) {
        navigationCoordinator.navigateToVNAMenu(this, menuItemType);
    }

    @Override
    protected void onConnectionAlertPositiveEvent() {
        presenter.fetchQuestionnaireSet();
    }

    @Override
    protected void onConnectionAlertNegativeEvent() {
        navigationCoordinator.navigateAfterVNAErrorCancelled(this);
    }

    @Override
    public void showVitalityAgeView() {

    }
}
