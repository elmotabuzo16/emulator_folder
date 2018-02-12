package com.vitalityactive.va.dependencyinjection;

import com.vitalityactive.va.vhc.VHCPrivacyPolicyActivity;
import com.vitalityactive.va.vhc.VHCSummaryActivity;
import com.vitalityactive.va.vhc.addproof.VHCAddProofActivity;
import com.vitalityactive.va.vhc.addproof.VHCProofItemDetailActivity;
import com.vitalityactive.va.vhc.captureresults.BaseVHCCaptureResultsActivity;

import dagger.Subcomponent;

@VHCCaptureScope
@Subcomponent(modules = {VHCCaptureModule.class})
public interface VHCCaptureDependencyInjector {
    void inject(VHCAddProofActivity activity);
    void inject(VHCProofItemDetailActivity activity);
    void inject(VHCPrivacyPolicyActivity activity);
    void inject(VHCSummaryActivity activity);

    void inject(BaseVHCCaptureResultsActivity activity);
}
