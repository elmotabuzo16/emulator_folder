package com.vitalityactive.va.dependencyinjection;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.BaseFragment;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.VitalityActiveGlideModule;
import com.vitalityactive.va.activerewards.ActiveRewardsLearnMoreActivity;
import com.vitalityactive.va.activerewards.BaseActiveRewardsLearnMoreActivity;
import com.vitalityactive.va.activerewards.history.GoalHistoryActivity;
import com.vitalityactive.va.activerewards.history.detailedscreen.HistoryWeekDetailsActivity;
import com.vitalityactive.va.activerewards.onboarding.ActiveRewardsOnboardingActivity;
import com.vitalityactive.va.activerewards.rewards.RewardsListActivity;
import com.vitalityactive.va.activerewards.termsandconditions.ActiveRewardsMedicallyFitAgreementActivity;
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
import com.vitalityactive.va.eventsfeed.views.EventsFeedActivity;
import com.vitalityactive.va.eventsfeed.views.fragments.EventsFeedFragment;
import com.vitalityactive.va.eventsfeed.views.fragments.EventsFeedMonthFragment;
import com.vitalityactive.va.feedback.BaseFeedbackFragment;
import com.vitalityactive.va.feedback.FeedbackActivity;
import com.vitalityactive.va.forgotpassword.ForgotPasswordActivity;
import com.vitalityactive.va.help.HelpFragment;
import com.vitalityactive.va.home.BaseHomeActivity;
import com.vitalityactive.va.home.HomeFragment;
import com.vitalityactive.va.home.activerewardcard.ActiveRewardsCardFragment;
import com.vitalityactive.va.home.activerewardsrewardscard.BaseRewardsVoucherCardFragment;
import com.vitalityactive.va.home.activerewardsrewardscard.RewardsCardFragment;
import com.vitalityactive.va.home.activerewardsrewardscard.RewardsSelectionCardFragment;
import com.vitalityactive.va.home.activerewardsrewardscard.RewardsVoucherCardFragment;
import com.vitalityactive.va.home.mwb.MWBCardFragment;
import com.vitalityactive.va.home.nonsmokerscard.NonSmokersCardFragment;
import com.vitalityactive.va.home.snv.SNVCardFragment;
import com.vitalityactive.va.home.vhc.VHCCardFragment;
import com.vitalityactive.va.home.vhr.VHRCardFragment;
import com.vitalityactive.va.home.vna.VNACardFragment;
import com.vitalityactive.va.login.FingerprintAlertDialogFragment;
import com.vitalityactive.va.login.LoginActivity;
import com.vitalityactive.va.login.LoginFragment;
import com.vitalityactive.va.login.LoginFragmentBase;
import com.vitalityactive.va.login.LoginPresenterImpl;
import com.vitalityactive.va.membershippass.BaseMembershipPassFragment;
import com.vitalityactive.va.membershippass.MembershipPassActivity;
import com.vitalityactive.va.membershippass.MembershipPassHelpActivity;
import com.vitalityactive.va.membershippass.VitalityNumberActivity;
import com.vitalityactive.va.mwb.MWBOnBoardingActivity;
import com.vitalityactive.va.mwb.landing.MWBLandingActivity;
import com.vitalityactive.va.mwb.learnmore.BaseMWBLearnMoreActivity;
import com.vitalityactive.va.myhealth.healthattributes.MyHealthAttributeDetailActivity;
import com.vitalityactive.va.myhealth.landing.MyHealthLandingFragment;
import com.vitalityactive.va.myhealth.learnmore.VitalityAgeLearnMoreActivity;
import com.vitalityactive.va.myhealth.moretips.MyHealthMoreTipsActivity;
import com.vitalityactive.va.myhealth.onboarding.MyHealthOnboardingActivity;
import com.vitalityactive.va.myhealth.tipdetail.MyHealthTipDetailActivity;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgeActivity;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthTipsMoreResultsActivity;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthVitalityAgeProfileActivity;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersDeclareActivity;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersPrivacyPolicyActivity;
import com.vitalityactive.va.nonsmokersdeclaration.onboarding.BaseNonSmokersDeclarationLearnMoreActivity;
import com.vitalityactive.va.nonsmokersdeclaration.onboarding.NonSmokersDeclarationOnboardingActivity;
import com.vitalityactive.va.partnerjourney.home.PartnerCardFragment;
import com.vitalityactive.va.pointsmonitor.PointsMonitorEntryDetailActivity;
import com.vitalityactive.va.pointsmonitor.PointsMonitorFragment;
import com.vitalityactive.va.pointsmonitor.PointsMonitorMonthFragment;
import com.vitalityactive.va.profile.BaseChangeEmailFragment;
import com.vitalityactive.va.profile.BasePersonalDetailsActivity;
import com.vitalityactive.va.profile.BasePersonalDetailsFragment;
import com.vitalityactive.va.profile.BaseProfileFragment;
import com.vitalityactive.va.profile.ProfileActivity;
import com.vitalityactive.va.profile.ProfileFragmentDev;
import com.vitalityactive.va.pushnotification.PushNotificationBroadcaster;
import com.vitalityactive.va.register.view.RegisterActivity;
import com.vitalityactive.va.search.SearchResultsActivity;
import com.vitalityactive.va.settings.SettingsActivity;
import com.vitalityactive.va.shared.activities.BaseTermsAndConditionsWithoutAgreeButtonBarActivity;
import com.vitalityactive.va.snv.SNVOnboardingActivity;
import com.vitalityactive.va.snv.confirmandsubmit.view.ConfirmAndSubmitActivity;
import com.vitalityactive.va.snv.confirmandsubmit.view.SNVAddProofActivity;
import com.vitalityactive.va.snv.confirmandsubmit.view.SNVPrivacyPolicyActivity;
import com.vitalityactive.va.snv.confirmandsubmit.view.SNVProofItemDetailActivity;
import com.vitalityactive.va.snv.confirmandsubmit.view.SNVSummaryActivity;
import com.vitalityactive.va.snv.history.views.SNVHistoryActivity;
import com.vitalityactive.va.snv.history.views.SNVHistoryDetailActivity;
import com.vitalityactive.va.snv.learnmore.ScreeningsActivity;
import com.vitalityactive.va.snv.learnmore.ScreeningsLearnMoreActivity;
import com.vitalityactive.va.snv.learnmore.SnvListDescriptionActivity;
import com.vitalityactive.va.snv.onboarding.BaseScreeningsAndVaccinationsOnboardingActivity;
import com.vitalityactive.va.snv.onboarding.ScreeningsAndVaccinationsHelp;
import com.vitalityactive.va.snv.onboarding.healthactions.HealthActionsActivity;
import com.vitalityactive.va.snv.partners.SnvParticipatingPartnersActivity;
import com.vitalityactive.va.snv.partners.SnvParticipatingPartnersDetailActivity;
import com.vitalityactive.va.splashscreen.SplashScreenActivity;
import com.vitalityactive.va.termsandconditions.TermAndConditionsWithoutAgreeButtonBarActivity;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsActivity;
import com.vitalityactive.va.uicomponents.LoadingIndicatorFragment;
import com.vitalityactive.va.userpreferences.PrivacyStatementActivity;
import com.vitalityactive.va.utilities.DataStoreClearer;
import com.vitalityactive.va.vhc.PDFActivity;
import com.vitalityactive.va.vhc.VHCMeasurementsConfirmedActivity;
import com.vitalityactive.va.vhc.VHCOnboardingActivity;
import com.vitalityactive.va.vhc.detail.HealthAttributeDetailActivity;
import com.vitalityactive.va.vhc.landing.BaseVHCHealthMeasurementsPresenterImpl;
import com.vitalityactive.va.vhc.landing.VHCLandingActivity;
import com.vitalityactive.va.vhc.learnmore.BaseVHCLearnMoreActivity;
import com.vitalityactive.va.vhc.learnmore.VHCLearnMoreItemDetailActivity;
import com.vitalityactive.va.vhr.VHRLearnMoreActivity;
import com.vitalityactive.va.vhr.VHROnBoardingActivity;
import com.vitalityactive.va.vhr.disclaimer.VHRDisclaimerActivity;
import com.vitalityactive.va.vhr.landing.VHRLandingActivity;
import com.vitalityactive.va.vhr.questions.VHRQuestionnaireCompletedActivity;
import com.vitalityactive.va.vitalitystatus.VitalityStatusLevelIncreasedActivity;
import com.vitalityactive.va.vitalitystatus.detail.VitalityStatusLevelDetailActivity;
import com.vitalityactive.va.vitalitystatus.earningpoints.StatusPointsActivity;
import com.vitalityactive.va.vitalitystatus.earningpoints.VitalityStatusDetailsActivity;
import com.vitalityactive.va.vitalitystatus.landing.VitalityStatusLandingActivity;
import com.vitalityactive.va.vitalitystatus.onboarding.VitalityStatusOnboarding;
import com.vitalityactive.va.vna.VNALearnMoreActivity;
import com.vitalityactive.va.vna.VNAQuestionnaireCompletedActivity;
import com.vitalityactive.va.vna.disclaimer.VNADisclaimerActivity;
import com.vitalityactive.va.vna.help.VNAHelpActivity;
import com.vitalityactive.va.vna.landing.VNALandingActivity;
import com.vitalityactive.va.vna.onboarding.VNAOnboardingActivity;
import com.vitalityactive.va.wellnessdevices.WellnessDevicesLearnMoreActivity;
import com.vitalityactive.va.wellnessdevices.informative.WellnessDevicesInfoActivity;
import com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingActivity;
import com.vitalityactive.va.wellnessdevices.linking.web.WellnessDevicesWebActivity;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.WellnessDevicesPointsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, PersistenceModule.class, DefaultModule.class, FlavorSpecificModule.class})
public interface DependencyInjector extends FlavorDependencyInjector {
    void inject(VitalityActiveApplication vitalityActiveApplication);

