package com.vitalityactive.va.persistence.models;

import android.util.Log;

import io.realm.RealmList;
import io.realm.RealmObject;

import com.google.gson.Gson;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/26/2017.
 */

public class MembershipProduct extends RealmObject implements Model {
    public RealmList<ProductFeature> productFeatures;

    public MembershipProduct() {
    }

    public MembershipProduct(LoginServiceResponse.MembershipProduct response) {
        productFeatures = new RealmList<ProductFeature>();

        for (LoginServiceResponse.ProductFeature prodF: response.productFeatures) {
            productFeatures.add(ProductFeature.create(prodF));
        }
    }
}
