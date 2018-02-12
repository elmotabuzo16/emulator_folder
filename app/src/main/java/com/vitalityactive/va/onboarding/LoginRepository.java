package com.vitalityactive.va.onboarding;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.register.entity.Username;

public interface LoginRepository {

    boolean persistLoginResponse(LoginServiceResponse response, Username username);
}
