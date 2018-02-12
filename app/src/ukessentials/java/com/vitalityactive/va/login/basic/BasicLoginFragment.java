package com.vitalityactive.va.login.basic;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.login.LoginFragmentBase;

public class BasicLoginFragment extends LoginFragmentBase {

    @TargetApi(Build.VERSION_CODES.M)
    protected boolean fingerprintEnabled(){
        //Removing a fingerprint altogether for UKE.
        return false;
    }


    @TargetApi(Build.VERSION_CODES.M)
    protected boolean checkFingerPrintChanges(){
        //Removing a fingerprint altogether for UKE.
        return false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (view.findViewById(R.id.login_vitality_logo) != null){
            ImageView viewLogo = view.findViewById(R.id.login_vitality_logo);
            viewLogo.setImageResource(R.drawable.login_logo);
        }
    }

    @Override
    protected void processLoginClick() {
        getPresenter().onUserTriesToLogIn();
    }

    @Override
    protected void checkForFingerPrint() {

    }
}
