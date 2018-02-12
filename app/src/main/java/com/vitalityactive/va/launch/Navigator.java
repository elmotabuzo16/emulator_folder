package com.vitalityactive.va.launch;

import android.app.Activity;
import android.support.annotation.StringRes;

import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.activerewards.dto.ActivityItem;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.partnerjourney.PartnerType;
import com.vitalityactive.va.snv.partners.dto.ProductFeatureGroupDto;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingInteractor;

public interface Navigator {
    void showLogin(Activity activity);

    void showHomeInitially(Activity activity);

    void showRegistration(Activity activity, String username);

    void showForgotPassword(Activity activity);

    void showTermsAndConditions(Activity activity);

    void showTermsAndConditionsWithoutButtonBar(Activity activity);

    void showSplash(Activity activity);

    void showFirstTimeUserPreferences(Activity activity);

    void showActiveRewardsOnboardingForRewardChoice(Activity activity);

    void showActiveRewardsOnboardingForProbabilisticRewards(Activity activity);

    void showActiveRewardsMedicallyFitAgreement(Activity activity);

    void showActiveRewardsActivationAcknowledgement(Activity activity);

    void showActiveRewardsLanding(Activity activity, boolean shouldCheckForLinkedDevicesOnStart);

    void showNonSmokersDeclarationGetStarted(Activity activity);

    void showNonSmokersPrivacyPolicy(Activity activity);

    void showNonSmokersAssessmentCompleted(Activity activity);

    void showHome(Activity activity);

    void showActiveRewardsLinkFitnessDevice(Activity activity);

    void showActiveRewardsLearnMore(Activity activity);

    void showActiveRewardsRewardsList(Activity activity);

    void showActiveRewardsActivityEventDetail(Activity activity, ActivityItem activityItem);

    void showActiveRewardsParticipatingPartners(Activity activity);

    void showActiveRewardsMonthlyActivity(Activity activity);

    void showHistoryWeekDetailedActivity(Activity activity, GoalTrackerOutDto data);

    void showActiveRewardsHelp(Activity activity);

    void showCachedActiveRewardsParticipatingPartnerDetail(Activity activity, int rewardId);

    void showActiveRewardsParticipatingPartnerDetail(Activity activity, long partnerId);

    void showWheelSpin(Activity activity, long uniqueId, int rewardId);

    void showStarbucksPartnerRegistration(Activity activity, long rewardUniqueId);

    void showVhrOnboardingFromActiveRewardsFlow(Activity activity);

    void showLoginAfterSuccessfulRegistration(Activity activity);

    void showLoginAfterForgotPassword(Activity activity, String username);

    void showNonSmokersDeclaration(Activity activity);

    void showAvailableDeviceDetails(Activity activity, PartnerDto partner);

    void showNonSmokersDeclarationLearnMore(Activity activity);

    void showVHCOnboarding(Activity activity);

    void showVHCHealthMeasurements(Activity activity, boolean isAfterFormSubmition);

    void showVHCCaptureResults(Activity activity);

    void showVHCHistoryActivity(Activity activity);

    void showVHCLearnMore(Activity activity);

    void showVHCHelp(Activity activity);

    void showVHCAddProof(Activity activity);

    void showVHCSummaryActivity(Activity activity);

    void showSNVSummaryActivity(Activity activity);

    void showSNVPrivacyPolicy(Activity activity);

    void showSNVDataConfirmed(Activity activity);

    void showVHCPrivacyPolicy(Activity activity);

    void showVHCMeasurementsConfirmed(Activity activity);

    void showVHCLearnMoreItemDetail(Activity activity, String tag);

    void showSNVOnboarding(Activity activity);

    void showMWBOnboarding(Activity activity);

    void showLoginAfterLogOut(VitalityActiveApplication activity, boolean isSessionExpired);

    void showHealthAttributeGroupDetail(Activity activity, int healthAttributeGroupFeatureType);

    void showVHRLearnMore(Activity activity);

    void showVHRLanding(Activity activity, boolean isFromFormSubmission);

    void showVHRLandingAfterOnboarding(Activity activity);

    void showVHRQuestionnaire(Activity activity, long typeKey);

    void showVHRHelp(Activity activity);

    void showVHRPrivacyPolicy(Activity activity, long questionnaireTypeKey);

    void showVNAPrivacyPolicy(Activity activity, long questionnaireTypeKey);

    void showVHRQuestionnaireCompletedActivity(Activity activity, long questionnaireTypeKey);

    void showVHROnboarding(Activity activity);

    void showWellnessDeviceOnboarding(Activity activity);

    void showWellnessDeviceLanding(Activity activity, boolean isInsideActionRewardsFlow);

    void showWellnessDeviceLandingAfterOnboarding(Activity activity, boolean isInsideActionRewardsFlow);

    void showWellnessDevicesLearnMore(Activity activity);

    void showWellnessDevicesWebLinkActivity(Activity activity, String partnerLink, @WellnessDevicesLinkingInteractor.RequestType String requestType, int requestCode);

    void showWellnessDevicesPointsActivity(Activity activity, int eventType);

    void showWellnessDevicesPrivacyPolicyActivity(Activity activity, PartnerDto partner, int requestCode);

