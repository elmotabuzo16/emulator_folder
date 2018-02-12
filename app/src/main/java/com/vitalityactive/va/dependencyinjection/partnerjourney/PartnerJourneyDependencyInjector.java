package com.vitalityactive.va.dependencyinjection.partnerjourney;

import com.vitalityactive.va.partnerjourney.PartnerDetailsActivity;
import com.vitalityactive.va.partnerjourney.PartnerListActivity;
import com.vitalityactive.va.partnerjourney.PartnerTermsAndConditionsActivity;

import dagger.Subcomponent;

@PartnerJourneyScope
@Subcomponent(modules = {PartnerJourneyModule.class})
public interface PartnerJourneyDependencyInjector {
    void inject(PartnerDetailsActivity activity);

    void inject(PartnerListActivity activity);

    void inject(PartnerTermsAndConditionsActivity activity);
}
