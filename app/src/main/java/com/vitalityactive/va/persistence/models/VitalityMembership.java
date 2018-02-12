package com.vitalityactive.va.persistence.models;

import io.realm.RealmList;
import io.realm.RealmObject;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/26/2017.
 */

public class VitalityMembership extends RealmObject implements Model {
    public Long id;
    public RealmList<MembershipProduct> membershipProducts;
    public CurrentVitalityMembershipPeriod currentVitalityMembershipPeriod;

    public VitalityMembership() {
    }
    public VitalityMembership(LoginServiceResponse.VitalityMembership response) {
        id = response.id;
        membershipProducts = new RealmList<MembershipProduct>();
        for (LoginServiceResponse.MembershipProduct memP: response.membershipProducts) {
            membershipProducts.add(new MembershipProduct(memP));
        }
        currentVitalityMembershipPeriod = new CurrentVitalityMembershipPeriod(response.currentVitalityMembershipPeriod);
    }
}
