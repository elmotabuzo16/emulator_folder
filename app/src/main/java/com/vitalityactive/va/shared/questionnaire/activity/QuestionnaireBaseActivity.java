package com.vitalityactive.va.shared.questionnaire.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivityWithConnectionAlert;
import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.QuestionnaireSections;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;
import com.vitalityactive.va.shared.questionnaire.capture.viewholders.Adapter;
import com.vitalityactive.va.uicomponents.ButtonBarConfigurator;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.List;

public abstract class QuestionnaireBaseActivity
        extends BasePresentedActivityWithConnectionAlert<QuestionnaireCapturePresenter.UserInterface, QuestionnaireCapturePresenter>
        implements QuestionnaireCapturePresenter.UserInterface
{
    public static final String EXTRA_QUESTIONNAIRE_TYPE_KEY = "questionnaireTypeKey";
    protected long questionnaireTypeKey;
    private RecyclerView recyclerView;
    private TextView headerTitle;
    private int selectedColor;
    private int deselectedColor;
    private int themeBlack;
    private Adapter adapter;

    @Override
    public void onBackPressed() {
        getQuestionnaireCapturePresenter().onBackButtonClicked();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        clearEditTextFocusWhenTappingOutsideIt(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    @CallSuper
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vhr_questionnaire);
        recyclerView = findViewById(R.id.recycler_view);
        headerTitle = findViewById(R.id.assessment_header_title);
        setQuestionnaireTypeKey();
        setColors();
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        injectDependenciesUsingCaptureScopeInjector();
    }

    @Override
    protected QuestionnaireCapturePresenter getPresenter() {
        return getQuestionnaireCapturePresenter();
    }

    @Override
    protected QuestionnaireCapturePresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected void onConnectionAlertPositiveEvent() {
        getQuestionnaireCapturePresenter().onForwardButtonClicked();
    }

    @Override
    protected void onConnectionAlertNegativeEvent() {
    }

    protected abstract void injectDependenciesUsingCaptureScopeInjector();

    protected abstract QuestionnaireCapturePresenter getQuestionnaireCapturePresenter();

    @Override
    public void bindQuestions(List<Question> questions) {
        adapter = new Adapter(this, getQuestionnaireCapturePresenter(), questions, selectedColor, deselectedColor, themeBlack);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void rebindQuestions(List<Question> questions) {
        adapter.replaceItems(questions);
    }

    @Override
    public abstract void navigateAfterQuestionnaireCompleted();

    @Override
    public void sectionProgress(int progress, long total) {
        int forwardButtonText = progress == total ? R.string.done_button_title_53 : R.string.next_button_title_84;
        boolean hasPreviousSection = progress > 1;
        setupButtonBar()
                .displayProgressBar(progress, (int) total)
                .displayBackButton(hasPreviousSection, true, R.string.back_button_336)
                .setForwardButtonText(forwardButtonText);
    }

    @Override
    public void setQuestionnaireTitle(String sectionTitle) {
        setActionBarTitleAndDisplayHomeAsUp(sectionTitle);

        setCloseAsActionBarIcon();

        setupButtonBar().setForwardButtonTextToNext()
                .setForwardButtonEnabled(true)
                .displayBackButton(true)
                .displayProgressBar(true)
                .setForwardButtonOnClick(new ButtonBarConfigurator.OnClickListener() {
                    @Override
                    public void onButtonBarForwardClicked() {
                        getQuestionnaireCapturePresenter().onForwardButtonClicked();
                    }
                })
                .setBackButtonOnClick(new ButtonBarConfigurator.OnBackButtonClickListener() {
                    @Override
                    public void onButtonBarBackClicked() {
                        getQuestionnaireCapturePresenter().onBackButtonClicked();
                    }
                });
    }

    @Override
    public void setQuestionnaireSectionTitle(String sectionTitle) {
        headerTitle.setText(sectionTitle);
        Drawable icon = ContextCompat.getDrawable(this, getIconResourceId());
        icon = ViewUtilities.tintDrawable(icon, ViewUtilities.getColorPrimaryFromTheme(this));
        headerTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
    }

    private int getIconResourceId() {
        Long sectionTypeKey = presenter.getSectionTypeKey();

        switch (sectionTypeKey.intValue()) {
            case QuestionnaireSections._YOURWELLBEING:
                return R.drawable.wellbeing;
            case QuestionnaireSections._SMOKING:
                return R.drawable.smoking;
            case QuestionnaireSections._SLEEPINGPATTERNS:
                return R.drawable.sleep;
            case QuestionnaireSections._OVERALLHEALTH:
                return R.drawable.overall_health;
            case QuestionnaireSections._MEDICALHISTORY:
                return R.drawable.medical_history;
            case QuestionnaireSections._FAMILYMEDICALHISTORY:
                return R.drawable.family_medical_history;
            case QuestionnaireSections._ALCOHOLINTAKE:
                return R.drawable.alcohol_intake;
            case QuestionnaireSections._EATINGHABITS:
                return R.drawable.eating_habits;
            case QuestionnaireSections._PHYSICALACTIVITYLEV:
                return R.drawable.physical_activity;
            case QuestionnaireSections._KEYMEASUREMENTS:
                return R.drawable.onboarding_capture_results;

                //TODO: jay: change to correct icon
            case QuestionnaireSections._DIETARYPREFERENCE:
                return R.drawable.onboarding_capture_results;
            case QuestionnaireSections._FOODALLERGIES:
                return R.drawable.onboarding_capture_results;

            default:
                return R.drawable.radio_unselected;
        }
    }

    @Override
    public void canProceedToNextSection(boolean yes) {
        setupButtonBar()
                .setBackButtonEnabled(true)
                .setForwardButtonEnabled(yes);
    }

    @Override
    public void clearFocus() {
        recyclerView.clearFocus();
    }

    @Override
    public void navigateToNextSection() {
        navigationCoordinator.navigateAfterQuestionnaireGoToSectionTapped(this, getQuestionnaireSetType(), questionnaireTypeKey);
    }

    @Override
    public void navigateToLandingScreen() {
        navigationCoordinator.back(this);
    }

    @Override
    public void showConnectionSubmissionRequestErrorMessage() {
        showConnectionError();
    }

    @Override
    public void showGenericSubmissionRequestErrorMessage() {
        showGenericError();
    }

    protected abstract int getQuestionnaireSetType();

    private void setQuestionnaireTypeKey() {
        questionnaireTypeKey = getIntent().getLongExtra(EXTRA_QUESTIONNAIRE_TYPE_KEY, -1);
        getQuestionnaireCapturePresenter().setQuestionnaireTypeKey(questionnaireTypeKey);
    }

    private void setColors() {
        selectedColor = ResourcesCompat.getColor(getResources(), R.color.jungle_green, getTheme());
        deselectedColor = ResourcesCompat.getColor(getResources(), R.color.light_disabled_38, getTheme());
        themeBlack = ResourcesCompat.getColor(getResources(), android.R.color.black, getTheme());
    }
}
