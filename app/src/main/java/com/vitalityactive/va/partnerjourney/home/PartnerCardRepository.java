package com.vitalityactive.va.partnerjourney.home;

import android.support.annotation.NonNull;

import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.home.repository.BaseHomeCardRepository;
import com.vitalityactive.va.partnerjourney.PartnerType;

public interface PartnerCardRepository extends BaseHomeCardRepository {
    @NonNull
    HomeCardDTO getHomeCard(PartnerType partnerType);
}