    void showWellnessDevicesBrowser(Activity activity, String url);

    void showWellnessDevicesWebLinkActivityWithRedirectedResult(Activity activity, String partnerLink, @WellnessDevicesLinkingInteractor.RequestType String requestType);

    void showWellnessDevicesInfoActivity(Activity activity, String title, String articleId);

    void back(Activity activity);

    void showPrivacyStatement(Activity activity);

    void showVNAOnboarding(Activity activity);

    void showVNALanding(Activity activity);

    void showVNALearnMore(Activity activity);

    void showVNAHelp(Activity activity);

    void showVNAQuestionnaire(Activity activity, long typeKey);

    void showVNADisclaimer(Activity activity);

    void showVNAQuestionnaireCompletedActivity(Activity activity, long questionnaireTypeKey);

    void showPDF(Activity activity, String fileName, @StringRes int title);

    void showVitalityAge(Activity activity, int mode);

    void showVHRDisclaimer(Activity activity);

    void showMyHealthOnboardingScreen(Activity activity, int resultCode);

    void showMyHealthVitalityAgeProfile(Activity activity);

    void showMyHealthMoreTips(Activity activity, int sectionTypeKey, int attributeTypeKey);

    void showPartnerJourney(Activity activity, PartnerType partnerType);

    void showPartnerDetail(Activity activity, PartnerType partnerType, long partnerId);

    void showVitalityStatusOnboarding(Activity activity);

    void showVitalityStatusLanding(Activity activity);

    void showVitalityStatusLearnMore(Activity activity);

    void showVitalityStatusDetail(Activity activity, String title);

    void showPointsCategoryFeatures(Activity activity, int type);

    void showPointsEarningMetricDetail(Activity activity, String type);

    void showMyHealthDisclaimer(Activity activity);

    void showVitalityStatusLevelIncreasedActivity(Activity activity);

    void showStatusMyRewards(Activity activity);

    void showActiveRewardsOnboardingWithoutRewards(Activity activity);

    void showPointsFeatureSubfeatures(Activity activity, int key);

    void showStatusHowToEarnPoints(Activity activity, int key, String name);

    void showPersonalDetailsActivity(Activity activity);

    void showActiveRewardsVoucher(Activity activity, long rewardUniqueId);

    void showActiveRewardsVoucherAfterStarbucksConfirm(Activity activity, long rewardUniqueId);

    void showMembershipPassActivity(Activity activity);

    void showCommunicationPreferences(Activity activity);

    void showSecurityPreferences(Activity activity);

    void showPrivacyPreferences(Activity activity);

    void showVitalityAgeTipMoreResults(Activity activity,int typeKey);

    void showChangePassword(Activity activity);

    void showScreeningsAndVaccinationsOnboarding(Activity activity);

    void showMentalWellbeingOnboarding(Activity activity);

    void showEventsFeedActivity(Activity activity);

    void showHealthAttributeDetails(Activity activity, int typeKey, int attributeTypeKey);

    void showActiveRewardsDataSharingConsent(Activity activity, long uniqueRewardId, int rewardId);

    void showActiveStarbucksDataSharingConsent(Activity activity, long uniqueRewardId, String partnerRegistrationEmailAddress);

    void showHealthActions(Activity activity, String actionType);

    void showSplashAndSkipAppConfig(Activity activity);

    void showCinemaRewardConfirmation(Activity activity, long rewardUniqueId);

    void showStandardRewardConfirmation(Activity activity, long uniqueRewardId);

    void showRewardSelection(Activity activity, long rewardUniqueId);

    void showVitalityAgeCardLearnMore(Activity activity, int effectiveTypeValue, int actualTypeValue, String vitalityAge, String vitalityAgeVariance);

    void showChangedPasswordActivity(Activity activity);

    void showChangePasswordAfterDone(Activity activity);

    void showFeedbackTipDetails(Activity activity, int tipTypeKey,String tipCode);

    void showHelpScreen(Activity activity);

    void showActiveRewardsRewardsListFromActiveRewards(Activity activity);

    void showFeedback(Activity activity, int requestCode);
    
    void showConfirmAndSubmitSNV(Activity activity);
    void showChooseReward(Activity activity, long uniqueId, int rewardId);

    void showAddProofActivitySNV(Activity activity);

    void showScreeningLearnMore(Activity activity);

    void showLearnMoreScreeningsList(Activity activity, String actionType);

    void showLearnMoreScreeningsListDescription(Activity activity, String actionType);

    void showSnvParticipatingPartnersActivity(Activity activity);

    void showSnvParticipatingPartnersDetailsActivity(Activity activity, ProductFeatureGroupDto snvParticipatingPartnersItem);

    void showScreeningAndVaccinationHistory(Activity activity);

    void showScreeningAndVaccinationHistoryDetail(Activity activity, String dateString, String dateMessage);


    void showProfile(Activity activity);

    void showSettings(Activity activity);

    void showMentalWellBeing(Activity activity, boolean isFromFormSubmission);

    void showMWBLearnMore(Activity activity);

    void showPartnerTermsAndConditions(Activity activity, String contentId);
}
