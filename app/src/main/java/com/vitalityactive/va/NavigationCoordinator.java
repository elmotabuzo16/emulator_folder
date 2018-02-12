package com.vitalityactive.va;

import android.app.Activity;
import android.util.Log;

import com.vitalityactive.va.activerewards.dto.ActivityItem;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.constants.RewardId;
import com.vitalityactive.va.dependencyinjection.VHCCaptureDependencyInjector;
import com.vitalityactive.va.dependencyinjection.VHCCaptureModule;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsDependencyInjector;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsModule;
import com.vitalityactive.va.dependencyinjection.mwb.MWBCaptureDependencyInjector;
import com.vitalityactive.va.dependencyinjection.mwb.MWBCaptureModule;
import com.vitalityactive.va.dependencyinjection.myhealth.MyHealthDependencyInjector;
import com.vitalityactive.va.dependencyinjection.myhealth.MyHealthModule;
import com.vitalityactive.va.dependencyinjection.partnerjourney.PartnerJourneyDependencyInjector;
import com.vitalityactive.va.dependencyinjection.partnerjourney.PartnerJourneyModule;
import com.vitalityactive.va.dependencyinjection.pns.PNSCaptureDependencyInjector;
import com.vitalityactive.va.dependencyinjection.pns.PNSCaptureModule;
import com.vitalityactive.va.dependencyinjection.vhr.VHRCaptureDependencyInjector;
import com.vitalityactive.va.dependencyinjection.vhr.VHRCaptureModule;
import com.vitalityactive.va.dependencyinjection.vna.VNACaptureDependencyInjector;
import com.vitalityactive.va.dependencyinjection.vna.VNACaptureModule;
import com.vitalityactive.va.dependencyinjection.wda.WDALinkingDependencyInjector;
import com.vitalityactive.va.dependencyinjection.wda.WDALinkingModule;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.home.HomeActivity;
import com.vitalityactive.va.launch.Navigator;
import com.vitalityactive.va.login.LoginPresenterImpl;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.partnerjourney.PartnerType;
import com.vitalityactive.va.pushnotification.InAppPreferences;
import com.vitalityactive.va.pushnotification.PushNotificationData;
import com.vitalityactive.va.pushnotification.ShowScreenUtility;
import com.vitalityactive.va.register.interactor.RegistrationInteractor;
import com.vitalityactive.va.snv.partners.dto.ProductFeatureGroupDto;
import com.vitalityactive.va.termsandconditions.GeneralTermsAndConditionsInstructionRepository;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.landing.repository.DeviceListRepository;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingInteractor;

