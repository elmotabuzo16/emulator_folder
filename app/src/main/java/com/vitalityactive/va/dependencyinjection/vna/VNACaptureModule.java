package com.vitalityactive.va.dependencyinjection.vna;

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
import com.vitalityactive.va.vna.VNAPrivacyPolicyPresenter;
import com.vitalityactive.va.vna.VNAPrivacyPolicyPresenterImpl;
import com.vitalityactive.va.vna.VNAQuestionnairePresenterImpl;
import com.vitalityactive.va.vna.VNASubmitterImpl;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class VNACaptureModule {
    @Provides
    @VNACaptureScope
    @Named(DependencyNames.VNA_PRIVACY_POLICY)
    static TermsAndConditionsInteractor provideVNAPrivacyPolicyInteractor(CMSServiceClient serviceClient, EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher, appConfigRepository.getVNAPrivacyPolicyContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @VNACaptureScope
    @Named(DependencyNames.VNA_PRIVACY_POLICY)
    static TermsAndConditionsConsenter provideVNAPrivacyPolicyConsenter(EventDispatcher eventDispatcher, EventServiceClient eventServiceClient) {
        return new TermsAndConditionsEventConsenter(
                eventDispatcher,
                eventServiceClient,
//                new long[]{EventType._VNADATASHARINGAGREE}, //TODO: Jay: is this correct?
                new long[]{EventType._VHRDATAPRIVACYAGREE}, //TODO: Jay: is this correct?
                new long[]{});
    }

    @Provides
    @VNACaptureScope
    @Named(DependencyNames.VNA_PRIVACY_POLICY)
    VNAPrivacyPolicyPresenter provideVNAPrivacyPolicyPresenter(MainThreadScheduler scheduler,
                                                               @Named(DependencyNames.VNA_PRIVACY_POLICY) TermsAndConditionsInteractor interactor,
                                                               @Named(DependencyNames.VNA_PRIVACY_POLICY) TermsAndConditionsConsenter consenter,
                                                               EventDispatcher eventDispatcher,
                                                               QuestionnaireSubmitter questionnaireSubmitter) {
        return new VNAPrivacyPolicyPresenterImpl(scheduler, interactor, consenter, eventDispatcher, questionnaireSubmitter);
    }

    @Provides
    @VNACaptureScope
    QuestionnaireCapturePresenter provideQuestionnairePresenter(QuestionnaireStateManager questionnaireStateManager,
                                                                InsurerConfigurationRepository insurerConfigurationRepository,
                                                                QuestionnaireSubmitter questionnaireSubmitter,
                                                                EventDispatcher eventDispatcher,
                                                                MainThreadScheduler scheduler)
    {
        return new VNAQuestionnairePresenterImpl(questionnaireStateManager,
                insurerConfigurationRepository,
                questionnaireSubmitter,
                eventDispatcher,
                scheduler
        );
    }

    @Provides
    @VNACaptureScope
    QuestionnaireStateManager provideQuestionnaireStateManager(@Named(DependencyNames.VNA) QuestionnaireSetRepository repository,
                                                               QuestionFactory questionFactory) {
        return new QuestionnaireStateManagerImpl(repository, questionFactory);
    }

    @Provides
    @VNACaptureScope
    QuestionnaireSubmitter provideSubmitter(QuestionnaireStateManager questionnaireStateManager,
                                            QuestionnaireSubmissionServiceClient questionnaireSubmissionServiceClient,
                                            EventDispatcher eventDispatcher) {
        return new VNASubmitterImpl(questionnaireStateManager, questionnaireSubmissionServiceClient, eventDispatcher);
    }

    @Provides
    @VNACaptureScope
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
    @VNACaptureScope
    QuestionnaireSubmissionService provideQuestionnaireSubmissionService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(QuestionnaireSubmissionService.class);
    }
}
