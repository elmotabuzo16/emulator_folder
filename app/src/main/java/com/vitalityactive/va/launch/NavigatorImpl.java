package com.vitalityactive.va.launch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.TaskStackBuilder;

import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.activerewards.ActiveRewardsActivatedActivity;
import com.vitalityactive.va.activerewards.ActiveRewardsLearnMoreActivity;
import com.vitalityactive.va.activerewards.dto.ActivityItem;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.activerewards.eventdetail.ActiveRewardsEventDetailActivity;
import com.vitalityactive.va.activerewards.help.ActiveRewardsHelpActivity;
import com.vitalityactive.va.activerewards.history.GoalHistoryActivity;
import com.vitalityactive.va.activerewards.history.detailedscreen.HistoryWeekDetailsActivity;
import com.vitalityactive.va.activerewards.landing.ActiveRewardsLandingActivity;
import com.vitalityactive.va.activerewards.onboarding.ActiveRewardsOnboardingActivity;
import com.vitalityactive.va.activerewards.onboarding.ActiveRewardsVhrStartActivity;
import com.vitalityactive.va.activerewards.participatingpartners.ActiveRewardsParticipatingPartnersActivity;
import com.vitalityactive.va.activerewards.participatingpartners.CachedParticipatingPartnerDetailActivity;
import com.vitalityactive.va.activerewards.participatingpartners.ParticipatingPartnerDetailActivity;
import com.vitalityactive.va.activerewards.rewards.ActiveRewardsDataSharingConsentActivity;
import com.vitalityactive.va.activerewards.rewards.CentralAssetWheelSpinConfirmationActivity;
import com.vitalityactive.va.activerewards.rewards.ChooseRewardActivity;
import com.vitalityactive.va.activerewards.rewards.CinemaRewardConfirmationActivity;
import com.vitalityactive.va.activerewards.rewards.CinemaRewardSelectionActivity;
import com.vitalityactive.va.activerewards.rewards.RewardVoucherActivity;
import com.vitalityactive.va.activerewards.rewards.RewardsListActivity;
import com.vitalityactive.va.activerewards.rewards.StarbucksDataSharingConsentActivity;
import com.vitalityactive.va.activerewards.rewards.StarbucksPartnerRegistrationActivity;
import com.vitalityactive.va.activerewards.rewards.WheelSpinActivity;
import com.vitalityactive.va.activerewards.termsandconditions.ActiveRewardsMedicallyFitAgreementActivity;
import com.vitalityactive.va.eventsfeed.views.EventsFeedActivity;
import com.vitalityactive.va.feedback.FeedbackActivity;
import com.vitalityactive.va.forgotpassword.ForgotPasswordActivity;
import com.vitalityactive.va.help.HelpFragment;
import com.vitalityactive.va.home.HomeActivity;
import com.vitalityactive.va.login.LoginActivity;
import com.vitalityactive.va.membershippass.MembershipPassActivity;
import com.vitalityactive.va.mwb.MWBOnBoardingActivity;
import com.vitalityactive.va.mwb.learnmore.MWBLearnMoreActivity;
import com.vitalityactive.va.mwb.landing.MWBLandingActivity;
import com.vitalityactive.va.myhealth.disclaimer.MyHealthDisclaimerActivity;
import com.vitalityactive.va.myhealth.healthattributes.MyHealthAttributeDetailActivity;
import com.vitalityactive.va.myhealth.learnmore.VitalityAgeLearnMoreActivity;
import com.vitalityactive.va.myhealth.moretips.MyHealthMoreTipsActivity;
import com.vitalityactive.va.myhealth.onboarding.MyHealthOnboardingActivity;
import com.vitalityactive.va.myhealth.tipdetail.MyHealthTipDetailActivity;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgeActivity;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthTipsMoreResultsActivity;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthVitalityAgeProfileActivity;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersAssessmentCompleteActivity;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersDeclareActivity;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersPrivacyPolicyActivity;
import com.vitalityactive.va.nonsmokersdeclaration.onboarding.NonSmokersDeclarationLearnMoreActivity;
import com.vitalityactive.va.nonsmokersdeclaration.onboarding.NonSmokersDeclarationOnboardingActivity;
import com.vitalityactive.va.partnerjourney.BasePartnerActivity;
import com.vitalityactive.va.partnerjourney.PartnerListActivity;
import com.vitalityactive.va.partnerjourney.PartnerTermsAndConditionsActivity;
import com.vitalityactive.va.partnerjourney.PartnerType;
import com.vitalityactive.va.profile.PersonalDetailsActivity;
import com.vitalityactive.va.profile.ProfileActivity;
import com.vitalityactive.va.register.view.RegisterActivity;
import com.vitalityactive.va.search.SearchResultsActivity;
import com.vitalityactive.va.settings.PasswordChangedActivity;
import com.vitalityactive.va.settings.SettingChangePasswordActivity;
import com.vitalityactive.va.settings.SettingsActivity;
import com.vitalityactive.va.shared.questionnaire.activity.QuestionnaireBaseActivity;
import com.vitalityactive.va.snv.SNVOnboardingActivity;
import com.vitalityactive.va.snv.confirmandsubmit.view.ConfirmAndSubmitActivity;
import com.vitalityactive.va.snv.confirmandsubmit.view.SNVAddProofActivity;
import com.vitalityactive.va.snv.confirmandsubmit.view.SNVDataConfirmedActivity;
import com.vitalityactive.va.snv.confirmandsubmit.view.SNVPrivacyPolicyActivity;
import com.vitalityactive.va.snv.confirmandsubmit.view.SNVSummaryActivity;
import com.vitalityactive.va.snv.history.views.SNVHistoryActivity;
import com.vitalityactive.va.snv.history.views.SNVHistoryDetailActivity;
import com.vitalityactive.va.snv.learnmore.ScreeningsActivity;
import com.vitalityactive.va.snv.learnmore.ScreeningsLearnMoreActivity;
import com.vitalityactive.va.snv.learnmore.SnvListDescriptionActivity;
import com.vitalityactive.va.snv.onboarding.ScreeningsAndVaccinationsHelp;
import com.vitalityactive.va.snv.onboarding.ScreeningsAndVaccinationsOnboardingActivity;
import com.vitalityactive.va.snv.onboarding.healthactions.HealthActionsActivity;
import com.vitalityactive.va.snv.partners.SnvParticipatingPartnersActivity;
import com.vitalityactive.va.snv.partners.SnvParticipatingPartnersDetailActivity;
import com.vitalityactive.va.snv.partners.dto.ProductFeatureGroupDto;
import com.vitalityactive.va.splashscreen.SplashScreenActivity;
import com.vitalityactive.va.termsandconditions.TermAndConditionsWithoutAgreeButtonBarActivity;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsActivity;
import com.vitalityactive.va.userpreferences.CommunicationPreferencesActivity;
import com.vitalityactive.va.userpreferences.FirstTimeUserPreferencesActivity;
import com.vitalityactive.va.userpreferences.PrivacyPreferencesActivity;
import com.vitalityactive.va.userpreferences.PrivacyStatementActivity;
import com.vitalityactive.va.userpreferences.SecurityPreferencesActivity;
import com.vitalityactive.va.vhc.PDFActivity;
import com.vitalityactive.va.vhc.VHCHelpActivity;
import com.vitalityactive.va.vhc.VHCHistoryActivity;
import com.vitalityactive.va.vhc.VHCMeasurementsConfirmedActivity;
import com.vitalityactive.va.vhc.VHCOnboardingActivity;
import com.vitalityactive.va.vhc.VHCPrivacyPolicyActivity;
import com.vitalityactive.va.vhc.VHCSummaryActivity;
import com.vitalityactive.va.vhc.addproof.VHCAddProofActivity;
import com.vitalityactive.va.vhc.captureresults.VHCCaptureResultsActivity;
import com.vitalityactive.va.vhc.detail.HealthAttributeDetailActivity;
import com.vitalityactive.va.vhc.landing.VHCLandingActivity;
import com.vitalityactive.va.vhc.learnmore.VHCLearnMoreActivity;
import com.vitalityactive.va.vhc.learnmore.VHCLearnMoreItemDetailActivity;
import com.vitalityactive.va.vhr.VHRLearnMoreActivity;
import com.vitalityactive.va.vhr.VHROnBoardingActivity;
import com.vitalityactive.va.vhr.VHRPrivacyPolicyActivity;
import com.vitalityactive.va.vhr.disclaimer.VHRDisclaimerActivity;
import com.vitalityactive.va.vhr.landing.VHRLandingActivity;
import com.vitalityactive.va.vhr.questions.VHRQuestionnaireActivity;
import com.vitalityactive.va.vhr.questions.VHRQuestionnaireCompletedActivity;
import com.vitalityactive.va.vitalitystatus.PointsEarningMetricDetail;
import com.vitalityactive.va.vitalitystatus.VitalityStatusLearnMoreActivity;
import com.vitalityactive.va.vitalitystatus.VitalityStatusLevelIncreasedActivity;
import com.vitalityactive.va.vitalitystatus.detail.StatusMyRewardsDetailActivity;
import com.vitalityactive.va.vitalitystatus.detail.VitalityStatusLevelDetailActivity;
import com.vitalityactive.va.vitalitystatus.earningpoints.StatusPointsActivity;
import com.vitalityactive.va.vitalitystatus.earningpoints.VitalityStatusDetailsActivity;
import com.vitalityactive.va.vitalitystatus.landing.VitalityStatusLandingActivity;
import com.vitalityactive.va.vitalitystatus.onboarding.VitalityStatusOnboarding;
import com.vitalityactive.va.vna.VNALearnMoreActivity;
import com.vitalityactive.va.vna.VNAPrivacyPolicyActivity;
import com.vitalityactive.va.vna.VNAQuestionnaireActivity;
import com.vitalityactive.va.vna.VNAQuestionnaireCompletedActivity;
import com.vitalityactive.va.vna.disclaimer.VNADisclaimerActivity;
import com.vitalityactive.va.vna.help.VNAHelpActivity;
import com.vitalityactive.va.vna.landing.VNALandingActivity;
import com.vitalityactive.va.vna.onboarding.VNAOnboardingActivity;
import com.vitalityactive.va.wellnessdevices.Constants;
import com.vitalityactive.va.wellnessdevices.SimpleBrowserActivity;
import com.vitalityactive.va.wellnessdevices.WellnessDevicesLearnMoreActivity;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.informative.WellnessDevicesInfoActivity;
import com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingActivity;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesDetailsActivity;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingInteractor;
import com.vitalityactive.va.wellnessdevices.linking.privacypolicy.WellnessDevicesPrivacyPolicyActivity;
import com.vitalityactive.va.wellnessdevices.linking.web.WellnessDevicesWebActivity;
import com.vitalityactive.va.wellnessdevices.onboarding.WellnessDevicesOnboardingActivity;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.WellnessDevicesPointsActivity;

