package com.vitalityactive.va.mwb.landing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

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

/**
 * Created by christian.j.p.capin on 1/30/2018.
 */

public class MWBLandingActivity extends BaseQuestionnaireLandingActivity<QuestionnaireLandingUserInterface, QuestionnaireLandingPresenter> implements MWBVitalityAgePresenter.UserInterface {
    @Inject
    @Named(DependencyNames.MWB)
    QuestionnaireLandingPresenter presenter;

    @Inject
    @Named(DependencyNames.MWB)
    MWBVitalityAgePresenter mwbVitalityAgePresenter;

    public static final String MODE_FORM_SUBMITTED = "MODE_FORM_SUBMITTED";

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vhr_landing);
        Log.e("cjc","MWBLandingActivity");
        mwbVitalityAgePresenter.setUserInterface(this);
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
       navigationCoordinator.navigateOnMenuItemFromMWBLanding(this, menuItemType);
    }

    @Override
    protected void onConnectionAlertPositiveEvent() {
        Log.e("cjc","onConnectionAlertPositiveEvent");
        presenter.fetchQuestionnaireSet();
    }

    @Override
    protected void onConnectionAlertNegativeEvent() {
        Log.e("cjc","onConnectionAlertNegativeEvent");
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
            Log.e("cjc","onNewIntent");
            mwbVitalityAgePresenter.setReturnfromFormSubmission(intent.getExtras().getBoolean(MODE_FORM_SUBMITTED, false));
        }
    }

    @Override
    protected void resume() {
        super.resume();
    }

    @Override
    public void hideLoadingIndicator() {
        Log.e("cjc","hideLoadingIndicator");
        if (mwbVitalityAgePresenter.isReturnfromFormSubmission() && mwbVitalityAgePresenter.shouldShowVitalityAge()) {
            Log.e("cjc","isReturnfromFormSubmission");
            mwbVitalityAgePresenter.setHasShownVitalityAge();
            mwbVitalityAgePresenter.setReturnfromFormSubmission(false);
            mwbVitalityAgePresenter.fetchVitalityAge();
        } else {
            Log.e("cjc","else hideLoadingIndicator");
            super.hideLoadingIndicator();
        }
    }

    @Override
    protected void pause() {
        super.pause();
    }

    public void showVitalityAgeView() {
        Log.e("cjc","showVitalityAgeView");
        navigationCoordinator.navigateToVitalityAge(MWBLandingActivity.this, VitalityAgeActivity.VHR_MODE);
    }

    @Override
    public void onVitalityAgeResponse(boolean isSuccess) {
        super.hideLoadingIndicator();
        if (isSuccess) {
            navigationCoordinator.navigateToVitalityAge(this, VitalityAgeActivity.VHR_MODE);
        }
    }
}