    void inject(VitalityActiveGlideModule module);

    void inject(LoginFragmentBase loginFragment);

    void inject(LoginFragment loginFragment);

    void inject(BaseActivity baseActivity);

    void inject(BaseTermsAndConditionsWithoutAgreeButtonBarActivity activity);

    void inject(RegisterActivity registerActivity);

    void inject(TermsAndConditionsActivity termsAndConditionsActivity);

    void inject(TermAndConditionsWithoutAgreeButtonBarActivity termAndConditionsWithoutAgreeButtonBarActivity);

    void inject(GoalHistoryActivity activity);

    void inject(ActiveRewardsMedicallyFitAgreementActivity activeRewardsMedicallyFitAgreementActivity);

    void inject(RewardsListActivity activity);

    void inject(ActiveRewardsLearnMoreActivity activity);

    void inject(BaseHomeActivity activity);

    void inject(BaseFragment fragment);

    void inject(ProfileFragmentDev fragment);

    void inject(HomeFragment fragment);

    void inject(PointsMonitorFragment fragment);

    void inject(PointsMonitorMonthFragment fragment);

    void inject(ActiveRewardsCardFragment fragment);

    void inject(NonSmokersCardFragment fragment);

    void inject(VHCCardFragment fragment);

    void inject(VHRCardFragment fragment);

