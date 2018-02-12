package com.vitalityactive.va.dependencyinjection;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.InputType;

import com.google.gson.Gson;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.EventService;
import com.vitalityactive.va.EventServiceClient;
import com.vitalityactive.va.ExecutorServiceScheduler;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.LanguageProvider;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.MainThreadSchedulerImpl;
import com.vitalityactive.va.NavigationCoordinator;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.PartyInformationRepositoryImpl;
import com.vitalityactive.va.R;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.VitalityMembershipRepository;
import com.vitalityactive.va.VitalityMembershipRepositoryImpl;
import com.vitalityactive.va.activerewards.ActiveRewardsActivationServiceClient;
import com.vitalityactive.va.activerewards.ActiveRewardsActivator;
import com.vitalityactive.va.activerewards.ActiveRewardsActivatorImpl;
import com.vitalityactive.va.activerewards.ActiveRewardsService;
import com.vitalityactive.va.activerewards.history.GoalHistoryInteractor;
import com.vitalityactive.va.activerewards.history.GoalHistoryInteractorImpl;
import com.vitalityactive.va.activerewards.history.GoalHistoryPresenter;
import com.vitalityactive.va.activerewards.history.GoalHistoryPresenterImpl;
import com.vitalityactive.va.activerewards.history.GoalHistoryRepository;
import com.vitalityactive.va.activerewards.history.GoalHistoryRepositoryImpl;
import com.vitalityactive.va.activerewards.history.detailedscreen.HistoryWeekDetailsPresenter;
import com.vitalityactive.va.activerewards.landing.repository.GoalsAndProgressRepository;
import com.vitalityactive.va.activerewards.landing.repository.GoalsAndProgressRepositoryImpl;
import com.vitalityactive.va.activerewards.landing.service.GoalsProgressService;
import com.vitalityactive.va.activerewards.landing.service.GoalsProgressServiceClient;
import com.vitalityactive.va.activerewards.termsandconditions.ActiveRewardsTermsAndConditionsPresenterImpl;
import com.vitalityactive.va.activerewards.termsandconditions.ActiveRewardsTermsAndConditionsUserInterface;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.appconfig.AppConfigService;
import com.vitalityactive.va.appconfig.AppConfigServiceClient;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.cms.CMSContentDownloader;
import com.vitalityactive.va.cms.CMSContentUploader;
import com.vitalityactive.va.cms.CMSService;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.cms.FallbackContentInputStreamFromAssetsLoader;
import com.vitalityactive.va.cms.keyvaluecontent.CMSKeyValueContentRepository;
import com.vitalityactive.va.cms.keyvaluecontent.FallbackContentInputStreamLoader;
import com.vitalityactive.va.connectivity.ConnectivityListener;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.eventsfeed.EventsFeedAvailableEventsCategoriesProvider;
import com.vitalityactive.va.eventsfeed.EventsFeedContent;
import com.vitalityactive.va.eventsfeed.EventsFeedContentImpl;
import com.vitalityactive.va.eventsfeed.EventsFeedRepository;
import com.vitalityactive.va.eventsfeed.EventsFeedSelectedCategoriesProvider;
import com.vitalityactive.va.eventsfeed.data.net.EventsFeedApiServiceClient;
import com.vitalityactive.va.eventsfeed.domain.EventsFeedInteractorImpl;
import com.vitalityactive.va.eventsfeed.presentation.EventsFeedMonthPresenter;
import com.vitalityactive.va.eventsfeed.presentation.EventsFeedMonthPresenterImpl;
import com.vitalityactive.va.eventsfeed.presentation.EventsFeedPresenter;
import com.vitalityactive.va.eventsfeed.presentation.EventsFeedPresenterImpl;
import com.vitalityactive.va.feedback.interactor.FeedbackInteractor;
import com.vitalityactive.va.feedback.interactor.FeedbackInteractorImpl;
import com.vitalityactive.va.feedback.presenter.FeedbackPresenter;
import com.vitalityactive.va.feedback.presenter.FeedbackPresenterImpl;
import com.vitalityactive.va.forgotpassword.ForgotPasswordInteractor;
import com.vitalityactive.va.forgotpassword.ForgotPasswordPresenter;
import com.vitalityactive.va.forgotpassword.ForgotPasswordPresenterImpl;
import com.vitalityactive.va.help.dto.HelpRepository;
import com.vitalityactive.va.help.dto.HelpRepositoryImpl;
import com.vitalityactive.va.help.interactor.HelpInteractor;
import com.vitalityactive.va.help.interactor.HelpInteractorImpl;
import com.vitalityactive.va.help.presenter.HelpPresenter;
import com.vitalityactive.va.help.presenter.HelpPresenterImpl;
import com.vitalityactive.va.help.service.HelpServiceClient;
import com.vitalityactive.va.help.service.HelpService;
import com.vitalityactive.va.help.service.HelpServiceClient;
import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.activerewardsrewardscard.RewardsHomeScreenCardRepository;
import com.vitalityactive.va.home.activerewardsrewardscard.RewardsHomeScreenCardRepositoryImpl;
import com.vitalityactive.va.home.interactor.HomeInteractor;
import com.vitalityactive.va.home.interactor.HomeInteractorImpl;
import com.vitalityactive.va.home.interactor.HomePresenter;
import com.vitalityactive.va.home.interactor.HomePresenterImpl;
import com.vitalityactive.va.home.mwb.MWBCardRepository;
import com.vitalityactive.va.home.nonsmokerscard.NonSmokersCardRepository;
import com.vitalityactive.va.home.repository.BaseHomeCardRepository;
import com.vitalityactive.va.home.repository.BaseHomeCardRepositoryImpl;
import com.vitalityactive.va.home.repository.HomeScreenCardSectionRepository;
import com.vitalityactive.va.home.repository.HomeScreenCardSectionRepositoryImpl;
import com.vitalityactive.va.home.repository.MWBCardRepositoryImpl;
import com.vitalityactive.va.home.repository.NonSmokersCardRepositoryImpl;
import com.vitalityactive.va.home.repository.SNVCardRepositoryImpl;
import com.vitalityactive.va.home.repository.VHCCardRepositoryImpl;
import com.vitalityactive.va.home.repository.VHRCardRepositoryImpl;
import com.vitalityactive.va.home.repository.VNACardRepositoryImpl;
import com.vitalityactive.va.home.service.EventByPartyService;
import com.vitalityactive.va.home.service.EventByPartyServiceClient;
import com.vitalityactive.va.home.service.HomeScreenCardStatusService;
import com.vitalityactive.va.home.service.HomeScreenCardStatusServiceClient;
import com.vitalityactive.va.home.service.ProductFeaturePointsService;
import com.vitalityactive.va.home.service.ProductFeaturePointsServiceClient;
import com.vitalityactive.va.home.snv.SNVCardRepository;
import com.vitalityactive.va.home.vhc.VHCCardRepository;
import com.vitalityactive.va.home.vhr.VHRCardRepository;
import com.vitalityactive.va.home.vna.VNACardRepository;
import com.vitalityactive.va.launch.NavigatorImpl;
import com.vitalityactive.va.login.BaseURLSwitcher;
import com.vitalityactive.va.login.LoginInteractorImpl;
import com.vitalityactive.va.login.LoginPresenter;
import com.vitalityactive.va.login.LoginPresenterImpl;
import com.vitalityactive.va.login.LoginRepositoryImpl;
import com.vitalityactive.va.membershippass.dto.MembershipPassRepository;
import com.vitalityactive.va.membershippass.dto.MembershipPassRepositoryImpl;
import com.vitalityactive.va.membershippass.interactor.MembershipPassInteractor;
import com.vitalityactive.va.membershippass.interactor.MembershipPassInteractorImpl;
import com.vitalityactive.va.membershippass.presenter.MembershipPassPresenter;
import com.vitalityactive.va.membershippass.presenter.MembershipPassPresenterImpl;
import com.vitalityactive.va.membershippass.service.MembershipPassService;
import com.vitalityactive.va.membershippass.service.MembershipPassServiceClient;
import com.vitalityactive.va.mwb.content.MWBContent;
import com.vitalityactive.va.mwb.content.MWBContentFromStringResource;
import com.vitalityactive.va.mwb.content.MWBHealthAttributeContent;
import com.vitalityactive.va.mwb.content.MWBHealthAttributeContentFromStringResource;
import com.vitalityactive.va.mwb.landing.MWBLandingPresenterImpl;
import com.vitalityactive.va.mwb.landing.MWBVitalityAgePresenter;
import com.vitalityactive.va.mwb.landing.MWBVitalityAgePresenterImpl;
import com.vitalityactive.va.mwb.questions.MWBAssessmentCompletedPresenter;
import com.vitalityactive.va.myhealth.content.MyHealthOnboardingContent;
import com.vitalityactive.va.myhealth.content.MyHealthOnboardingContentImpl;
import com.vitalityactive.va.myhealth.dto.MyHealthRepository;
import com.vitalityactive.va.myhealth.dto.MyHealthRepositoryImpl;
import com.vitalityactive.va.myhealth.dto.VitalityAgeRepository;
import com.vitalityactive.va.myhealth.dto.VitalityAgeRepositoryImpl;
import com.vitalityactive.va.myhealth.healthattributes.MyHealthAttributeDetailInteractor;
import com.vitalityactive.va.myhealth.healthattributes.MyHealthAttributeDetailInteractorImpl;
import com.vitalityactive.va.myhealth.healthattributes.MyHealthAttributeDetailPresenter;
import com.vitalityactive.va.myhealth.healthattributes.MyHealthAttributeDetailPresenterImpl;
import com.vitalityactive.va.myhealth.landing.MyHealthLandingInteractor;
import com.vitalityactive.va.myhealth.landing.MyHealthLandingInteractorImpl;
import com.vitalityactive.va.myhealth.landing.MyHealthLandingPresenter;
import com.vitalityactive.va.myhealth.landing.MyHealthLandingPresenterImpl;
import com.vitalityactive.va.myhealth.learnmore.VitalityAgeLearnMorePresenter;
import com.vitalityactive.va.myhealth.learnmore.VitalityAgeLearnMorePresenterImpl;
import com.vitalityactive.va.myhealth.moretips.MyHealthMoreTipsInteractor;
import com.vitalityactive.va.myhealth.moretips.MyHealthMoreTipsInteractorImpl;
import com.vitalityactive.va.myhealth.moretips.MyHealthMoreTipsPresenter;
import com.vitalityactive.va.myhealth.moretips.MyHealthMoreTipsPresenterImpl;
import com.vitalityactive.va.myhealth.service.HealthAttributeFeedbackService;
import com.vitalityactive.va.myhealth.service.HealthAttributeFeedbackServiceClient;
import com.vitalityactive.va.myhealth.service.HealthAttributeInformationServiceClient;
import com.vitalityactive.va.myhealth.service.VitalityAgeService;
import com.vitalityactive.va.myhealth.service.VitalityAgeServiceClient;
import com.vitalityactive.va.myhealth.tipdetail.MyHealthTipDetailInteractor;
import com.vitalityactive.va.myhealth.tipdetail.MyHealthTipDetailInteractorImpl;
import com.vitalityactive.va.myhealth.tipdetail.MyHealthTipDetailPresenter;
import com.vitalityactive.va.myhealth.tipdetail.MyHealthTipDetailPresenterImpl;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgeInteractor;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgeInteractorImpl;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgePreferencesManager;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgePreferencesManagerImpl;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgePresenter;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgePresenterImpl;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthTipsInteractor;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthTipsInteractorImpl;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthTipsMoreResultsPresenter;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthTipsMoreResultsPresenterImpl;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthVitalityAgeProfileInteractor;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthVitalityAgeProfileInteractorImpl;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthVitalityAgeProfilePresenter;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthVitalityAgeProfilePresenterImpl;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersDeclarationConsenter;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersDeclarationContent;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersDeclarePresenter;
import com.vitalityactive.va.nonsmokersdeclaration.onboarding.NonSmokersDeclarationStringsFileContent;
import com.vitalityactive.va.onboarding.LoginRepository;
import com.vitalityactive.va.onboarding.OnboardingPresenter;
import com.vitalityactive.va.onboarding.OnboardingPresenterImpl;
import com.vitalityactive.va.partnerjourney.home.PartnerCardRepository;
import com.vitalityactive.va.partnerjourney.home.PartnerCardRepositoryImpl;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.pointsmonitor.PointsEntryRepository;
import com.vitalityactive.va.pointsmonitor.PointsMonitorAvailablePointsCategoriesProvider;
import com.vitalityactive.va.pointsmonitor.PointsMonitorContent;
import com.vitalityactive.va.pointsmonitor.PointsMonitorContentImpl;
import com.vitalityactive.va.pointsmonitor.PointsMonitorInteractorImpl;
import com.vitalityactive.va.pointsmonitor.PointsMonitorPresenter;
import com.vitalityactive.va.pointsmonitor.PointsMonitorPresenterImpl;
import com.vitalityactive.va.pointsmonitor.PointsMonitorRepository;
import com.vitalityactive.va.pointsmonitor.PointsMonitorSelectedCategoriesProvider;
import com.vitalityactive.va.pointsmonitor.PointsMonitorService;
import com.vitalityactive.va.profile.ChangeEmailPresenter;
import com.vitalityactive.va.profile.ChangeEmailPresenterImpl;
import com.vitalityactive.va.profile.PersonalDetailsClient;
import com.vitalityactive.va.profile.PersonalDetailsInteractor;
import com.vitalityactive.va.profile.PersonalDetailsInteractorImpl;
import com.vitalityactive.va.profile.PersonalDetailsPresenter;
import com.vitalityactive.va.profile.PersonalDetailsPresenterImpl;
import com.vitalityactive.va.profile.ProfileImageProvider;
import com.vitalityactive.va.profile.ProfileInteractor;
import com.vitalityactive.va.profile.ProfileInteractorImpl;
import com.vitalityactive.va.profile.ProfilePresenter;
import com.vitalityactive.va.profile.ProfilePresenterImpl;
import com.vitalityactive.va.pushnotification.InAppPreferences;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.questionnaire.QuestionnaireUnitOfMeasureContent;
import com.vitalityactive.va.questionnaire.types.QuestionFactory;
import com.vitalityactive.va.register.RegistrationPresenter;
import com.vitalityactive.va.register.RegistrationPresenterImpl;
import com.vitalityactive.va.register.interactor.RegistrationInteractorImpl;
import com.vitalityactive.va.register.presenter.ConfirmationPasswordPresenter;
import com.vitalityactive.va.register.presenter.CredentialPresenter;
import com.vitalityactive.va.register.presenter.CredentialPresenterCallback;
import com.vitalityactive.va.register.presenter.InsurerCodePresenter;
import com.vitalityactive.va.register.presenter.PasswordPresenter;
import com.vitalityactive.va.register.presenter.UsernamePresenter;
import com.vitalityactive.va.search.ContentHelpInteractor;
import com.vitalityactive.va.search.ContentHelpInteractorImpl;
import com.vitalityactive.va.search.ContentHelpPresenter;
import com.vitalityactive.va.search.ContentHelpPresenterImpl;
import com.vitalityactive.va.settings.ChangePasswordClient;
import com.vitalityactive.va.settings.SettingChangePasswordInteractor;
import com.vitalityactive.va.settings.SettingChangePasswordInteractorImpl;
import com.vitalityactive.va.shared.questionnaire.AssessmentCompletedPresenter;
import com.vitalityactive.va.shared.questionnaire.QuestionnaireLandingPresenter;
import com.vitalityactive.va.shared.questionnaire.repository.QuestionnaireSetRepositoryImpl;
import com.vitalityactive.va.shared.questionnaire.repository.ValidationRuleMapper;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetService;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetServiceClient;
import com.vitalityactive.va.snv.confirmandsubmit.interactor.ConfirmAndSubmitInteractor;
import com.vitalityactive.va.snv.confirmandsubmit.interactor.ConfirmAndSubmitInteractorImpl;
import com.vitalityactive.va.snv.confirmandsubmit.presenter.ConfirmAndSubmitPresenter;
import com.vitalityactive.va.snv.confirmandsubmit.presenter.ConfirmAndSubmitPresenterImpl;
import com.vitalityactive.va.snv.confirmandsubmit.presenter.SNVAddProofPresenter;
import com.vitalityactive.va.snv.confirmandsubmit.presenter.SNVAddProofPresenterImpl;
import com.vitalityactive.va.snv.confirmandsubmit.presenter.SNVPrivacyPolicyPresenter;
import com.vitalityactive.va.snv.confirmandsubmit.presenter.SNVPrivacyPolicyPresenterImpl;
import com.vitalityactive.va.snv.confirmandsubmit.presenter.SNVSummaryPresenter;
import com.vitalityactive.va.snv.confirmandsubmit.presenter.SNVSummaryPresenterImpl;
import com.vitalityactive.va.snv.confirmandsubmit.repository.SNVItemRepository;
import com.vitalityactive.va.snv.confirmandsubmit.repository.SNVItemRepositoryImpl;
import com.vitalityactive.va.snv.confirmandsubmit.service.SNVSubmitter;
import com.vitalityactive.va.snv.confirmandsubmit.service.SNVSubmitterImpl;
import com.vitalityactive.va.snv.content.SNVHealthAttributeContent;
import com.vitalityactive.va.snv.content.SNVHealthAttributeContentFromStringResource;
import com.vitalityactive.va.snv.history.interactor.ScreeningAndVaccinationsHistoryInteractor;
import com.vitalityactive.va.snv.history.interactor.ScreeningAndVaccinationsHistoryInteractorImpl;
import com.vitalityactive.va.snv.history.presenter.ScreenAndVaccinationHistoryDetailPresenter;
import com.vitalityactive.va.snv.history.presenter.ScreenAndVaccinationHistoryDetailPresenterImpl;
import com.vitalityactive.va.snv.history.presenter.ScreenAndVaccinationHistoryPresenter;
import com.vitalityactive.va.snv.history.presenter.ScreenAndVaccinationHistoryPresenterImpl;
import com.vitalityactive.va.snv.history.repository.ScreenAndVaccinationHistoryRepository;
import com.vitalityactive.va.snv.history.repository.ScreenAndVaccinationHistoryRepositoryImpl;
import com.vitalityactive.va.snv.history.service.ScreenAndVaccinationHistoryServiceClient;
import com.vitalityactive.va.snv.history.service.ScreeningAndVaccinationHistoryService;
import com.vitalityactive.va.snv.history.utility.SNVHistoryDetailProofProvider;
import com.vitalityactive.va.snv.learnmore.content.ScreeningDeclarationContent;
import com.vitalityactive.va.snv.learnmore.content.ScreeningStringsFileContent;
import com.vitalityactive.va.snv.learnmore.presenter.SnvLearnMorePresenter;
import com.vitalityactive.va.snv.learnmore.presenter.SnvLearnMorePresenterImpl;
import com.vitalityactive.va.snv.onboarding.interactor.ScreeningsAndVaccinationsInteractor;
import com.vitalityactive.va.snv.onboarding.interactor.ScreeningsAndVaccinationsInteractorImpl;
import com.vitalityactive.va.snv.onboarding.presenter.HealthActionsPresenter;
import com.vitalityactive.va.snv.onboarding.presenter.HealthActionsPresenterImpl;
import com.vitalityactive.va.snv.onboarding.presenter.ScreeningsAndVaccinationsOnboardingPresenter;
import com.vitalityactive.va.snv.onboarding.presenter.ScreeningsAndVaccinationsOnboardingPresenterImpl;
import com.vitalityactive.va.snv.onboarding.repository.ScreeningsAndVaccinationsRepositoryImpl;
import com.vitalityactive.va.snv.onboarding.repository.ScreeningsAndVaccinationsRepositoy;
import com.vitalityactive.va.snv.onboarding.service.GetPotentialPointsAndEventsCompletedPointsService;
import com.vitalityactive.va.snv.onboarding.service.GetPotentialPointsAndEventsCompletedPointsServiceClient;
import com.vitalityactive.va.snv.partners.interactor.SnvParticipatingPartnersInteractor;
import com.vitalityactive.va.snv.partners.interactor.SnvParticipatingPartnersInteractorImpl;
import com.vitalityactive.va.snv.partners.presenter.SnvParticipatingPartnersPresenter;
import com.vitalityactive.va.snv.partners.presenter.SnvParticipatingPartnersPresenterImpl;
import com.vitalityactive.va.snv.partners.service.GetPartnersByCategoryService;
import com.vitalityactive.va.snv.partners.service.GetPartnersByCategoryServiceClient;
import com.vitalityactive.va.splashscreen.AppConfigDataUpdater;
import com.vitalityactive.va.splashscreen.AppConfigDataUpdaterImpl;
import com.vitalityactive.va.termsandconditions.CaptureLoginEventService;
import com.vitalityactive.va.termsandconditions.CaptureLoginEventServiceClient;
import com.vitalityactive.va.termsandconditions.GeneralTermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.GeneralTermsAndConditionsInstructionRepositoryImpl;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsEventConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractorImpl;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenterImpl;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsWithoutAgreeButtonBarPresenter;
import com.vitalityactive.va.userpreferences.PreferencesPrivacyPolicyPresenterImpl;
import com.vitalityactive.va.userpreferences.ShareVitalityStatusPreferenceServiceClient;
import com.vitalityactive.va.userpreferences.UserPreferencesService;
import com.vitalityactive.va.utilities.CMSImageLoader;
import com.vitalityactive.va.utilities.PackageManagerUtilities;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;
import com.vitalityactive.va.utilities.photohandler.CameraGalleryInvoker;
import com.vitalityactive.va.utilities.photohandler.CameraGalleryInvokerImpl;
import com.vitalityactive.va.vhc.PDFPresenter;
import com.vitalityactive.va.vhc.PDFPresenterImpl;
import com.vitalityactive.va.vhc.addproof.ProofImageFetcher;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContentFromStringResource;
import com.vitalityactive.va.vhc.detail.HealthAttributeDetailPresenter;
import com.vitalityactive.va.vhc.detail.HealthAttributeDetailPresenterImpl;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepositoryImpl;
import com.vitalityactive.va.vhc.landing.VHCHealthMeasurementsPresenter;
import com.vitalityactive.va.vhc.landing.VHCHealthMeasurementsPresenterImpl;
import com.vitalityactive.va.vhc.service.HealthAttributeService;
import com.vitalityactive.va.vhc.service.HealthAttributeServiceClient;
import com.vitalityactive.va.vhr.content.QuestionnaireContent;
import com.vitalityactive.va.vhr.content.QuestionnaireContentFromResourceString;
import com.vitalityactive.va.vhr.content.VHRContent;
import com.vitalityactive.va.vhr.content.VHRContentFromStringResource;
import com.vitalityactive.va.vhr.disclaimer.VHRDisclaimerPresenter;
import com.vitalityactive.va.vhr.landing.VHRLandingPresenterImpl;
import com.vitalityactive.va.vhr.landing.VHRVitalityAgePresenter;
import com.vitalityactive.va.vhr.landing.VHRVitalityAgePresenterImpl;
import com.vitalityactive.va.vhr.questions.VHRAssessmentCompletedPresenter;
import com.vitalityactive.va.vitalitystatus.ProductPointsContent;
import com.vitalityactive.va.vitalitystatus.ProductPointsContentImpl;
import com.vitalityactive.va.vitalitystatus.VitalityStatusContent;
import com.vitalityactive.va.vitalitystatus.VitalityStatusLevelIncreasedPresenter;
import com.vitalityactive.va.vitalitystatus.VitalityStatusLevelIncreasedPresenterImpl;
import com.vitalityactive.va.vitalitystatus.detail.VitalityStatusLevelDetailPresenter;
import com.vitalityactive.va.vitalitystatus.detail.VitalityStatusLevelDetailPresenterImpl;
import com.vitalityactive.va.vitalitystatus.earningpoints.StatusPointsPresenter;
import com.vitalityactive.va.vitalitystatus.earningpoints.StatusPointsPresenterImpl;
import com.vitalityactive.va.vitalitystatus.earningpoints.VitalityStatusDetailsPresenter;
import com.vitalityactive.va.vitalitystatus.earningpoints.VitalityStatusDetailsPresenterImpl;
import com.vitalityactive.va.vitalitystatus.landing.StatusLandingInteractor;
import com.vitalityactive.va.vitalitystatus.landing.StatusLandingInteractorImpl;
import com.vitalityactive.va.vitalitystatus.landing.VitalityStatusLandingPresenter;
import com.vitalityactive.va.vitalitystatus.landing.VitalityStatusLandingPresenterImpl;
import com.vitalityactive.va.vitalitystatus.repository.ProductPointsRepository;
import com.vitalityactive.va.vitalitystatus.repository.ProductPointsRepositoryImpl;
import com.vitalityactive.va.vitalitystatus.repository.VitalityStatusRepository;
import com.vitalityactive.va.vitalitystatus.repository.VitalityStatusRepositoryImpl;
import com.vitalityactive.va.vna.VNAAssessmentCompletedPresenter;
import com.vitalityactive.va.vna.content.VNAContent;
import com.vitalityactive.va.vna.disclaimer.VNADisclaimerPresenter;
import com.vitalityactive.va.vna.landing.VNALandingPresenter;
import com.vitalityactive.va.wellnessdevices.informative.WellnessDevicesInformativeInteractor;
import com.vitalityactive.va.wellnessdevices.informative.WellnessDevicesInformativeInteractorImpl;
import com.vitalityactive.va.wellnessdevices.informative.WellnessDevicesInformativePresenter;
import com.vitalityactive.va.wellnessdevices.informative.WellnessDevicesInformativePresenterImpl;
import com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingInteractorImpl;
import com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingPresenter;
import com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingPresenterImpl;
import com.vitalityactive.va.wellnessdevices.landing.repository.DeviceListRepository;
import com.vitalityactive.va.wellnessdevices.landing.repository.DeviceListRepositoryImpl;
import com.vitalityactive.va.wellnessdevices.landing.service.WellnessDevicesService;
import com.vitalityactive.va.wellnessdevices.landing.service.WellnessDevicesServiceClient;
import com.vitalityactive.va.wellnessdevices.linking.repository.LinkingPageRepository;
import com.vitalityactive.va.wellnessdevices.linking.repository.LinkingPageRepositoryImpl;
import com.vitalityactive.va.wellnessdevices.linking.service.DelinkDeviceService;
import com.vitalityactive.va.wellnessdevices.linking.service.LinkDeviceService;
import com.vitalityactive.va.wellnessdevices.linking.service.LinkDeviceServiceClient;
import com.vitalityactive.va.wellnessdevices.linking.web.WellnessDevicesWebFlowPresenter;
import com.vitalityactive.va.wellnessdevices.linking.web.WellnessDevicesWebFlowPresenterImpl;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.MeasurementContentFromResourceString;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.WellnessDevicesPointsInteractor;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.WellnessDevicesPointsInteractorImpl;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.WellnessDevicesPointsPresenter;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.WellnessDevicesPointsPresenterImpl;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.PotentialPointsService;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.PotentialPointsServiceClient;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DefaultModule {
    public static final String SHARED_PREFERENCES_FILENAME = "com.vitalityactive.va.sharedpreferences";
    private static final int CORE_POOL_SIZE = 3;
    private static final int MAXIMUM_POOL_SIZE = 10;
    private static final int KEEP_ALIVE_TIME = 120;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private final VitalityActiveApplication vitalityActiveApplication;

    public DefaultModule(VitalityActiveApplication vitalityActiveApplication) {
        this.vitalityActiveApplication = vitalityActiveApplication;
    }

    @Provides
    @Singleton
    static ExecutorServiceScheduler provideExecutorServiceScheduler() {
        return new ExecutorServiceScheduler(new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, new LinkedBlockingQueue<Runnable>()));
    }

    @Provides
    @Singleton
    static EventDispatcher provideEventDispatcher() {
        return new EventDispatcher();
    }

    @Provides
    @Singleton
    static WebServiceClient provideWebServiceClient(ExecutorServiceScheduler scheduler, EventDispatcher eventDispatcher) {
        return new WebServiceClient(scheduler, eventDispatcher);
    }

    @Provides
    @Singleton
    static ActiveRewardsActivator provideActiveRewardsActivator(EventDispatcher eventDispatcher,
                                                                ActiveRewardsActivationServiceClient activeRewardsActivationServiceClient,
                                                                WellnessDevicesLandingInteractorImpl wellnessDevicesLandingInteractor) {
        return new ActiveRewardsActivatorImpl(eventDispatcher, activeRewardsActivationServiceClient, wellnessDevicesLandingInteractor);
    }

    @Provides
    static MainThreadScheduler provideMainThreadScheduler() {
        return new MainThreadSchedulerImpl();
    }

    @Provides
    @Singleton
    static TermsAndConditionsPresenter<ActiveRewardsTermsAndConditionsUserInterface> provideActiveRewardsTermsAndConditionsPresenter(MainThreadScheduler scheduler, ActiveRewardsActivator activeRewardsActivator, @Named(DependencyNames.ACTIVE_REWARDS_TERMS_AND_CONDITIONS) TermsAndConditionsInteractor interactor, EventDispatcher eventDispatcher, @Named(DependencyNames.ACTIVE_REWARDS_TERMS_AND_CONDITIONS) TermsAndConditionsConsenter consenter) {
        ActiveRewardsTermsAndConditionsPresenterImpl presenter = new ActiveRewardsTermsAndConditionsPresenterImpl(scheduler, activeRewardsActivator, interactor, eventDispatcher, consenter);
        interactor.setCallback(presenter);
        return presenter;
    }

    @Provides
    @Named(DependencyNames.ACTIVE_REWARDS_TERMS_AND_CONDITIONS)
    @Singleton
    static TermsAndConditionsInteractor provideActiveRewardsTermsAndConditionsInteractor(CMSServiceClient serviceClient, EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher, appConfigRepository.getActiveRewardsMedicallyFitContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @Named(DependencyNames.ACTIVE_REWARDS_TERMS_AND_CONDITIONS)
    @Singleton
    static TermsAndConditionsConsenter provideActiveRewardsTermsAndConditionsConsenter(EventDispatcher eventDispatcher, EventServiceClient eventServiceClient) {
        return new TermsAndConditionsEventConsenter(eventDispatcher, eventServiceClient, new long[]{EventType._MEDICALLYFITAGREE}, new long[]{EventType._MEDICALLYFITDISAGREE});
    }

    @Provides
    @Singleton
    static RegistrationPresenter provideRegistrationPresenter(EventDispatcher eventDispatcher, RegistrationInteractorImpl interactor, MainThreadScheduler scheduler) {
        return new RegistrationPresenterImpl(eventDispatcher, interactor, scheduler);
    }

    @Provides
    @Singleton
    static LoginPresenter provideLoginPresenter(EventDispatcher eventDispatcher, LoginInteractorImpl interactor, MainThreadScheduler scheduler, DeviceSpecificPreferences deviceSpecificPreferences) {
        return new LoginPresenterImpl(eventDispatcher, interactor, scheduler, deviceSpecificPreferences);
    }

    @Provides
    @Singleton
    static ForgotPasswordPresenter provideForgotPasswordPresenter(ForgotPasswordInteractor interactor, EventDispatcher eventDispatcher) {
        return new ForgotPasswordPresenterImpl(interactor, eventDispatcher);
    }

    @Provides
    @Named(DependencyNames.GENERAL_TERMS_AND_CONDITIONS)
    static TermsAndConditionsInteractor provideMainTermsAndConditionsInteractor(CMSServiceClient serviceClient, EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher, appConfigRepository.getGeneralTermsAndConditionsContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @Singleton
    @Named(DependencyNames.NON_SMOKERS_DECLARATION)
    static TermsAndConditionsPresenter provideNonSmokersDeclarePresenter(MainThreadScheduler scheduler, @Named(DependencyNames.NON_SMOKERS_DECLARATION) TermsAndConditionsInteractor interactor, @Named(DependencyNames.NON_SMOKERS_DECLARATION) TermsAndConditionsConsenter consenter, EventDispatcher eventDispatcher, InsurerConfigurationRepository insurerConfigurationRepository) {
        if (insurerConfigurationRepository.shouldShowNonSmokersPrivacyPolicy()) {
            return new NonSmokersDeclarePresenter(scheduler, interactor, consenter, eventDispatcher);
        }
        return new TermsAndConditionsPresenterImpl(scheduler, interactor, consenter, eventDispatcher);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.NON_SMOKERS_DECLARATION)
    static TermsAndConditionsInteractor provideNonSmokersDeclareInteractor(CMSServiceClient serviceClient, EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher, appConfigRepository.getNonSmokersDeclarationContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @Singleton
    @Named(DependencyNames.NON_SMOKERS_DECLARATION)
    static TermsAndConditionsConsenter provideNonSmokersDeclareConsenter(EventDispatcher eventDispatcher, EventServiceClient eventServiceClient, InsurerConfigurationRepository insurerConfigurationRepository) {
        return new NonSmokersDeclarationConsenter(eventDispatcher, eventServiceClient, insurerConfigurationRepository);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.NON_SMOKERS_PRIVACY_POLICY)
    static TermsAndConditionsPresenter provideNonSmokersPrivacyPolicyPresenter(MainThreadScheduler scheduler, @Named(DependencyNames.NON_SMOKERS_PRIVACY_POLICY) TermsAndConditionsInteractor interactor, @Named(DependencyNames.NON_SMOKERS_PRIVACY_POLICY) TermsAndConditionsConsenter consenter, EventDispatcher eventDispatcher) {
        return new TermsAndConditionsPresenterImpl(scheduler, interactor, consenter, eventDispatcher);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.NON_SMOKERS_PRIVACY_POLICY)
    static TermsAndConditionsInteractor provideNonSmokersPrivacyPolicyInteractor(CMSServiceClient serviceClient, EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher, appConfigRepository.getNonSmokersPrivacyPolicyContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @Singleton
    @Named(DependencyNames.NON_SMOKERS_PRIVACY_POLICY)
    static TermsAndConditionsConsenter provideNonSmokersPrivacyPolicyConsenter(EventDispatcher eventDispatcher, EventServiceClient eventServiceClient, InsurerConfigurationRepository insurerConfigurationRepository) {
        return new NonSmokersDeclarationConsenter(eventDispatcher, eventServiceClient, insurerConfigurationRepository);
    }

    @Provides
    @Singleton
    static PointsMonitorPresenter providePointsMonitorAllPointsPresenter(MainThreadScheduler scheduler, PointsMonitorInteractorImpl interactor, EventDispatcher eventDispatcher, TimeUtilities timeUtilities, PointsMonitorContent content) {
        return new PointsMonitorPresenterImpl(scheduler, interactor, eventDispatcher, timeUtilities, content);
    }

    @Provides
    @Singleton
    static PointsMonitorSelectedCategoriesProvider providePointsMonitorSelectedCategoriesProvider(PointsMonitorPresenter presenter) {
        return presenter;
    }

    @Provides
    @Singleton
    static PointsMonitorAvailablePointsCategoriesProvider providePointsMonitorAvailablePointsCategoriesProvider(PointsMonitorRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    static PointsEntryRepository providePointsEntryProvider(PointsMonitorRepository repository) {
        return repository;
    }

    @Provides
    static VNAContent provideVNAContent(CMSKeyValueContentRepository contentRepository,
                                        LanguageProvider languageProvider) {
        return contentRepository.getContent(languageProvider.getCurrentSystemLanguage()).getVNAContent();
    }

    @Provides
    static WellnessDevicesWebFlowPresenter provideWellnessDevicesWebFlowPresenter() {
        return new WellnessDevicesWebFlowPresenterImpl();
    }

    @Provides
    @Singleton
    @Named(DependencyNames.PREFERENCES_PRIVACY_POLICY)
    static TermsAndConditionsInteractor providePreferencesPrivacyPolicyInteractor(CMSServiceClient serviceClient, EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher, appConfigRepository.getPreferencesPrivacyPolicyContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VNA)
    static TermsAndConditionsInteractor provideVNADisclaimerInteractor(CMSServiceClient serviceClient, EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher,
                appConfigRepository.getVNADisclaimerContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VHR)
    static TermsAndConditionsInteractor provideVHRDisclaimerInteractor(CMSServiceClient serviceClient, EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher,
                appConfigRepository.getVHRDisclaimerContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @Singleton
    static VitalityAgePresenter provideVitalityAgePresenter(VitalityAgeInteractor vitalityAgeInteractor, DeviceSpecificPreferences deviceSpecificPreferences) {
        return new VitalityAgePresenterImpl(vitalityAgeInteractor, deviceSpecificPreferences);
    }

    @Provides
    @Singleton
    static VitalityAgeInteractor provideVitalityAgeInteractor(VitalityAgeRepository vitalityAgeRepository, MyHealthRepository myHealthRepository) {
        return new VitalityAgeInteractorImpl(vitalityAgeRepository,myHealthRepository);
    }

    @Provides
    @Singleton
    static MyHealthLandingPresenter provideMyHealthPresenter(EventDispatcher eventDispatcher, HealthAttributeInformationServiceClient attributeInformationServiceClient, MyHealthLandingInteractor myHealthInteractor) {
        return new MyHealthLandingPresenterImpl(eventDispatcher, attributeInformationServiceClient, myHealthInteractor);
    }

    @Provides
    @Singleton
    static MyHealthRepository provideMyHealthRepository(DataStore dataStore) {
        return new MyHealthRepositoryImpl(dataStore);
    }

    @Provides
    @Singleton
    static MyHealthVitalityAgeProfileInteractor provideMyHealthVitalityAgeProfileInteractorImpl(MyHealthRepository myHealthRepository) {
        return new MyHealthVitalityAgeProfileInteractorImpl(myHealthRepository);
    }

    @Provides
    @Singleton
    static MyHealthVitalityAgeProfilePresenter provideMyHealthVitalityAgeProfilePresenter(MyHealthVitalityAgeProfileInteractor myHealthVitalityAgeProfileInteractor) {
        return new MyHealthVitalityAgeProfilePresenterImpl(myHealthVitalityAgeProfileInteractor);
    }

    @Provides
    @Singleton
    BaseURLSwitcher provideBaseURLSwitcher(DeviceSpecificPreferences devicePreferences, ServiceGenerator serviceGenerator) {
        return new BaseURLSwitcher(devicePreferences, serviceGenerator);
    }

    @Provides
    @Singleton
    GoalsAndProgressRepository provideGoalsAndProgressRepository(DataStore dataStore) {
        return new GoalsAndProgressRepositoryImpl(dataStore);
    }

    @Provides
    @Singleton
    GoalsProgressService provideGoalsProgressService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(GoalsProgressService.class);
    }

    @Provides
    @Singleton
    GoalsProgressServiceClient provideGoalsProgressServiceClient(WebServiceClient webServiceClient,
                                                                 PartyInformationRepository partyInformationRepository,
                                                                 GoalsProgressService service,
                                                                 AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        return new GoalsProgressServiceClient(webServiceClient, partyInformationRepository, service, accessTokenAuthorizationProvider);
    }

    @Provides
    @Singleton
    GoalHistoryRepository provideActiveRewardsHistoryRepository(DataStore dataStore) {
        return new GoalHistoryRepositoryImpl(dataStore);
    }

    @Provides
    @Singleton
    GoalHistoryInteractor provideActiveRewardsHistoryInteractor(GoalsProgressServiceClient serviceClient,
                                                                EventDispatcher eventDispatcher,
                                                                ConnectivityListener connectivityListener,
                                                                GoalHistoryRepository repository) {
        return new GoalHistoryInteractorImpl(serviceClient, eventDispatcher, connectivityListener, repository);
    }

    @Provides
    GoalHistoryPresenter provideActiveRewardsHistoryPresenter(GoalHistoryInteractor interactor,
                                                              EventDispatcher eventDispatcher,
                                                              MainThreadScheduler scheduler,
                                                              GoalHistoryRepository repository,
                                                              GoalsAndProgressRepository goalsAndProgressRepository,
                                                              DateFormattingUtilities dateFormattingUtilities) {
        return new GoalHistoryPresenterImpl(interactor, eventDispatcher, scheduler, repository, goalsAndProgressRepository, dateFormattingUtilities);
    }

    @Provides
    HistoryWeekDetailsPresenter provideHistoryWeekDetailsPresenter() {
        return new HistoryWeekDetailsPresenter();
    }

    @Provides
    NonSmokersDeclarationContent provideNonSmokersDeclarationContent() {
        return new NonSmokersDeclarationStringsFileContent(vitalityActiveApplication);
    }

    @Provides
    ScreeningDeclarationContent provideScreeningDeclarationContent() {
        return new ScreeningStringsFileContent(vitalityActiveApplication);
    }

    @Provides
    VHRContent provideVHRContent() {
        return new VHRContentFromStringResource(vitalityActiveApplication);
    }

    @Provides
    @Singleton
    PointsMonitorContent providePointsMonitorContent() {
        return new PointsMonitorContentImpl(vitalityActiveApplication);
    }

    @Provides
    @Singleton
    public DeviceSpecificPreferences provideDeviceSpecificPreferences() {
        return new DeviceSpecificPreferences(getSharedPreferences(), vitalityActiveApplication);
    }

    SharedPreferences getSharedPreferences() {
        return vitalityActiveApplication.getSharedPreferences(SHARED_PREFERENCES_FILENAME, Context.MODE_PRIVATE);
    }

    SharedPreferences getSharedPreferences(String name) {
        return vitalityActiveApplication.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public InAppPreferences provideBadgePreferences() {
        return new InAppPreferences(getSharedPreferences(InAppPreferences.SHARED_PREFERENCES_FILENAME));
    }

    @Provides
    @Singleton
    LoginRepository provideLoginRepository(DataStore dataStore, AppConfigRepository appConfigRepository) {
        return new LoginRepositoryImpl(dataStore, appConfigRepository);
    }

    @Provides
    @Singleton
    AppConfigRepository provideAppConfigRepository(DataStore dataStore) {
        return new AppConfigRepository(dataStore, vitalityActiveApplication);
    }

    @Provides
    @Singleton
    AppConfigDataUpdater provideAppConfigDataUpdater(AppConfigServiceClient appConfigServiceClient, AppConfigRepository appConfigRepository, CMSContentDownloader contentDownloader, EventDispatcher eventDispatcher) {
        return new AppConfigDataUpdaterImpl(appConfigServiceClient, appConfigRepository, contentDownloader, eventDispatcher);
    }

    @Provides
    @Singleton
    LanguageProvider provideLanguageProvider() {
        return new LanguageProvider(vitalityActiveApplication.getResources());
    }

    @Provides
    FallbackContentInputStreamLoader provideDefaultContentInputStreamLoader() {
        return new FallbackContentInputStreamFromAssetsLoader(vitalityActiveApplication);
    }

    @Provides
    @Singleton
    CMSKeyValueContentRepository provideCMSKeyValueContentProvider(Gson gson,
                                                                   AppConfigRepository appConfigRepository,
                                                                   FallbackContentInputStreamLoader fallbackContentInputStreamLoader) {
        return new CMSKeyValueContentRepository(gson, vitalityActiveApplication.getFilesDir(), appConfigRepository, fallbackContentInputStreamLoader);
    }

    @Provides
    @Singleton
    NavigationCoordinator provideNavigationCoordinator(GeneralTermsAndConditionsInstructionRepositoryImpl termsAndConditionsInstructionRepository,
                                                       RegistrationInteractorImpl registrationInteractor,
                                                       LoginPresenter loginPresenter,
                                                       InsurerConfigurationRepository insurerConfigurationRepository,
                                                       DeviceSpecificPreferences deviceSpecificPreferences,
                                                       EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository, DeviceListRepository deviceListRepository,
                                                       InAppPreferences inAppPreferences) {
        return new NavigationCoordinator(new NavigatorImpl(),
                termsAndConditionsInstructionRepository,
                registrationInteractor,
                (LoginPresenterImpl) loginPresenter,
                insurerConfigurationRepository,
                deviceSpecificPreferences,
                eventDispatcher,
                vitalityActiveApplication, appConfigRepository, deviceListRepository, inAppPreferences);
    }

    @Provides
    @Singleton
    ConnectivityListener provideConnectivityListener(EventDispatcher eventDispatcher) {
        return new ConnectivityListener(vitalityActiveApplication, eventDispatcher);
    }

    @Provides
    List<CredentialPresenter> provideCredentialPresenters(RegistrationInteractorImpl registrationInteractor) {
        CredentialPresenter confirmationPasswordPresenter = new ConfirmationPasswordPresenter.Builder()
                .setHint(R.string.registration_confirm_password_field_title_30)
                .setValidationMessage(vitalityActiveApplication.getString(R.string.registration_mismatched_password_footnote_error_36))
                .setIconResourceId(R.drawable.confirm_active)
                .setDisabledIconResourceId(R.drawable.confirm_inactive)
                .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .setRegistrationInteractor(registrationInteractor)
                .build();
        return Arrays.asList(
                new UsernamePresenter.Builder()
                        .setHint(R.string.registration_email_field_title_26)
                        .setValidationMessage(vitalityActiveApplication.getString(R.string.registration_invalid_email_footnote_error_35))
                        .setIconResourceId(R.drawable.email_active)
                        .setDisabledIconResourceId(R.drawable.email_inactive)
                        .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                        .setRegistrationInteractor(registrationInteractor)
                        .build(),
                new PasswordPresenter.Builder()
                        .setHint(R.string.password_field_placeholder_19)
                        .setFieldDescription(vitalityActiveApplication.getString(R.string.registration_password_field_footnote_29))
                        .setValidationMessage(vitalityActiveApplication.getString(R.string.registration_password_field_footnote_29))
                        .setIconResourceId(R.drawable.password_active)
                        .setDisabledIconResourceId(R.drawable.password_inactive)
                        .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .setRegistrationInteractor(registrationInteractor)
                        .setCallback((CredentialPresenterCallback) confirmationPasswordPresenter)
                        .build(),
                confirmationPasswordPresenter,
                new InsurerCodePresenter.Builder()
                        .setHint(R.string.registration_registration_code_field_title_32)
                        .setValidationMessage(vitalityActiveApplication.getString(R.string.registration_registration_code_field_placeholder_33))
                        .setIconResourceId(R.drawable.code_active)
                        .setDisabledIconResourceId(R.drawable.code_inactive)
                        .setInputType(InputType.TYPE_CLASS_TEXT)
                        .setRegistrationInteractor(registrationInteractor)
                        .build()
        );
    }

    @Provides
    @Singleton
    TermsAndConditionsPresenter provideTermsAndConditionsPresenter(MainThreadScheduler scheduler,
                                                                   @Named(DependencyNames.GENERAL_TERMS_AND_CONDITIONS) TermsAndConditionsInteractor interactor,
                                                                   TermsAndConditionsConsenter consenter,
                                                                   EventDispatcher eventDispatcher) {
        return new TermsAndConditionsPresenterImpl(scheduler, interactor, consenter, eventDispatcher);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.TERMS_AND_CONDITIONS_NO_AGREE_BUTTON)
    TermsAndConditionsPresenter provideTermsAndConditionsWithoutAgreeButtonBarPresenter(MainThreadScheduler scheduler,
                                                                                        @Named(DependencyNames.GENERAL_TERMS_AND_CONDITIONS) TermsAndConditionsInteractor interactor,
                                                                                        TermsAndConditionsConsenter consenter,
                                                                                        EventDispatcher eventDispatcher) {
        return new TermsAndConditionsWithoutAgreeButtonBarPresenter(scheduler, interactor, consenter, eventDispatcher);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.PREFERENCES_PRIVACY_POLICY)
    TermsAndConditionsWithoutAgreeButtonBarPresenter providePreferencesPrivacyPolicyPresenter(MainThreadScheduler scheduler,
                                                                                              @Named(DependencyNames.PREFERENCES_PRIVACY_POLICY) TermsAndConditionsInteractor interactor,
                                                                                              TermsAndConditionsConsenter consenter,
                                                                                              EventDispatcher eventDispatcher) {
        return new PreferencesPrivacyPolicyPresenterImpl(scheduler, interactor, consenter, eventDispatcher);
    }

    @Provides
    @Singleton
    TermsAndConditionsConsenter provideTermsAndConditionsConsenter(GeneralTermsAndConditionsInstructionRepositoryImpl instructionRepository,
                                                                   EventDispatcher eventDispatcher,
                                                                   CaptureLoginEventServiceClient serviceClient) {
        return new GeneralTermsAndConditionsConsenter(eventDispatcher, serviceClient, instructionRepository);
    }

    @Provides
    @Singleton
    PartyInformationRepository providePartyInformationRepository(DataStore dataStore, DeviceSpecificPreferences deviceSpecificPreferences) {
        return new PartyInformationRepositoryImpl(dataStore, deviceSpecificPreferences);
    }

    @Provides
    @Singleton
    ActiveRewardsService provideActiveRewardsService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(ActiveRewardsService.class);
    }

    @Provides
    @Singleton
    AppConfigService provideAppConfigService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(AppConfigService.class);
    }

    @Provides
    @Singleton
    EventService provideEventService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(EventService.class);
    }

    @Provides
    @Singleton
    CMSService provideCMSService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(CMSService.class);
    }

    @Provides
    @Singleton
    PointsMonitorService providePointsMonitorService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(PointsMonitorService.class);
    }

    @Provides
    @Singleton
    CaptureLoginEventService provideCaptureLoginEventService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(CaptureLoginEventService.class);
    }

    @Provides
    OnboardingPresenter provideOnboardingPresenter(DeviceSpecificPreferences preferences) {
        return new OnboardingPresenterImpl(preferences);
    }

    @Provides
    NonSmokersCardRepository provideNonSmokersCardRepository(HomeScreenCardSectionRepository repository) {
        return new NonSmokersCardRepositoryImpl(repository);
    }

    @Provides
    VHCCardRepository provideVHCCardRepository(HomeScreenCardSectionRepository repository) {
        return new VHCCardRepositoryImpl(repository);
    }

    @Provides
    SNVCardRepository provideSNVCardRepository(HomeScreenCardSectionRepository repository) {
        return new SNVCardRepositoryImpl(repository);
    }

    @Provides
    MWBCardRepository provideMWBCardRepository(HomeScreenCardSectionRepository repository) {
        return new MWBCardRepositoryImpl(repository);
    }

    @Provides
    VHRCardRepository provideVHRCardRepository(HomeScreenCardSectionRepository repository) {
        return new VHRCardRepositoryImpl(repository);
    }

    @Provides
    VNACardRepository provideVNACardRepository(HomeScreenCardSectionRepository repository) {
        return new VNACardRepositoryImpl(repository);
    }

    @Provides
    PartnerCardRepository provideHealthServiceCardRepository(HomeScreenCardSectionRepository repository) {
        return new PartnerCardRepositoryImpl(repository);
    }

    @Provides
    @Singleton
    QuestionFactory provideQuestionFactory(QuestionnaireUnitOfMeasureContent content) {
        return new QuestionFactory(content);
    }

    @Provides
    @Singleton
    ValidationRuleMapper provideValidationRuleFactory() {
        return new ValidationRuleMapper();
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VHR)
    QuestionnaireSetRepository provideVHRQuestionnaireSetRepository(@Named(DependencyNames.VHR) DataStore dataStore,
                                                                    QuestionFactory questionFactory,
                                                                    ValidationRuleMapper validationRuleFactory) {
        return new QuestionnaireSetRepositoryImpl(dataStore, questionFactory, validationRuleFactory, 30);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VNA)
    QuestionnaireSetRepository provideVNAQuestionnaireSetRepository(@Named(DependencyNames.VNA) DataStore dataStore,
                                                                    QuestionFactory questionFactory,
                                                                    ValidationRuleMapper validationRuleFactory) {
        return new QuestionnaireSetRepositoryImpl(dataStore, questionFactory, validationRuleFactory, 30);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VNA)
    TermsAndConditionsWithoutAgreeButtonBarPresenter provideVNADiclaimerPresenter(MainThreadScheduler scheduler,
                                                                                  @Named(DependencyNames.VNA) TermsAndConditionsInteractor interactor,
                                                                                  TermsAndConditionsConsenter consenter,
                                                                                  EventDispatcher eventDispatcher) {
        return new VNADisclaimerPresenter(scheduler, interactor, consenter, eventDispatcher);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VHR)
    TermsAndConditionsWithoutAgreeButtonBarPresenter provideVHRDiclaimerPresenter(MainThreadScheduler scheduler,
                                                                                  @Named(DependencyNames.VHR) TermsAndConditionsInteractor interactor,
                                                                                  TermsAndConditionsConsenter consenter,
                                                                                  EventDispatcher eventDispatcher) {
        return new VHRDisclaimerPresenter(scheduler, interactor, consenter, eventDispatcher);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.ACTIVE_REWARDS_HOME_CARD_REPOSITORY)
    BaseHomeCardRepository provideActiveRewardsCardRepository(HomeScreenCardSectionRepository repository) {
        return new BaseHomeCardRepositoryImpl(repository, HomeCardType.ACTIVE_REWARDS);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.AR_REWARDS_HOME_CARD_REPOSITORY)
    BaseHomeCardRepository provideActiveRewardsRewardsCardRepository(HomeScreenCardSectionRepository repository) {
        return new BaseHomeCardRepositoryImpl(repository, HomeCardType.REWARDS);
    }

    @Provides
    @Singleton
    UserPreferencesService provideUserPreferencesService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(UserPreferencesService.class);
    }

    @Provides
    @Singleton
    HealthAttributeService provideHealthMeasurementsService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(HealthAttributeService.class);
    }

    @Provides
    @Singleton
    HomeScreenCardStatusService provideHomeScreenCardStatusService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(HomeScreenCardStatusService.class);
    }

    @Provides
    @Singleton
    EventByPartyService provideEventByPartyService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(EventByPartyService.class);
    }

    @Provides
    @Singleton
    QuestionnaireSetService provideQuestionnaireSetService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(QuestionnaireSetService.class);
    }

    @Provides
    @Singleton
    HomeScreenCardSectionRepository provideHomeScreenCardSectionRepository(DataStore dataStore) {
        return new HomeScreenCardSectionRepositoryImpl(dataStore);
    }

    @Provides
    @Singleton
    RewardsHomeScreenCardRepository provideRewardsVoucherHomeScreenCardRepository(DataStore dataStore) {
        return new RewardsHomeScreenCardRepositoryImpl(dataStore);
    }

    @Provides
    @Singleton
    VitalityStatusRepository provideVitalityStatusRepository(DataStore dataStore) {
        return new VitalityStatusRepositoryImpl(dataStore);
    }

    @Provides
    HomeInteractor provideHomeFragmentInteractor(EventDispatcher eventDispatcher,
                                                 HomeScreenCardStatusServiceClient cardsServiceClient,
                                                 EventByPartyServiceClient eventByPartyServiceClient,
                                                 ConnectivityListener connectivityListener,
                                                 HomeScreenCardSectionRepository cardRepository,
                                                 VitalityStatusRepository vitalityStatusRepository) {
        return new HomeInteractorImpl(cardsServiceClient,
                eventByPartyServiceClient,
                connectivityListener,
                cardRepository,
                eventDispatcher,
                vitalityStatusRepository);
    }

    @Provides
    HomePresenter provideHomeFragmentPresenter(HomeInteractor interactor,
                                               EventDispatcher eventDispatcher,
                                               MainThreadScheduler scheduler,
                                               VitalityStatusRepository statusRepository) {
        return new HomePresenterImpl(interactor, eventDispatcher, scheduler, statusRepository);
    }


    @Provides
    @Singleton
    CMSContentDownloader provideCMSContentDownloader(CMSServiceClient serviceClient) {
        return new CMSContentDownloader(serviceClient, vitalityActiveApplication);
    }

    @Provides
    @Singleton
    CMSContentUploader provideCMSContentUploader(CMSServiceClient serviceClient) {
        return new CMSContentUploader(serviceClient);
    }

    @Provides
    @Singleton
    HealthAttributeRepository provideHealthAttributeRepository(DataStore dataStore, InsurerConfigurationRepository appConfigRepository, VHCHealthAttributeContent vhcHealthAttributeContent) {
        return new HealthAttributeRepositoryImpl(dataStore, appConfigRepository, vhcHealthAttributeContent);
    }

    @Provides
    @Singleton
    VHCHealthMeasurementsPresenter provideVHCHealthMeasurementsPresenter(HealthAttributeServiceClient healthAttributeServiceClient,
                                                                         HealthAttributeRepository healthAttributeRepository,
                                                                         EventDispatcher eventDispatcher,
                                                                         MainThreadScheduler scheduler,
                                                                         InsurerConfigurationRepository insurerConfigurationRepository, VitalityAgeServiceClient vitalityAgeServiceClient, DeviceSpecificPreferences deviceSpecificPreferences) {
        return new VHCHealthMeasurementsPresenterImpl(healthAttributeServiceClient, healthAttributeRepository, eventDispatcher, scheduler, insurerConfigurationRepository, vitalityAgeServiceClient, deviceSpecificPreferences);
    }

    @Provides
    @Singleton
    HealthAttributeDetailPresenter provideHealthAttributeDetailPresenter(HealthAttributeRepository healthAttributeRepository,
                                                                         InsurerConfigurationRepository insurerConfigurationRepository) {
        return new HealthAttributeDetailPresenterImpl(healthAttributeRepository,
                insurerConfigurationRepository);
    }

    @Provides
    @Singleton
    VHCHealthAttributeContent provideVHCHealthAttributeContent(InsurerConfigurationRepository insurerConfigurationRepository,
                                                               PartyInformationRepository partyInformationRepository) {
        return new VHCHealthAttributeContentFromStringResource(vitalityActiveApplication, insurerConfigurationRepository, partyInformationRepository);
    }


    @Provides
    @Singleton
    SNVHealthAttributeContent provideSNVHealthAttributeContent(InsurerConfigurationRepository insurerConfigurationRepository) {
        return new SNVHealthAttributeContentFromStringResource(vitalityActiveApplication, insurerConfigurationRepository);
    }

    @Provides
    @Singleton
    MWBHealthAttributeContent provideMWBHealthAttributeContent(InsurerConfigurationRepository insurerConfigurationRepository) {
        return new MWBHealthAttributeContentFromStringResource(vitalityActiveApplication, insurerConfigurationRepository);
    }

    @Provides
    @Singleton
    QuestionnaireUnitOfMeasureContent provideQuestionnaireUnitOfMeasureContent() {
        return new QuestionnaireContentFromResourceString(vitalityActiveApplication);
    }

    @Provides
    @Singleton
    QuestionnaireContent provideQuestionniareContent() {
        return new QuestionnaireContentFromResourceString(vitalityActiveApplication);
    }

    @Provides
    @Singleton
    ProofImageFetcher provideProofImageFetcher() {
        return new ProofImageFetcher(vitalityActiveApplication);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VHR)
    QuestionnaireLandingPresenter provideQuestionnaireLandingPresenter(VHRContent content,
                                                                       QuestionnaireSetServiceClient serviceClient,
                                                                       EventDispatcher eventDispatcher,
                                                                       MainThreadScheduler scheduler,
                                                                       @Named(DependencyNames.VHR) QuestionnaireSetRepository questionnaireSetRepository) {
        return new VHRLandingPresenterImpl(content, serviceClient, eventDispatcher, scheduler, questionnaireSetRepository);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VNA)
    QuestionnaireLandingPresenter provideVNALandingPresenter(VNAContent content,
                                                             QuestionnaireSetServiceClient serviceClient,
                                                             EventDispatcher eventDispatcher,
                                                             MainThreadScheduler scheduler,
                                                             @Named(DependencyNames.VNA) QuestionnaireSetRepository questionnaireSetRepository) {
        return new VNALandingPresenter(content, serviceClient, eventDispatcher, scheduler, questionnaireSetRepository);
    }

    @Provides
    @Singleton
    CMSServiceClient provideCMSServiceClient(WebServiceClient webServiceClient,
                                             CMSService cmsService,
                                             EventDispatcher eventDispatcher,
                                             AccessTokenAuthorizationProvider accessTokenAuthorizationProvider,
                                             PartyInformationRepository partyInformationRepository,
                                             ProofImageFetcher proofImageFetcher,
                                             ExecutorServiceScheduler scheduler) {
        return new CMSServiceClient(webServiceClient,
                cmsService,
                eventDispatcher,
                accessTokenAuthorizationProvider,
                partyInformationRepository,
                proofImageFetcher,
                scheduler);
    }

    @Provides
    @Singleton
    QuestionnaireSetServiceClient provideQuestionnaireSetServiceClient(
            AccessTokenAuthorizationProvider accessTokenAuthorizationProvider,
            QuestionnaireSetService service,
            WebServiceClient webServiceClient,
            PartyInformationRepository partyInformationRepository,
            EventDispatcher eventDispatcher,
            FallbackContentInputStreamLoader fallbackContentInputStreamLoader) {
        return new QuestionnaireSetServiceClient(accessTokenAuthorizationProvider,
                service,
                webServiceClient,
                partyInformationRepository,
                eventDispatcher,
                fallbackContentInputStreamLoader
        );
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VHR)
    AssessmentCompletedPresenter provideVHRAssessmentCompletedPresenter(@Named(DependencyNames.VHR) QuestionnaireSetRepository questionnaireSetRepository) {
        return new VHRAssessmentCompletedPresenter(questionnaireSetRepository);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VNA)
    AssessmentCompletedPresenter provideVNAAssessmentCompletedPresenter(@Named(DependencyNames.VNA) QuestionnaireSetRepository questionnaireSetRepository) {
        return new VNAAssessmentCompletedPresenter(questionnaireSetRepository);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.MWB)
    AssessmentCompletedPresenter provideMWBAssessmentCompletedPresenter(@Named(DependencyNames.MWB) QuestionnaireSetRepository questionnaireSetRepository) {
        return new MWBAssessmentCompletedPresenter(questionnaireSetRepository);
    }

    @Provides
    @Singleton
    WellnessDevicesService provideWellnessDevicesService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(WellnessDevicesService.class);
    }

    @Provides
    WellnessDevicesLandingInteractorImpl provideWellnessDevicesLandingInteractor(WellnessDevicesServiceClient wellnessDevicesServiceClient,
                                                                                 PotentialPointsServiceClient pointsService,
                                                                                 CMSServiceClient cmsServiceClient,
                                                                                 EventDispatcher eventDispatcher,
                                                                                 ConnectivityListener connectivityListener,
                                                                                 DeviceListRepository deviceListRepository, AppConfigRepository appConfigRepository) {
        return new WellnessDevicesLandingInteractorImpl(wellnessDevicesServiceClient, pointsService, cmsServiceClient,
                eventDispatcher, connectivityListener, deviceListRepository, appConfigRepository);
    }

    @Provides
    WellnessDevicesLandingPresenter provideWellnessDevicesLandingPresenter(WellnessDevicesLandingInteractorImpl interactor,
                                                                           EventDispatcher eventDispatcher,
                                                                           MainThreadScheduler scheduler) {
        return new WellnessDevicesLandingPresenterImpl(interactor, eventDispatcher, scheduler);
    }

    @Provides
    @Singleton
    LinkDeviceService provideLinkDeviceService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(LinkDeviceService.class);
    }

    @Provides
    @Singleton
    DelinkDeviceService provideDelinkDeviceServiceClient() {
        return new DelinkDeviceService();
    }

    @Provides
    @Singleton
    LinkDeviceServiceClient provideLinkDeviceServiceClient(WebServiceClient webServiceClient,
                                                           PartyInformationRepository partyInformationRepository,
                                                           LinkDeviceService service,
                                                           AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        return new LinkDeviceServiceClient(webServiceClient, partyInformationRepository, service, accessTokenAuthorizationProvider);
    }

    @Provides
    @Singleton
    WellnessDevicesInformativeInteractor provideWellnessDevicesInformativeInteractor(CMSServiceClient cmsServiceClient,
                                                                                     EventDispatcher eventDispatcher,
                                                                                     ConnectivityListener connectivityListener,
                                                                                     AppConfigRepository appConfigRepository) {
        return new WellnessDevicesInformativeInteractorImpl(cmsServiceClient, eventDispatcher,
                connectivityListener, appConfigRepository.getLiferayGroupId());
    }

    @Provides
    WellnessDevicesInformativePresenter provideWellnessDevicesInformativePresenter(MainThreadScheduler scheduler,
                                                                                   WellnessDevicesInformativeInteractor interactor) {
        return new WellnessDevicesInformativePresenterImpl(scheduler, interactor);
    }

    @Provides
    @Singleton
    DeviceListRepository provideDeviceListRepository(DataStore dataStore) {
        return new DeviceListRepositoryImpl(dataStore);
    }

    @Provides
    @Singleton
    LinkingPageRepository provideLinkingPageRepository(DataStore dataStore) {
        return new LinkingPageRepositoryImpl(dataStore);
    }

    @Provides
    @Singleton
    MeasurementContentFromResourceString provideMeasurementContentFromResourceString() {
        return new MeasurementContentFromResourceString(vitalityActiveApplication);
    }

    @Provides
    @Singleton
    PotentialPointsService providePotentialPointsService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(PotentialPointsService.class);
    }

    @Provides
    @Singleton
    PotentialPointsServiceClient providePotentialPointsServiceClient(WebServiceClient webServiceClient,
                                                                     PartyInformationRepository partyInformationRepository,
                                                                     PotentialPointsService service,
                                                                     AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        return new PotentialPointsServiceClient(webServiceClient, partyInformationRepository, service, accessTokenAuthorizationProvider);
    }

    @Provides
    @Singleton
    VitalityAgeService provideVitalityAgeService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(VitalityAgeService.class);
    }

    @Provides
    @Singleton
    VitalityAgeServiceClient provideVitalityAgeServiceClient(WebServiceClient webServiceClient,
                                                             PartyInformationRepository partyInformationRepository,
                                                             VitalityAgeService eventByPartyService,
                                                             AccessTokenAuthorizationProvider accessTokenAuthorizationProvider, InsurerConfigurationRepository insurerConfigurationRepository, EventDispatcher eventDispatcher) {
        return new VitalityAgeServiceClient(webServiceClient,
                partyInformationRepository,
                eventByPartyService,
                accessTokenAuthorizationProvider, insurerConfigurationRepository, eventDispatcher);
    }

    @Provides
    @Singleton
    HealthAttributeFeedbackService provideHealthAttributeFeedbackService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(HealthAttributeFeedbackService.class);
    }

    @Provides
    @Singleton
    HealthAttributeFeedbackServiceClient provideHealthAttributeFeedbackServiceClient(WebServiceClient webServiceClient,
                                                                                     HealthAttributeFeedbackService attributeFeedbackService,
                                                                                     PartyInformationRepository partyInformationRepository,
                                                                                     EventDispatcher eventDispatcher,
                                                                                     AccessTokenAuthorizationProvider authorizationProvider, MyHealthRepository myHealthRepository) {
        return new HealthAttributeFeedbackServiceClient(webServiceClient,
                attributeFeedbackService,
                partyInformationRepository,
                eventDispatcher,
                authorizationProvider, myHealthRepository);
    }

    @Provides
    @Singleton
    WellnessDevicesPointsInteractor provideWellnessDevicesPointsInteractor(EventDispatcher eventDispatcher,
                                                                           ConnectivityListener connectivityListener,
                                                                           LinkingPageRepository linkingPageRepository) {
        return new WellnessDevicesPointsInteractorImpl(eventDispatcher, connectivityListener, linkingPageRepository);
    }

    @Provides
    WellnessDevicesPointsPresenter provideWellnessDevicesPointsPresenter(WellnessDevicesPointsInteractorImpl interactor,
                                                                         EventDispatcher eventDispatcher,
                                                                         MainThreadScheduler scheduler,
                                                                         MeasurementContentFromResourceString stringsProvider) {
        return new WellnessDevicesPointsPresenterImpl(interactor, eventDispatcher, scheduler, stringsProvider);
    }

    @Provides
    @Singleton
    PDFPresenter provideHealthCareBenefitPresenter(CMSContentDownloader contentDownloader, AppConfigRepository appConfigRepository, MainThreadScheduler scheduler) {
        return new PDFPresenterImpl(contentDownloader, appConfigRepository, scheduler);
    }

    @Provides
    @Singleton
    VitalityAgeRepository provideVitalityAgeRepository(DataStore dataStore, InsurerConfigurationRepository insurerConfigurationRepository) {
        return new VitalityAgeRepositoryImpl(dataStore, insurerConfigurationRepository);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.VHR)
    VHRVitalityAgePresenter provideVHRLandingPresenter(EventDispatcher eventDispatcher, HealthAttributeInformationServiceClient attributeInformationServiceClient,VitalityAgeInteractor vitalityAgeInteractor, DeviceSpecificPreferences deviceSpecificPreferences) {
        return new VHRVitalityAgePresenterImpl( eventDispatcher, attributeInformationServiceClient,vitalityAgeInteractor, deviceSpecificPreferences);
    }

    @Provides
    @Singleton
    VitalityAgePreferencesManager provideVitalityAgePreferencesManager(DeviceSpecificPreferences deviceSpecificPreferences, InsurerConfigurationRepository insurerConfigurationRepository) {
        return new VitalityAgePreferencesManagerImpl(deviceSpecificPreferences, insurerConfigurationRepository);
    }

    @Provides
    @Singleton
    MyHealthLandingInteractor provideMyHealthInteractor(MyHealthRepository myHealthRepository) {
        return new MyHealthLandingInteractorImpl(myHealthRepository);
    }

    @Provides
    @Singleton
    ProfileImageProvider profileImageProvider(CMSContentDownloader cmsContentDownloader, EventDispatcher eventDispatcher) {
        return new ProfileImageProvider(vitalityActiveApplication, cmsContentDownloader, eventDispatcher);
    }

    @Provides
    VitalityStatusContent provideVitalityStatusContent() {
        return new VitalityStatusContent(vitalityActiveApplication);
    }

    @Provides
    @Singleton
    VitalityStatusLandingPresenter provideVitalityStatusLandingPresenter(StatusLandingInteractor statusLandingInteractor,
                                                                         EventDispatcher eventDispatcher) {
        return new VitalityStatusLandingPresenterImpl(statusLandingInteractor, eventDispatcher);
    }

    @Provides
    @Singleton
    VitalityStatusLevelDetailPresenter provideVitalityStatusDetailPresenter(VitalityStatusRepository vitalityStatusRepository) {
        return new VitalityStatusLevelDetailPresenterImpl(vitalityStatusRepository);
    }

    @Provides
    @Singleton
    VitalityStatusDetailsPresenter provideVitalityStatusEarningPointsPresenter(ProductPointsRepository productPointsRepository,
                                                                               ProductPointsContent productPointsContent) {
        return new VitalityStatusDetailsPresenterImpl(productPointsRepository, productPointsContent);
    }

    @Provides
    @Singleton
    VitalityStatusLevelIncreasedPresenter provideVitalityStatusLevelIncreasedPresenter(VitalityStatusRepository statusRepository) {
        return new VitalityStatusLevelIncreasedPresenterImpl(statusRepository);
    }

    @Provides
    @Singleton
    MyHealthOnboardingContent provideMyHealthOnboardingContent() {
        return new MyHealthOnboardingContentImpl(vitalityActiveApplication);
    }

    @Provides
    @Singleton
    CMSImageLoader provideCmsImageLoader(CMSService cmsService, AppConfigRepository appConfigRepository,
                                         AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        return new CMSImageLoader(cmsService, appConfigRepository, accessTokenAuthorizationProvider);
    }

    @Provides
    @Singleton
    ProductFeaturePointsService provideStatusRewardsService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(ProductFeaturePointsService.class);
    }

    @Provides
    @Singleton
    ProductFeaturePointsServiceClient provideStatusRewardsServiceClient(WebServiceClient webServiceClient,
                                                                        ProductFeaturePointsService service,
                                                                        PartyInformationRepository partyInformationRepository,
                                                                        AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        return new ProductFeaturePointsServiceClient(webServiceClient, service, partyInformationRepository, accessTokenAuthorizationProvider);
    }

    @Provides
    @Singleton
    StatusLandingInteractor provideStatusLandingInteractor(ProductFeaturePointsServiceClient serviceClient,
                                                           EventDispatcher eventDispatcher,
                                                           VitalityStatusRepository vitalityStatusRepository,
                                                           ProductPointsRepository productPointsRepository) {
        return new StatusLandingInteractorImpl(serviceClient, eventDispatcher, vitalityStatusRepository, productPointsRepository);
    }

    @Provides
    @Singleton
    DateFormattingUtilities provideDateFormattingUtilities() {
        return new DateFormattingUtilities(vitalityActiveApplication);
    }

    @Provides
    @Singleton
    ProductPointsRepository provideProductPointsRepository(DataStore dataStore) {
        return new ProductPointsRepositoryImpl(dataStore);
    }

    @Provides
    @Singleton
    ProductPointsContent provideProductPointsContent() {
        return new ProductPointsContentImpl(vitalityActiveApplication);
    }

    @Provides
    ProfileInteractor provideProfileInteractor(ProfileImageProvider profileImageProvider,
                                               PartyInformationRepository partyInformationRepository) {
        return new ProfileInteractorImpl(profileImageProvider, partyInformationRepository);
    }

    @Provides
    ProfilePresenter provideProfilePresenter(ProfileInteractor profileInteractor, EventDispatcher eventDispatcher) {
        return new ProfilePresenterImpl(profileInteractor, eventDispatcher);
    }

    @Provides
    @Singleton
    StatusPointsPresenter provideStatusPointsPresenter(ProductPointsRepository pointsRepository) {
        return new StatusPointsPresenterImpl(pointsRepository);
    }

    @Provides
    @Singleton
    PersonalDetailsPresenter providePersonalDetailsPresenter(EventDispatcher eventDispatcher,
                                                             DateFormattingUtilities dateFormatUtil,
                                                             ProfileImageProvider profileImageProvider,
                                                             PartyInformationRepository partyInformationRepository,
                                                             PersonalDetailsInteractor personalDetailsInteractor, AppConfigRepository appConfigRepository) {
        return new PersonalDetailsPresenterImpl(eventDispatcher, dateFormatUtil, profileImageProvider, partyInformationRepository, personalDetailsInteractor, appConfigRepository);
    }


    @Provides
    @Singleton
    MyHealthTipsMoreResultsPresenter provideMyHealthTipsPresenter(MyHealthTipsInteractor myHealthTipsInteractor) {
        return new MyHealthTipsMoreResultsPresenterImpl(myHealthTipsInteractor);
    }

    @Provides
    @Singleton
    MyHealthAttributeDetailInteractor provideMyHealthAttributeDetailInteractor(MyHealthRepository myHealthRepository) {
        return new MyHealthAttributeDetailInteractorImpl(myHealthRepository);

    }

    @Provides
    @Singleton
    MyHealthAttributeDetailPresenter provideMyHealthAttributeDetailPresenter(MyHealthAttributeDetailInteractor myHealthAttributeDetailInteractor) {
        return new MyHealthAttributeDetailPresenterImpl(myHealthAttributeDetailInteractor);
    }

    @Provides
    @Singleton
    MyHealthMoreTipsPresenter provideMyHealthMoreTipsPresenter(MyHealthMoreTipsInteractor myHealthMoreTipsInteractor) {
        return new MyHealthMoreTipsPresenterImpl(myHealthMoreTipsInteractor);
    }

    @Provides
    @Singleton
    MyHealthTipsInteractor provideMyHealthTipsInteractor(MyHealthRepository myHealthRepository) {
        return new MyHealthTipsInteractorImpl(myHealthRepository);
    }

    @Provides
    MembershipPassPresenter provideMembershipPresenter(MembershipPassInteractor interactor,
                                                       EventDispatcher eventDispatcher,
                                                       ProfileImageProvider profileImageProvider,
                                                       PartyInformationRepository partyInfoRepo,
                                                       VitalityStatusRepository statusRepository,
                                                       HomeScreenCardSectionRepository homeScreenCardSectionRepository,
                                                       DateFormattingUtilities dateFormatUtil,
                                                       MembershipPassRepository membershipPassRepository,
                                                       MainThreadScheduler scheduler) {
        return new MembershipPassPresenterImpl(interactor, eventDispatcher, profileImageProvider, partyInfoRepo, statusRepository, homeScreenCardSectionRepository, dateFormatUtil, membershipPassRepository, scheduler);
    }

    @Provides
    MembershipPassInteractor provideMembershipPassInteractorImpl(MembershipPassServiceClient membershipPassServiceClient,
                                                                 @NonNull EventDispatcher eventDispatcher,
                                                                 ConnectivityListener connectivityListener,
                                                                 MembershipPassRepository membershipPassRepository) {
        return new MembershipPassInteractorImpl(membershipPassServiceClient, eventDispatcher, connectivityListener, membershipPassRepository);
    }

    @Provides
    @Singleton
    MembershipPassServiceClient providesMembershipPassServiceClient(WebServiceClient webServiceClient,
                                                                    PartyInformationRepository partyInformationRepository,
                                                                    MembershipPassService membershipPassService,
                                                                    AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        return new MembershipPassServiceClient(webServiceClient, partyInformationRepository, membershipPassService, accessTokenAuthorizationProvider);
    }

    @Provides
    @Singleton
    MembershipPassService providesMembershipPassService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(MembershipPassService.class);
    }

    @Provides
    @Singleton
    MembershipPassRepository provideMembershipRepository(DataStore dataStore) {
        return new MembershipPassRepositoryImpl(dataStore);
    }

    @Provides
    ChangeEmailPresenter provideChangeEmailPresenter(EventDispatcher eventDispatcher, PartyInformationRepository partyInformationRepository, PersonalDetailsInteractor personalDetailsInteractor, DeviceSpecificPreferences deviceSpecificPreferences) {
        return new ChangeEmailPresenterImpl(eventDispatcher, partyInformationRepository, personalDetailsInteractor, deviceSpecificPreferences);
    }

    @Provides
    @Singleton
    MyHealthMoreTipsInteractor provideMyHealthMoreTipsInteractor(MyHealthRepository myHealthRepository) {
        return new MyHealthMoreTipsInteractorImpl(myHealthRepository);
    }

    @Provides
    @Singleton
    MyHealthTipDetailInteractor provideMyHealthTipDetailInteractor(CMSServiceClient cmsServiceClient,
                                                                   EventDispatcher eventDispatcher,
                                                                   ConnectivityListener connectivityListener,
                                                                   AppConfigRepository appConfigRepository) {
        return new MyHealthTipDetailInteractorImpl(cmsServiceClient,
                eventDispatcher,
                connectivityListener,
                appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @Singleton
    MyHealthTipDetailPresenter provideMyHealthTipDetailPresenter(MainThreadScheduler scheduler,
                                                                 MyHealthTipDetailInteractor interactor) {
        return new MyHealthTipDetailPresenterImpl(scheduler, interactor);
    }

    @Provides
    @Singleton
    VitalityAgeLearnMorePresenter provideVitalityAgeLearnMorePresenter(MyHealthVitalityAgeProfileInteractor myHealthVitalityAgeProfileInteractor) {
        return new VitalityAgeLearnMorePresenterImpl(myHealthVitalityAgeProfileInteractor);
    }


    @Provides
    CameraGalleryInvoker provideCameraGalleryInvoker(EventDispatcher eventDispatcher) {
        return new CameraGalleryInvokerImpl(eventDispatcher);
    }


    @Provides
    @Singleton
    PersonalDetailsInteractor providePersonalDetailsInteractor(PersonalDetailsClient personalDetailsClient, EventDispatcher eventDispatcher, PartyInformationRepository partyInformationRepository, CMSContentUploader cmsContentUploader, AccessTokenAuthorizationProvider accessTokenAuthorizationProvider, DeviceSpecificPreferences deviceSpecificPreferences) {
        return new PersonalDetailsInteractorImpl(personalDetailsClient, eventDispatcher, partyInformationRepository, cmsContentUploader, accessTokenAuthorizationProvider, deviceSpecificPreferences );
    }


    @Provides
    @Singleton
    PackageManagerUtilities providePackageManagerUtilities() {
        return new PackageManagerUtilities(vitalityActiveApplication);
    }

    @Provides
    @Singleton
    VitalityMembershipRepository provideVitalityMembershipRepository(DataStore dataStore) {
        return new VitalityMembershipRepositoryImpl(dataStore);
    }

    @Provides
    @Singleton
    SettingChangePasswordInteractor provideSettingChangePasswordInteractor(PartyInformationRepository partyInformationRepository, ChangePasswordClient changePasswordClient, EventDispatcher eventDispatcher, AccessTokenAuthorizationProvider authorizationProvider) {
        return new SettingChangePasswordInteractorImpl(partyInformationRepository, changePasswordClient, eventDispatcher, authorizationProvider);
    }

    @Provides
    @Singleton
    EventsFeedPresenter provideEventsFeedPresenter(MainThreadScheduler scheduler, EventsFeedInteractorImpl interactor, EventDispatcher eventDispatcher, TimeUtilities timeUtilities, EventsFeedContent content, PartyInformationRepository partyInformationRepository) {
        return new EventsFeedPresenterImpl(scheduler, interactor, eventDispatcher, timeUtilities, content, partyInformationRepository);
    }

    @Provides
    @Singleton
    ScreeningsAndVaccinationsRepositoy provideScreeningsAndVaccinationsRepositoy(DataStore dataStore) {
        return new ScreeningsAndVaccinationsRepositoryImpl(dataStore);
    }

    @Provides
    @Singleton
    ScreeningsAndVaccinationsInteractor provideScreeningsAndVaccinationsInteractor(GetPotentialPointsAndEventsCompletedPointsServiceClient serviceClient,
                                                                                   ScreeningsAndVaccinationsRepositoy screeningsAndVaccinationsRepositoy) {
        return new ScreeningsAndVaccinationsInteractorImpl(serviceClient, screeningsAndVaccinationsRepositoy);
    }

    @Provides
    @Singleton
    GetPotentialPointsAndEventsCompletedPointsService provideGetPotentialPointsAndEventsCompletedPointsService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(GetPotentialPointsAndEventsCompletedPointsService.class);
    }

    @Provides
    @Singleton
    EventsFeedMonthPresenter provideEventsFeedMonthPresenter(EventsFeedRepository repository,
                                                             EventsFeedSelectedCategoriesProvider selectedCategoriesProvider,
                                                             EventsFeedInteractorImpl interactor) {
        return new EventsFeedMonthPresenterImpl(repository, selectedCategoriesProvider, interactor);
    }


    @Provides
    @Singleton
    EventsFeedApiServiceClient provideEventsFeedApiServiceClient(WebServiceClient webServiceClient, ServiceGenerator serviceGenerator) {
        return new EventsFeedApiServiceClient(webServiceClient, serviceGenerator);
    }


    @Provides
    @Singleton
    EventsFeedContent provideEventsFeedContent() {
        return new EventsFeedContentImpl(vitalityActiveApplication);
    }

    @Provides
    @Singleton
    EventsFeedAvailableEventsCategoriesProvider provideEventsFeedAvailableEventsCategoriesProvider(EventsFeedRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    EventsFeedSelectedCategoriesProvider provideEventsFeedSelectedCategoriesProvider(EventsFeedPresenter presenter) {
        return presenter;
    }

    @Provides
    HelpPresenter provideHelpPresenter(HelpInteractor interactor,
                                       EventDispatcher eventDispatcher,
                                       HelpRepository helpRepository,
                                       MainThreadScheduler scheduler) {
        return new HelpPresenterImpl(interactor, eventDispatcher, helpRepository, scheduler);
    }

    @Provides
    HelpInteractor provideHelpInteractorImpl(HelpServiceClient helpServiceClient,
                                                                 @NonNull EventDispatcher eventDispatcher,
                                                                 ConnectivityListener connectivityListener,
                                                                 HelpRepository helpRepository) {
        return new HelpInteractorImpl(helpServiceClient, eventDispatcher, connectivityListener, helpRepository);
    }

    @Provides
    @Singleton
    HelpServiceClient providesHelpServiceClient(WebServiceClient webServiceClient,
                                                ServiceGenerator serviceGenerator,
                                                                    AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        return new HelpServiceClient(webServiceClient, serviceGenerator, accessTokenAuthorizationProvider);
    }

    @Provides
    @Singleton
    HelpService providesHelpService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(HelpService.class);
    }

    @Provides
    @Singleton
    HelpRepository provideHelpRepository(DataStore dataStore) {
        return new HelpRepositoryImpl(dataStore);
    }

    @Provides
    @Singleton
    ContentHelpPresenter provideContentHelpPresenter(EventDispatcher eventDispatcher, ContentHelpInteractor contentHelpInteractor){
        return new ContentHelpPresenterImpl(eventDispatcher, contentHelpInteractor);
    }

    @Provides
    @Singleton
    ContentHelpInteractor provideContentHelpInteractor(EventDispatcher eventDispatcher, HelpServiceClient helpServiceClient, PartyInformationRepository partyInfo, MainThreadScheduler mainThreadScheduler, HelpRepository helpRepository){
        return new ContentHelpInteractorImpl(eventDispatcher, helpServiceClient, partyInfo, mainThreadScheduler, helpRepository);
    }

    @Provides
    Context context() {
        return vitalityActiveApplication;
    }

    @Provides
    @Singleton
    GetPotentialPointsAndEventsCompletedPointsServiceClient provideGetPotentialPointsAndEventsCompletedPointsServiceClient(
            WebServiceClient webServiceClient,
            GetPotentialPointsAndEventsCompletedPointsService getPotentialPointsAndEventsCompletedPointsService,
            ScreeningsAndVaccinationsRepositoy screeningsAndVaccinationsRepositoy,
            EventDispatcher eventDispatcher,
            PartyInformationRepository partyInformationRepository,
            AccessTokenAuthorizationProvider accessTokenAuthorizationProvider,
            VitalityMembershipRepository vitalityMembershipRepository) {
        return new GetPotentialPointsAndEventsCompletedPointsServiceClient(
                webServiceClient, getPotentialPointsAndEventsCompletedPointsService, screeningsAndVaccinationsRepositoy, eventDispatcher,
                accessTokenAuthorizationProvider, partyInformationRepository, vitalityMembershipRepository);
    }

    @Provides
    @Singleton
    ScreeningsAndVaccinationsOnboardingPresenter provideScreeningsAndVaccinationsOnboardingPresenter(EventDispatcher eventDispatcher,
                                                                                                     ScreeningsAndVaccinationsInteractor interactor, ConfirmAndSubmitInteractor confirmAndSubmitInteractor) {
        return new ScreeningsAndVaccinationsOnboardingPresenterImpl(eventDispatcher, interactor, confirmAndSubmitInteractor);
    }


    @Provides
    @Singleton
    SnvLearnMorePresenter provideScreeningsAndVaccinationsLearnMorePresenter(ScreeningsAndVaccinationsInteractor interactor) {
        return new SnvLearnMorePresenterImpl(interactor);
    }

    @Provides
    @Singleton
    HealthActionsPresenter provideHealthActionsPresenter(ScreeningsAndVaccinationsInteractor interactor) {
        return new HealthActionsPresenterImpl(interactor);

    }


    @Provides
    @Singleton
    GetPartnersByCategoryService provideGetPartnersByCategoryService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(GetPartnersByCategoryService.class);
    }

    @Provides
    @Singleton
    GetPartnersByCategoryServiceClient provideGetPartnersByCategoryServiceClient(
            WebServiceClient webServiceClient,
            GetPartnersByCategoryService getPartnersByCategoryService,
            ScreeningsAndVaccinationsRepositoy screeningsAndVaccinationsRepositoy,
            EventDispatcher eventDispatcher,
            PartyInformationRepository partyInformationRepository,
            AccessTokenAuthorizationProvider accessTokenAuthorizationProvider,
            VitalityMembershipRepository vitalityMembershipRepository) {
        return new GetPartnersByCategoryServiceClient(
                webServiceClient, getPartnersByCategoryService, screeningsAndVaccinationsRepositoy, eventDispatcher,
                accessTokenAuthorizationProvider, partyInformationRepository, vitalityMembershipRepository);
    }

    @Provides
    @Singleton
    SnvParticipatingPartnersInteractor provideSnvParticipatingPartnersInteractor(GetPartnersByCategoryServiceClient serviceClient,
                                                                                 ScreeningsAndVaccinationsRepositoy screeningsAndVaccinationsRepositoy) {
        return new SnvParticipatingPartnersInteractorImpl(serviceClient);
    }

    @Provides
    @Singleton
    SnvParticipatingPartnersPresenter provideSnvParticipatingPartnersPresenter(EventDispatcher eventDispatcher, SnvParticipatingPartnersInteractor interactor, CMSImageLoader cmsImageLoader) {
        return new SnvParticipatingPartnersPresenterImpl(eventDispatcher, interactor, cmsImageLoader);
    }

    @Provides
    @Singleton
    ConfirmAndSubmitPresenter provideConfirmAndSubmitPresenter(ScreeningsAndVaccinationsInteractor interactor, ConfirmAndSubmitInteractor confirmAndSubmitInteractor) {
        return new ConfirmAndSubmitPresenterImpl(interactor, confirmAndSubmitInteractor);
    }

    @Provides
    @Singleton
    SNVSummaryPresenter provideSNVSummaryPresenter(ScreeningsAndVaccinationsInteractor interactor, ConfirmAndSubmitInteractor confirmAndSubmitInteractor, InsurerConfigurationRepository insurerConfigurationRepository) {
        return new SNVSummaryPresenterImpl(interactor, confirmAndSubmitInteractor, insurerConfigurationRepository);
    }

    @Provides
    @Singleton
    SNVAddProofPresenter provideSNVAddProofPresenter(ConfirmAndSubmitInteractor interactor) {
        return new SNVAddProofPresenterImpl(interactor);
    }

    @Provides
    @Singleton
    ConfirmAndSubmitInteractor provideConfirmAndSubmitInteractor(SNVItemRepository repository) {
        return new ConfirmAndSubmitInteractorImpl(repository);
    }

    @Provides
    @Singleton
    SNVItemRepository provideSNVProofItemRepository(DataStore dataStore) {
        return new SNVItemRepositoryImpl(dataStore);
    }

    @Provides
    @Named(DependencyNames.SNV_PRIVACY_POLICY)
    SNVPrivacyPolicyPresenter provideSNVPrivacyPolicyPresenter(MainThreadScheduler scheduler, @Named(DependencyNames.SNV_PRIVACY_POLICY) TermsAndConditionsInteractor interactor, @Named(DependencyNames.SNV_PRIVACY_POLICY) TermsAndConditionsConsenter consenter, EventDispatcher eventDispatcher, SNVSubmitter snvSubmitter) {
        return new SNVPrivacyPolicyPresenterImpl(scheduler, interactor, consenter, eventDispatcher, snvSubmitter);
    }

    @Provides
    @Named(DependencyNames.SNV_PRIVACY_POLICY)
    static TermsAndConditionsInteractor provideSNVPrivacyPolicyInteractor(CMSServiceClient serviceClient, EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new TermsAndConditionsInteractorImpl(serviceClient, eventDispatcher, appConfigRepository.getSNVPrivacyPolicyContentId(), appConfigRepository.getLiferayGroupId());
    }

    @Provides
    @Named(DependencyNames.SNV_PRIVACY_POLICY)
    static TermsAndConditionsConsenter provideSNVPrivacyPolicyConsenter(EventDispatcher eventDispatcher, EventServiceClient eventServiceClient) {
        return new TermsAndConditionsEventConsenter(eventDispatcher, eventServiceClient, new long[]{EventType._SVDATAPRIVACYAGREE}, new long[]{});
    }


    @Provides
    SNVSubmitter provideSNVSubmitter(EventServiceClient eventServiceClient, EventDispatcher eventDispatcher, CMSServiceClient cmsServiceClient, SNVItemRepository repository) {
        return new SNVSubmitterImpl(eventServiceClient, eventDispatcher, cmsServiceClient, repository);
    }

    @Provides
    @Singleton
    ScreenAndVaccinationHistoryRepository provideScreenAndVaccinationHistoryRepository(DataStore dataStore) {
        return new ScreenAndVaccinationHistoryRepositoryImpl(dataStore);
    }

    @Provides
    @Singleton
    ScreeningAndVaccinationHistoryService providScreeningAndVaccinationHistoryService(ServiceGenerator serviceGenerator) {
        return serviceGenerator.create(ScreeningAndVaccinationHistoryService.class);
    }

    @Provides
    @Singleton
    ScreenAndVaccinationHistoryServiceClient provideScreeningAndVaccinationHistoryServiceClient(AccessTokenAuthorizationProvider accessToken,
                                                                                                EventDispatcher eventDispatcher,
                                                                                                WebServiceClient webServiceClient,
                                                                                                ScreenAndVaccinationHistoryRepository repository,
                                                                                                ScreeningAndVaccinationHistoryService service,
                                                                                                PartyInformationRepository partyInformationRepository,
                                                                                                VitalityMembershipRepository vitalityMembershipRepository) {
        return new ScreenAndVaccinationHistoryServiceClient(accessToken, eventDispatcher,
                webServiceClient, repository, service, partyInformationRepository, vitalityMembershipRepository);
    }

    @Provides
    @Singleton
    ScreeningAndVaccinationsHistoryInteractor provScreeningsAndVaccinationsHistoryInteractor(
            ScreenAndVaccinationHistoryServiceClient serviceClient,
            ScreenAndVaccinationHistoryRepository repository) {

        return new ScreeningAndVaccinationsHistoryInteractorImpl(serviceClient, repository);
    }

    @Provides
    @Singleton
    ScreenAndVaccinationHistoryPresenter provideScreenAndVaccinationHistoryPresenter(
            EventDispatcher eventDispatcher, ScreeningAndVaccinationsHistoryInteractor interactor,
            MainThreadScheduler scheduler) {
        return new ScreenAndVaccinationHistoryPresenterImpl(eventDispatcher, interactor, scheduler);
    }

    @Provides
    ScreenAndVaccinationHistoryDetailPresenter provideScreenAndVaccinationHistoryDetailPresenter(
            EventDispatcher eventDispatcher, SNVHistoryDetailProofProvider snvHistoryDetailProofProvider,
            DataStore dataStore, MainThreadScheduler scheduler) {
        return new ScreenAndVaccinationHistoryDetailPresenterImpl(eventDispatcher,
                snvHistoryDetailProofProvider, dataStore, scheduler);
    }

    @Provides
    @Singleton
    SNVHistoryDetailProofProvider snvHistoryDetailProofProvider(CMSContentDownloader cmsContentDownloader,
                                                                EventDispatcher eventDispatcher) {
        return new SNVHistoryDetailProofProvider(vitalityActiveApplication, cmsContentDownloader, eventDispatcher);
    }

    @Provides
    @Singleton
    FeedbackPresenter provideFeedbackPresenter(FeedbackInteractor interactor,
                                               EventDispatcher eventDispatcher, MainThreadScheduler scheduler) {
        return new FeedbackPresenterImpl(interactor, eventDispatcher, scheduler);
    }

    @Provides
    @Singleton
    FeedbackInteractor provideFeedbackInteractorImpl(@NonNull CMSServiceClient cmsServiceClient, @NonNull EventDispatcher eventDispatcher, AppConfigRepository appConfigRepository) {
        return new FeedbackInteractorImpl(cmsServiceClient, eventDispatcher, "profile-settings-feedback", appConfigRepository.getLiferayGroupId());
    }

    ShareVitalityStatusPreferenceServiceClient provideShareVitalityStatusServiceClient(WebServiceClient webServiceClient,
                                                                                       UserPreferencesService service,
                                                                                       PartyInformationRepository repository,
                                                                                       EventDispatcher eventDispatcher,
                                                                                       AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        return new ShareVitalityStatusPreferenceServiceClient(webServiceClient, service, repository, eventDispatcher, accessTokenAuthorizationProvider);
    }

    @Provides
    MWBContent provideMWBContent() {
        return new MWBContentFromStringResource(vitalityActiveApplication);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.MWB)
    MWBVitalityAgePresenter provideMWBLandingPresenter(EventDispatcher eventDispatcher, HealthAttributeInformationServiceClient attributeInformationServiceClient, VitalityAgeInteractor vitalityAgeInteractor, DeviceSpecificPreferences deviceSpecificPreferences) {
        return new MWBVitalityAgePresenterImpl( eventDispatcher, attributeInformationServiceClient,vitalityAgeInteractor, deviceSpecificPreferences);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.MWB)
    QuestionnaireLandingPresenter provideMWBQuestionnaireLandingPresenter(MWBContent content,
                                                                       QuestionnaireSetServiceClient serviceClient,
                                                                       EventDispatcher eventDispatcher,
                                                                       MainThreadScheduler scheduler,
                                                                       @Named(DependencyNames.MWB) QuestionnaireSetRepository questionnaireSetRepository) {
        return new MWBLandingPresenterImpl(content, serviceClient, eventDispatcher, scheduler, questionnaireSetRepository);
    }

    @Provides
    @Singleton
    @Named(DependencyNames.MWB)
    QuestionnaireSetRepository provideMWBQuestionnaireSetRepository(DataStore dataStore,
                                                                    QuestionFactory questionFactory,
                                                                    ValidationRuleMapper validationRuleFactory) {
        return new QuestionnaireSetRepositoryImpl(dataStore, questionFactory, validationRuleFactory, 30);
    }

}
