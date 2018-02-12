package com.vitalityactive.va.home.activerewardsrewardscard;

class RewardsExpiryDTO {

    final int spinCount;
    final int expiryCount;

    RewardsExpiryDTO() {
        this(0, 0);
    }

    RewardsExpiryDTO(int spinCount, int expiryCount) {
        this.spinCount = spinCount;
        this.expiryCount = expiryCount;
    }

}
