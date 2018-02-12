package com.vitalityactive.va.persistence.models.screeningsandvaccinations;

import com.vitalityactive.va.home.service.EventByPartyResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by dharel.h.rosell on 12/8/2017.
 */

public class ScreeningVaccinationEventByParty extends RealmObject implements Model {
    RealmList<ScreeningVaccinationEventByPartyOutbound> realmEventList;

    public ScreeningVaccinationEventByParty() {
    }

    public ScreeningVaccinationEventByParty(EventByPartyResponse eventByPartyResponse) {
        realmEventList = new RealmList<ScreeningVaccinationEventByPartyOutbound>();

        for(EventByPartyResponse.EventByPartyOutbound eventByPartyOutbound: eventByPartyResponse.getEvent()){
            realmEventList.add(new ScreeningVaccinationEventByPartyOutbound(eventByPartyOutbound));
        }
    }

    public RealmList<ScreeningVaccinationEventByPartyOutbound> getRealmEventList() {
        return realmEventList;
    }

    public void setRealmEventList(RealmList<ScreeningVaccinationEventByPartyOutbound> realmEventList) {
        this.realmEventList = realmEventList;
    }

}
