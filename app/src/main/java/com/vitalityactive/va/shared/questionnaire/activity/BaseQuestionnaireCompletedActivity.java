package com.vitalityactive.va.shared.questionnaire.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;
import com.vitalityactive.va.shared.questionnaire.viewholder.CompletedAssessmentViewHolder;
import com.vitalityactive.va.uicomponents.OnboardingCirclesAnimator;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.shared.questionnaire.AssessmentCompletedPresenter;

import java.util.List;

public abstract class BaseQuestionnaireCompletedActivity
        extends BasePresentedActivity<AssessmentCompletedPresenter.UserInterface, AssessmentCompletedPresenter>
        implements AssessmentCompletedPresenter.UserInterface, GenericRecyclerViewAdapter.OnItemClickListener<QuestionnaireDTO>
{
    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vhr_questionnaire_completed);      // todo: this is showing VHR, refactor the layout to be usable for all
        getPresenter().setQuestionnaireCompletedTypeKey(getIntent().getStringExtra("questionnaireTypeKey"));
        animateCircles();
    }

    private void animateCircles() {
        final ViewGroup onboardingCircles = (ViewGroup) findViewById(R.id.non_smokers_onboarding_circles);
        onboardingCircles.setVisibility(View.INVISIBLE);
        final OnboardingCirclesAnimator circlesAnimator =
                new OnboardingCirclesAnimator(this,
                        onboardingCircles,
                        R.drawable.onboarding_activated,
                        getResources().getDisplayMetrics().widthPixels);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                circlesAnimator.animateCirclesAndIcon();
                onboardingCircles.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    @Override
    protected AssessmentCompletedPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    public void setTitle(String title) {
        TextView titleView = (TextView) findViewById(R.id.title);
        ViewUtilities.setTextOfView(titleView, title);
    }

    @Override
    public void setSubtitle(String subtitle) {
        TextView subTitleView = (TextView) findViewById(R.id.subtitle);
        ViewUtilities.setTextOfView(subTitleView, subtitle);
    }

    @Override
    public void setText(String text) {
        TextView textTextView = (TextView) findViewById(R.id.text);
        ViewUtilities.setTextOfView(textTextView, text);
    }

    @Override
    public void showCompletedState() {
        findViewById(R.id.complete_state).setVisibility(View.VISIBLE);
        findViewById(R.id.incomplete_state).setVisibility(View.GONE);

        setTitle(getString(R.string.complete_screen_section_completed_title_2_333));
        setSubtitle(getString(R.string.complete_screen_section_completed_message_2_334));
        setText(getString(R.string.confirmation_completed_footnote_points_message_119));
    }

    private String buildTitleString(String questionnaireTitle) {
        return String.format(getString(R.string.completed_single_questionnaire_completed_title_9998), questionnaireTitle);
    }

    @Override
    public void showInCompleteState(String questionnaireTitle,
                                    int potentialPoints,
                                    List<QuestionnaireDTO> incompleteQuestionnaires) {
        findViewById(R.id.complete_state).setVisibility(View.GONE);
        findViewById(R.id.incomplete_state).setVisibility(View.VISIBLE);

        setTitle(buildTitleString(questionnaireTitle));
        setSubtitle(buildSubtitleString(incompleteQuestionnaires.size(), potentialPoints));

        GenericRecyclerViewAdapter adapter =
                new GenericRecyclerViewAdapter<>(this,
                        incompleteQuestionnaires,
                        R.layout.vhr_incomplete_questionnaire_item,
                        new CompletedAssessmentViewHolder.Factory(this));

        ((RecyclerView) findViewById(R.id.recycler_view)).setAdapter(adapter);
    }

    private String buildSubtitleString(int questionnairesRemaining, int potentialPoints) {
        String resourceString;

        if (questionnairesRemaining > 1) {
            resourceString = getString(R.string.completed_multiple_remaining_questionnaires_earn_points_message_9992);
        } else {
            resourceString = getString(R.string.completed_single_remaining_questionnaires_earn_points_message_9993);
        }

        return String.format(resourceString, questionnairesRemaining, potentialPoints);
    }

    public void handleCloseButtonTapped(View view) {
        onBackPressed();
    }

    public void handleAssessmentCompletedButtonClicked(View view) {
        handleDismiss();
    }

    @Override
    public void onClicked(int position, QuestionnaireDTO item) {
        navigationCoordinator.navigateAfterQuestionnaireStartTapped(this, getQuestionnaireSetType(), item.getTypeKey());
    }

    @Override
    public void onBackPressed() {
        handleDismiss();
    }

    private void handleDismiss() {
        navigationCoordinator.navigateAfterAssessmentsCompletedDismissed(this, getQuestionnaireSetType());
    }

    protected abstract int getQuestionnaireSetType();
}
