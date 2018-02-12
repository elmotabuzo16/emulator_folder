package com.vitalityactive.va.nonsmokersdeclaration.onboarding;

public class NonSmokersDeclarationLearnMoreActivity extends BaseNonSmokersDeclarationLearnMoreActivity {
    @Override
    protected boolean shouldHaveCurrentSmokerItem() {
        // not in Sumitomo build
        return false;
    }
}
