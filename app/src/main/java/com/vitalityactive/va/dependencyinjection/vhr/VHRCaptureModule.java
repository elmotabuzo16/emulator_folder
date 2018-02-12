package com.vitalityactive.va.dependencyinjection.vhr;

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
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmitter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsEventConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractorImpl;
import com.vitalityactive.va.vhr.VHRPrivacyPolicyPresenter;
import com.vitalityactive.va.vhr.VHRPrivacyPolicyPresenterImpl;
import com.vitalityactive.va.vhr.questions.VHRQuestionnairePresenterImpl;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmissionService;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmissionServiceClient;
import com.vitalityactive.va.vhr.service.VHRSubmitterImpl;
import com.vitalityactive.va.myhealth.dto.VitalityAgeRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class VHRCaptureModule {
    @Provides
    @VHRCaptureScope
    @Named(DependencyNames.VHR_PRIVACY_POLICY)
    static TermsAndConditionsInteractor provideVHRPrivacyPolicyInteractor(CMSServiceClient serviceClient, EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher, appConfigRepository.getVHRPrivacyPolicyContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @VHRCaptureScope
    @Named(DependencyNames.VHR_PRIVACY_POLICY)
    static TermsAndConditionsConsenter provideVHRPrivacyPolicyConsenter(EventDispatcher eventDispatcher, EventServiceClient eventServiceClient) {
        return new TermsAndConditionsEventConsenter(eventDispatcher, eventServiceClient, new long[]{EventType._VHRDATAPRIVACYAGREE}, new long[]{});
    }

    @Provides
    @VHRCaptureScope
    @Named(DependencyNames.VHR_PRIVACY_POLICY)
    VHRPrivacyPolicyPresenter provideVHRPrivacyPolicyPresenter(MainThreadScheduler scheduler,
                                                               @Named(DependencyNames.VHR_PRIVACY_POLICY) TermsAndConditionsInteractor interactor,
                                                               @Named(DependencyNames.VHR_PRIVACY_POLICY) TermsAndConditionsConsenter consenter,
                                                               EventDispatcher eventDispatcher,
                                                               QuestionnaireSubmitter questionnaireSubmitter) {
        return new VHRPrivacyPolicyPresenterImpl(scheduler, interactor, consenter, eventDispatcher, questionnaireSubmitter);
    }

    @Provides
    @VHRCaptureScope
    QuestionnaireCapturePresenter provideVHRQuestionnairePresenter(QuestionnaireStateManager questionnaireStateManager,
                                                                   InsurerConfigurationRepository insurerConfigurationRepository,
                                                                   QuestionnaireSubmitter questionnaireSubmitter,
                                                                   EventDispatcher eventDispatcher,
                                                                   MainThreadScheduler scheduler) {
        return new VHRQuestionnairePresenterImpl(questionnaireStateManager,
                insurerConfigurationRepository,
                questionnaireSubmitter,
                eventDispatcher,
                scheduler);
    }

    @Provides
    @VHRCaptureScope
    QuestionnaireSubmitter provideVHRSubmitter(QuestionnaireStateManager questionnaireStateManager,
                                               QuestionnaireSubmissionServiceClient questionnaireSetServiceClient,
                                               EventDispatcher eventDispatcher, VitalityAgeRepository vitalityAgeRepository) {
        return new VHRSubmitterImpl(questionnaireStateManager, questionnaireSetServiceClient, eventDispatcher, vitalityAgeRepository);
    }

    @Provides
    @VHRCaptureScope
    QuestionnaireStateManager provideQuestionnaireStateManager(@Named(DependencyNames.VHR) QuestionnaireSetRepository repository,
                                                               QuestionFactory questionFactory) {
        return new QuestionnaireStateManagerImpl(repository, questionFactory);
    }

    @Provides
    @VHRCaptureScope
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
    @VHRCaptureScope
    QuestionnaireSubmissionService provideQuestionnaireSubmissionService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(QuestionnaireSubmissionService.class);
    }
}
