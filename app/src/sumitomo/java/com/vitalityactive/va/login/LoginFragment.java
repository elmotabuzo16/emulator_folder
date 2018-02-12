package com.vitalityactive.va.login;

public class LoginFragment extends LoginFragmentBase {




    @Override
    protected void checkForFingerPrint() {

        if (!fingerprintEnabled()){
            return;
        }else if (fingerprintEnabled() && deviceSpecificPreferences.isFingerprint() && hasFingerprintChanges()) {
            showAuthenticationUponLogin();
        }else if(deviceSpecificPreferences.isFingerprint() && !deviceSpecificPreferences.getRememberedUsername().equals("")){
            //VA-22460 and 22461
            showFingerprintLogin();
        }
    }

}
