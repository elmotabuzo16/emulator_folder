package com.vitalityactive.va.termsandconditions;

public interface GeneralTermsAndConditionsInstructionRepository {
    long getInstructionId();

    boolean hasInstruction();

    void removeInstruction();
}
