package com.vitalityactive.va.networking.model;

import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.register.entity.Password;
import com.vitalityactive.va.register.entity.Username;

public class PersonalDetailsRequest {

    //private ChangeEmailRequest changeEmailRequest;
    private VerifyNewEmailRequest getPartyByEmailRequest;
    public AuthenticateUserRequest loginRequest;

//    public PersonalDetailsRequest(ChangeEmailRequest changeEmailRequest) {
//        this.changeEmailRequest = changeEmailRequest;
//    }

    public PersonalDetailsRequest(VerifyNewEmailRequest verifyNewEmailRequest) {
        this.getPartyByEmailRequest = verifyNewEmailRequest;
    }
    
    public PersonalDetailsRequest( AuthenticateUserRequest authenticateUserRequest ) {
        this.loginRequest = authenticateUserRequest;
    }


    public static class VerifyNewEmailRequest {
        String value;
        Integer roleTypeKey;

        public VerifyNewEmailRequest(EmailAddress newEmailAddress) {
            this.value = newEmailAddress.toString();
            roleTypeKey = 1;
        }
    }
    
    public static class AuthenticateUserRequest {
        public String username;
        public String password;

        public AuthenticateUserRequest(Username username, Password password) {
            this.username = username.toString();
            this.password = password.toString();
        }
    }
}