import static com.vitalityactive.va.activerewards.eventdetail.ActiveRewardsEventDetailActivity.EXTRA_ACTIVITY_ITEM;
import static com.vitalityactive.va.activerewards.onboarding.ActiveRewardsOnboardingActivity.CONTENT_TYPE_DEFINED;
import static com.vitalityactive.va.activerewards.onboarding.ActiveRewardsOnboardingActivity.CONTENT_TYPE_PROBABILISTIC;
import static com.vitalityactive.va.activerewards.onboarding.ActiveRewardsOnboardingActivity.EXTRA_CONTENT_TYPE;

public class NavigatorImpl implements Navigator {

    @Override
    public void showLogin(Activity activity) {
        navigateToActivityAndFinish(activity, LoginActivity.class);
    }

    @Override
    public void showHomeInitially(Activity activity) {
        navigateToActivityInNewTask(activity, HomeActivity.class);
    }

    @Override
    public void showRegistration(Activity activity, String username) {
        navigateToActivityWithExtra(activity, RegisterActivity.class,
                RegisterActivity.EXTRA_USERNAME, username);
    }

    @Override
    public void showForgotPassword(Activity activity) {
        navigateToActivity(activity, ForgotPasswordActivity.class);
    }

    @Override
    public void showTermsAndConditions(Activity activity) {
        /*
            todo: not sure what the intention was with this change, but it disables the button bar on *ALL* terms and conditions activities
            introduced in https://bitbucket.org/glucode/dpm-vitalityactive-android/pull-requests/91/va-2844-update-view-terms-and-conditions
            adding showTermsAndConditionsWithoutButtonBar that had the old code
         */
        navigateToActivityAndFinish(activity, TermsAndConditionsActivity.class);
    }

    @Override
    public void showTermsAndConditionsWithoutButtonBar(Activity activity) {
        navigateToActivity(activity, TermAndConditionsWithoutAgreeButtonBarActivity.class);
    }

    @Override
    public void showSplash(Activity activity) {
        navigateToActivityAndFinish(activity, SplashScreenActivity.class);
    }

    @Override
    public void showFirstTimeUserPreferences(Activity activity) {
        navigateToActivity(activity, FirstTimeUserPreferencesActivity.class);
    }

    @Override
    public void showActiveRewardsOnboardingForRewardChoice(Activity activity) {
        navigateToActivityWithExtra(activity, ActiveRewardsOnboardingActivity.class,
                EXTRA_CONTENT_TYPE, CONTENT_TYPE_DEFINED);
    }

