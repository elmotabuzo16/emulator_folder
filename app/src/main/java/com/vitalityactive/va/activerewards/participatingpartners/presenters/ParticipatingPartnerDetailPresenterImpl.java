package com.vitalityactive.va.activerewards.participatingpartners.presenters;

import com.vitalityactive.va.partnerjourney.repository.PartnerRepository;

public class ParticipatingPartnerDetailPresenterImpl implements ParticipatingPartnerDetailPresenter {

    private final PartnerRepository partnerRepository;

    private UI ui;
    private long partnerId;

    public ParticipatingPartnerDetailPresenterImpl(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
    }

    @Override
    public void onUserInterfaceAppeared() {
        ui.showPartnerDetails(partnerRepository.getPartnerItem(partnerId));
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
    }

    @Override
    public void onUserInterfaceDestroyed() {
    }

    @Override
    public void setUserInterface(UI ui) {
        this.ui = ui;
    }

    @Override
    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public String getPartnerName() {
        return partnerRepository.getName(partnerId);
    }
}
