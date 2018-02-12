package com.vitalityactive.va.nonsmokersdeclaration.onboarding;

import android.view.View;

import com.vitalityactive.va.R;

public class NonSmokersDeclarationLearnMoreActivity extends BaseNonSmokersDeclarationLearnMoreActivity {
    @Override
    protected ButtonConfigurations getButtonConfigurations() {
        ButtonConfigurations configurations = super.getButtonConfigurations();
        configurations.button1 = new ButtonConfiguration(R.string.participating_partners_title_262, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationCoordinator.navigateOnParticipatingPartnersFromNonSmokersLearnMore(NonSmokersDeclarationLearnMoreActivity.this);
            }
        });
        return configurations;
    }
}