    @Override
    public void showActiveRewardsOnboardingForProbabilisticRewards(Activity activity) {
        navigateToActivityWithExtra(activity, ActiveRewardsOnboardingActivity.class,
                EXTRA_CONTENT_TYPE, CONTENT_TYPE_PROBABILISTIC);
    }

    @Override
    public void showActiveRewardsOnboardingWithoutRewards(Activity activity) {
        navigateToActivity(activity, ActiveRewardsOnboardingActivity.class);
    }

    @Override
    public void showPersonalDetailsActivity(Activity activity) {
        navigateToActivity(activity, PersonalDetailsActivity.class);
    }


    @Override
    public void showEventsFeedActivity(Activity activity) {
        navigateToActivity(activity, EventsFeedActivity.class);
    }

    @Override
    public void showActiveRewardsVoucher(Activity activity, long rewardUniqueId) {
        navigateToActivityWithExtra(activity, RewardVoucherActivity.class, RewardVoucherActivity.VOUCHER_UNIQUE_ID, rewardUniqueId);
    }

    @Override
    public void showActiveRewardsVoucherAfterStarbucksConfirm(Activity activity, long rewardUniqueId) {
        navigateToActivityWithExtraAndFinish(activity, RewardVoucherActivity.class, RewardVoucherActivity.VOUCHER_UNIQUE_ID, rewardUniqueId);
    }

    @Override
    public void showCommunicationPreferences(Activity activity) {
        navigateToActivity(activity, CommunicationPreferencesActivity.class);
    }

    @Override
    public void showSecurityPreferences(Activity activity) {
        navigateToActivity(activity, SecurityPreferencesActivity.class);
    }

    @Override
    public void showPrivacyPreferences(Activity activity) {
        navigateToActivity(activity, PrivacyPreferencesActivity.class);
    }

    @Override
    public void showVitalityAgeTipMoreResults(Activity activity, int sectionTypeKey) {
        navigateToActivityWithExtra(activity, MyHealthTipsMoreResultsActivity.class, MyHealthTipsMoreResultsActivity.SECTION_TYPE_KEY, sectionTypeKey);
    }

    @Override
    public void showHealthAttributeDetails(Activity activity, int sectionTypeKey, int attributeTypeKey) {
        navigateToActivityWithExtra(activity, MyHealthAttributeDetailActivity.class, MyHealthAttributeDetailActivity.SECTION_TYPE_KEY, sectionTypeKey, MyHealthAttributeDetailActivity.ATTRIBUTE_TYPE_KEY, attributeTypeKey);
    }

    @Override
    public void showChangePassword(Activity activity) {
        Intent intent = getIntent(activity, SettingChangePasswordActivity.class);
        activity.startActivity(intent);
    }

    @Override

    public void showActiveRewardsDataSharingConsent(Activity activity, long uniqueRewardId, int rewardId) {

        Bundle bundle = new Bundle();
        bundle.putLong(ActiveRewardsDataSharingConsentActivity.EXTRA_REWARD_UNIQUE_ID, uniqueRewardId);
        bundle.putInt(ActiveRewardsDataSharingConsentActivity.EXTRA_REWARD_ID, rewardId);

        navigateToActivityWithExtra(activity, ActiveRewardsDataSharingConsentActivity.class, ActiveRewardsDataSharingConsentActivity.EXTRA_BUNDLE_KEY, bundle);
    }

    public void showActiveStarbucksDataSharingConsent(Activity activity, long uniqueRewardId, String partnerRegistrationEmailAddress) {

        Bundle bundle = new Bundle();
        bundle.putLong(StarbucksPartnerRegistrationActivity.REWARD_UNIQUE_ID, uniqueRewardId);
        bundle.putString(StarbucksPartnerRegistrationActivity.PARTNER_REGISTRATION_EMAIL, partnerRegistrationEmailAddress);

        navigateToActivityWithExtra(activity, StarbucksDataSharingConsentActivity.class, StarbucksPartnerRegistrationActivity.OUTGOING_BUNDLE_KEY, bundle);
    }

    @Override
    public void showSplashAndSkipAppConfig(Activity activity) {
        navigateToActivityWithExtraAndFinish(activity, SplashScreenActivity.class, SplashScreenActivity.SKIP_APP_CONFIG, true);
    }

    @Override
    public void showCinemaRewardConfirmation(Activity activity, long rewardUniqueId) {
        navigateToActivityWithExtraAndFinish(activity, CinemaRewardConfirmationActivity.class, CinemaRewardConfirmationActivity.REWARD_UNIQUE_ID, rewardUniqueId);
    }

    @Override
    public void showStandardRewardConfirmation(Activity activity, long rewardUniqueId) {
        // TODO: 2017/12/05 Remember to finish activity as wheelspin should not show again
        navigateToActivityWithExtraAndFinish(activity, CentralAssetWheelSpinConfirmationActivity.class, CentralAssetWheelSpinConfirmationActivity.REWARD_UNIQUE_ID, rewardUniqueId);
    }

    @Override
    public void showRewardSelection(Activity activity, long rewardUniqueId) {
        navigateToActivityWithExtra(activity, CinemaRewardSelectionActivity.class,
                CinemaRewardSelectionActivity.REWARD_UNIQUE_ID, rewardUniqueId);
    }

    @Override
    public void showFeedbackTipDetails(Activity activity, int tipTypeKey, String tipCode) {
        navigateToActivityWithExtra(activity, MyHealthTipDetailActivity.class, MyHealthTipDetailActivity.TIP_TYPE_KEY, tipTypeKey, MyHealthTipDetailActivity.TIP_CODE, tipCode);
    }

    @Override
    public void showActiveRewardsRewardsListFromActiveRewards(Activity activity) {
        navigateToActivityAndFinish(activity, RewardsListActivity.class);
    }

    @Override
    public void showFeedback(Activity activity, int requestCode) {
        navigateToActivityForResult(activity, FeedbackActivity.class, requestCode);
    }

    @Override
    public void showActiveRewardsMedicallyFitAgreement(Activity activity) {
        navigateToActivity(activity, ActiveRewardsMedicallyFitAgreementActivity.class);
    }

    @Override
    public void showActiveRewardsActivationAcknowledgement(Activity activity) {
        navigateToActivityWithNewBackStack(activity, ActiveRewardsActivatedActivity.class);
    }

    @Override
    public void showActiveRewardsLanding(Activity activity, boolean shouldCheckForLinkedDevicesOnStart) {
        navigateToActivityWithExtra(activity,
                ActiveRewardsLandingActivity.class,
                ActiveRewardsLandingActivity.EXTRA_SHOULD_CHECK_LINKED_DEVICES_ON_START,
                shouldCheckForLinkedDevicesOnStart);
    }

