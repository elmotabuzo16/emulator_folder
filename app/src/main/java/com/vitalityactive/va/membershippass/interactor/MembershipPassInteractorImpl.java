package com.vitalityactive.va.membershippass.interactor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.vitalityactive.va.connectivity.ConnectivityListener;
import com.vitalityactive.va.dto.MembershipPassDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.membershippass.MembershipPassEvent;
import com.vitalityactive.va.membershippass.dto.MembershipPassRepository;
import com.vitalityactive.va.membershippass.model.MembershipPassResponse;
import com.vitalityactive.va.membershippass.service.MembershipPassServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by christian.j.p.capin on 11/17/2017.
 */
@Singleton
public class MembershipPassInteractorImpl implements MembershipPassInteractor, WebServiceResponseParser<MembershipPassResponse> {
    private String TAG = "MembershipPassInteractorImpl";
    private final EventDispatcher eventDispatcher;
    private MembershipPassServiceClient membershipPassServiceClient;
    private MembershipPassResponse response;
    private final ConnectivityListener connectivityListener;
    private MembershipPassRepository membershipPassRepository;
    @Inject
    public MembershipPassInteractorImpl(MembershipPassServiceClient membershipPassServiceClient,
                                        @NonNull EventDispatcher eventDispatcher,
                                        @NonNull ConnectivityListener connectivityListener,
                                        MembershipPassRepository  membershipPassRepository) {
        this.membershipPassServiceClient = membershipPassServiceClient;
        this.eventDispatcher = eventDispatcher;
        this.connectivityListener = connectivityListener;
        this.membershipPassRepository= membershipPassRepository;
    }

    @Override
    public void parseResponse(MembershipPassResponse body) {

        membershipPassRepository.persistMembershipPassResponse(body);
        eventDispatcher.dispatchEvent(new MembershipPassEvent(body, MembershipPassRequestResult.SUCCESSFUL,MembershipPassEvent.Event.RequestMembershipPassStatus));
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        eventDispatcher.dispatchEvent(new MembershipPassEvent(MembershipPassRequestResult.GENERIC_ERROR,MembershipPassEvent.Event.RequestMembershipPassStatus));
    }

    @Override
    public void handleGenericError(Exception exception) {
        eventDispatcher.dispatchEvent(new MembershipPassEvent(MembershipPassRequestResult.GENERIC_ERROR,MembershipPassEvent.Event.RequestMembershipPassStatus));
    }

    @Override
    public void handleConnectionError() {
        eventDispatcher.dispatchEvent(new MembershipPassEvent(MembershipPassRequestResult.CONNECTION_ERROR,MembershipPassEvent.Event.RequestMembershipPassStatus));
    }

    @Override
    public boolean isRequestInProgress() {
        return (membershipPassServiceClient != null && membershipPassServiceClient.isRequestInProgress());
    }

    @Override
    public boolean initialize() {
       if (this.connectivityListener.isOnline()) {
            membershipPassServiceClient.fetchMembershipPassDetails(this);
        }
        return true;
    }
}