    void inject(VNACardFragment fragment);

    void inject(SNVCardFragment fragment);

    void inject(SNVOnboardingActivity fragment);

    void inject(MWBCardFragment fragment);

    void inject(MWBOnBoardingActivity fragment);

    void inject(PartnerCardFragment fragment);

    void inject(NonSmokersDeclarationOnboardingActivity activity);

    void inject(BaseNonSmokersDeclarationLearnMoreActivity activity);

    void inject(NonSmokersDeclareActivity activity);

    void inject(ScreeningsLearnMoreActivity activity);

    void inject(NonSmokersPrivacyPolicyActivity activity);

    void inject(LoginPresenterImpl loginPresenterImpl);

    void inject(PointsMonitorEntryDetailActivity activity);

    void inject(SplashScreenActivity activity);

    void inject(LoginActivity activity);

    void inject(ForgotPasswordActivity activity);

    void inject(BaseVHCLearnMoreActivity activity);

    void inject(VHCLearnMoreItemDetailActivity activity);

    void inject(VHCOnboardingActivity activity);

    void inject(VHCLandingActivity activity);

    void inject(BaseVHCHealthMeasurementsPresenterImpl vhcHealthMeasurementsPresenter);

    void inject(HealthAttributeDetailActivity activity);

    void inject(VHRLearnMoreActivity activity);

    void inject(VNALearnMoreActivity activity);

    void inject(VHROnBoardingActivity activity);

    void inject(VHRLandingActivity activity);

    void inject(VHRQuestionnaireCompletedActivity activity);

    void inject(PrivacyStatementActivity activity);

    void inject(VNADisclaimerActivity activity);

    void inject(WellnessDevicesLandingActivity activity);

    void inject(WellnessDevicesLearnMoreActivity activity);

    void inject(WellnessDevicesWebActivity activity);

    void inject(WellnessDevicesPointsActivity activity);

    void inject(WellnessDevicesInfoActivity activity);

    void inject(VNAOnboardingActivity activity);

    void inject(VNALandingActivity activity);

    void inject(VNAQuestionnaireCompletedActivity activity);

    void inject(LoadingIndicatorFragment fragment);

    void inject(DataStoreClearer dataStoreClearer);