    @Override
    public void showHome(Activity activity) {
        navigateToActivityInNewTask(activity, HomeActivity.class);
    }

    @Override
    public void showActiveRewardsLinkFitnessDevice(Activity activity) {
        navigateToActivityWithExtra(activity,
                WellnessDevicesOnboardingActivity.class,
                Constants.IS_INSIDE_ACTIVE_REWARDS_FLOW,
                true);
    }

    @Override
    public void showWellnessDeviceOnboarding(Activity activity) {
        navigateToActivity(activity, WellnessDevicesOnboardingActivity.class);
    }

    @Override
    public void showWellnessDeviceLanding(Activity activity, boolean isInsideActionRewardsFlow) {
        navigateToActivityWithExtra(activity,
                WellnessDevicesLandingActivity.class,
                Constants.IS_INSIDE_ACTIVE_REWARDS_FLOW,
                isInsideActionRewardsFlow);
    }

    @Override
    public void showWellnessDeviceLandingAfterOnboarding(Activity activity, boolean isInsideActionRewardsFlow) {
        navigateToActivityWithExtraAndFinish(activity,
                WellnessDevicesLandingActivity.class,
                Constants.IS_INSIDE_ACTIVE_REWARDS_FLOW,
                isInsideActionRewardsFlow);
    }

    @Override
    public void showWellnessDevicesLearnMore(Activity activity) {
        navigateToActivity(activity, WellnessDevicesLearnMoreActivity.class);
    }

    @Override
    public void showActiveRewardsLearnMore(Activity activity) {
        navigateToActivity(activity, ActiveRewardsLearnMoreActivity.class);
    }

    @Override
    public void showActiveRewardsRewardsList(Activity activity) {
        navigateToActivity(activity, RewardsListActivity.class);
    }

    @Override
    public void showActiveRewardsActivityEventDetail(Activity activity, ActivityItem activityItem) {
        navigateToActivityWithExtra(activity, ActiveRewardsEventDetailActivity.class,
                EXTRA_ACTIVITY_ITEM, activityItem);
    }

    @Override
    public void showActiveRewardsParticipatingPartners(Activity activity) {
        navigateToActivity(activity, ActiveRewardsParticipatingPartnersActivity.class);
    }

    @Override
    public void showActiveRewardsMonthlyActivity(Activity activity) {
        navigateToActivity(activity, GoalHistoryActivity.class);
    }

    @Override
    public void showHistoryWeekDetailedActivity(Activity activity, GoalTrackerOutDto data) {
        navigateToActivityWithExtra(activity, HistoryWeekDetailsActivity.class,
                HistoryWeekDetailsActivity.EXTRA_DATA, data);
    }

    @Override
    public void showActiveRewardsHelp(Activity activity) {
        navigateToActivity(activity, ActiveRewardsHelpActivity.class);
    }

    @Override
    public void showCachedActiveRewardsParticipatingPartnerDetail(Activity activity, int rewardId) {
        navigateToActivityWithExtra(activity, CachedParticipatingPartnerDetailActivity.class,
                ParticipatingPartnerDetailActivity.KEY_PARTNER_ID, rewardId);
    }

    @Override
    public void showActiveRewardsParticipatingPartnerDetail(Activity activity, long partnerId) {
        navigateToActivityWithExtra(activity, ParticipatingPartnerDetailActivity.class,
                ParticipatingPartnerDetailActivity.KEY_PARTNER_ID, partnerId);
    }

    @Override
    public void showWheelSpin(Activity activity, long uniqueId, int rewardId) {
        Intent intent = getIntent(activity, WheelSpinActivity.class);
        intent.putExtra(WheelSpinActivity.WHEEL_SPIN_UNCLAIMED_REWARD_UNIQUE_ID, uniqueId);
        intent.putExtra(WheelSpinActivity.WHEEL_SPIN_OUTCOME_REWARD_ID, rewardId);
        activity.startActivity(intent);
    }

    @Override
    public void showChooseReward(Activity activity, long uniqueId, int rewardId) {
        Intent intent = getIntent(activity, ChooseRewardActivity.class);
        intent.putExtra(ChooseRewardActivity.CHOOSE_REWARD_UNCLAIMED_REWARD_UNIQUE_ID, uniqueId);
        intent.putExtra(ChooseRewardActivity.CHOOSE_REWARDS_OUTCOME_REWARD_ID, rewardId);
        activity.startActivity(intent);
    }

    @Override
    public void showStarbucksPartnerRegistration(Activity activity, long rewardUniqueId) {
        navigateToActivityWithExtraAndFinish(activity, StarbucksPartnerRegistrationActivity.class, StarbucksPartnerRegistrationActivity.REWARD_UNIQUE_ID, rewardUniqueId);
    }

    @Override
    public void showVhrOnboardingFromActiveRewardsFlow(Activity activity) {
        navigateToActivity(activity, ActiveRewardsVhrStartActivity.class);
    }

