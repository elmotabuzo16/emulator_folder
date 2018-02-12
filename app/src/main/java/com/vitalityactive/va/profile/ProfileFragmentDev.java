package com.vitalityactive.va.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.BaseFragment;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.R;
import com.vitalityactive.va.nonsmokersdeclaration.onboarding.NonSmokersDeclarationOnboardingActivity;
import com.vitalityactive.va.partnerjourney.PartnerType;
import com.vitalityactive.va.pushnotification.InAppPreferences;
import com.vitalityactive.va.vhc.VHCOnboardingActivity;
import com.vitalityactive.va.vhc.landing.VHCLandingActivity;
import com.vitalityactive.va.vhr.VHROnBoardingActivity;
import com.vitalityactive.va.vitalitystatus.onboarding.VitalityStatusOnboarding;
import com.vitalityactive.va.vna.onboarding.VNAOnboardingActivity;
import com.vitalityactive.va.wellnessdevices.onboarding.WellnessDevicesOnboardingActivity;

import javax.inject.Inject;

public class ProfileFragmentDev extends BaseFragment {
    @Inject
    DeviceSpecificPreferences deviceSpecificPreferences;
    @Inject
    InAppPreferences inAppPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_dev, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDependencyInjector().inject(this);

        if (getView() == null) {
            return;
        }

        setToolbarDrawerIconColourToWhite();

        getView().findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogout();
                inAppPreferences.clearAll();
            }
        });

        getView().findViewById(R.id.clear_and_logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogout();
                deviceSpecificPreferences.clearAll();
                inAppPreferences.clearAll();
            }
        });

        getView().findViewById(R.id.active_rewards).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationCoordinator.navigateAfterActiveRewardsSelected(getActivity());
            }
        });

        getView().findViewById(R.id.first_time_preferences).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFirstTimePreferencesClicked();
            }
        });

        getView().findViewById(R.id.vhc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VHCLandingActivity.class));
            }
        });

        getView().findViewById(R.id.non_smokers_declaration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NonSmokersDeclarationOnboardingActivity.class));
            }
        });

        getView().findViewById(R.id.vhc_onboarding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VHCOnboardingActivity.class));
            }
        });

        getView().findViewById(R.id.vhr_button_onboarding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VHROnBoardingActivity.class));
            }
        });

        getView().findViewById(R.id.vhr_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationCoordinator.navigateAfterVHRCardTapped(getActivity());
            }
        });

        getView().findViewById(R.id.btn_wellness_devices_onboarding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WellnessDevicesOnboardingActivity.class));
            }
        });

        getView().findViewById(R.id.btn_wellness_devices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationCoordinator.navigateAfterWellnessDevicesTapped(getActivity());
            }
        });

        getView().findViewById(R.id.btn_vna_onboarding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VNAOnboardingActivity.class));
            }
        });

        getView().findViewById(R.id.btn_vna).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationCoordinator.navigateAfterVNACardTapped(getActivity());
            }
        });

        getView().findViewById(R.id.btn_partner_journey_rewards).setOnClickListener(getPartnerJourneyListener());
        getView().findViewById(R.id.btn_partner_journey_wellness).setOnClickListener(getPartnerJourneyListener());
        getView().findViewById(R.id.btn_partner_journey_health).setOnClickListener(getPartnerJourneyListener());

        getView().findViewById(R.id.btn_vitality_status_onboarding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VitalityStatusOnboarding.class));
            }
        });

        getView().findViewById(R.id.btn_vitality_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationCoordinator.navigateAfterVitalityStatusTapped(getActivity());
            }
        });
    }

    @NonNull
    private View.OnClickListener getPartnerJourneyListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_partner_journey_rewards:
                        navigationCoordinator.navigateAfterPartnerHomeCardTapped(getActivity(), PartnerType.REWARDS);
                        break;
                    case R.id.btn_partner_journey_wellness:
                        navigationCoordinator.navigateAfterPartnerHomeCardTapped(getActivity(), PartnerType.WELLNESS);
                        break;
                    case R.id.btn_partner_journey_health:
                        navigationCoordinator.navigateAfterPartnerHomeCardTapped(getActivity(), PartnerType.HEALTH);
                        break;
                }
            }
        };
    }

    private void onFirstTimePreferencesClicked() {
        navigationCoordinator.navigateFromHomeOnFirstTimePreferences(getActivity());
    }

    public void onLogout() {
        navigationCoordinator.logOut();
    }
}