    void inject(PDFActivity activity);

    void inject(VHRDisclaimerActivity activity);

    void inject(VitalityAgeActivity activity);

    void inject(MyHealthVitalityAgeProfileActivity activity);

    void inject(VHCMeasurementsConfirmedActivity activity);

    void inject(VitalityStatusOnboarding activity);

    void inject(VitalityStatusLandingActivity activity);

    void inject(VitalityStatusLevelDetailActivity activity);

    void inject(VitalityStatusDetailsActivity activity);

    void inject(VitalityAgeLearnMoreActivity activity);

    void inject(MyHealthLandingFragment fragment);

    void inject(MyHealthOnboardingActivity activity);

    void inject(VitalityStatusLevelIncreasedActivity activity);

    void inject(ActiveRewardsOnboardingActivity activity);

    void inject(HistoryWeekDetailsActivity activity);

    void inject(StatusPointsActivity activity);

    void inject(BaseProfileFragment fragment);

    void inject(BaseChangeEmailFragment fragment);

    void inject(BasePersonalDetailsActivity fragment);

    void inject(BasePersonalDetailsFragment fragment);

    void inject(MembershipPassActivity activity);

    void inject(BaseMembershipPassFragment fragment);

    void inject(VitalityNumberActivity activity);

    void inject(MembershipPassHelpActivity activity);

    void inject(RewardsCardFragment fragment);

    void inject(BaseRewardsVoucherCardFragment fragment);

    void inject(RewardsVoucherCardFragment fragment);

    void inject(MyHealthTipsMoreResultsActivity activity);

    void inject(BaseScreeningsAndVaccinationsOnboardingActivity activity);

    void inject(HealthActionsActivity activity);

    void inject(EventsFeedActivity activity);

    void inject(EventsFeedFragment fragment);

    void inject(EventsFeedMonthFragment fragment);

    void inject(RewardsSelectionCardFragment fragment);

    void inject(ConfirmAndSubmitActivity activity);

    void inject(MyHealthAttributeDetailActivity activity);

    void inject(SNVAddProofActivity activity);

    void inject(SNVProofItemDetailActivity activity);

    void inject(SNVSummaryActivity activity);

    void inject(SNVPrivacyPolicyActivity activity);

    void inject(MyHealthTipDetailActivity activity);

    void inject(MyHealthMoreTipsActivity activity);

    void inject(HelpFragment fragment);

    void inject(SearchResultsActivity activity);

    void inject(FeedbackActivity activity);

    void inject(ScreeningsActivity activity);

    void inject(SnvListDescriptionActivity activity);

    void inject(SnvParticipatingPartnersActivity activity);

    void inject(SnvParticipatingPartnersDetailActivity activity);

    void inject(SNVHistoryActivity activity);

    void inject(SNVHistoryDetailActivity activity);

    void inject(ScreeningsAndVaccinationsHelp activity);

    void inject(ProfileActivity activity);

    void inject(SettingsActivity activity);

    void inject(FingerprintAlertDialogFragment fragment);

    void inject(BaseActiveRewardsLearnMoreActivity baseActiveRewardsLearnMoreActivity);

    void inject(BaseFeedbackFragment fragment);

    void inject(PushNotificationBroadcaster pushNotificationBroadcaster);

    void inject(MWBLandingActivity activity);

    void inject(BaseMWBLearnMoreActivity activity);

    void inject(VNAHelpActivity activity);

    //void inject(MWBQuestionnaireCompletedActivity activity);

    EventDispatcher eventDispatcher();

    DeviceSpecificPreferences preferences();

    VHCCaptureDependencyInjector createDependencyInjector(VHCCaptureModule vhcCaptureModule);

    PNSCaptureDependencyInjector createDependencyInjector(PNSCaptureModule pnsCaptureModule);

    VHRCaptureDependencyInjector createDependencyInjector(VHRCaptureModule vhrCaptureModule);

    VNACaptureDependencyInjector createDependencyInjector(VNACaptureModule vnaCaptureModule);

    WDALinkingDependencyInjector createDependencyInjector(WDALinkingModule wdaLinkingModule);

    MyHealthDependencyInjector createDependencyInjector(MyHealthModule myHealthModule);

    PartnerJourneyDependencyInjector createDependencyInjector(PartnerJourneyModule module);

    ActiveRewardsDependencyInjector createDependencyInjector(ActiveRewardsModule activeRewardsModule);

    MWBCaptureDependencyInjector createDependencyInjector(MWBCaptureModule mwbCaptureModule);
}