    @Override
    public void showLoginAfterSuccessfulRegistration(Activity activity) {
        Intent intent = getIntent(activity, LoginActivity.class);
        intent.putExtra(LoginActivity.EXTRA_LOGIN_AFTER_REGISTRATION, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void showLoginAfterForgotPassword(Activity activity, String username) {
        Intent intent = getIntent(activity, LoginActivity.class);
        intent.putExtra(LoginActivity.EXTRA_LOGIN_AFTER_FORGOT_PASSWORD_USERNAME, username);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void showNonSmokersDeclaration(Activity activity) {
        navigateToActivity(activity, NonSmokersDeclarationOnboardingActivity.class);
    }

    @Override
    public void showNonSmokersDeclarationGetStarted(Activity activity) {
        navigateToActivity(activity, NonSmokersDeclareActivity.class);
    }

    @Override
    public void showNonSmokersPrivacyPolicy(Activity activity) {
        navigateToActivity(activity, NonSmokersPrivacyPolicyActivity.class);
    }

    public void showNonSmokersAssessmentCompleted(Activity activity) {
        navigateToActivityWithNewBackStack(activity, NonSmokersAssessmentCompleteActivity.class);
    }

    @Override
    public void showAvailableDeviceDetails(Activity activity, PartnerDto partner) {
        navigateToActivityWithExtra(activity,
                WellnessDevicesDetailsActivity.class,
                WellnessDevicesDetailsActivity.KEY_DEVICE,
                partner);
    }

    @Override
    public void showWellnessDevicesWebLinkActivity(Activity activity,
                                                   String partnerLink,
                                                   @WellnessDevicesLinkingInteractor.RequestType String requestType,
                                                   int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putString(WellnessDevicesWebActivity.KEY_REQUEST_TYPE, requestType);
        bundle.putString(WellnessDevicesWebActivity.KEY_PARTNER_LINK, partnerLink);
        navigateToActivityForResultWithExtra(activity, WellnessDevicesWebActivity.class,
                WellnessDevicesWebActivity.KEY_WEB_EXTRA, bundle, requestCode);
    }

    @Override
    public void showWellnessDevicesPointsActivity(Activity activity,
                                                  int eventType) {
        Bundle bundle = new Bundle();
        bundle.putInt(WellnessDevicesPointsActivity.KEY_EVENT_TYPE, eventType);
        navigateToActivityWithExtra(activity,
                WellnessDevicesPointsActivity.class,
                WellnessDevicesPointsActivity.KEY_BUNDLE,
                bundle);
    }

    @Override
    public void showWellnessDevicesPrivacyPolicyActivity(Activity activity,
                                                         PartnerDto partner,
                                                         int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WellnessDevicesDetailsActivity.KEY_DEVICE, partner);
        bundle.putString(WellnessDevicesWebActivity.KEY_REQUEST_TYPE, WellnessDevicesLinkingInteractor.WD_LINK_DEVICE);
        navigateToActivityForResultWithExtra(activity,
                WellnessDevicesPrivacyPolicyActivity.class,
                WellnessDevicesWebActivity.KEY_WEB_EXTRA,
                bundle, requestCode);
    }

    @Override
    public void showWellnessDevicesBrowser(Activity activity, String url) {
        navigateToActivityWithExtra(activity,
                SimpleBrowserActivity.class,
                SimpleBrowserActivity.KEY_URL_EXTRA,
                url);
    }

    @Override
    public void showWellnessDevicesWebLinkActivityWithRedirectedResult(Activity activity,
                                                                       String partnerLink,
                                                                       @WellnessDevicesLinkingInteractor.RequestType String requestType) {
        Bundle bundle = new Bundle();
        bundle.putString(WellnessDevicesWebActivity.KEY_REQUEST_TYPE, requestType);
        bundle.putString(WellnessDevicesWebActivity.KEY_PARTNER_LINK, partnerLink);
        navigateToActivityForRedirectedResultWithExtra(activity, WellnessDevicesWebActivity.class,
                WellnessDevicesWebActivity.KEY_WEB_EXTRA, bundle);
    }

    @Override
    public void showWellnessDevicesInfoActivity(Activity activity,
                                                String title,
                                                String articleId) {
        Bundle bundle = new Bundle();
        bundle.putString(WellnessDevicesInfoActivity.KEY_TITLE, title);
        bundle.putString(WellnessDevicesInfoActivity.KEY_ARTICLE_ID, articleId);
        navigateToActivityWithExtra(activity,
                WellnessDevicesInfoActivity.class,
                WellnessDevicesInfoActivity.KEY_BUNDLE,
                bundle);
    }

    @Override
    public void showNonSmokersDeclarationLearnMore(Activity activity) {
        navigateToActivity(activity, NonSmokersDeclarationLearnMoreActivity.class);
    }

    @Override
    public void showScreeningLearnMore(Activity activity) {
        navigateToActivity(activity, ScreeningsLearnMoreActivity.class);
    }


    @Override
    public void showLearnMoreScreeningsList(Activity activity, String actionType) {
        navigateToActivityWithExtra(activity, ScreeningsActivity.class, ScreeningsActivity.KEY, actionType);
    }

    @Override
    public void showLearnMoreScreeningsListDescription(Activity activity, String actionType) {
        navigateToActivityWithExtra(activity, SnvListDescriptionActivity.class, SnvListDescriptionActivity.KEY, actionType);
    }


    @Override
    public void showVHCOnboarding(Activity activity) {
        navigateToActivity(activity, VHCOnboardingActivity.class);
    }

    @Override
    public void showVHCHealthMeasurements(Activity activity, boolean isAfterFormSubmission) {
        navigateToActivityWithExtra(activity, VHCLandingActivity.class, VHCLandingActivity.REFRESH, VHCLandingActivity.MODE_FORM_SUBMITTED, true, isAfterFormSubmission);
    }

    @Override
    public void showVHCCaptureResults(Activity activity) {
        navigateToActivity(activity, VHCCaptureResultsActivity.class);
    }

    @Override
    public void showVHCHistoryActivity(Activity activity) {
        navigateToActivity(activity, VHCHistoryActivity.class);
    }

    @Override
    public void showVHCLearnMore(Activity activity) {
        navigateToActivity(activity, VHCLearnMoreActivity.class);
    }

    @Override
    public void showVHCHelp(Activity activity) {
        navigateToActivity(activity, VHCHelpActivity.class);
    }

    @Override
    public void showVHCAddProof(Activity activity) {
        navigateToActivity(activity, VHCAddProofActivity.class);
    }

    @Override
    public void showVHCSummaryActivity(Activity activity) {
        navigateToActivity(activity, VHCSummaryActivity.class);
    }

    @Override
    public void showSNVSummaryActivity(Activity activity) {
        navigateToActivity(activity, SNVSummaryActivity.class);
    }

    @Override
    public void showVHCPrivacyPolicy(Activity activity) {
        navigateToActivity(activity, VHCPrivacyPolicyActivity.class);
    }

    @Override
    public void showSNVPrivacyPolicy(Activity activity) {
        navigateToActivity(activity, SNVPrivacyPolicyActivity.class);
    }

    @Override
    public void showVHCMeasurementsConfirmed(Activity activity) {
        navigateToActivity(activity, VHCMeasurementsConfirmedActivity.class);
    }

    @Override
    public void showSNVDataConfirmed(Activity activity) {
        navigateToActivity(activity, SNVDataConfirmedActivity.class);
    }

    @Override
    public void showVHCLearnMoreItemDetail(Activity activity, String tag) {
        navigateToActivityWithExtra(activity,
                VHCLearnMoreItemDetailActivity.class,
                VHCLearnMoreItemDetailActivity.LEARN_MORE_ITEM,
                tag);
    }


    public void showLoginAfterLogOut(VitalityActiveApplication vitalityActiveApplication, boolean isSessionExpired) {
        Intent intent = new Intent(vitalityActiveApplication, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(LoginActivity.SESSION_EXPIRED_KEY, isSessionExpired);
        vitalityActiveApplication.startActivity(intent);
    }

    @Override
    public void showHealthAttributeGroupDetail(Activity activity, int healthAttributeGroupFeatureType) {
        navigateToActivityWithExtra(activity,
                HealthAttributeDetailActivity.class,
                HealthAttributeDetailActivity.HEALTH_ATTRIBUTE_GROUP_FEATURE_TYPE,
                healthAttributeGroupFeatureType);
    }

    @Override
    public void showVHRLearnMore(Activity activity) {
        navigateToActivity(activity, VHRLearnMoreActivity.class);
    }

    @Override
    public void showVHRLandingAfterOnboarding(Activity activity) {
        navigateToActivityAndFinish(activity, VHRLandingActivity.class);
    }

    @Override
    public void showVHRQuestionnaire(Activity activity, long typeKey) {
        navigateToActivityWithExtra(activity, VHRQuestionnaireActivity.class,
                QuestionnaireBaseActivity.EXTRA_QUESTIONNAIRE_TYPE_KEY, typeKey);
    }

    @Override
    public void showVHRHelp(Activity activity) {
        navigateToActivityWithExtra(activity, SearchResultsActivity.class, HelpFragment.ITEM_QUERY, "active rewards");
    }

    @Override
    public void showVHRPrivacyPolicy(Activity activity, long questionnaireTypeKey) {
        navigateToActivityWithExtra(activity, VHRPrivacyPolicyActivity.class,
                "questionnaireTypeKey", String.valueOf(questionnaireTypeKey));
    }

    @Override
    public void showVNAPrivacyPolicy(Activity activity, long questionnaireTypeKey) {
        navigateToActivityWithExtra(activity, VNAPrivacyPolicyActivity.class,
                "questionnaireTypeKey", String.valueOf(questionnaireTypeKey));
    }

    @Override
    public void showVHRQuestionnaireCompletedActivity(Activity activity, long questionnaireTypeKey) {
        navigateToActivityWithExtraAndFinish(activity,
                VHRQuestionnaireCompletedActivity.class,
                "questionnaireTypeKey",
                String.valueOf(questionnaireTypeKey));
    }

    @Override
    public void showVHROnboarding(Activity activity) {
        navigateToActivity(activity, VHROnBoardingActivity.class);
    }

    @Override
    public void showSNVOnboarding(Activity activity) {
        navigateToActivity(activity, SNVOnboardingActivity.class);
    }

    @Override
    public void showMWBOnboarding(Activity activity) {
        navigateToActivity(activity, MWBOnBoardingActivity.class);

    }

    @Override
    public void back(Activity activity) {
        activity.finish();
    }

    @Override
    public void showPrivacyStatement(Activity activity) {
        navigateToActivity(activity, PrivacyStatementActivity.class);
    }

    @Override
    public void showVNAOnboarding(Activity activity) {
        navigateToActivity(activity, VNAOnboardingActivity.class);
    }

    @Override
    public void showVNALanding(Activity activity) {
        navigateToActivity(activity, VNALandingActivity.class);
    }

    @Override
    public void showVNALearnMore(Activity activity) {
        navigateToActivity(activity, VNALearnMoreActivity.class);
    }

    @Override
    public void showVNAHelp(Activity activity) {
        navigateToActivity(activity, VNAHelpActivity.class);
    }

    @Override
    public void showVNAQuestionnaire(Activity activity, long typeKey) {
        navigateToActivityWithExtra(activity, VNAQuestionnaireActivity.class,
                QuestionnaireBaseActivity.EXTRA_QUESTIONNAIRE_TYPE_KEY, typeKey);
    }

    @Override
    public void showVNADisclaimer(Activity activity) {
        navigateToActivity(activity, VNADisclaimerActivity.class);
    }

    @Override
    public void showVNAQuestionnaireCompletedActivity(Activity activity, long questionnaireTypeKey) {
        navigateToActivityWithExtraAndFinish(activity,
                VNAQuestionnaireCompletedActivity.class,
                "questionnaireTypeKey",
                String.valueOf(questionnaireTypeKey));
    }

    @Override
    public void showPDF(Activity activity, String fileName, @StringRes int title) {
        Intent intent = getIntent(activity, PDFActivity.class);
        intent.putExtra(PDFActivity.FILE_NAME, fileName);
        intent.putExtra(PDFActivity.TITLE, title);
        activity.startActivity(intent);
    }

    @Override
    public void showVHRDisclaimer(Activity activity) {
        navigateToActivity(activity, VHRDisclaimerActivity.class);
    }

    @Override
    public void showPartnerJourney(Activity activity, PartnerType partnerType) {
        navigateToActivityWithExtra(activity, PartnerListActivity.class,
                BasePartnerActivity.EXTRA_PARTNER_TYPE, partnerType.toString());
    }

    @Override
    public void showPartnerDetail(Activity activity, PartnerType partnerType, long partnerId) {
        navigateToActivityWithExtra(activity, partnerType.detailsActivityClass,
                BasePartnerActivity.EXTRA_PARTNER_TYPE, partnerType.toString(),
                BasePartnerActivity.KEY_PARTNER_ID, partnerId);
    }

    @Override
    public void showVitalityStatusOnboarding(Activity activity) {
        navigateToActivity(activity, VitalityStatusOnboarding.class);
    }

    @Override
    public void showVitalityStatusLanding(Activity activity) {
        navigateToActivity(activity, VitalityStatusLandingActivity.class);
    }

    @Override
    public void showVitalityStatusLearnMore(Activity activity) {
        navigateToActivity(activity, VitalityStatusLearnMoreActivity.class);
    }

    @Override
    public void showVitalityStatusDetail(Activity activity, String key) {
        navigateToActivityWithExtra(activity, VitalityStatusLevelDetailActivity.class, VitalityStatusLevelDetailActivity.PRODUCT_FEATURE_KEY, key);
    }

    @Override
    public void showPointsCategoryFeatures(Activity activity, int key) {
        navigateToActivityWithExtra(activity, VitalityStatusDetailsActivity.class, VitalityStatusLevelDetailActivity.PRODUCT_FEATURE_KEY, key);
    }

    @Override
    public void showPointsFeatureSubfeatures(Activity activity, int key) {
        navigateToActivityWithExtra(activity, VitalityStatusDetailsActivity.class, VitalityStatusLevelDetailActivity.SUBFEATURE_KEY, key);
    }

    @Override
    public void showStatusHowToEarnPoints(Activity activity, int key, String name) {
        Bundle bundle = new Bundle();
        bundle.putInt(VitalityStatusLevelDetailActivity.PRODUCT_FEATURE_KEY, key);
        bundle.putString(VitalityStatusLevelDetailActivity.NAME, name);

        navigateToActivityWithExtra(activity, StatusPointsActivity.class, VitalityStatusLevelDetailActivity.BUNDLE_KEY, bundle);
    }

    @Override
    public void showPointsEarningMetricDetail(Activity activity, String type) {
        navigateToActivityWithExtra(activity, PointsEarningMetricDetail.class, PointsEarningMetricDetail.TYPE, type);
    }

    @Override
    public void showVitalityStatusLevelIncreasedActivity(Activity activity) {
        navigateToActivity(activity, VitalityStatusLevelIncreasedActivity.class);
    }

    @Override
    public void showStatusMyRewards(Activity activity) {
        navigateToActivity(activity, StatusMyRewardsDetailActivity.class);
    }

    @Override
    public void showMyHealthDisclaimer(Activity activity) {
        navigateToActivity(activity, MyHealthDisclaimerActivity.class);
    }

    @Override
    public void showVitalityAge(Activity activity, int vhrMode) {
        navigateToActivityWithExtraAndPreviousBackStack(activity, VitalityAgeActivity.class, VitalityAgeActivity.MODE, vhrMode);
    }

    @Override
    public void showVHRLanding(Activity activity, boolean isFromFormSubmission) {
        navigateToActivityWithExtraAndPreviousBackStack(activity, VHRLandingActivity.class, VHRLandingActivity.MODE_FORM_SUBMITTED, isFromFormSubmission);
    }

    @Override
    public void showMyHealthOnboardingScreen(Activity activity, int resultCode) {
        navigateToActivityForResult(activity, MyHealthOnboardingActivity.class, resultCode);
    }

    @Override
    public void showMyHealthVitalityAgeProfile(Activity activity) {
        navigateToActivity(activity, MyHealthVitalityAgeProfileActivity.class);
    }

    @Override
    public void showMyHealthMoreTips(Activity activity, int sectionTypeKey, int attributeTypeKey) {
        navigateToActivityWithExtra(activity, MyHealthMoreTipsActivity.class, MyHealthMoreTipsActivity.SECTION_TYPE_KEY, sectionTypeKey, MyHealthMoreTipsActivity.ATTRIBUTE_TYPE_KEY, attributeTypeKey);
    }

    @Override
    public void showVitalityAgeCardLearnMore(Activity activity, int effectiveTypeValue, int actualTypeValue, String vitalityAge, String vitalityAgeVariance) {
        navigateToActivityWithExtra(activity, VitalityAgeLearnMoreActivity.class, VitalityAgeLearnMoreActivity.VITALITY_AGE_EFFECTIVE_TYPE, effectiveTypeValue, VitalityAgeLearnMoreActivity.VITALITY_AGE_ACTUAL_TYPE, actualTypeValue, VitalityAgeLearnMoreActivity.VITALITY_AGE_VALUE, vitalityAge, VitalityAgeLearnMoreActivity.VITALITY_AGE_VARIANCE, vitalityAgeVariance);
    }

    @Override
    public void showChangedPasswordActivity(Activity activity){
        navigateToActivity(activity, PasswordChangedActivity.class);
        activity.finish();
    }

    @Override
    public void showChangePasswordAfterDone(Activity activity) {
        navigateToActivityClearTop(activity, SecurityPreferencesActivity.class);
    }

    @Override
    public void showScreeningsAndVaccinationsOnboarding(Activity activity) {
        navigateToActivity(activity, ScreeningsAndVaccinationsOnboardingActivity.class);
    }

    @Override
    public void showMentalWellbeingOnboarding(Activity activity) {

    }

    @Override
    public void showHealthActions(Activity activity, String actionType) {
        navigateToActivityWithExtra(activity, HealthActionsActivity.class, HealthActionsActivity.KEY, actionType);
    }

    @Override
    public void showConfirmAndSubmitSNV(Activity activity) {
        navigateToActivity(activity, ConfirmAndSubmitActivity.class);
    }

    @Override
    public void showAddProofActivitySNV(Activity activity) {
        navigateToActivity(activity, SNVAddProofActivity.class);
    }

    @Override
    public void showScreeningAndVaccinationHistory(Activity activity) {
        Intent intent = getIntent(activity, SNVHistoryActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void showScreeningAndVaccinationHistoryDetail(Activity activity, String dateString, String dateMessage) {
        Intent intent = new Intent(activity, SNVHistoryDetailActivity.class);
        intent.putExtra(SNVHistoryDetailActivity.DATE_STRING, dateString);
        intent.putExtra(SNVHistoryDetailActivity.DATE_MESSAGE, dateMessage);
        activity.startActivity(intent);
    }

    @Override
    public void showHelpScreen(Activity activity) {
        navigateToActivity(activity, ScreeningsAndVaccinationsHelp.class);
    }

    @NonNull
    private Intent getIntent(Activity activity, Class<?> activityClass) {
        return new Intent(activity, activityClass);
    }

    protected void navigateToActivity(Activity activity, Class<?> activityClass) {
        Intent intent = getIntent(activity, activityClass);
        activity.startActivity(intent);
    }

    @SuppressWarnings("unused")     // protected, usable in other flavors
    protected void navigateToActivity(Activity activity, Class<?> activityClass, boolean finishCurrentActivity,
                                      String... keyValues) {
        navigateToActivity(activity, activityClass, finishCurrentActivity, null, null, keyValues);
    }

    @SuppressWarnings("unused")     // protected, usable in other flavors
    protected void navigateToActivity(Activity activity, Class<?> activityClass, boolean finishCurrentActivity,
                                      Parcelable parcelable,
                                      String... keyValues) {
        navigateToActivity(activity, activityClass, finishCurrentActivity, null, parcelable, keyValues);
    }

    @SuppressWarnings("unused")     // protected, usable in other flavors
    protected void navigateToActivity(Activity activity, Class<?> activityClass, boolean finishCurrentActivity,
                                      Uri data,
                                      String... keyValues) {
        navigateToActivity(activity, activityClass, finishCurrentActivity, data, null, keyValues);
    }

    protected void navigateToActivity(Activity activity, Class<?> activityClass, boolean finishCurrentActivity,
                                      @Nullable Uri data, @Nullable Parcelable parcelable,
                                      String... keyValues) {
        if (keyValues.length % 2 == 1) {
            throw new RuntimeException("expected keyValues to have a length dividable by 2");
        }

        Intent intent = getIntent(activity, activityClass);
        intent.setData(data);
        addNullableParcelableToIntent(parcelable, intent);
        addExtrasToIntent(intent, keyValues);
        activity.startActivity(intent);

        if (finishCurrentActivity) {
            activity.finish();
        }
    }

    private void addNullableParcelableToIntent(@Nullable Parcelable parcelable, Intent intent) {
        if (parcelable != null) {
            intent.putExtra(parcelable.getClass().getSimpleName(), parcelable);
        }
    }

    private void addExtrasToIntent(Intent intent, String[] keyValues) {
        for (int i = 0; i < keyValues.length; i += 2) {
            intent.putExtra(keyValues[i], keyValues[i + 1]);
        }
    }

    private void navigateToActivityWithExtra(Activity activity, Class<?> activityClass, String key, long value) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key, value);
        activity.startActivity(intent);
    }

    private void navigateToActivityWithExtra(Activity activity, Class<?> activityClass, String key, boolean value) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key, value);
        activity.startActivity(intent);
    }

    private void navigateToActivityWithExtra(Activity activity, Class<?> activityClass, String key1, String key2, boolean value1, boolean value2) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key1, value1);
        intent.putExtra(key2, value2);
        activity.startActivity(intent);
    }

    private void navigateToActivityWithExtra(Activity activity, Class<?> activityClass, String key1, String value1, String key2, long value2) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key1, value1);
        intent.putExtra(key2, value2);
        activity.startActivity(intent);
    }

    private void navigateToActivityWithExtra(Activity activity, Class<?> activityClass, String key1, int value1, String key2, String value2) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key1, value1);
        intent.putExtra(key2, value2);
        activity.startActivity(intent);
    }

    private void navigateToActivityWithExtra(Activity activity, Class<?> activityClass, String key, int value) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key, value);
        activity.startActivity(intent);
    }

    private void navigateToActivityWithExtra(Activity activity, Class<?> activityClass, String key1, int value1, String key2, int value2) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key1, value1);
        intent.putExtra(key2, value2);
        activity.startActivity(intent);
    }

    private void navigateToActivityWithExtra(Activity activity,
                                             Class<?> activityClass,
                                             String key,
                                             String value) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key, value);
        activity.startActivity(intent);
    }

    private void navigateToActivityWithExtra(Activity activity,
                                             Class<?> activityClass,
                                             String key,
                                             Parcelable value) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key, value);
        activity.startActivity(intent);
    }

    private void navigateToActivityAndFinish(Activity activity,
                                             Class<?> destinationClass) {
        navigateToActivity(activity, destinationClass);
        activity.finish();
    }

    private void navigateToActivityWithExtraAndFinish(Activity activity,
                                                      Class<?> activityClass,
                                                      String key,
                                                      String value) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key, value);
        navigateToActivityWithExtra(activity, activityClass, key, value);
        activity.finish();
    }

    private void navigateToActivityWithExtraAndFinish(Activity activity,
                                                      Class<?> activityClass,
                                                      String key,
                                                      long value) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key, value);
        navigateToActivityWithExtra(activity, activityClass, key, value);
        activity.finish();
    }

    private void navigateToActivityWithExtraAndFinish(Activity activity,
                                                      Class<?> activityClass,
                                                      String key,
                                                      boolean value) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key, value);
        navigateToActivityWithExtra(activity, activityClass, key, value);
        activity.finish();
    }

    protected void navigateToActivityInNewTask(Activity origin,
                                               Class<?> destinationClass) {
        Intent intent = getIntent(origin, destinationClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        origin.startActivity(intent);
    }

    protected void navigateToActivityInNewTask(Activity origin,
                                               Class<?> destinationClass,
                                               String key, String value) {
        Intent intent = getIntent(origin, destinationClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(key, value);
        origin.startActivity(intent);
    }

    protected void navigateToActivityInNewTask(Activity origin,
                                               Class<?> destinationClass,
                                               Bundle extras) {
        Intent intent = getIntent(origin, destinationClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(extras);
        origin.startActivity(intent);
    }

    private void navigateToActivityClearTop(Activity origin,
                                            Class<?> destinationClass) {
        Intent intent = getIntent(origin, destinationClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        origin.startActivity(intent);
    }

    private void navigateToActivityWithNewBackStack(Activity activity,
                                                    Class<?> newActivityClass) {
        Intent intent = getIntent(activity, newActivityClass);

        TaskStackBuilder.create(activity)
                .addNextIntentWithParentStack(intent)
                .startActivities();
    }

    private void navigateToActivityForResult(Activity activity,
                                             Class<?> activityClass,
                                             int requestCode) {
        Intent intent = getIntent(activity, activityClass);
        activity.startActivityForResult(intent, requestCode);
    }

    private void navigateToActivityForResultWithExtra(Activity activity,
                                                      Class<?> activityClass,
                                                      String key,
                                                      Bundle value,
                                                      int requestCode) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key, value);
        activity.startActivityForResult(intent, requestCode);
    }

    private void navigateToActivityForRedirectedResultWithExtra(Activity activity,
                                                                Class<?> activityClass,
                                                                String key,
                                                                Bundle value) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key, value);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        activity.startActivity(intent);
    }

    private void navigateToActivityWithExtraAndPreviousBackStack(Activity activity, Class<?> newActivityClass, String key, int value) {
        Intent intent = getIntent(activity, newActivityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(key, value);
        activity.startActivity(intent);
    }

    private void navigateToActivityWithExtraAndPreviousBackStack(Activity activity, Class<?> newActivityClass, String key, boolean value) {
        Intent intent = getIntent(activity, newActivityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(key, value);
        activity.startActivity(intent);
    }

    private void navigateToActivityWithExtra(Activity activity, Class<?> activityClass, String key1, int value1, String key2, int value2, String key3, String value3, String key4, String value4) {
        Intent intent = getIntent(activity, activityClass);
        intent.putExtra(key1, value1);
        intent.putExtra(key2, value2);
        intent.putExtra(key3, value3);
        intent.putExtra(key4, value4);
        activity.startActivity(intent);
    }

    public void showMembershipPassActivity(Activity activity) {
        navigateToActivity(activity, MembershipPassActivity.class);
    }

    @Override
    public void showProfile(Activity activity) {
        navigateToActivity(activity, ProfileActivity.class);
    }

    @Override
    public void showSettings(Activity activity) {
        navigateToActivity(activity, SettingsActivity.class);
    }

    @Override
    public void showMentalWellBeing(Activity activity,boolean isFromFormSubmission) {
        navigateToActivityWithExtraAndPreviousBackStack(activity,MWBLandingActivity.class,MWBLandingActivity.MODE_FORM_SUBMITTED,isFromFormSubmission);
    }

    public void showSnvParticipatingPartnersActivity(Activity activity) {
        navigateToActivity(activity, SnvParticipatingPartnersActivity.class);
    }

    public void showSnvParticipatingPartnersDetailsActivity(Activity activity, ProductFeatureGroupDto snvParticipatingPartnersItem) {
        Intent intent = getIntent(activity, SnvParticipatingPartnersDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString(SnvParticipatingPartnersDetailActivity.DESC_KEY, snvParticipatingPartnersItem.getLongDescription());
        extras.putString(SnvParticipatingPartnersDetailActivity.NAME_KEY, snvParticipatingPartnersItem.getName());
        intent.putExtras(extras);
        activity.startActivity(intent);
    }

    public void showMWBLearnMore(Activity activity){
        navigateToActivity(activity, MWBLearnMoreActivity.class);
    }
    @Override
    public void showPartnerTermsAndConditions(Activity activity, String articleId) {
        navigateToActivityWithExtra(activity, PartnerTermsAndConditionsActivity.class,
                PartnerTermsAndConditionsActivity.TERMS_AND_CONDITIONS_ARTICLE_ID, articleId);
    }
}
