package com.vitalityactive.va.snv.partners.interactor;

import com.vitalityactive.va.snv.partners.service.GetPartnersByCategoryServiceClient;

/**
 * Created by kerry.e.lawagan on 12/8/2017.
 */

public class SnvParticipatingPartnersInteractorImpl implements SnvParticipatingPartnersInteractor {

    private GetPartnersByCategoryServiceClient serviceClient;

    public  SnvParticipatingPartnersInteractorImpl(GetPartnersByCategoryServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    @Override
    public void fetchData() {
        serviceClient.invokeApi();
    }
}
