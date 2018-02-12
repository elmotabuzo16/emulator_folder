package com.vitalityactive.va.activerewards.participatingpartners.presenters;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.dto.PartnerItemDTO;

@ActiveRewardsScope
public interface ParticipatingPartnerDetailPresenter extends Presenter<ParticipatingPartnerDetailPresenter.UI> {
    void setPartnerId(long partnerId);

    String getPartnerName();

    interface UI {
        void showPartnerDetails(PartnerItemDTO partnerItem);
    }
}