public class NavigationCoordinator
        implements EventListener<WebServiceClient.RequestUnauthorizedEvent> {

    private final RegistrationInteractor registrationInteractor;
    private final LoginPresenterImpl loginPresenter;
    private Navigator navigator;
    private GeneralTermsAndConditionsInstructionRepository generalTermsAndConditionsInstructionRepository;
    private InsurerConfigurationRepository insurerConfigurationRepository;
    private DeviceSpecificPreferences deviceSpecificPreferences;
    private VHCCaptureDependencyInjector vhcCaptureDependencyInjector;
    private VHRCaptureDependencyInjector vhrCaptureDependencyInjector;
    private VNACaptureDependencyInjector vnaCaptureDependencyInjector;
    private WDALinkingDependencyInjector wdaLinkingDependencyInjector;
    private PartnerJourneyDependencyInjector partnerJourneyDependencyInjector;
    private PNSCaptureDependencyInjector pnsDependencyInjector;
    private VitalityActiveApplication vitalityActiveApplication;
    private MyHealthDependencyInjector myHealthDependencyInjector;
    private AppConfigRepository appConfigRepository;
    private DeviceListRepository deviceListRepository;
    private ActiveRewardsDependencyInjector activeRewardsDependencyInjector;
    private InAppPreferences inAppPreferences;
    private MWBCaptureDependencyInjector mwbCaptureDependencyInjector;

    public NavigationCoordinator(Navigator navigator,
                                 GeneralTermsAndConditionsInstructionRepository generalTermsAndConditionsInstructionRepository,
                                 RegistrationInteractor registrationInteractor,
                                 LoginPresenterImpl loginPresenter,
                                 InsurerConfigurationRepository insurerConfigurationRepository,
                                 DeviceSpecificPreferences deviceSpecificPreferences,
                                 EventDispatcher eventDispatcher,
                                 VitalityActiveApplication vitalityActiveApplication,
                                 AppConfigRepository appConfigRepository, DeviceListRepository deviceListRepository,
                                 InAppPreferences inAppPreferences) {
        this.navigator = navigator;
        this.generalTermsAndConditionsInstructionRepository = generalTermsAndConditionsInstructionRepository;
        this.registrationInteractor = registrationInteractor;
        this.loginPresenter = loginPresenter;
        this.insurerConfigurationRepository = insurerConfigurationRepository;
        this.deviceSpecificPreferences = deviceSpecificPreferences;
        this.vitalityActiveApplication = vitalityActiveApplication;
        this.appConfigRepository = appConfigRepository;
        this.deviceListRepository = deviceListRepository;
        this.inAppPreferences = inAppPreferences;
        eventDispatcher.addEventListener(WebServiceClient.RequestUnauthorizedEvent.class, this);
    }

    public PNSCaptureDependencyInjector getPNSCaptureDependencyInjector() {
        if (pnsDependencyInjector == null) {
            pnsDependencyInjector = vitalityActiveApplication.getDependencyInjector().createDependencyInjector(new PNSCaptureModule());
        }
        return pnsDependencyInjector;
    }

    public VHCCaptureDependencyInjector getVHCCaptureDependencyInjector() {
        if (vhcCaptureDependencyInjector == null) {
            vhcCaptureDependencyInjector = vitalityActiveApplication.getDependencyInjector().createDependencyInjector(new VHCCaptureModule());
        }
        return vhcCaptureDependencyInjector;
    }

    public VHRCaptureDependencyInjector getVHRCaptureDependencyInjector() {
        if (vhrCaptureDependencyInjector == null) {
            vhrCaptureDependencyInjector = vitalityActiveApplication.getDependencyInjector().createDependencyInjector(new VHRCaptureModule());
        }
        return vhrCaptureDependencyInjector;
    }

    public VNACaptureDependencyInjector getVNACaptureDependencyInjector() {
        if (vnaCaptureDependencyInjector == null) {
            vnaCaptureDependencyInjector = vitalityActiveApplication.getDependencyInjector().createDependencyInjector(new VNACaptureModule());
        }
        return vnaCaptureDependencyInjector;
    }

    public WDALinkingDependencyInjector getWDALinkingDependencyInjector() {
        if (wdaLinkingDependencyInjector == null) {
            wdaLinkingDependencyInjector = vitalityActiveApplication.getDependencyInjector().createDependencyInjector(new WDALinkingModule());
        }
        return wdaLinkingDependencyInjector;
    }

    public MyHealthDependencyInjector getMyHealthDependencyInjector() {
        if (myHealthDependencyInjector == null) {
            myHealthDependencyInjector = vitalityActiveApplication.getDependencyInjector().createDependencyInjector(new MyHealthModule());
        }

        return myHealthDependencyInjector;
    }

    public PartnerJourneyDependencyInjector getPartnerJourneyDependencyInjector() {
        if (partnerJourneyDependencyInjector == null) {
            partnerJourneyDependencyInjector = vitalityActiveApplication.getDependencyInjector()
                    .createDependencyInjector(new PartnerJourneyModule());
        }
        return partnerJourneyDependencyInjector;
    }

    public void navigateOnLaunch(Activity activity) {
        if (generalTermsAndConditionsInstructionRepository.hasInstruction()) {
            logOut();
        } else if (deviceSpecificPreferences.isRememberMeOn() && insurerConfigurationRepository.insurerConfigurationExists()) {
            navigator.showSplash(activity);
        } else {
            navigator.showLogin(activity);
        }
    }

    public void navigateFromLoginAfterRegisterTapped(Activity activity, String username) {
        navigator.showRegistration(activity, username);
    }

    public void navigateFromLoginAfterForgotPasswordTapped(Activity activity) {
        navigator.showForgotPassword(activity);
    }

    public void navigateAfterSuccessfulRegistration(Activity activity) {
        loginPresenter.logInAfterRegistration(registrationInteractor.getUsername().getText(),
                registrationInteractor.getPassword().getText());
        navigator.showLoginAfterSuccessfulRegistration(activity);
    }

    public void navigateAfterTermsAndConditionsAccepted(Activity activity) {
        if (deviceSpecificPreferences.currentUserHasSeenFirstTimePreferences()) {
            navigator.showHome(activity);
        } else {
            showFirstTimeUserPreferences(activity);
        }
    }

    private void showFirstTimeUserPreferences(Activity activity) {
        navigator.showFirstTimeUserPreferences(activity);
        deviceSpecificPreferences.setCurrentUserHasSeenFirstTimePreferences();
    }

    public void navigateFromSplashScreen(Activity activity) {
        if (generalTermsAndConditionsInstructionRepository.hasInstruction()) {
            navigator.showTermsAndConditions(activity);
        } else if (deviceSpecificPreferences.currentUserHasSeenFirstTimePreferences()) {
            navigateToScreen(activity);
        } else {
            showFirstTimeUserPreferences(activity);
        }
    }

    public void navigateAfterSuccessfulLogin(Activity activity) {
        navigator.showSplashAndSkipAppConfig(activity);
    }

    @SuppressWarnings("unused")
    public void navigateAfterTermsAndConditionsDeclined(Activity activity) {
        logOut();
    }

    public void navigateAfterActiveRewardsSelected(Activity activity) {
        if (insurerConfigurationRepository.hasRewardChoice() || insurerConfigurationRepository.hasDefinedRewards()) {
            navigator.showActiveRewardsOnboardingForRewardChoice(activity);
        } else if (insurerConfigurationRepository.hasProbabilisticRewards()) {
            navigator.showActiveRewardsOnboardingForProbabilisticRewards(activity);
        } else {
            navigator.showActiveRewardsOnboardingWithoutRewards(activity);
        }
    }

    public void navigateAfterActiveRewardsSelectedWithoutVhr(Activity activity) {
        navigator.showVhrOnboardingFromActiveRewardsFlow(activity);
    }

    public void navigateAfterNonSmokersDeclarationSelected(Activity activity) {
        navigator.showNonSmokersDeclaration(activity);
    }

    public void navigateFromActiveRewardsOnboarding(Activity activity) {
        if (insurerConfigurationRepository.shouldShowARMedicallyFitAgreement()) {
            navigator.showActiveRewardsMedicallyFitAgreement(activity);
        } else {
            navigateAfterActiveRewardsTermsAndConditionsAccepted(activity);
        }
    }

    public void navigateAfterActiveRewardsTermsAndConditionsAccepted(Activity activity) {
        navigator.showActiveRewardsActivationAcknowledgement(activity);
    }

    public void navigateAfterActiveRewardsActivationAcknowledged(Activity activity) {
        if (deviceListRepository.hasLinkedDevice()) {
            navigateAfterDoneLinkingDevicesFromOnboarding(activity);
        } else {
            navigator.showActiveRewardsLinkFitnessDevice(activity);
        }
    }

    public void navigateOnGetStartedFromNonSmokersDeclarationOnboarding(Activity activity) {
        navigator.showNonSmokersDeclarationGetStarted(activity);
    }

    public void navigateAfterDoneLinkingDevicesFromOnboarding(Activity activity) {
        navigator.showActiveRewardsLanding(activity, false);
    }

    public void navigateToWellnessDevicesWebLinkFlow(Activity activity,
                                                     String partnerLink,
                                                     @WellnessDevicesLinkingInteractor.RequestType String requestType,
                                                     int requestCode) {
        navigator.showWellnessDevicesWebLinkActivity(activity, partnerLink, requestType, requestCode);
    }

    public void navigateToWellnessDevicesWebLinkFlowFromPrivacyPolicy(Activity activity,
                                                                      String partnerLink,
                                                                      @WellnessDevicesLinkingInteractor.RequestType String requestType) {
        navigator.showWellnessDevicesWebLinkActivityWithRedirectedResult(activity, partnerLink, requestType);
    }

    public void navigateToWellnessDevicesInfoActivity(Activity activity,
                                                      String titleId,
                                                      String articleId) {
        navigator.showWellnessDevicesInfoActivity(activity, titleId, articleId);
    }

    public void navigateOnMenuItemFromActiveRewardsLanding(Activity activity,
                                                           MenuItemType menuItemType) {
        if (menuItemType.equals(MenuItemType.Activity)) {
            navigator.showActiveRewardsMonthlyActivity(activity);
        } else if (menuItemType.equals(MenuItemType.Rewards)) {
            navigator.showActiveRewardsRewardsList(activity);
        } else if (menuItemType.equals(MenuItemType.LearnMore)) {
            navigator.showActiveRewardsLearnMore(activity);
        } else if (menuItemType.equals(MenuItemType.Help)) {
            navigator.showActiveRewardsHelp(activity);
        }
    }

    public void navigateOnMenuItemFromVHCHealthMeasurements(Activity activity,
                                                            MenuItemType menuItemType) {
        if (menuItemType.equals(MenuItemType.History)) {
            navigator.showVHCHistoryActivity(activity);
        } else if (menuItemType.equals(MenuItemType.LearnMore)) {
            navigator.showVHCLearnMore(activity);
        } else if (menuItemType.equals(MenuItemType.Help)) {
            navigator.showVHCHelp(activity);
        } else if (menuItemType.equals(MenuItemType.HealthCarePDF)) {
            navigator.showPDF(activity, appConfigRepository.getHealthCareBenefitFileName(), R.string.landing_screen_healthcare_pdf_label_248);
        }
    }

    public void navigateOnThisWeeksActivityItemFromActiveRewardsLanding(Activity activity,
                                                                        ActivityItem activityItem) {
        navigator.showActiveRewardsActivityEventDetail(activity, activityItem);
    }

    // NOTE: This is used in the UKE target. Do not remove this method!
    public void navigateOnParticipatingPartnersFromActiveRewardsLearnMore(Activity activity) {
        navigator.showActiveRewardsParticipatingPartners(activity);
    }

    public void navigateOnParticipatingPartnersFromWheelSpin(Activity activity) {
        navigator.showActiveRewardsParticipatingPartners(activity);
    }

    public void navigateOnHelpFromMonthlyActivity(Activity activity) {
        navigator.showActiveRewardsHelp(activity);
    }

    public void navigateToHistoryWeekDetailedActivity(Activity activity, GoalTrackerOutDto data) {
        navigator.showHistoryWeekDetailedActivity(activity, data);
    }

    public void navigateToCachedActiveRewardsParticipatingPartner(Activity activity, int rewardId) {
        navigator.showCachedActiveRewardsParticipatingPartnerDetail(activity, rewardId);
    }

    public void navigateAfterActiveRewardsParticipatingPartner(Activity activity, long partnerId) {
        navigator.showActiveRewardsParticipatingPartnerDetail(activity, partnerId);
    }

    public void navigateFromRewardsListAfterWheelSpinSelected(Activity activity, long uniqueId, int rewardId) {
        navigator.showWheelSpin(activity, uniqueId, rewardId);
    }

    public void navigateFromRewardsListAfterChooseRewardSelected(Activity activity, long uniqueId, int rewardId) {
        navigator.showChooseReward(activity, uniqueId, rewardId);
    }

    public void navigateAfterWheelSpin(Activity activity, UnclaimedRewardDTO wheelSpin) {
        if (wheelSpin != null && wheelSpin.outcomeRewardId > 0) {
            switch (wheelSpin.outcomeRewardId) {
                case RewardId._STARBUCKSVOUCHER:
                    navigator.showStarbucksPartnerRegistration(activity, wheelSpin.uniqueId);
                    break;
                case RewardId._CINEWORLDORVUE:
                case RewardId._CINEWORLD:
                case RewardId._VUE:
                case RewardId._NOWIN:
                    navigator.showCinemaRewardConfirmation(activity, wheelSpin.uniqueId);
                    break;
                case RewardId._AMAZONVOUCHER:
                case RewardId._VITALITYPOINTS:
                case RewardId._TOKEN:
                case RewardId._ZAPPOSVOUCHER:
                    navigator.showStandardRewardConfirmation(activity, wheelSpin.uniqueId);
                    break;
            }
        } else {
            Log.e("Error", "Reward details is empty");
        }
    }

    public void navigateVitalityActiveRewardsFromHome(Activity activity) {
        navigator.showActiveRewardsLanding(activity, true);
    }

    public void navigateToAvailableDeviceDetails(Activity activity, PartnerDto partner) {
        wdaLinkingDependencyInjector = null;
        navigator.showAvailableDeviceDetails(activity, partner);
    }

    public void navigateAfterDeclaringNonSmokerStatus(Activity activity) {
        if (insurerConfigurationRepository.shouldShowNonSmokersPrivacyPolicy()) {
            navigator.showNonSmokersPrivacyPolicy(activity);
        } else {
            navigator.showNonSmokersAssessmentCompleted(activity);
        }
    }

    public void navigateAfterNonSmokersPrivacyPolicyAccepted(Activity activity) {
        navigator.showNonSmokersAssessmentCompleted(activity);
    }

    public void navigateAfterNonSmokersAssessmentComplete(Activity activity) {
        activity.finish();
    }

    public void navigateOnLearnMoreFromNonSmokersDeclarationOnboarding(Activity activity) {
        navigator.showNonSmokersDeclarationLearnMore(activity);
    }

    public void navigateToWellnessDevicesPointsMonitor(Activity activity,
                                                       int eventType) {
        navigator.showWellnessDevicesPointsActivity(activity, eventType);
    }

    public void navigateToWellnessDevicesPrivacyPolicy(Activity activity,
                                                       PartnerDto partner,
                                                       int requestCode) {
        wdaLinkingDependencyInjector = null;
        navigator.showWellnessDevicesPrivacyPolicyActivity(activity, partner, requestCode);
    }

    public void navigateToWellnessDevicesBrowser(Activity activity,
                                                 String url) {
        navigator.showWellnessDevicesBrowser(activity, url == null ? "" : url);
    }

    public void navigateAfterVHCSelected(Activity activity) {
        if (deviceSpecificPreferences.hasCurrentUserSeenVHCOnboarding()) {
            navigator.showVHCHealthMeasurements(activity, false);
        } else {
            navigator.showVHCOnboarding(activity);
            deviceSpecificPreferences.setCurrentUserHasSeenVHCOnboarding();
        }
    }

    public void navigateAfterVHCGetStartedTapped(Activity activity) {
        navigator.showVHCHealthMeasurements(activity, false);
    }

    public void navigateAfterVHCCaptureResultsTapped(Activity activity) {
        vhcCaptureDependencyInjector = null;
        navigator.showVHCCaptureResults(activity);
    }

    public void navigateAfterVHCCaptureResultsCaptured(Activity activity) {
        navigator.showVHCAddProof(activity);
    }

    public void navigateAfterVHCProofAdded(Activity activity) {
        navigator.showVHCSummaryActivity(activity);
    }

    public void navigateAfterVHCSummaryConfirmed(Activity activity) {
        if (insurerConfigurationRepository.shouldShowVHCPrivacyPolicy()) {
            navigator.showVHCPrivacyPolicy(activity);
        } else {
            navigator.showVHCMeasurementsConfirmed(activity);
        }
    }

    public void navigateAfterVHCPrivacyPolicyAccepted(Activity activity) {
        navigator.showVHCMeasurementsConfirmed(activity);
    }

    public void navigateAfterVHCSubmitSuccessful(Activity activity, boolean isAfterFormSubmition) {
        navigator.showVHCHealthMeasurements(activity, isAfterFormSubmition);
    }

    public void navigateAfterVHCLearnMoreTapped(Activity activity) {
        navigator.showVHCLearnMore(activity);
    }


    public void navigateAfterSNVSelected(Activity activity) {
        if (deviceSpecificPreferences.hasCurrentUserSeenSNVOnboarding()) {
            navigator.showScreeningsAndVaccinationsOnboarding(activity);
        } else {
            navigator.showSNVOnboarding(activity);
            deviceSpecificPreferences.setCurrentUserHasSeenSNVOnboarding();
        }
    }

    public void navigateAfterMWBSelected(Activity activity) {
        if (deviceSpecificPreferences.hasCurrentUserSeenMWBOnboarding()) {
            navigator.showMentalWellbeingOnboarding(activity);
        } else {
            navigator.showMWBOnboarding(activity);
            deviceSpecificPreferences.setCurrentUserHasSeenMWBOnboarding();
        }
    }

    public void navigateAfterSNVLearnMoreTapped(Activity activity) {
        navigator.showScreeningLearnMore(activity);
    }

    public void navigateAfterActiveRewardsTermsAndConditionsDeclined(Activity activity) {
        activity.finish();
    }


    public void navigateAfterSNVProofAdded(Activity activity) {
        navigator.showSNVSummaryActivity(activity);
    }

    public void navigateAfterSNVSummaryConfirmed(Activity activity) {
        if (insurerConfigurationRepository.shouldShowSNVPrivacyPolicy()) {
            navigator.showSNVPrivacyPolicy(activity);
        } else {
            navigator.showSNVDataConfirmed(activity);
        }
    }

    public void navigateAfterSNVPrivacyPolicyAccepted(Activity activity) {
        navigator.showSNVDataConfirmed(activity);
    }

    public void navigateAfterSNVSubmitSuccessful(Activity activity, boolean isAfterFormSubmition) {
        navigator.showScreeningsAndVaccinationsOnboarding(activity);
    }

    public void navigateFromFirstTimePreferences(Activity activity) {
        navigator.showHome(activity);
    }

    public void navigateFromHomeOnFirstTimePreferences(Activity activity) {
        navigator.showFirstTimeUserPreferences(activity);
    }

    public void navigateAfterVHCLearnMoreItemTapped(Activity activity, String tag) {
        navigator.showVHCLearnMoreItemDetail(activity, tag);
    }

    public void navigateAfterForgotPasswordCompleted(Activity activity, String username) {
        navigator.showLoginAfterForgotPassword(activity, username);
    }

    @Override
    public void onEvent(WebServiceClient.RequestUnauthorizedEvent event) {
        navigator.showLoginAfterLogOut(vitalityActiveApplication, true);
    }

    public void logOut() {
        inAppPreferences.clearAll();
        navigator.showLoginAfterLogOut(vitalityActiveApplication, false);
    }

    public void navigateAfterHealthAttributeGroupTapped(Activity activity, int healthAttributeGroupFeatureType) {
        navigator.showHealthAttributeGroupDetail(activity, healthAttributeGroupFeatureType);
    }

    public void navigateAfterStartVhrButtonClicked(Activity activity) {
        navigateToVhrFlow(activity);
    }

    public void navigateAfterVHRCardTapped(Activity activity) {
        navigateToVhrFlow(activity);
    }

    private void navigateToVhrFlow(Activity activity) {
        if (deviceSpecificPreferences.hasCurrentUserSeenVHROnboarding()) {
             navigator.showVHRLanding(activity, false);
        }
        else {
            navigator.showVHROnboarding(activity);
            deviceSpecificPreferences.setCurrentUserHasSeenVHROnboarding();
        }
    }

    public void navigateAfterVHRQuestionnaireStartTapped(Activity activity, long questionnaireTypeKey) {
        navigateAfterQuestionnaireStartTapped(activity, QuestionnaireSet._VHR, questionnaireTypeKey);
    }

    public void navigateAfterVNAQuestionnaireStartTapped(Activity activity, long questionnaireTypeKey) {
        navigateAfterQuestionnaireStartTapped(activity, QuestionnaireSet._VNA, questionnaireTypeKey);
    }

    public void navigateAfterQuestionnaireStartTapped(Activity activity, int questionnaireSetType, long questionnaireTypeKey) {
        resetQuestionnaireSetDependencyInjector(questionnaireSetType);
        navigateAfterQuestionnaireGoToSectionTapped(activity, questionnaireSetType, questionnaireTypeKey);
    }

    private void resetQuestionnaireSetDependencyInjector(int questionnaireSetType) {
        switch (questionnaireSetType) {
            case QuestionnaireSet._VNA:
                vnaCaptureDependencyInjector = null;
                break;
            case QuestionnaireSet._VHR:
                vhrCaptureDependencyInjector = null;
                break;
        }
    }

    public void navigateAfterQuestionnaireGoToSectionTapped(Activity activity, int questionnaireSetType, long questionnaireTypeKey) {
        switch (questionnaireSetType) {
            case QuestionnaireSet._VHR:
                navigator.showVHRQuestionnaire(activity, questionnaireTypeKey);
                break;
            case QuestionnaireSet._VNA:
                navigator.showVNAQuestionnaire(activity, questionnaireTypeKey);
                break;
            default:
                navigator.showHome(activity);
        }
    }

    public void navigateOnMenuItemFromVHRLanding(Activity activity, MenuItemType menuItemType) {
        if (menuItemType.equals(MenuItemType.LearnMore)) {
            navigator.showVHRLearnMore(activity);
        } else if (menuItemType.equals(MenuItemType.Help)) {
            navigator.showVHRHelp(activity);
        }
    }

    public void navigateAfterVHRGotItTapped(Activity activity) {
        navigator.showVHRLandingAfterOnboarding(activity);
    }

    public void navigateAfterVHRQuestionnaireCompleted(Activity activity, long questionnaireTypeKey) {
        if (insurerConfigurationRepository.shouldShowVHRPrivacyPolicy()) {
            navigator.showVHRPrivacyPolicy(activity, questionnaireTypeKey);
        } else {
            navigator.showVHRQuestionnaireCompletedActivity(activity, questionnaireTypeKey);
        }
    }

    public void navigateAfterVHRPrivacyPolicyAccepted(Activity activity, long questionnaireTypeKey) {
        navigator.showVHRQuestionnaireCompletedActivity(activity, questionnaireTypeKey);
    }

    public void back(Activity activity) {
        navigator.back(activity);
    }

    public void navigateAfterVHRErrorCancelled(Activity activity) {
        navigator.showHome(activity);
    }

    public void navigateAfterWellnessDevicesTapped(Activity activity) {
        if (!deviceSpecificPreferences.hasCurrentUserSeenWellnessDevicesOnboarding()) {
            navigator.showWellnessDeviceOnboarding(activity);
            deviceSpecificPreferences.setCurrentUserHasSeenWellnessDevicesOnboarding();
        } else {
            navigator.showWellnessDeviceLanding(activity, false);
        }
    }

    public void navigateToWellnessDeviceLandingAfterOnboarding(Activity activity, boolean isInsideActiveRewardsFlow) {
        navigator.showWellnessDeviceLandingAfterOnboarding(activity, isInsideActiveRewardsFlow);
    }

    public void navigateToWellnessDevicesLearnMore(Activity activity) {
        navigator.showWellnessDevicesLearnMore(activity);
    }

    public void navigateAfterAssessmentsCompletedDismissed(Activity activity, int questionnaireSetType) {
        if (questionnaireSetType == QuestionnaireSet._VHR) {
            navigator.showVHRLanding(activity, true);
        } else if (questionnaireSetType == QuestionnaireSet._VNA) {
            navigator.showVNALanding(activity);
        }
    }

    public void AfterPrivacyStatementButtonTapped(Activity activity) {
        navigator.showPrivacyStatement(activity);
    }

    public void navigateAfterVNACardTapped(Activity activity) {
        if (!deviceSpecificPreferences.hasCurrentUserSeenVNAOnboarding()) {
            navigator.showVNAOnboarding(activity);
            deviceSpecificPreferences.setCurrentUserHasSeenVNAOnboarding();
        } else {
            navigator.showVNALanding(activity);
        }
    }

    public void navigateAfterVNAOnboardingGotItTapped(Activity activity) {
        navigator.showVNALanding(activity);
    }

    public void navigateAfterVNAErrorCancelled(Activity activity) {
        navigator.showHome(activity);
    }

    public void navigateToVNAMenu(Activity activity, MenuItemType menuItemType) {
        if (menuItemType.equals(MenuItemType.LearnMore)) {
            navigator.showVNALearnMore(activity);
        } else if (menuItemType.equals(MenuItemType.Help)) {
            navigator.showVNAHelp(activity);
        }
    }

    public void navigateAfterVNADisclaimerTapped(Activity activity) {
        navigator.showVNADisclaimer(activity);
    }


    public void navigateToVitalityAge(Activity activity, int vitalityAgeMode) {
        navigator.showVitalityAge(activity, vitalityAgeMode);
    }

    public void navigateAfterVHRDisclaimerTapped(Activity activity) {
        navigator.showVHRDisclaimer(activity);
    }

    public void navigateToMyHealthOnboarding(Activity activity) {
        navigator.showMyHealthOnboardingScreen(activity, HomeActivity.RequestCode.MYHEALTH_ONBOARDING);
    }

    public void navigateToMyHealthVitalityAgeProfile(Activity activity) {
        navigator.showMyHealthVitalityAgeProfile(activity);
    }

    public void navigateAfterPartnerHomeCardTapped(Activity activity, PartnerType partnerType) {
        navigator.showPartnerJourney(activity, partnerType);
    }

    public void navigateAfterPartnerTappedInPartnerList(Activity activity, PartnerType partnerType, long partnerId) {
        navigator.showPartnerDetail(activity, partnerType, partnerId);
    }

    // NOTE: This is used in the UKE target. Do not remove this method!
    public void navigateOnParticipatingPartnersFromNonSmokersLearnMore(Activity activity) {
        navigator.showPartnerJourney(activity, PartnerType.NON_SMOKERS);
    }

    public void navigateAfterVitalityStatusTapped(Activity activity) {
        if (!deviceSpecificPreferences.hasCurrentUserHasSeenVitalityStatusOnboarding()) {
            navigator.showVitalityStatusOnboarding(activity);
            deviceSpecificPreferences.setCurrentUserHasSeenVitalityStatusOnboarding();
        } else {
            navigator.showVitalityStatusLanding(activity);
        }
    }

    public void navigateToVitalityStatusLevelIncreased(Activity activity) {
        navigator.showVitalityStatusLevelIncreasedActivity(activity);
    }

    public void navigateToVitalityStatusLanding(Activity activity) {
        navigator.showVitalityStatusLanding(activity);
    }

    public void navigateOnMenuItemFromVitalityStatus(Activity activity, MenuItemType menuItemType) {
        if (menuItemType.equals(MenuItemType.LearnMore)) {
            navigator.showVitalityStatusLearnMore(activity);
        } else if (menuItemType.equals(MenuItemType.Help)) {
            navigator.showHelpScreen(activity);
        }
    }

    public void navigateToVitalityStatusDetail(Activity activity, String title) {
        navigator.showVitalityStatusDetail(activity, title);
    }

    public void navigateAfterTermsAndConditionsAccepted(Activity activity, long questionnaireTypeKey) {
        navigator.showVNAQuestionnaireCompletedActivity(activity, questionnaireTypeKey);
    }

    public void navigateAfterVNAQuestionnaireCompleted(Activity activity, long questionnaireTypeKey) {
        if (insurerConfigurationRepository.shouldShowVNAPrivacyPolicy()) {
            navigator.showVNAPrivacyPolicy(activity, questionnaireTypeKey);
        } else {
            navigator.showVNAQuestionnaireCompletedActivity(activity, questionnaireTypeKey);
        }
    }

    public void navigateAfterPointsCategoryTapped(Activity activity, int key) {
        navigator.showPointsCategoryFeatures(activity, key);
    }

    public void navigateAfterPointsFeatureTapped(Activity activity, int key) {
        navigator.showPointsFeatureSubfeatures(activity, key);
    }

    public void navigateAfterVitalityAgeCardLearnMoreButtonClicked(Activity activity, int effectiveTypeValue, int actualTypeValue, String vitalityAge, String vitalityAgeVariance) {
        navigator.showVitalityAgeCardLearnMore(activity, effectiveTypeValue, actualTypeValue, vitalityAge, vitalityAgeVariance);
    }

    public void navigateAfterVitalityAgeLearnMoreDisclaimerButtonClicked(Activity activity) {
        navigator.showMyHealthDisclaimer(activity);
    }

    public void navigateToStatusMyRewardsDetail(Activity activity) {
        navigator.showStatusMyRewards(activity);
    }

    public void navigateToActiveRewardsBenefitGuide(Activity activity) {
        navigator.showPDF(activity, appConfigRepository.getActiveRewardsBenefitGuideFileName(), R.string.AR_learn_more_benefit_guide_title_728);
    }

    public void navigateToStatusHowToEarnPoints(Activity activity, int key, String name) {
        navigator.showStatusHowToEarnPoints(activity, key, name);
    }

    public void navigateAfterPersonalDetailsButtonTapped(Activity activity) {
        navigator.showPersonalDetailsActivity(activity);
    }

    public void navigateAfterMembershipPassItemTapped(Activity activity) {
        navigator.showMembershipPassActivity(activity);
    }

    public void navigateToCommunicationPreferences(Activity activity) {
        navigator.showCommunicationPreferences(activity);
    }

    public void navigateToSecurityPreferences(Activity activity) {
        navigator.showSecurityPreferences(activity);
    }

    public void navigateToPrivacyPreferences(Activity activity) {
        navigator.showPrivacyPreferences(activity);
    }

    public ActiveRewardsDependencyInjector getActiveRewardsDependencyInjector() {
        if (activeRewardsDependencyInjector == null) {
            activeRewardsDependencyInjector = vitalityActiveApplication.getDependencyInjector().createDependencyInjector(new ActiveRewardsModule());
        }
        return activeRewardsDependencyInjector;
    }

    public void resetActiveRewardsDependencies() {
        activeRewardsDependencyInjector = null;
    }

    public void navigateAfterARDoneTapped(Activity activity) {
        navigator.showActiveRewardsRewardsListFromActiveRewards(activity);
    }

    public void navigateAfterStarbucksRewardConfirmed(Activity activity,
                                                      long uniqueID) {
        navigator.showActiveRewardsVoucherAfterStarbucksConfirm(activity, uniqueID);
    }

    public void navigateToActiveRewardsDataSharingConsent(Activity activity, long uniqueVoucherId, int rewardId) {
        navigator.showActiveRewardsDataSharingConsent(activity, uniqueVoucherId, rewardId);
    }

    public void navigateToStarbucksDataSharingConsent(Activity activity,
                                                          long uniqueVoucherId,
                                                          String partnerRegistrationEmailAddress) {
        navigator.showActiveStarbucksDataSharingConsent(activity, uniqueVoucherId, partnerRegistrationEmailAddress);
    }

    public void navigateAfterARRewardsLearnMoreCardTapped(Activity activity) {
        navigator.showActiveRewardsLearnMore(activity);
    }

    public void navigateAfterARViewAvailableSpinsCardTapped(Activity activity) {
        navigator.showActiveRewardsRewardsList(activity);
    }

    public void navigateAfterARRewardsSpinNowCardTapped(Activity activity, long uniqueId, int rewardId) {
        navigator.showWheelSpin(activity, uniqueId, rewardId);
    }

    public void navigateAfterARRewardVoucherCardTapped(Activity activity, long rewardUniqueId) {
        navigator.showActiveRewardsVoucher(activity, rewardUniqueId);
    }

    public void navigateToVitalityAgeTipMoreResults(Activity activity, int typeKey) {
        navigator.showVitalityAgeTipMoreResults(activity, typeKey);
    }

    public void navigateFromSettingsToTermsAndConditions(Activity activity) {
        navigator.showTermsAndConditionsWithoutButtonBar(activity);
    }

    public void navigateProfileEventsFeedTapped(Activity activity) {
        navigator.showEventsFeedActivity(activity);
    }

    public void navigateToScreeningsAndVaccination(Activity activity) {
        navigator.showScreeningsAndVaccinationsOnboarding(activity);
    }


    public void navigateToHealthActions(Activity activity, String actionType) {
        navigator.showHealthActions(activity, actionType);
    }

    public void navigateAfterARRewardSelectionHomeCardTapped(Activity activity, long rewardUniqueId) {
        navigator.showRewardSelection(activity, rewardUniqueId);
    }

    public void navigateToTermsAndConditions(Activity activity) {
        navigator.showTermsAndConditions(activity);
    }

    public void navigateToChangePassword(Activity activity) {
        navigator.showChangePassword(activity);
    }

    public void navigateToChangePasswordActvity(Activity activity) {
        navigator.showChangedPasswordActivity(activity);
    }

    public void navigateToChangePasswordAfterDone(Activity activity) {
        navigator.showChangePasswordAfterDone(activity);
    }

    public void navigateToFeedbackHealthAttributeDetails(Activity activity, int sectionTypeKey, int attributeTypeKey) {
        navigator.showHealthAttributeDetails(activity, sectionTypeKey, attributeTypeKey);
    }

    public void navigateToFeedbackTipDetails(Activity activity, int tipTypeKey, String tipCode) {
        navigator.showFeedbackTipDetails(activity, tipTypeKey, tipCode);
    }

    public void navigateAfterSpecificCinemaSelection(Activity activity, long uniqueId) {
        navigator.showActiveRewardsVoucher(activity, uniqueId);
        activity.finish();
    }

    public void navigateAfterConfirmAndSubmitTapped(Activity activity) {
        navigator.showConfirmAndSubmitSNV(activity);
    }

    public void navigateAfterNextIsTappedConfirmAndSubmit(Activity activity) {
        navigator.showAddProofActivitySNV(activity);
    }

    public void navigateToMoreTips(Activity activity, int sectionTypeKey, int attributeTypeKey) {
        navigator.showMyHealthMoreTips(activity, sectionTypeKey, attributeTypeKey);
    }

    public void navigateHelpScreen(Activity activity) {
        navigator.showHelpScreen(activity);
    }

    public void navigateFromRewardsListOnRewardVoucherTapped(Activity activity, RewardVoucherDTO reward) {
        if (reward.rewardId == RewardId._CINEWORLDORVUE && reward.availableToRedeem) {
            navigator.showRewardSelection(activity, reward.uniqueId);
        } else {
            navigator.showActiveRewardsVoucher(activity, reward.uniqueId);
        }
    }

    public void navigateToFeedback(Activity activity, int requestCode) {
        navigator.showFeedback(activity, requestCode);
    }

    public void navigateAfterARRewardsChooseRewardCardTapped(Activity activity, long uniqueId, int rewardId) {
        navigator.showChooseReward(activity, uniqueId, rewardId);
    }

    public void navigateAfterRewardChoiceConfirmed(Activity activity, long uniqueID) {
        navigator.showActiveRewardsVoucherAfterStarbucksConfirm(activity, uniqueID);
    }

    public void navigateToProfile(Activity activity) {
        navigator.showProfile(activity);
    }

    public void navigateToSetings(Activity activity) {
        navigator.showSettings(activity);
    }

    public void navigateToSnvLearnMore(Activity activity) {
        navigator.showScreeningLearnMore(activity);
    }

    public void navigateToLearnMoreScreenings(Activity activity, String actionType) {
        navigator.showLearnMoreScreeningsList(activity, actionType);
    }

    public void navigateToSnvListDescription(Activity activity, String actionType) {
        navigator.showLearnMoreScreeningsListDescription(activity, actionType);
    }

    public void navigateToSnvParticipatingPartnersActivity(Activity activity) {
        navigator.showSnvParticipatingPartnersActivity(activity);
    }

    public void navigateToSnvParticipatingPartnersDetailActivity(Activity activity, ProductFeatureGroupDto snvParticipatingPartnersItem) {
        navigator.showSnvParticipatingPartnersDetailsActivity(activity, snvParticipatingPartnersItem);
    }

    public void navigateToScreenAndVaccinationsHistory(Activity activity) {
        navigator.showScreeningAndVaccinationHistory(activity);
    }

    public void navigateToScreenAndVacctionHistoryDetail(Activity activity, String dateString, String dateMessage) {
        navigator.showScreeningAndVaccinationHistoryDetail(activity, dateString, dateMessage);
    }

    public void navigateAfterPendingRewardSelectionHomeCardTapped(Activity activity, long rewardUniqueId) {
        navigator.showActiveRewardsVoucher(activity, rewardUniqueId);
    }

    public void navigateToScreen(Activity activity) {
        switch(ShowScreenUtility.screen) {
            case PushNotificationData.SCREEN_ACTIVE_REWARDS_REWARDS:
                navigateOnMenuItemFromActiveRewardsLanding(activity, MenuItemType.Rewards);
                break;
            case PushNotificationData.SCREEN_ACTIVE_REWARDS_LEARN_MORE:
                navigateOnMenuItemFromActiveRewardsLanding(activity, MenuItemType.LearnMore);
                break;
            case PushNotificationData.SCREEN_ACTIVE_REWARDS_ACTIVATE:
                navigateAfterActiveRewardsSelected(activity);
          		break;
//            case PushNotificationData.SCREEN_MENU_PROFILE:
//                navigateToProfile(activity);
//                break;
            default:
                navigator.showHomeInitially(activity);
        }
        ShowScreenUtility.screen = 0;

    }

    public MWBCaptureDependencyInjector getMWBCaptureDependencyInjector() {
        if (mwbCaptureDependencyInjector == null) {
            mwbCaptureDependencyInjector = vitalityActiveApplication.getDependencyInjector().createDependencyInjector(new MWBCaptureModule());
        }
        return mwbCaptureDependencyInjector;
    }

    public void navigateOnMenuItemFromMWBLanding(Activity activity, MenuItemType menuItemType) {
        if (menuItemType.equals(MenuItemType.LearnMore)) {
            navigator.showMWBLearnMore(activity);
        } else if (menuItemType.equals(MenuItemType.Help)) {
            navigator.showVHRHelp(activity);
        }
    }

    public void navigateToMentalWellbeingOnboarding(Activity activity){
        navigator.showMentalWellBeing(activity,false);
    }

    public void navigateAfterMWBLearnMoreTapped(Activity activity){
        navigator.showMWBLearnMore(activity);
    }

    public void navigateToPartnerTermsAndConditions(Activity activity, String articleId) {
        navigator.showPartnerTermsAndConditions(activity, articleId);
    }
}
