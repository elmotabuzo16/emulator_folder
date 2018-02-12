package com.vitalityactive.va.vhr.landing;

import android.content.Intent;
import android.os.Bundle;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgeActivity;
import com.vitalityactive.va.shared.questionnaire.QuestionnaireLandingPresenter;
import com.vitalityactive.va.shared.questionnaire.QuestionnaireLandingUserInterface;
import com.vitalityactive.va.shared.questionnaire.activity.BaseQuestionnaireLandingActivity;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;

import javax.inject.Inject;
import javax.inject.Named;

public class VHRLandingActivity extends BaseQuestionnaireLandingActivity<QuestionnaireLandingUserInterface, QuestionnaireLandingPresenter> implements VHRVitalityAgePresenter.UserInterface {
    @Inject
    @Named(DependencyNames.VHR)
    QuestionnaireLandingPresenter presenter;

    @Inject
    @Named(DependencyNames.VHR)
    VHRVitalityAgePresenter vhrVitalityAgePresenter;

    public static final String MODE_FORM_SUBMITTED = "MODE_FORM_SUBMITTED";

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vhr_landing);
        vhrVitalityAgePresenter.setUserInterface(this);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        getDependencyInjector().inject(this);
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
    public void onClicked(MenuItemType menuItemType) {
        navigationCoordinator.navigateOnMenuItemFromVHRLanding(this, menuItemType);
    }

    @Override
    protected void onConnectionAlertPositiveEvent() {
        presenter.fetchQuestionnaireSet();
    }

    @Override
    protected void onConnectionAlertNegativeEvent() {
        navigationCoordinator.navigateAfterVHRErrorCancelled(this);
    }

    @Override
    public void onClicked(int position, QuestionnaireDTO questionnaireDTO) {
        navigationCoordinator.navigateAfterVHRQuestionnaireStartTapped(this, questionnaireDTO.getTypeKey());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            vhrVitalityAgePresenter.setReturnfromFormSubmission(intent.getExtras().getBoolean(MODE_FORM_SUBMITTED, false));
        }
    }

    @Override
    protected void resume() {
        super.resume();
    }

    @Override
    public void hideLoadingIndicator() {
        if (vhrVitalityAgePresenter.isReturnfromFormSubmission() && vhrVitalityAgePresenter.shouldShowVitalityAge()) {
            vhrVitalityAgePresenter.setHasShownVitalityAge();
            vhrVitalityAgePresenter.setReturnfromFormSubmission(false);
            vhrVitalityAgePresenter.fetchVitalityAge();
        } else {
            super.hideLoadingIndicator();
        }
    }

    @Override
    protected void pause() {
        super.pause();
    }

    public void showVitalityAgeView() {
        navigationCoordinator.navigateToVitalityAge(VHRLandingActivity.this, VitalityAgeActivity.VHR_MODE);
    }

    @Override
    public void onVitalityAgeResponse(boolean isSuccess) {
        super.hideLoadingIndicator();
        if (isSuccess) {
            navigationCoordinator.navigateToVitalityAge(this, VitalityAgeActivity.VHR_MODE);
        }
    }
}

