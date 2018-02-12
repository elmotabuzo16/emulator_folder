package com.vitalityactive.va.dependencyinjection.mwb;

import com.vitalityactive.va.EventServiceClient;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.mwb.MWBPrivacyPolicyPresenter;
import com.vitalityactive.va.mwb.MWBPrivacyPolicyPresenterImpl;
import com.vitalityactive.va.mwb.questions.MWBQuestionnairePresenterImpl;
import com.vitalityactive.va.mwb.service.MWBSubmitterImpl;
import com.vitalityactive.va.myhealth.dto.VitalityAgeRepository;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.questionnaire.QuestionnaireStateManager;
import com.vitalityactive.va.questionnaire.QuestionnaireStateManagerImpl;
import com.vitalityactive.va.questionnaire.types.QuestionFactory;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmissionService;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmissionServiceClient;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmitter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsEventConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractorImpl;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by christian.j.p.capin on 1/30/2018.
 */
@Module
public class MWBCaptureModule {

    @Provides
    @MWBCaptureScope
    @Named(DependencyNames.MWB_PRIVACY_POLICY)
    static TermsAndConditionsInteractor provideMWBPrivacyPolicyInteractor(CMSServiceClient
    serviceClient, EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher, appConfigRepository.getMWBPrivacyPolicyContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @MWBCaptureScope
    @Named(DependencyNames.MWB_PRIVACY_POLICY)
    static TermsAndConditionsConsenter provideMWBPrivacyPolicyConsenter(EventDispatcher eventDispatcher, EventServiceClient
    eventServiceClient) {
        return new TermsAndConditionsEventConsenter(eventDispatcher, eventServiceClient, new long[]{EventType._MWBDATASHARINGAGREE}, new long[]{});
    }

    @Provides
    @MWBCaptureScope
    @Named(DependencyNames.MWB_PRIVACY_POLICY)
    MWBPrivacyPolicyPresenter provideMWBPrivacyPolicyPresenter(MainThreadScheduler scheduler,
                                                               @Named(DependencyNames.MWB_PRIVACY_POLICY) TermsAndConditionsInteractor interactor,
                                                               @Named(DependencyNames.MWB_PRIVACY_POLICY) TermsAndConditionsConsenter consenter,
                                                               EventDispatcher eventDispatcher,
                                                               QuestionnaireSubmitter questionnaireSubmitter) {
        return new MWBPrivacyPolicyPresenterImpl(scheduler, interactor, consenter, eventDispatcher, questionnaireSubmitter);
    }

    @Provides
    @MWBCaptureScope
    QuestionnaireCapturePresenter provideMWBQuestionnairePresenter(QuestionnaireStateManager
    questionnaireStateManager,
            InsurerConfigurationRepository insurerConfigurationRepository,
            QuestionnaireSubmitter questionnaireSubmitter,
            EventDispatcher eventDispatcher,
            MainThreadScheduler scheduler) {
        return new MWBQuestionnairePresenterImpl(questionnaireStateManager,
                insurerConfigurationRepository,
                questionnaireSubmitter,
                eventDispatcher,
                scheduler);
    }

    @Provides
    @MWBCaptureScope
    QuestionnaireSubmitter provideMWBSubmitter(QuestionnaireStateManager questionnaireStateManager,
            QuestionnaireSubmissionServiceClient questionnaireSetServiceClient,
            EventDispatcher eventDispatcher, VitalityAgeRepository
    vitalityAgeRepository) {
        return new MWBSubmitterImpl(questionnaireStateManager, questionnaireSetServiceClient, eventDispatcher, vitalityAgeRepository);
    }

    @Provides
    @MWBCaptureScope
    QuestionnaireStateManager provideQuestionnaireStateManager(@Named(DependencyNames.MWB) QuestionnaireSetRepository repository,
            QuestionFactory questionFactory) {
        return new QuestionnaireStateManagerImpl(repository, questionFactory);
    }

    @Provides
    @MWBCaptureScope
    QuestionnaireSubmissionServiceClient provideQuestionnaireSubmissionServiceClient(
            AccessTokenAuthorizationProvider accessTokenAuthorizationProvider,
            QuestionnaireSubmissionService service,
            WebServiceClient webServiceClient,
            PartyInformationRepository partyInformationRepository) {
        return new QuestionnaireSubmissionServiceClient(accessTokenAuthorizationProvider,
                service,
                webServiceClient,
                partyInformationRepository
        );
    }

    @Provides
    @MWBCaptureScope
    QuestionnaireSubmissionService provideQuestionnaireSubmissionService(ServiceGenerator
    serviceGenerator) {
        return serviceGenerator.create(QuestionnaireSubmissionService.class);
    }
}
